package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.Customer;

public class CustomerRepository {

    private final Map<String,Customer> customerByID = new HashMap<>();

    public List<Customer> findAll(){
        return new ArrayList<>(customerByID.values());
    }
        
    public String nameByCustomerID(String customerID){
        return customerByID.get(customerID).getName();
    };

    public void save(Customer customer){
        customerByID.put(customer.getID(),customer);
    };
}
