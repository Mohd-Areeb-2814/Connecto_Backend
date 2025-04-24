package com.fb.user.dto;

import com.fb.user.validation.ValidAge;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;


@Data
public class UserDTO {

    private Long userId;

    @NotNull(message = "First Name should not be null")
    @Pattern(regexp = "^[A-Z][a-zA-Z0-9]*$", message = "First Name not valid")
    private String firstName;

    @NotNull(message = "Last Name should not be null")
    @Pattern(regexp = "^[A-Z][a-zA-Z0-9]*$", message = "Last Name not valid")
    private String surname;

    @NotNull(message = "Email is required")
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|org|in)$", message = "email not valid")
    private String email;

    @NotNull(message = "Date of Birth is required")
    @Past(message = "dateOfBirth should be past date")
    @ValidAge
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    @Pattern(regexp = "Male|Female|Others", message = "gender should be male or female or others")
    private String gender;

    @NotNull(message = "password is required")
    @Size(min = 8, message = "minimum length of password should be 8")
    @Size(max = 16, message = "maximum length of password should be 18")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.{8,16}$).*$", message = "password must be alpha-numeric")
    private String password;


    private String username;
    private String bio;

    private String profilePictureUrl;

    private String coverPhotoUrl;

    private Boolean online;
}
