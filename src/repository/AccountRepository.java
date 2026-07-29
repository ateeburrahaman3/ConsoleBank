package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import domain.Account;

public class AccountRepository {

    private final Map<String,Account> accountByNumber = new HashMap<>();

    public void save(Account account){
        accountByNumber.put(account.getAccountNumber(),account);

    }

    public List<Account> findAll(){
        return new ArrayList<>(accountByNumber.values());
    }

    public Optional<Account> findByAccountNumber(String accountNumber){
        return Optional.ofNullable(accountByNumber.get(accountNumber));
    }

    public List<Account> findByCustomerID(String customerID){
        List<Account> result = new ArrayList<>();
        for (Account customer :accountByNumber.values()){
            if(customer.getCustomerID().equals(customerID))
                result.add(customer);
            
        }
        return result;
    }

    
}



