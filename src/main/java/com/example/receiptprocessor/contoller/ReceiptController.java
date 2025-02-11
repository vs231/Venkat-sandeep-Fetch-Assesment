package com.example.receiptprocessor.controller;

import com.example.receiptprocessor.model.PointsResponse;
import com.example.receiptprocessor.model.Receipt;
import com.example.receiptprocessor.model.ReceiptResponse;
import com.example.receiptprocessor.service.ReceiptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

	private final Re	ceiptService receiptService;

	public ReceiptController(ReceiptService receiptService) {
		this.receiptService = receiptService;
	}

	@PostMapping("/process")
	public ResponseEntity<ReceiptResponse> processReceipt(@RequestBody Receipt receipt) {
		String id = receiptService.processReceipt(receipt);
		return new ResponseEntity<>(new ReceiptResponse(id), HttpStatus.OK);
	}

	@GetMapping("/{id}/points")
	public ResponseEntity<PointsResponse> getPoints(@PathVariable String id) {
		Integer points = receiptService.getPoints(id);
		if (points == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new PointsResponse(points), HttpStatus.OK);
	}
}
