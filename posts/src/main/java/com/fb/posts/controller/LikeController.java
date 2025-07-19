package com.fb.posts.controller;

import com.fb.posts.exception.ResourceNotFoundException;
import com.fb.posts.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@CrossOrigin
@Validated
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/toggle")

    public ResponseEntity<?> toggleLike(@RequestParam("postId") Long postId,
                                        @RequestParam("username") String username) throws ResourceNotFoundException {
        Object like = likeService.toggleLikePost(postId, username);
        if (like == null) {
            return ResponseEntity.ok(like);
        } else {
            return ResponseEntity.ok(like);
        }
    }
}