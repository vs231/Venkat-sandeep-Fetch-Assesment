package com.example.receiptprocessor.model;

import java.util.List;

public class Receipt {
    private String retailer;
    private String purchaseDate; // Format: "yyyy-MM-dd"
    private String purchaseTime; // Format: "HH:mm"
    private List<Item> items;
    private String total; // Represented as a String (e.g., "35.35")

    // Getters and Setters
    public String getRetailer() {
        return retailer;
    }
    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }
    public String getPurchaseDate() {
        return purchaseDate;
    }
    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    public String getPurchaseTime() {
        return purchaseTime;
    }
    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }
    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }
    public String getTotal() {
        return total;
    }
    public void setTotal(String total) {
        this.total = total;
    }
}
