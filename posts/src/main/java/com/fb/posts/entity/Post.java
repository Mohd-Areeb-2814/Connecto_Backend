package com.fb.posts.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "post_table")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String mediaUrl;

    private String privacy;

    private String username;

    private Long likesCount;

    private LocalDateTime postTimeStamp;

    // Getters and Setters

}