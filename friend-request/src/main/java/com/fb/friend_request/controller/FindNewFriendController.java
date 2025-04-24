package com.fb.friend_request.controller;

import com.fb.friend_request.dto.FindNewFriendDTO;
import com.fb.friend_request.service.FindNewFriendService;
import com.fb.friend_request.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/newUser")
public class FindNewFriendController {
	
	@Autowired
	private FindNewFriendService findNewFriendService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	//Bharat
	@PostMapping("/addFriend")
	public ResponseEntity<?> toggleFriendRequestSend(
	        @RequestParam("senderUsername") String senderUsername,
	        @RequestParam("receiverUsername") String receiverUsername)
	       // @RequestParam("senderProfilePictureUrl") String senderProfilePictureUrl) 
	        {
      
	    Object response = findNewFriendService.friendRequestSend(senderUsername, receiverUsername);
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/username-profile")
	public List<FindNewFriendDTO> getAllUsernameAndProfile(@RequestHeader("Authorization") String authHeader) {
		String token = authHeader.substring(7); // Bearer <token>

		String loggedInUsername = (String) jwtUtil.extractClaims(token).get("username"); // extractClaims() means extracting username from claim
		
		return findNewFriendService.getUsernameProfile(loggedInUsername);
	
	}

	
	

}
