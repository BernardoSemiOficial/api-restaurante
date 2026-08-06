package com.api.restaurant.service;

import com.api.restaurant.interfaces.AuthRequestBodyDTO;
import com.api.restaurant.model.Customer;
import com.api.restaurant.model.Restaurant;
import com.api.restaurant.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final CustomerService customerService;
    private final RestaurantService restaurantService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(CustomerService customerService, RestaurantService restaurantService, PasswordEncoder passwordEncoder) {
        this.customerService = customerService;
        this.restaurantService = restaurantService;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(AuthRequestBodyDTO dto) {
        Optional<Customer> customer = this.customerService.findCustomer(dto.login());
        Optional<Restaurant> restaurant = this.restaurantService.findRestaurant(dto.login());

        if(customer.isEmpty() && restaurant.isEmpty()) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }

        if(customer.isPresent()) {
            User user = customer.get().getUser();
            boolean isValidPassword = passwordEncoder.matches(dto.password(), user.getPassword());
            if(!isValidPassword) {
                throw new IllegalArgumentException("Não foi possível realizar o login");
            }
            return "Login realizado com sucesso";
        }

        User user = restaurant.get().getUser();
        boolean isValidPassword = passwordEncoder.matches(dto.password(), user.getPassword());
        if(!isValidPassword) {
            throw new IllegalArgumentException("Não foi possível realizar o login");
        }
        return "Login realizado com sucesso";
    }
}
