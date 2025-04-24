package com.fb.posts.controller;

import com.fb.posts.dto.AllFriendsDTO;
import com.fb.posts.dto.PostDTO;
import com.fb.posts.entity.Post;
import com.fb.posts.exception.PostsException;
import com.fb.posts.exception.ResourceNotFoundException;
import com.fb.posts.repository.PostRepository;
import com.fb.posts.service.PostService;
import com.fb.posts.utility.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/posts")
@CrossOrigin
@Validated
public class PostController {

    @Autowired
    private PostService postService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private RestTemplate template;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Create a new post

    @PostMapping
    public ResponseEntity<PostDTO> createPost(
            @RequestParam("content") String content,
            @RequestParam("privacy") String privacy,
            @RequestParam("username") String username,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            // Ensure the upload directory exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Handle image upload
            String mediaUrl = null;
            if (image != null && !image.isEmpty()) {
                String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path imagePath = uploadPath.resolve(imageName);
                Files.write(imagePath, image.getBytes());
                mediaUrl = "/uploads/" + imageName; // Save relative path of the image
            }

            // Save Post to DB

            Post post = new Post();
            post.setContent(content);
            post.setPrivacy(privacy);
            post.setUsername(username);
            post.setMediaUrl(mediaUrl); // Save the image URL in DB

            //12-24-2024
            post.setPostTimeStamp(LocalDateTime.now());
            Post createdPost = postService.createPost(post);

            // Return the created post as DTO

            return new ResponseEntity<>(convertToDTO(createdPost), HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all posts

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7); // Bearer <token>

            String loggedInUser = (String) jwtUtil.extractClaims(token).get("username"); // extractClaims() means extracting username from claim

            List<Post> posts = postService.getAllPosts();
            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<PostDTO> postDTOs = posts.stream().map(this::convertToDTO).collect(Collectors.toList());

            //12-28-2024, Areeb
            //String loggedInUser = "DrGautam";

            String url = "http://localhost:9900/allFriends/getAllFriends?username=" + loggedInUser;


            ResponseEntity<List<AllFriendsDTO>> response = template.exchange(url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<AllFriendsDTO>>() {
                    });

            List<AllFriendsDTO> loggedInUserFriendListDTO = response.getBody();



            loggedInUserFriendListDTO.forEach((item) -> {

            });

            List<PostDTO> filteredPostDTOsList = filterPosts(postDTOs, loggedInUserFriendListDTO, loggedInUser);

            filteredPostDTOsList.forEach((dto) -> {
                String profilePictureUrl = template.getForEntity("http://localhost:3336/user-api/v1/users/getProfileByUsername?username=" + dto.getUsername(), String.class).getBody();

                dto.setProfilePictureUrl(profilePictureUrl);
            } );
            return new ResponseEntity<>(filteredPostDTOsList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<PostDTO> filterPosts(List<PostDTO> postDTOs, List<AllFriendsDTO> loggedInUserFriendListDTO, String loggedInUsername) {
        // Step 1: Extract friend usernames into a Set [OprahWinfrey, VineetKumar, MohdAreeb]
        Set<String> friendUsernames = loggedInUserFriendListDTO.stream()
                .flatMap(friend -> Stream.of(friend.getSenderUsername(), friend.getReceiverUsername()))
                .collect(Collectors.toSet());

        //if loggedInUser have no friends then return public and private post of loggedInUser
        if(friendUsernames.isEmpty()) {
            List<PostDTO> filteredPosts = postDTOs.stream()
                    .filter(post -> post.getPrivacy().equals("public") ||
                            (post.getPrivacy().equals("private") &&
                                    post.getUsername().equals(loggedInUsername)))
                    .collect(Collectors.toList());

            return filteredPosts;
        }


        // Step 2: Filter posts based on privacy and friend status
        List<PostDTO> filteredPosts = postDTOs.stream()
                .filter(post -> post.getPrivacy().equals("public") ||
                        (post.getPrivacy().equals("private") &&
                                friendUsernames.contains(post.getUsername())))
                .collect(Collectors.toList());

        return filteredPosts;
    }


    @GetMapping("/getFriendPostByUsername")
    public ResponseEntity<List<PostDTO>> getAllFriendPostByUsername(@RequestParam("username") String username) {
        try {

            List<Post> posts = postRepository.getPostByUsername(username);
            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<PostDTO> postDTOs = posts.stream().map(this::convertToDTO).collect(Collectors.toList());

            String url = "http://localhost:3336/user-api/v1/users/getProfileByUsername?username=" + username;

            String profilePictureUrl = template.getForEntity(url, String.class).getBody();

            postDTOs.forEach((dto) -> {
                dto.setProfilePictureUrl(profilePictureUrl);
            });

            return new ResponseEntity<>(postDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get post by ID
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable("id") Long id) {
        try {
            Post post = postService.getPostById(id);
            return new ResponseEntity<>(convertToDTO(post), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update post by ID
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable("id") Long id, @RequestBody @Valid PostDTO postDTO) {
        try {
            Post post = convertToEntity(postDTO);
            Post updatedPost = postService.updatePost(id, post);
            return new ResponseEntity<>(convertToDTO(updatedPost), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // Delete post by ID

    @DeleteMapping("/{username}/{id}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable("username") String username, @PathVariable("id") Long id ) {
        try {
            postService.deletePost(username, id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to convert Post entity to PostDTO

    private PostDTO convertToDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setContent(post.getContent());
        postDTO.setMediaUrl(post.getMediaUrl());
        postDTO.setPrivacy(post.getPrivacy());
        postDTO.setUsername(post.getUsername());
        postDTO.setLikesCount(post.getLikesCount());
        postDTO.setPostTimeStamp(post.getPostTimeStamp());
        return postDTO;

    }

    // Helper method to convert PostDTO to Post entity

    private Post convertToEntity(PostDTO postDTO) {
        Post post = new Post();
        post.setId(postDTO.getId());
        post.setContent(postDTO.getContent());
        post.setMediaUrl(postDTO.getMediaUrl());
        post.setPrivacy(postDTO.getPrivacy());
        post.setUsername(postDTO.getUsername());
        post.setLikesCount(postDTO.getLikesCount());
        post.setPostTimeStamp(postDTO.getPostTimeStamp());
        return post;

    }

    // New GET endpoint to serve images

    @GetMapping("/uploads/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get(uploadDir).resolve(imageName);
            Resource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.IMAGE_JPEG) // You can set the correct content type for different image
                        // formats (e.g., PNG, JPEG)
                        .body(resource);

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }

    }

}

