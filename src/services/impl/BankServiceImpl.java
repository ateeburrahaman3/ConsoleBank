package services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import domain.Account;
import domain.Customer;
import domain.Transaction;
import domain.Type;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientBalanceException;
import exceptions.ValidationException;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import services.BankService;
import util.Validation;


public class BankServiceImpl implements BankService{

    private final AccountRepository accountRepository  = new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();
    private final Validation<String> validateName = name -> {
        if (name == null || name.isBlank() ) throw new ValidationException("name Is Required");
    };
    private final Validation<String> validateEmail = email -> {
        if (email == null || !email.contains("@")) throw new ValidationException("Incorrect Email");
    };
    private final Validation<String> validateType = type -> {
        if (type == null ||!(type.equalsIgnoreCase("Savings") || type.equalsIgnoreCase("Current"))) 
                throw new ValidationException("Invalid Account Type");
    };
    private final Validation<Double> validateAmount = amount -> {
        if (amount < 0) throw new ValidationException("Invalid Amount"); 
    };


    @Override
    public String openAccount(String name, String email,String accountType, Double initial)
    {
        validateName.validate(name);
        validateEmail.validate(email);
        validateType.validate(accountType);
        validateAmount.validate(initial);
        String customerID = UUID.randomUUID().toString();
        String accountNumber = getAccountNumber();

        Account account = new Account(accountNumber,customerID,accountType,initial);
        accountRepository.save(account);
        
        deposit(accountNumber,initial,"Initial Deposit");

        //Save Customer
        Customer customer = new Customer(customerID,name,email);
        customerRepository.save(customer);

        return accountNumber;
    
    }

    public String getNameByID(String customerID){
        return customerRepository.nameByCustomerID(customerID);
    }

    public String getAccountNumber(){
            int size = accountRepository.findAll().size()+1;
            
            return String.format("AC%06d",size);
    }



    


    @Override
    public List<Account> listAccounts(){
        return (accountRepository.findAll().stream()
            .sorted(Comparator.comparing(Account::getAccountNumber))
            .collect(Collectors.toList()));
            
    }


    @Override
    public Account deposit(String accountNumber,Double amount,String note){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                          .orElseThrow(() -> new AccountNotFoundException("Account Not Found "+accountNumber));
        validateAmount.validate(amount);                  
        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction(accountNumber,amount,UUID.randomUUID().toString()
                                    ,Type.DEPOSITE,LocalDateTime.now(),note);
        transactionRepository.add(transaction);
        return account;
    }
    

    @Override
    public Account withdraw(String accountNumber,Double amount,String note){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                            .orElseThrow(() -> new AccountNotFoundException("Account Not Found"+accountNumber));
        validateAmount.validate(amount);                    
        if (account.getBalance() < amount)
            throw new InsufficientBalanceException("Insufficient Balance...Account Balance = " +account.getBalance() );
        account.setBalance(account.getBalance()-amount);
        Transaction transaction = new Transaction(accountNumber, amount, UUID.randomUUID().toString()
                                    ,Type.WITHDRAW, LocalDateTime.now(), note);                    
        transactionRepository.add(transaction);
        return account;
    }


    @Override
    public Account transfer(String accountNumber,String RecipentAccountNumber,Double  amount){
        if (accountNumber.equals(RecipentAccountNumber))
            throw new ValidationException("Same Senders And Recipent Account");
        Account account = accountRepository.findByAccountNumber(accountNumber) 
                        .orElseThrow(() -> new AccountNotFoundException("Senders Account Not Found"));
        Account RecipentAccount = accountRepository.findByAccountNumber(RecipentAccountNumber)
                        .orElseThrow(() -> new AccountNotFoundException("Recipent Account Not Found"));
        validateAmount.validate(amount);
        if (account.getBalance()< amount)
            throw new InsufficientBalanceException("Insufficient Balance In Senders Account");
        account.setBalance(account.getBalance() - amount) ;
        RecipentAccount.setBalance(RecipentAccount.getBalance() + amount) ;
        transactionRepository.add(new Transaction(accountNumber, amount,UUID.randomUUID().toString()
                            ,Type.TRANSFER_OUT, LocalDateTime.now(),"TO  "+RecipentAccountNumber));
        transactionRepository.add(new Transaction(RecipentAccountNumber, amount,UUID.randomUUID().toString()
                            ,Type.TRANSFER_IN, LocalDateTime.now(),"From  "+accountNumber));
        return account;    
    
    
    }

    @Override
    public List<Transaction> getStatement(String accountNumber){
        accountRepository.findByAccountNumber(accountNumber) 
                        .orElseThrow(() -> new AccountNotFoundException("Account Not Found"));
        return transactionRepository.findByAccountNumber(accountNumber).stream()
        .sorted(Comparator.comparing(Transaction::getTimestamp))
        .toList();

    }
    
    public Account findByAccountNumber(String accountNumber){
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account Not Found"));
    }

    @Override
    public List<Account> searchAccountByCustomerName(String q){
        List<Account> result = new ArrayList<>();
        String query = (q == null) ? "" : q.toLowerCase();
        for (Customer customer : customerRepository.findAll()){
            if (customer.getName().toLowerCase().contains(query))
                result.addAll(accountRepository.findByCustomerID(customer.getID()));
        };
        result.sort(Comparator.comparing(Account::getAccountNumber));
        return result;
    }

}
 