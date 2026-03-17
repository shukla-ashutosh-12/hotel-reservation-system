package com.example.demo.controller;

import com.example.demo.api.HotelResource;
import com.example.demo.model.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ReservationController {

    private final HotelResource hotelResource;

    public ReservationController(HotelResource hotelResource){
        this.hotelResource = hotelResource;
    }

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/createAccount")
    public String createAccountPage(){
        return "createAccount";
    }

    @PostMapping("/createCustomer")
    public String createCustomer(@RequestParam String email,
                                 @RequestParam String firstName,
                                 @RequestParam String lastName,
                                 Model model){

        try{

            if(email == null || email.isEmpty() ||
               firstName == null || firstName.isEmpty() ||
               lastName == null || lastName.isEmpty()){

                model.addAttribute("error","All fields are required");
                return "createAccount";
            }

            if(!email.contains("@") || !email.contains(".")){
                model.addAttribute("error","Invalid email format");
                return "createAccount";
            }

            hotelResource.createACustomer(email,firstName,lastName);

            model.addAttribute("success","Account created successfully");

        }catch(Exception e){
            model.addAttribute("error",e.getMessage());
        }

        return "createAccount";
    }

    @GetMapping("/findRoom")
    public String findRoom(){
        return "findRoom";
    }

    @PostMapping("/searchRooms")
    public String searchRooms(@RequestParam String checkIn,
                              @RequestParam String checkOut,
                              Model model){

        try{

            if(checkIn == null || checkIn.isEmpty() ||
               checkOut == null || checkOut.isEmpty()){

                model.addAttribute("error","Please select both dates");
                return "findRoom";
            }

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date checkInDate = df.parse(checkIn);
            Date checkOutDate = df.parse(checkOut);

            if(checkOutDate.before(checkInDate)){
                model.addAttribute("error","Check-out must be after check-in");
                return "findRoom";
            }

            Collection<IRoom> rooms =
                    hotelResource.findARoom(checkInDate,checkOutDate);

            if(rooms.isEmpty()){
                model.addAttribute("error","No rooms available");
                return "findRoom";
            }

            model.addAttribute("rooms", rooms);
            model.addAttribute("checkIn", checkIn);
            model.addAttribute("checkOut", checkOut);

            return "rooms";

        }catch(Exception e){

            model.addAttribute("error","Invalid date format");
            return "findRoom";
        }
    }

    @PostMapping("/bookRoom")
    public String bookRoom(@RequestParam String email,
                           @RequestParam String roomNumber,
                           @RequestParam String checkIn,
                           @RequestParam String checkOut,
                           Model model){

        try{

            if(email == null || email.isEmpty()){
                model.addAttribute("error","Please enter email");
                return "findRoom";
            }

            Customer customer = hotelResource.getCustomer(email);

            if(customer == null){
                model.addAttribute("error","Customer not found. Create account first");
                return "findRoom";
            }

            if(checkIn == null || checkIn.isEmpty() ||
               checkOut == null || checkOut.isEmpty()){

                model.addAttribute("error","Dates missing");
                return "findRoom";
            }

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date checkInDate = df.parse(checkIn);
            Date checkOutDate = df.parse(checkOut);

            IRoom room = hotelResource.getRoom(roomNumber);

            if(room == null){
                model.addAttribute("error","Room not found");
                return "findRoom";
            }

            hotelResource.bookARoom(email,room,checkInDate,checkOutDate);

            model.addAttribute("success","Reservation successful");

        }catch(Exception e){
            model.addAttribute("error",e.getMessage());
        }

        return "findRoom";
    }

    @PostMapping("/seeReservations")
    public String seeReservations(@RequestParam String email, Model model){

        try{

            if(email == null || email.isEmpty()){
                model.addAttribute("error","Please enter email");
                return "home";
            }

            Customer customer = hotelResource.getCustomer(email);

            if(customer == null){
                model.addAttribute("error","Customer not found");
                return "home";
            }

            Collection<Reservation> reservations =
                    hotelResource.getCustomersReservations(email);

            if(reservations.isEmpty()){
                model.addAttribute("error","No reservations found");
                return "home";
            }

            model.addAttribute("reservations",reservations);

            return "seeReservations";

        }catch(Exception e){

            model.addAttribute("error",e.getMessage());
            return "home";
        }
    }
}