package com.fb.friend_profile.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendPostDTO {

	private Long id;
	private String content;
	private String mediaUrl;
	private String privacy;
	private String username;
	
	private String profilePictureUrl;
	private Long likesCount;
	
	//12-24
	private LocalDateTime postTimeStamp;

	// Getters and Setters
}