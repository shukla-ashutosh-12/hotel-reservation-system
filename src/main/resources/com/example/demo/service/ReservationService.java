package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.example.demo.model.Room;
import com.example.demo.model.Reservation;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.ReservationRepository;

@Service
public class ReservationService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Room> getAvailableRooms() {
        return roomRepository.findByAvailableTrue();
    }

    public void reserveRoom(Reservation reservation) {
        reservationRepository.save(reservation);
    }
}