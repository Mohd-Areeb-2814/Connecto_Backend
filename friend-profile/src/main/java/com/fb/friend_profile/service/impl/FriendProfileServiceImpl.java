package com.fb.friend_profile.service.impl;

import com.fb.friend_profile.dto.AllFriendsDTO;
import com.fb.friend_profile.dto.CommentDTO;
import com.fb.friend_profile.dto.FriendPostDTO;
import com.fb.friend_profile.dto.ProfileDTO;
import com.fb.friend_profile.service.FriendProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@Transactional
public class FriendProfileServiceImpl implements FriendProfileService {

	@Autowired
	private RestTemplate restTemplate;

//	@Override
//	public ProfileDTO getFriendProfile(String token) {
//		String url = "http://localhost:3336/user-api/v1/users/profile";
//
//	    // Step 1: Create the Authorization Header
//	    HttpHeaders headers = new HttpHeaders();
//	    headers.set("Authorization", "Bearer " + token);
//
//	    // Step 2: Create the HttpEntity
//	    HttpEntity<Void> entity = new HttpEntity<>(headers);
//	    
//          System.out.println("Before RestTemplate Call");
//
//	    // Step 3: Use the RestTemplate to make the request
//	    ResponseEntity<ProfileDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<ProfileDTO>() {
//	    });
//	    
//          System.out.println("After RestTemplate Call");
//		
//		ProfileDTO profileDTO = response.getBody();
//		

//		
//		return profileDTO;
//	}

	@Override
	public ProfileDTO getFriendProfile(String username) {
		// Build the URL with the username as a query parameter
		String url = UriComponentsBuilder
				.fromHttpUrl("http://localhost:3336/user-api/v1/users/getFriendProfileByUsername")
				.queryParam("username", username).toUriString();

	        System.out.println("Before RestTemplate Call");

		ResponseEntity<ProfileDTO> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<ProfileDTO>() {
				});

	        System.out.println("After RestTemplate Call");

		ProfileDTO profileDTO = response.getBody();

	

		return profileDTO;
	}

	@Override
	public List<FriendPostDTO> getFriendPost(String username) {
		
		String url = UriComponentsBuilder
				.fromHttpUrl("http://localhost:8082/post-service/posts/getFriendPostByUsername")
				.queryParam("username", username).toUriString();

                System.out.println("Before RestTemplate Call");

		ResponseEntity<List<FriendPostDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<FriendPostDTO>>() {
				});
		
		List<FriendPostDTO> friendPostDTOs = response.getBody();

                System.out.println("After RestTemplate Call");
		return friendPostDTOs;
	}

	@Override
	public List<CommentDTO> getCommentsByPostId(Long postId) {
		
		String url = UriComponentsBuilder
				.fromHttpUrl("http://localhost:8082/post-service/api/comments")
				.queryParam("postId", postId).toUriString();

                System.out.println("Before RestTemplate Call");

		ResponseEntity<List<CommentDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<CommentDTO>>() {
				});
		
		List<CommentDTO> friendPostComments = response.getBody();
                System.out.println("After RestTemplate Call");

		return friendPostComments;
	}

	@Override
	public List<AllFriendsDTO> getFriendsOfFriend(String username) {
		String url = UriComponentsBuilder
				.fromHttpUrl("http://localhost:9900/allFriends/getAllFriends")
				.queryParam("username", username).toUriString();

                System.out.println("Before RestTemplate Call");

		ResponseEntity<List<AllFriendsDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<AllFriendsDTO>>() {
				});
		
		List<AllFriendsDTO> allFriends = response.getBody();

                System.out.println("After RestTemplate Call");
		return allFriends;
	}

}
