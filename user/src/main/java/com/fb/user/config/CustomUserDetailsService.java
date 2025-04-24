package com.fb.user.config;

import com.fb.user.entity.User;
import com.fb.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired

    private UserRepository repository;

    @Override

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> credential = repository.findByEmail(email);
        return credential.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found with email :" + email));

    }

}
