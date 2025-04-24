package com.fb.posts.service;

import com.fb.posts.dto.PostDTO;
import com.fb.posts.entity.Post;
import com.fb.posts.exception.PostsException;
import com.fb.posts.exception.ResourceNotFoundException;

import java.util.List;

public interface PostService {

    Post createPost(Post post);

    List<Post> getAllPosts();

    Post getPostById(Long id) throws PostsException, ResourceNotFoundException;

    Post updatePost(Long id, Post postDetails) throws PostsException, ResourceNotFoundException;

    void deletePost(String username, Long id) throws PostsException, ResourceNotFoundException;

}

