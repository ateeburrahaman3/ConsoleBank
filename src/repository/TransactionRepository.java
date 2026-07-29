package repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.Transaction;

public class TransactionRepository {
    
    public Map<String,List<Transaction>> transactionByAccount = new HashMap<>();


    public void add(Transaction transaction){
        transactionByAccount.computeIfAbsent(transaction.getAccountNumber(),
                                k -> new ArrayList<>()).add(transaction);
    }

    public List<Transaction> findByAccountNumber(String accountNumber){
        return new ArrayList<>(transactionByAccount.getOrDefault(accountNumber,Collections.emptyList()));

    }
}
