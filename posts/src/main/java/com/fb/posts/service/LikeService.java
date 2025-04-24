package com.fb.posts.service;

import com.fb.posts.entity.Like;
import com.fb.posts.exception.ResourceNotFoundException;

public interface LikeService {

    Like likePost(Long postId, String username);

    Object toggleLikePost(Long postId, String username) throws ResourceNotFoundException;

}