package com.example.receiptprocessor.service;

import com.example.receiptprocessor.model.Item;
import com.example.receiptprocessor.model.Receipt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReceiptService {

    private final Map<String, Integer> receiptPoints = new ConcurrentHashMap<>();


    private final boolean generatedByLLM = false; // change to true if desired

    public String processReceipt(Receipt receipt) {
        int points = computePoints(receipt);
        String id = UUID.randomUUID().toString();
        receiptPoints.put(id, points);
        return id;
    }

    public Integer getPoints(String id) {
        return receiptPoints.get(id);
    }


    private int computePoints(Receipt receipt) {
        int points = 0;

        // Rule 1: Count alphanumeric characters in the retailer name.
        if (receipt.getRetailer() != null) {
            points += (int) receipt.getRetailer()
                    .chars()
                    .filter(ch -> Character.isLetterOrDigit(ch))
                    .count();
        }

        BigDecimal total = new BigDecimal(receipt.getTotal());

        if (total.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
            points += 50;
        }

        BigDecimal quarter = new BigDecimal("0.25");
        if (total.remainder(quarter).compareTo(BigDecimal.ZERO) == 0) {
            points += 25;
        }

        int numberOfItems = receipt.getItems() != null ? receipt.getItems().size() : 0;
        points += (numberOfItems / 2) * 5;


        if (receipt.getItems() != null) {
            for (Item item : receipt.getItems()) {
                String description = item.getShortDescription() != null ? item.getShortDescription().trim() : "";
                if (!description.isEmpty() && description.length() % 3 == 0) {
                    BigDecimal price = new BigDecimal(item.getPrice());
                    BigDecimal multiplier = new BigDecimal("0.2");
                    BigDecimal itemPoints = price.multiply(multiplier);
                    // Round up to the nearest integer
                    int bonus = itemPoints.setScale(0, RoundingMode.CEILING).intValue();
                    points += bonus;
                }
            }
        }

        if (generatedByLLM && total.compareTo(new BigDecimal("10.00")) > 0) {
            points += 5;
        }

        LocalDate purchaseDate = LocalDate.parse(receipt.getPurchaseDate());
        if (purchaseDate.getDayOfMonth() % 2 == 1) {
            points += 6;
        }

        LocalTime purchaseTime = LocalTime.parse(receipt.getPurchaseTime());
        LocalTime start = LocalTime.of(14, 0);
        LocalTime end = LocalTime.of(16, 0);
        if (!purchaseTime.isBefore(start) && purchaseTime.isBefore(end)) {
            points += 10;
        }

        return points;
    }
}
