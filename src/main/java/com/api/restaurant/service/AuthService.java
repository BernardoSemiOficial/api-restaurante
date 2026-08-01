package com.api.restaurant.service;

import com.api.restaurant.interfaces.AuthRequestBodyDTO;
import com.api.restaurant.model.Customer;
import com.api.restaurant.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final CustomerService customerService;
    private final RestaurantService restaurantService;

    public AuthService(CustomerService customerService, RestaurantService restaurantService) {
        this.customerService = customerService;
        this.restaurantService = restaurantService;
    }

    public String login(AuthRequestBodyDTO dto) {
        Customer customer = this.customerService.findCustomer(dto.login())
                .orElseThrow(() -> new EntityNotFoundException("Customer não encontrado com Login: " + dto.login()));

        User user = customer.getUser();
        boolean isValidPassword = user.getPassword().equals(dto.password());

        if(!isValidPassword) {
            throw new IllegalArgumentException("Não foi possível realizar o login");
        }

        return "Login realizado com sucesso";
    }
}
