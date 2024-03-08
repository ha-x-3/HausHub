package org.launchcode.homebase.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecretKeyController {

    @Value("${application.security.jwt.secret-key}")
    private String jwtSecret;

    @GetMapping("/api/secret-key")
    public String getSecretKey() {
        return jwtSecret;
    }
}