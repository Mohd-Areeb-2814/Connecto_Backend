package com.fb.posts.service.impl;

import com.fb.posts.entity.Like;
import com.fb.posts.entity.Post;
import com.fb.posts.exception.ResourceNotFoundException;
import com.fb.posts.repository.LikeRepository;
import com.fb.posts.repository.PostRepository;
import com.fb.posts.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikeServiceImpl implements LikeService {

    @Autowired

    private LikeRepository likeRepository;

    @Autowired

    private PostRepository postRepository;

    public Object toggleLikePost(Long postId, String username) throws ResourceNotFoundException {

        Post post = postRepository.findById(postId)

                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        Like existingLike = likeRepository.findByPostIdAndUsername(postId, username);

        if (existingLike != null) {

            likeRepository.delete(existingLike);

            Long likesCount= likeRepository.countByPostId(postId);

            post.setLikesCount(likesCount);

            postRepository.save(post);

            return "post unliked successfully";

        }

        Like like = new Like();

        like.setPost(post);

        like.setUsername(username);

        likeRepository.save(like);

        Long likesCount= likeRepository.countByPostId(postId);

        post.setLikesCount(likesCount);

        postRepository.save(post);

        return "post like successfull";

    }

//

    @Override

    public Like likePost(Long postId, String username) {

        // TODO Auto-generated method stub

        return null;

    }

}

