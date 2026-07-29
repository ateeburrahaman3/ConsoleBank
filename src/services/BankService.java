package services;

import java.util.List;

import domain.Account;

import domain.Transaction;

public interface BankService {
    String openAccount(String name , String email,String accountType,Double initial);
    List<Account> listAccounts();

    Account deposit(String accountNumber,Double amount,String note);

    Account withdraw(String accountNumber,Double amount,String note);

    Account transfer(String accountNumber,String RecipentAccountNumber,Double amount);

    List<Transaction> getStatement(String accountNumber);
    List<Account> searchAccountByCustomerName(String q);

    public String getNameByID(String customerID);

    public Account findByAccountNumber(String accountNumber);
}
