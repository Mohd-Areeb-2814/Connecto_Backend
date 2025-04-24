package com.fb.friend_profile.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {

	private Long id;
	private Long postId;
    private String username;
	private String text;
	private String profilePictureUrl;
	private LocalDateTime commentTimeStamp;
}