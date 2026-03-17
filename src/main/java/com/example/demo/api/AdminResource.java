package com.example.demo.api;

import com.example.demo.model.*;
import com.example.demo.service.CustomerService;
import com.example.demo.service.ReservationService;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AdminResource {

    private final CustomerService customerService;
    private final ReservationService reservationService;

    public AdminResource(CustomerService customerService,
                         ReservationService reservationService){

        this.customerService = customerService;
        this.reservationService = reservationService;
    }

    public void addRoom(IRoom room){
        reservationService.addRoom(room);
    }

    public Collection<IRoom> getAllRooms(){
        return reservationService.getAllRooms();
    }

    public Collection<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    public Collection<Reservation> getAllReservations(){
        return reservationService.getAllReservations();
    }
    public void deleteCustomer(String email){
        customerService.deleteCustomer(email);
    }
    public void deleteReservation(Long id){
        reservationService.deleteReservation(id);
    }
    public void updateCustomer(String email,String firstName,String lastName){

        customerService.updateCustomer(email,firstName,lastName);
    }
    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }
}