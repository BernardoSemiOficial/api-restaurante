package com.api.restaurant.controller;

import com.api.restaurant.interfaces.CreateRequestBodyCustomerDTO;
import com.api.restaurant.interfaces.EditRequestBodyCustomerDTO;
import com.api.restaurant.interfaces.EditRequestBodyCustomerPasswordDTO;
import com.api.restaurant.interfaces.ResponseBodyCustomerDTO;
import com.api.restaurant.model.Customer;
import com.api.restaurant.service.CustomerService;
import com.api.restaurant.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Executable;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final RestaurantService restaurantService;

    public CustomerController(CustomerService customerService, RestaurantService restaurantService) {
        this.customerService = customerService;
        this.restaurantService = restaurantService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ResponseBodyCustomerDTO> findCustomer(@PathVariable Long customerId) {
        return customerService.findCustomer(customerId)
                .map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping()
    public ResponseEntity<List<ResponseBodyCustomerDTO>> findCustomer(@RequestParam String customerName) {
        List<ResponseBodyCustomerDTO> customers = customerService.findCustomerByName(customerName);
        return ResponseEntity.ok().body(customers);
    }

    @PostMapping()
    public ResponseEntity<ResponseBodyCustomerDTO> createCustomer(@RequestBody CreateRequestBodyCustomerDTO dto) {
        ResponseBodyCustomerDTO customer = customerService.createCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<ResponseBodyCustomerDTO> editCustomer(@PathVariable Long customerId, @RequestBody EditRequestBodyCustomerDTO dto) {
        ResponseBodyCustomerDTO customer = customerService.editCustomer(customerId, dto);
        return ResponseEntity.ok().body(customer);
    }

    @PatchMapping("/{customerId}/change-password")
    public ResponseEntity<ResponseBodyCustomerDTO> editCustomerPassword(@PathVariable Long customerId, @RequestBody EditRequestBodyCustomerPasswordDTO dto) {
        customerService.editCustomerPassword(customerId, dto);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok().build();
    }
}
