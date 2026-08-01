package com.api.restaurant.controller;

import com.api.restaurant.interfaces.CreateRequestBodyCustomerDTO;
import com.api.restaurant.interfaces.EditRequestBodyCustomerDTO;
import com.api.restaurant.interfaces.EditRequestBodyCustomerPasswordDTO;
import com.api.restaurant.interfaces.ResponseBodyCustomerDTO;
import com.api.restaurant.model.Customer;
import com.api.restaurant.service.CustomerService;
import com.api.restaurant.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Executable;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final RestaurantService restaurantService;

    public CustomerController(CustomerService customerService, RestaurantService restaurantService) {
        this.customerService = customerService;
        this.restaurantService = restaurantService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ResponseBodyCustomerDTO> findCustomer(@PathVariable Long customerId) {
        try {
            return customerService.findCustomer(customerId)
                    .map(value -> ResponseEntity.ok().body(value))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception error) {
            error.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping()
    public ResponseEntity<List<ResponseBodyCustomerDTO>> findCustomer(@RequestParam String customerName) {
        try {
             List<ResponseBodyCustomerDTO> customers = customerService.findCustomerByName(customerName);
             return ResponseEntity.ok().body(customers);
        } catch (Exception error) {
            error.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseBodyCustomerDTO> createCustomer(@RequestBody CreateRequestBodyCustomerDTO dto) {
        try {
            ResponseBodyCustomerDTO customer = customerService.createCustomer(dto);
            return ResponseEntity.ok().body(customer);
        } catch (Exception error) {
            error.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<ResponseBodyCustomerDTO> editCustomer(@PathVariable Long customerId, @RequestBody EditRequestBodyCustomerDTO dto) {
        try {
            ResponseBodyCustomerDTO customer = customerService.editCustomer(customerId, dto);
            return ResponseEntity.ok().body(customer);
        } catch (Exception error) {
            error.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{customerId}/change-password")
    public ResponseEntity<ResponseBodyCustomerDTO> editCustomerPassword(@PathVariable Long customerId, @RequestBody EditRequestBodyCustomerPasswordDTO dto) {
        try {
            customerService.editCustomerPassword(customerId, dto);
            return ResponseEntity.ok().build();
        } catch (Exception error) {
            error.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        try {
            customerService.deleteCustomer(customerId);
            return ResponseEntity.ok().build();
        } catch(Exception error) {
            error.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }
}
