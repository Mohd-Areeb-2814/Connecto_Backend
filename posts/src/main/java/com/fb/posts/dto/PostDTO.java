package com.fb.posts.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDTO {
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