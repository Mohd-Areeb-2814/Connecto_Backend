package com.fb.posts.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {


    private Long id;
    @Column(unique = true)
    private Long postId;
    private String username;
    private String text;
    private String profilePictureUrl;

    //12-24-2024
    private LocalDateTime commentTimeStamp;



    // Getters and setters
}