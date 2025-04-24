package com.fb.friend_profile.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileDTO {

	private Long userId;
	
	private String firstName;
	
	private String surname;
	
	private String email;
	
	private LocalDate dateOfBirth;
	
	private String gender;
	
	private String username;
	
	private String bio;

	private String profilePictureUrl;
	
	private String coverPhotoUrl;
}