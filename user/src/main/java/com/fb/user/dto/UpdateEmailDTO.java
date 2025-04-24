package com.fb.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateEmailDTO {

	@NotNull(message = "Email is required")

	@Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|org|in)$", message = "email not valid")

	private String email;
 
}