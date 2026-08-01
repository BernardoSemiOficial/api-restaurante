package com.api.restaurant.controller;

import com.api.restaurant.interfaces.CreateRequestBodyCustomerDTO;
import com.api.restaurant.interfaces.CreateRequestBodyRestaurantDTO;
import com.api.restaurant.interfaces.ResponseBodyCustomerDTO;
import com.api.restaurant.interfaces.ResponseBodyRestaurantDTO;
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

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final CustomerService customerService;
    private final RestaurantService restaurantService;

    public RestaurantController(CustomerService customerService, RestaurantService restaurantService) {
        this.customerService = customerService;
        this.restaurantService = restaurantService;
    }

    // RESTAURANTE
    @GetMapping()
    public ResponseEntity<List<ResponseBodyRestaurantDTO>> getRestaurants() {
        List<ResponseBodyRestaurantDTO> restaurants = this.restaurantService.getRestaurants();
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
    public String putRestaurant(@PathVariable String restaurantId) {
        return "Restaurante " + restaurantId +" atualizado";
    }

    @DeleteMapping("/{restaurantId}")
    public String deleteRestaurant(@PathVariable String restaurantId) {
        return "Restaurante " + restaurantId + " deletado";
    }

    // USUÁRIOS DO RESTAURANTE
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
