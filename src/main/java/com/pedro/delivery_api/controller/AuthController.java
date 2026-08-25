package com.pedro.delivery_api.controller;


import com.pedro.delivery_api.dto.request.LoginRequestDTO;
import com.pedro.delivery_api.dto.request.RegisterUserRequestDTO;
import com.pedro.delivery_api.dto.response.LoginResponseDTO;
import com.pedro.delivery_api.dto.response.RegisterUserResponseDTO;
import com.pedro.delivery_api.entity.Role;
import com.pedro.delivery_api.entity.User;
import com.pedro.delivery_api.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDTO> register(@Valid @RequestBody RegisterUserRequestDTO request) {
        User newUser = new User();
        newUser.setEmail(request.email());
        newUser.setPassword(request.password()); //Senha terá que ser hash nao salva pura
        newUser.setRole(Role.CUSTOMER);
        newUser.setName(request.name());

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterUserResponseDTO(newUser.getEmail(), newUser.getName(), newUser.getRole()));
    }
}
