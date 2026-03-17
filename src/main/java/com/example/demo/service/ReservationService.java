package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.ReservationRepository;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReservationService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(RoomRepository roomRepository,
                              ReservationRepository reservationRepository){

        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    // Add Room
    public void addRoom(IRoom room){

        if(room == null){
            throw new IllegalArgumentException("Room cannot be null");
        }

        if(roomRepository.existsById(room.getRoomNumber())){
            throw new IllegalArgumentException("Room already exists");
        }

        roomRepository.save((Room) room);
    }

    // Get Room
    public IRoom getRoom(String roomNumber){

        if(roomNumber == null || roomNumber.isEmpty()){
            throw new IllegalArgumentException("Room number required");
        }

        IRoom room = roomRepository.findById(roomNumber).orElse(null);

        if(room == null){
            throw new IllegalArgumentException("Room not found");
        }

        return room;
    }

    // Get All Rooms
    public Collection<IRoom> getAllRooms(){
        return new ArrayList<>(roomRepository.findAll());
    }

    // Reserve Room
    public Reservation reserveRoom(Customer customer, IRoom room, Date checkIn, Date checkOut){

        if(customer == null){
            throw new IllegalArgumentException("Customer cannot be null");
        }

        if(room == null){
            throw new IllegalArgumentException("Room cannot be null");
        }

        if(checkIn == null || checkOut == null){
            throw new IllegalArgumentException("Dates cannot be null");
        }

        if(checkOut.before(checkIn)){
            throw new IllegalArgumentException("Check-out must be after check-in");
        }

        // check if room already booked
        Collection<Reservation> existingReservations = reservationRepository.findAll();

        for(Reservation reservation : existingReservations){

            if(reservation.getRoom().getRoomNumber().equals(room.getRoomNumber())){

                if(checkIn.before(reservation.getCheckOutDate())
                        && checkOut.after(reservation.getCheckInDate())){

                    throw new IllegalArgumentException("Room already booked for these dates");
                }
            }
        }

        Reservation reservation = new Reservation(customer,room,checkIn,checkOut);

        return reservationRepository.save(reservation);
    }

    // Get All Reservations
    public Collection<Reservation> getAllReservations(){
        return reservationRepository.findAll();
    }

    // Get Customer Reservations
    public Collection<Reservation> getCustomersReservation(Customer customer){

        if(customer == null){
            throw new IllegalArgumentException("Customer cannot be null");
        }

        return reservationRepository.findByCustomer(customer);
    }

    // Find Available Rooms
    public Collection<IRoom> findRooms(Date checkIn, Date checkOut){

        if(checkIn == null || checkOut == null){
            throw new IllegalArgumentException("Dates cannot be null");
        }

        if(checkOut.before(checkIn)){
            throw new IllegalArgumentException("Check-out must be after check-in");
        }

        Collection<IRoom> availableRooms = new ArrayList<>();

        Collection<Reservation> reservations = reservationRepository.findAll();

        for(IRoom room : roomRepository.findAll()){

            boolean available = true;

            for(Reservation reservation : reservations){

                if(reservation.getRoom().getRoomNumber().equals(room.getRoomNumber())){

                    if(checkIn.before(reservation.getCheckOutDate())
                            && checkOut.after(reservation.getCheckInDate())){

                        available = false;
                        break;
                    }
                }
            }

            if(available){
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }
    public void deleteReservation(Long id){

        if(!reservationRepository.existsById(id)){
            throw new IllegalArgumentException("Reservation not found");
        }

        reservationRepository.deleteById(id);
    }
}