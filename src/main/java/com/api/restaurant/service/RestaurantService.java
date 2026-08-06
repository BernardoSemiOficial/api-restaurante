package com.api.restaurant.service;

import com.api.restaurant.interfaces.*;
import com.api.restaurant.model.Address;
import com.api.restaurant.model.Customer;
import com.api.restaurant.model.Restaurant;
import com.api.restaurant.model.User;
import com.api.restaurant.repository.RestaurantRepository;
import com.api.restaurant.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.restaurantRepository = restaurantRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public Optional<Restaurant> findRestaurant(String login) {
        return this.restaurantRepository.findByUserLogin(login);
    }

    public List<ResponseBodyRestaurantDTO> getRestaurants(String restaurantName) {
        if(!restaurantName.isEmpty()) {
            List<ResponseBodyRestaurantDTO> restaurants = this.restaurantRepository.findByUserNameContainingIgnoreCase(restaurantName)
                    .stream()
                    .map(ResponseBodyRestaurantDTO::new)
                    .toList();
            return restaurants;
        }

        List<ResponseBodyRestaurantDTO> restaurants = this.restaurantRepository.findAll()
                .stream()
                .map(ResponseBodyRestaurantDTO::new)
                .toList();
        return restaurants;
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

        String passwordHash = this.passwordEncoder.encode(dto.user().password());
        user.setPassword(passwordHash);
        user.setUpdatedAt(LocalDateTime.now());

        Restaurant restaurant = new Restaurant();
        restaurant.setUser(user);
        restaurant.setCnpj(dto.cnpj());
        restaurant.setCuisineType(dto.cuisineType());

        boolean isExistUserEmail = this.userRepository.findByEmail(dto.user().email()).isPresent();
        if(isExistUserEmail) throw new DataIntegrityViolationException("E-mail já cadastrado");

        return this.restaurantRepository.save(restaurant);
    }

    @Transactional
    public ResponseBodyRestaurantDTO editRestaurant(Long restaurantId, EditRequestBodyRestaurantDTO dto) {
        Restaurant restaurant = this.restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + restaurantId));

        User user = restaurant.getUser();
        boolean isEmailEquals = dto.user().email().equals(user.getEmail());

        if (dto.user() != null) {
            if (dto.user().name() != null) user.setName(dto.user().name());
            if (dto.user().email() != null) user.setEmail(dto.user().email());
            if (dto.user().login() != null) user.setLogin(dto.user().login());

            if (dto.user().address() != null && user.getAddress() != null) {
                Address address = user.getAddress();
                if (dto.user().address().zipCode() != null) address.setZipCode(dto.user().address().zipCode());
                if (dto.user().address().street() != null) address.setStreet(dto.user().address().street());
                if (dto.user().address().number() != null) address.setNumber(dto.user().address().number());
                if (dto.user().address().city() != null) address.setCity(dto.user().address().city());
                if (dto.user().address().state() != null) address.setState(dto.user().address().state());
            }
        }

        if (dto.cnpj() != null) {
            restaurant.setCnpj(dto.cnpj());
        }

        user.setUpdatedAt(LocalDateTime.now());

        boolean isExistUserEmail = this.userRepository.findByEmail(dto.user().email()).isPresent();
        if(!isEmailEquals && isExistUserEmail) throw new DataIntegrityViolationException("E-mail já cadastrado");

        Restaurant savedRestaurant = this.restaurantRepository.save(restaurant);
        return new ResponseBodyRestaurantDTO(savedRestaurant);
    }

    @Transactional
    public void editRestaurantPassword(Long restaurantId, EditRequestBodyRestaurantPasswordDTO dto) {
        Restaurant restaurant = this.restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + restaurantId));

        User user = restaurant.getUser();
        boolean passwordIsOk = passwordEncoder.matches(dto.currentPassword(), user.getPassword());

        if(!passwordIsOk) {
            throw new IllegalArgumentException("A senha atual informada está incorreta.");
        }

        String passwordHash = passwordEncoder.encode(dto.newPassword());
        user.setPassword(passwordHash);
        user.setUpdatedAt(LocalDateTime.now());

        this.restaurantRepository.save(restaurant);
    }

    @Transactional
    public void deleteRestaurant(Long restaurantId) {
        this.restaurantRepository.deleteById(restaurantId);
    }
}
