package com.fb.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateNameDTO {
	
	@NotNull(message = "First Name should not be null")
    @Pattern(regexp = "^[A-Z][a-zA-Z0-9]*$", message = "First Name not valid")
    private String firstName;
 
    @NotNull(message = "Last Name should not be null")
    @Pattern(regexp = "^[A-Z][a-zA-Z0-9]*$", message = "Last Name not valid")
    private String surname;
 
 
}