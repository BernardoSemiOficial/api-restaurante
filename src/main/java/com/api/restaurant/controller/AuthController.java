package com.api.restaurant.controller;

import com.api.restaurant.interfaces.AuthRequestBodyDTO;
import com.api.restaurant.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody AuthRequestBodyDTO dto) {
        String auth = this.authService.login(dto);
        return ResponseEntity.ok(auth);
    }

}
