package com.example.demo.controller;

import com.example.demo.api.AdminResource;
import com.example.demo.model.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminResource adminResource;

    public AdminController(AdminResource adminResource){
        this.adminResource = adminResource;
    }

    @GetMapping("")
    public String adminPage(Model model){

        int totalCustomers = adminResource.getAllCustomers().size();
        int totalRooms = adminResource.getAllRooms().size();
        int totalReservations = adminResource.getAllReservations().size();

        int availableRooms = totalRooms - totalReservations;

        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("totalRooms", totalRooms);
        model.addAttribute("totalReservations", totalReservations);
        model.addAttribute("availableRooms", availableRooms);

        return "admin";
    }

    @GetMapping("/customers")
    public String viewCustomers(Model model){

        Collection<Customer> customers = adminResource.getAllCustomers();
        model.addAttribute("customers",customers);

        return "customers";
    }

    @GetMapping("/rooms")
    public String viewRooms(Model model){

        Collection<IRoom> rooms = adminResource.getAllRooms();
        model.addAttribute("rooms",rooms);

        return "roomsAdmin";
    }

    @GetMapping("/reservations")
    public String viewReservations(Model model){

        Collection<Reservation> reservations = adminResource.getAllReservations();
        model.addAttribute("reservations", reservations);

        return "reservationsAdmin";
    }

    @GetMapping("/addRoom")
    public String addRoomPage(){
        return "addRoom";
    }

    @PostMapping("/addRoom")
    public String addRoom(@RequestParam String roomNumber,
                          @RequestParam double price,
                          @RequestParam String roomType,
                          Model model){

        try{

            if(roomNumber == null || roomNumber.isEmpty()){
                throw new IllegalArgumentException("Room number required");
            }

            if(price < 0){
                throw new IllegalArgumentException("Price cannot be negative");
            }

            RoomType type = RoomType.valueOf(roomType);

            IRoom room;

            if(price == 0){
                room = new FreeRoom(roomNumber,type);
            }else{
                room = new Room(roomNumber,price,type);
            }

            adminResource.addRoom(room);

            model.addAttribute("success","Room added successfully");

        }catch(Exception e){
            model.addAttribute("error",e.getMessage());
        }

        return "addRoom";
    }
    @GetMapping("/deleteCustomer")
    public String deleteCustomer(@RequestParam String email, Model model){

        try{
            adminResource.deleteCustomer(email);
            model.addAttribute("success","Customer deleted successfully");
        }
        catch(Exception e){
            model.addAttribute("error",e.getMessage());
        }

        model.addAttribute("customers", adminResource.getAllCustomers());

        return "customers";
    }
    @GetMapping("/deleteReservation")
    public String deleteReservation(@RequestParam Long id, Model model){

        try{

            adminResource.deleteReservation(id);
            model.addAttribute("success","Reservation deleted successfully");

        }catch(Exception e){

            model.addAttribute("error",e.getMessage());
        }

        model.addAttribute("reservations", adminResource.getAllReservations());

        return "reservationsAdmin";
    }
    @GetMapping("/editCustomer")
    public String editCustomer(@RequestParam String email, Model model){

        Customer customer = adminResource.getCustomer(email);

        model.addAttribute("customer", customer);

        return "editCustomer";
    }
    @PostMapping("/updateCustomer")
    public String updateCustomer(@RequestParam String email,
                                 @RequestParam String firstName,
                                 @RequestParam String lastName,
                                 Model model){

        try{

            adminResource.updateCustomer(email, firstName, lastName);

            model.addAttribute("success","Customer updated successfully");

        }catch(Exception e){

            model.addAttribute("error",e.getMessage());
        }

        model.addAttribute("customers", adminResource.getAllCustomers());

        return "customers";
    }
    @GetMapping("/searchCustomer")
    public String searchCustomer(@RequestParam String email, Model model){

        Customer customer = adminResource.getCustomer(email);

        if(customer == null){
            model.addAttribute("error","Customer not found");
            return "customers";
        }

        model.addAttribute("customer", customer);

        return "customerDetails";
    }
}