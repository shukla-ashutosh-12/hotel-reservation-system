package com.example.demo.api;

import com.example.demo.model.*;
import com.example.demo.service.CustomerService;
import com.example.demo.service.ReservationService;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Service
public class HotelResource {

    private final CustomerService customerService;
    private final ReservationService reservationService;

    public HotelResource(CustomerService customerService,
                         ReservationService reservationService){

        this.customerService = customerService;
        this.reservationService = reservationService;
    }

    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email,String firstName,String lastName){
        customerService.addCustomer(email,firstName,lastName);
    }

    public IRoom getRoom(String roomNumber){
        return reservationService.getRoom(roomNumber);
    }

    public Reservation bookARoom(String email, IRoom room, Date checkIn, Date checkOut){

        Customer customer = customerService.getCustomer(email);

        return reservationService.reserveRoom(customer,room,checkIn,checkOut);
    }

    public Collection<Reservation> getCustomersReservations(String email){

        Customer customer = customerService.getCustomer(email);

        return reservationService.getCustomersReservation(customer);
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut){

        return reservationService.findRooms(checkIn,checkOut);
    }
}