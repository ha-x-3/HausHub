package org.launchcode.homebase.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.launchcode.homebase.data.UserRepository;
import org.launchcode.homebase.models.User;
import org.launchcode.homebase.models.dto.JwtResponse;
import org.launchcode.homebase.models.dto.LoginFormDTO;
import org.launchcode.homebase.models.dto.RegisterFormDTO;
import org.launchcode.homebase.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        // You can modify this as needed, maybe return a URL or a message
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
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.set("Authorization", "Bearer " + token);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            SecurityContextHolder.clearContext();
            return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Forbidden: Not authenticated", HttpStatus.FORBIDDEN);
        }
    }

}