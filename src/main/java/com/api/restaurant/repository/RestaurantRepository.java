package com.api.restaurant.repository;

import com.api.restaurant.model.Restaurant;
import com.api.restaurant.service.RestaurantService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    public List<Restaurant> findByCustomersId(Long customerId);
    public List<Restaurant> findByUserNameContainingIgnoreCase(String name);
    public Optional<Restaurant> findByUserLogin(String login);
    public Optional<Restaurant> findByUserEmail(String email);
}
