package com.saurav.ArtCorner.controller;

import com.saurav.ArtCorner.model.Transaction;
import com.saurav.ArtCorner.service.SellerService;
import com.saurav.ArtCorner.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping()
    public ResponseEntity<List<Transaction>> getAllTransactions()
    {
        List<Transaction> transactions=transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }



}
