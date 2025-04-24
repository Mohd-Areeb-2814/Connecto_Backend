package com.fb.user.config;

import com.fb.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private String username;

    private String email;

    private String password;



    public CustomUserDetails(User userCredential) {

        this.email = userCredential.getEmail();

        this.username = userCredential.getUsername(); // Add username

        this.password = userCredential.getPassword();

    }



    @Override

    public Collection<? extends GrantedAuthority> getAuthorities() {

        return null;

    }



    @Override

    public String getPassword() {

        return password;

    }



    @Override

    public String getUsername() {

        return email; // You can use email as username for authentication, but you can access both email and username now.

    }



    // Getter for username

    public String getUserName() {

        return username; // Access username directly if needed

    }



    @Override

    public boolean isAccountNonExpired() {

        return true;

    }



    @Override

    public boolean isAccountNonLocked() {

        return true;

    }



    @Override

    public boolean isCredentialsNonExpired() {

        return true;

    }



    @Override

    public boolean isEnabled() {

        return true;

    }

}