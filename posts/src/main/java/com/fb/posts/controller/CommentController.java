package com.fb.posts.controller;

import com.fb.posts.dto.CommentDTO;
import com.fb.posts.entity.Comment;
import com.fb.posts.exception.ResourceNotFoundException;
import com.fb.posts.exception.UnauthorizedActionException;
import com.fb.posts.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
@Validated
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private RestTemplate template;

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@RequestParam Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        List<CommentDTO> commentDTOs = comments.stream()
                .map(commentService::convertToDTO)
                .collect(Collectors.toList());
        commentDTOs.forEach((dto) -> {
            String profilePictureUrl = template.getForEntity("http://localhost:3336/user-api/v1/users/getProfileByUsername?username=" + dto.getUsername(), String.class).getBody();

            dto.setProfilePictureUrl(profilePictureUrl);
        } );
        return ResponseEntity.ok(commentDTOs);

    }

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestParam Long postId, @RequestParam String username,
                                        @RequestParam String text) throws UnauthorizedActionException {
        try {
            Comment comment = commentService.addComment(postId, username, text);
            CommentDTO responseDTO = commentService.convertToDTO(comment);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteComment")
    public ResponseEntity<?> deleteCommentByIdAndPostIdAndUsername(@RequestParam Long commentId,
                                                                   @RequestParam Long postId, @RequestParam String username) throws UnauthorizedActionException {
        try {
            String response = commentService.deleteCommentByIdAndPostIdAndUsername(commentId, postId, username);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}



