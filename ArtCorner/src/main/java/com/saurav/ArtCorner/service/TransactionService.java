package com.saurav.ArtCorner.service;

import com.saurav.ArtCorner.model.Order;
import com.saurav.ArtCorner.model.Seller;
import com.saurav.ArtCorner.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Order order);
    List<Transaction> getAllTransactions();
}
