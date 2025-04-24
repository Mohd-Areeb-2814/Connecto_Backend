package com.fb.posts.service.impl;

import com.fb.posts.entity.Comment;
import com.fb.posts.entity.Like;
import com.fb.posts.entity.Post;
import com.fb.posts.exception.ResourceNotFoundException;
import com.fb.posts.repository.CommentRepository;
import com.fb.posts.repository.LikeRepository;
import com.fb.posts.repository.PostRepository;
import com.fb.posts.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long id) throws ResourceNotFoundException {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
    }

    @Override
    public Post updatePost(Long id, Post postDetails) throws ResourceNotFoundException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        post.setContent(postDetails.getContent());
        post.setMediaUrl(postDetails.getMediaUrl());
        post.setPrivacy(postDetails.getPrivacy());
        post.setUsername(postDetails.getUsername());

        return postRepository.save(post);
    }

    @Override
    public void deletePost(String username, Long id) throws ResourceNotFoundException {
        Post post = postRepository.findByUsernameAndId(username, id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with username: " + username + " and id: " + id));

        List<Comment> deleteAllComment = commentRepository.getCommentByPostId(id);
        commentRepository.deleteAllInBatch(deleteAllComment);

        List<Like> deleteAllLike = likeRepository.getLikeByPostId(id);
        likeRepository.deleteAllInBatch(deleteAllLike);

        postRepository.delete(post);
    }
}