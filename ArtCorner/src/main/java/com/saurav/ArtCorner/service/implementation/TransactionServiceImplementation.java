package com.saurav.ArtCorner.service.implementation;

import com.saurav.ArtCorner.model.Order;
import com.saurav.ArtCorner.model.Seller;
import com.saurav.ArtCorner.model.Transaction;
import com.saurav.ArtCorner.repository.SellerRepository;
import com.saurav.ArtCorner.repository.TransactionRepository;
import com.saurav.ArtCorner.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImplementation implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;

    @Override
    public Transaction createTransaction(Order order) {
        Seller seller=sellerRepository.findAll().getFirst();

        Transaction transaction=new Transaction();
        transaction.setSeller(seller);
        transaction.setCustomer(order.getUser());

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
