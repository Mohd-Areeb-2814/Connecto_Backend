package com.fb.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;



    // Updated to accept both username and email

    public String generateToken(String username, String email) {

        return jwtService.generateToken(username, email);

    }



    public void validateToken(String token) {

        jwtService.validateToken(token);

    }

}

