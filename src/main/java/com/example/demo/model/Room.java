package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Room implements IRoom {

    @Id
    private String roomNumber;

    private Double price;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    public Room(){}

    public Room(String roomNumber, Double price, RoomType roomType){
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomType = roomType;
    }

    public String getRoomNumber(){
        return roomNumber;
    }

    public Double getRoomPrice(){
        return price;
    }

    public RoomType getRoomType(){
        return roomType;
    }

    public boolean isFree(){
        return price == 0;
    }

    @Override
    public String toString(){
        return "Room " + roomNumber + " Price " + price + " Type " + roomType;
    }
}