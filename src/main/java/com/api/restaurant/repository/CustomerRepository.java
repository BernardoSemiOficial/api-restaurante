package com.api.restaurant.repository;

import com.api.restaurant.model.Customer;
import com.api.restaurant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    public List<Customer> findByRestaurantsId(Long restaurantId);
    public List<Customer> findByUserNameContainingIgnoreCase(String name);
    public Optional<Customer> findByUserLogin(String login);
    public Optional<Customer> findByUserEmail(String email);
}
