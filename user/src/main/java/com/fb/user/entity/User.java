package com.fb.user.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String firstName;

    private String surname;

    @Column(unique = true)
    private String username;

    private String email;

    private LocalDate dateOfBirth;

    private String gender;

    private String password;

    //	12-19-2024
    private String bio;

    private String profilePictureUrl;
    private String coverPhotoUrl;

    private Boolean online;



}
