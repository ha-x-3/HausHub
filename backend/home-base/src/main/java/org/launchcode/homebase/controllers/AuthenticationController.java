package org.launchcode.homebase.controllers;

import jakarta.validation.Valid;
import org.launchcode.homebase.data.UserRepository;
import org.launchcode.homebase.models.User;
import org.launchcode.homebase.models.dto.JwtResponse;
import org.launchcode.homebase.models.dto.LoginFormDTO;
import org.launchcode.homebase.models.dto.RegisterFormDTO;
import org.launchcode.homebase.models.enums.Role;
import org.launchcode.homebase.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> registerUser(@RequestBody @Valid RegisterFormDTO registerFormDTO, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (userRepository.findByUsername(registerFormDTO.getUsername()) != null) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        String encodedPassword = passwordEncoder.encode(registerFormDTO.getPassword());
        User newUser = new User(registerFormDTO.getUsername(), registerFormDTO.getEmail(), encodedPassword, registerFormDTO.getRole());
        userRepository.save(newUser);

        String token = jwtService.generateToken(newUser);
        JwtResponse jwtResponse = new JwtResponse(token);
        return new ResponseEntity<>(jwtResponse, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<String> displayLoginForm() {
        return new ResponseEntity<>("Please log in", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginFormDTO loginFormDTO, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>("Invalid login data", HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(loginFormDTO.getEmail());
        if (user == null || !passwordEncoder.matches(loginFormDTO.getPassword(), user.getPassword())) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(token));
    }

}