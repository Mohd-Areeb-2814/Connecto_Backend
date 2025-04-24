package com.fb.friend_profile.controller;

import com.fb.friend_profile.dto.AllFriendsDTO;
import com.fb.friend_profile.dto.CommentDTO;
import com.fb.friend_profile.dto.FriendPostDTO;
import com.fb.friend_profile.dto.ProfileDTO;
import com.fb.friend_profile.service.FriendProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/friend-profile-api")
public class FriendProfileController {

	@Autowired
	private FriendProfileService friendProfileService;
	
//	@Autowired
//	private JwtUtil jwtUtil;
	
//	@GetMapping("/profile")
//	public ResponseEntity<?> getFriendProfile(@RequestHeader("Authorization") String authHeader){
//		
//		String token = authHeader.substring(7); // Bearer <token>
//		
//		ProfileDTO profileDTO =  friendProfileService.getFriendProfile(token);
//		
//		return new ResponseEntity<ProfileDTO> (profileDTO, HttpStatus.OK);
//	}
	
	@GetMapping("/friendProfile")
	public ResponseEntity<?> getFriendProfile(@RequestParam("username") String username){
		
		ProfileDTO profileDTO =  friendProfileService.getFriendProfile(username);
		
		return new ResponseEntity<ProfileDTO> (profileDTO, HttpStatus.OK);
	}
	
	@GetMapping("/friendPost")
	public ResponseEntity<List<FriendPostDTO>> getFriendPost(@RequestParam("username") String username){
		
		List<FriendPostDTO> friendPostDTOs =  friendProfileService.getFriendPost(username);
		
		return new ResponseEntity<List<FriendPostDTO>> (friendPostDTOs, HttpStatus.OK);
	}
	
	@GetMapping("/postComments")
	public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@RequestParam Long postId) {
		List<CommentDTO> friendPostComments =  friendProfileService.getCommentsByPostId(postId);
		return ResponseEntity.ok(friendPostComments);

	}
	
	@GetMapping("/friendsOfFriend")
	public ResponseEntity<List<AllFriendsDTO>> getFriendsOfFriend(@RequestParam("username") String username) {
		List<AllFriendsDTO> friendPostComments =  friendProfileService.getFriendsOfFriend(username);
		return ResponseEntity.ok(friendPostComments);

	}
}