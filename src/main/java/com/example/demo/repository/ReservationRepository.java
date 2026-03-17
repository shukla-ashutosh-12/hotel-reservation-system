package com.example.demo.repository;

import com.example.demo.model.Reservation;
import com.example.demo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    Collection<Reservation> findByCustomer(Customer customer);
}