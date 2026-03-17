package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public void addCustomer(String email,String firstName,String lastName){

        if(customerRepository.existsById(email)){
            throw new IllegalArgumentException("Customer already exists");
        }

        Customer customer = new Customer(firstName,lastName,email);

        customerRepository.save(customer);
    }

    public Customer getCustomer(String email){
        return customerRepository.findById(email).orElse(null);
    }

    public Collection<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }
    public void deleteCustomer(String email){

        try{

            if(!customerRepository.existsById(email)){
                throw new IllegalArgumentException("Customer not found");
            }

            customerRepository.deleteById(email);

        }catch(Exception e){

            throw new IllegalArgumentException(
                "Customer has reservations. Delete reservations first."
            );
        }
    }
    public void updateCustomer(String email,String firstName,String lastName){

        Customer customer = customerRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        customer.setFirstName(firstName);
        customer.setLastName(lastName);

        customerRepository.save(customer);
    }
}