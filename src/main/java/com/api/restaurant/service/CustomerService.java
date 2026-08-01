package com.api.restaurant.service;

import com.api.restaurant.interfaces.*;
import com.api.restaurant.model.Address;
import com.api.restaurant.model.Customer;
import com.api.restaurant.model.Restaurant;
import com.api.restaurant.model.User;
import com.api.restaurant.repository.CustomerRepository;
import com.api.restaurant.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;

    public CustomerService(CustomerRepository customerRepository, RestaurantRepository restaurantRepository) {
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public List<ResponseBodyCustomerDTO> getCustomersByRestaurant(Long restaurantId) {
        List<Customer> customers = customerRepository.findByRestaurantsId(restaurantId);

        return customers.stream()
                .map(ResponseBodyCustomerDTO::new)
                .toList();
    }

    @Transactional
    public Customer createCustomerToRestaurant(Long restaurantId, CreateRequestBodyCustomerDTO dto) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com o ID: " + restaurantId));

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

        Customer customer = new Customer();
        customer.setUser(user);
        customer.setCpf(dto.cpf());
        customer.getRestaurants().add(restaurant);

        return this.customerRepository.save(customer);
    }

    public Optional<Customer> findCustomer(String login) {
        return this.customerRepository.findByUserLogin(login);
    }

    public Optional<ResponseBodyCustomerDTO> findCustomer(Long customerId) {
        return this.customerRepository.findById(customerId).map(ResponseBodyCustomerDTO::new);
    }

    public List<ResponseBodyCustomerDTO> findCustomerByName(String customerName) {
        return this.customerRepository.findByUserNameContainingIgnoreCase(customerName).stream().map(ResponseBodyCustomerDTO::new).toList();
    }

    public void deleteCustomer(Long customerId) {
        this.customerRepository.deleteById(customerId);
    }

    @Transactional
    public ResponseBodyCustomerDTO createCustomer(CreateRequestBodyCustomerDTO dto) {
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

        Customer customer = new Customer();
        customer.setUser(user);
        customer.setCpf(dto.cpf());

        Customer newCustomer = this.customerRepository.save(customer);
        return new ResponseBodyCustomerDTO(newCustomer);
    }

    @Transactional
    public ResponseBodyCustomerDTO editCustomer(Long customerId, EditRequestBodyCustomerDTO dto) {
        Customer customer = this.customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + customerId));

        User user = customer.getUser();

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

        if (dto.cpf() != null) {
            customer.setCpf(dto.cpf());
        }

        user.setUpdatedAt(LocalDateTime.now());

        Customer savedCustomer = this.customerRepository.save(customer);
        return new ResponseBodyCustomerDTO(savedCustomer);
    }

    @Transactional
    public void editCustomerPassword(Long customerId, EditRequestBodyCustomerPasswordDTO dto) {
        Customer customer = this.customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + customerId));

        User user = customer.getUser();
        boolean passwordIsOk = dto.currentPassword().equals(user.getPassword());

        if(!passwordIsOk) {
            throw new IllegalArgumentException("A senha atual informada está incorreta.");
        }

        user.setPassword(dto.newPassword());
        user.setUpdatedAt(LocalDateTime.now());

        this.customerRepository.save(customer);
    }
}
