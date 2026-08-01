package com.api.restaurant.service;

import com.api.restaurant.interfaces.*;
import com.api.restaurant.model.Address;
import com.api.restaurant.model.Restaurant;
import com.api.restaurant.model.User;
import com.api.restaurant.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<ResponseBodyRestaurantDTO> getRestaurants() {
        List<Restaurant> restaurant = this.restaurantRepository.findAll();
        return restaurant.stream()
                .map(c -> new ResponseBodyRestaurantDTO(
                        c.getId(),
                        c.getCnpj(),
                        c.getCuisineType(),
                        new ResponseBodyUserDTO(
                                c.getUser().getId(),
                                c.getUser().getName(),
                                c.getUser().getEmail(),
                                c.getUser().getLogin(),
                                new ResponseBodyAddressDTO(
                                        c.getUser().getAddress().getZipCode(),
                                        c.getUser().getAddress().getStreet(),
                                        c.getUser().getAddress().getNumber(),
                                        c.getUser().getAddress().getCity(),
                                        c.getUser().getAddress().getState()
                                )
                        )
                ))
                .toList();
    }

    @Transactional
    public Restaurant createRestaurant(CreateRequestBodyRestaurantDTO dto) {

        Address address = new Address();
        address.setZipCode(dto.user().address().zipCode());
        address.setStreet(dto.user().address().street());
        address.setNumber(dto.user().address().number());
        address.setCity(dto.user().address().city());
        address.setState(dto.user().address().state());

        User user = new User();
        user.setAddress(address);
        user.setName(dto.user().name());
        user.setEmail(dto.user().email());
        user.setLogin(dto.user().login());
        user.setPassword(dto.user().password());
        user.setUpdatedAt(LocalDateTime.now());

        Restaurant restaurant = new Restaurant();
        restaurant.setUser(user);
        restaurant.setCnpj(dto.cnpj());
        restaurant.setCuisineType(dto.cuisineType());

        return this.restaurantRepository.save(restaurant);
    }
}
