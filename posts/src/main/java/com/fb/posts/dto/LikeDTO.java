package com.fb.posts.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class LikeDTO {
    private Long id;

    @Column(unique = true)
    private String username;
    private Long postId;

    // Getters and Setters
}

