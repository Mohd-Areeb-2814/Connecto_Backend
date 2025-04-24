package com.fb.friend_profile.dto;

import lombok.Data;

@Data
public class AllFriendsDTO {
	private Long id;
	private String senderUsername;
	private String receiverUsername;
	private String receiverProfilePictureUrl;
	private String senderProfilePictureUrl;
}