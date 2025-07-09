package com.example.notestackapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notestackapi.dto.AuthResponse;
import com.example.notestackapi.dto.LoginRequest;
import com.example.notestackapi.dto.RegisterRequest;
import com.example.notestackapi.service.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = userService.loginUser(request);

        if (token.equals("Invalid password!") || token.equals("User not found!")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(token);
        }

        return ResponseEntity.ok(new AuthResponse(token));
    }

}
