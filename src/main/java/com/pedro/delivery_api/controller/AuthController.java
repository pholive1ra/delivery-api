package com.pedro.delivery_api.controller;


import com.pedro.delivery_api.dto.request.LoginRequest;
import com.pedro.delivery_api.dto.request.RegisterUserRequest;
import com.pedro.delivery_api.dto.response.LoginResponse;
import com.pedro.delivery_api.dto.response.RegisterUserResponse;
import com.pedro.delivery_api.entity.User;
import com.pedro.delivery_api.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        User newUser = new User();
        newUser.setEmail(request.email());
        newUser.setPassword(request.password());
        newUser.setName(request.name());

        userRepository.save(newUser);

        return ResponseEntity.ok(new RegisterUserResponse(newUser.getEmail(), newUser.getName()));
    }
}
