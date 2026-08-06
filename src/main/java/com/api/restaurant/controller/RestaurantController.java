package com.api.restaurant.controller;

import com.api.restaurant.interfaces.*;
import com.api.restaurant.model.Customer;
import com.api.restaurant.model.Restaurant;
import com.api.restaurant.repository.CustomerRepository;
import com.api.restaurant.repository.RestaurantRepository;
import com.api.restaurant.service.CustomerService;
import com.api.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final CustomerService customerService;
    private final RestaurantService restaurantService;

    public RestaurantController(CustomerService customerService, RestaurantService restaurantService) {
        this.customerService = customerService;
        this.restaurantService = restaurantService;
    }

    @GetMapping()
    public ResponseEntity<List<ResponseBodyRestaurantDTO>> getRestaurants(@RequestParam String restaurantName) {
        List<ResponseBodyRestaurantDTO> restaurants = this.restaurantService.getRestaurants(restaurantName);
        return ResponseEntity.ok().body(restaurants);
    }

    @PostMapping()
    public ResponseEntity<Void> createRestaurant(@RequestBody CreateRequestBodyRestaurantDTO dto) {
        try {
            this.restaurantService.createRestaurant(dto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Throwable error) {
            error.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<ResponseBodyRestaurantDTO> editRestaurant(@PathVariable Long restaurantId, @RequestBody EditRequestBodyRestaurantDTO dto) {
        try {
            ResponseBodyRestaurantDTO restaurant = restaurantService.editRestaurant(restaurantId, dto);
            return ResponseEntity.ok().body(restaurant);
        } catch (Exception error) {
            error.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{restaurantId}/change-password")
    public ResponseEntity<ResponseBodyRestaurantDTO> editRestaurantPassword(@PathVariable Long restaurantId, @RequestBody EditRequestBodyRestaurantPasswordDTO dto) {
        try {
            restaurantService.editRestaurantPassword(restaurantId, dto);
            return ResponseEntity.ok().build();
        } catch (Exception error) {
            error.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<ResponseBodyRestaurantDTO> deleteRestaurant(@PathVariable Long restaurantId) {
        try {
            restaurantService.deleteRestaurant(restaurantId);
            return ResponseEntity.ok().build();
        } catch (Exception error) {
            error.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{restaurantId}/customers")
    public ResponseEntity<List<ResponseBodyCustomerDTO>> getUsersByRestaurant(@PathVariable Long restaurantId) {
        List<ResponseBodyCustomerDTO> customers = customerService.getCustomersByRestaurant(restaurantId);
        return ResponseEntity.ok().body(customers);
    }

    @PostMapping("/{restaurantId}/customers")
    public ResponseEntity<Void> createCustomerToRestaurant(@PathVariable Long restaurantId, @RequestBody CreateRequestBodyCustomerDTO dto) {
        try {
            customerService.createCustomerToRestaurant(restaurantId, dto);
            return ResponseEntity.ok().build();
        } catch (Exception error) {
            error.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
