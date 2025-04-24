package com.fb.friend_request.controller;

import com.fb.friend_request.dto.FriendRequestsDTO;
import com.fb.friend_request.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/friendRequest")
public class FriendRequestsController {

	@Autowired
	FriendRequestService friendRequestService;

	// if confirm view name should be save in db and view profile option will appear

	@PostMapping("/confirm")
	public FriendRequestsDTO confirmFriendRequest(@RequestParam("senderUsername") String senderUsername,
												  @RequestParam("receiverUsername") String receiverUsername) {
		return friendRequestService.confirmFriendRequest(senderUsername, receiverUsername);

	}

//	
	// this data will come from view profile service restTemplate 12/20/2024 12:00
	@GetMapping
	public void viewProfile() {

	}

	@DeleteMapping("/deleteRequest")
    public ResponseEntity<Object> deleteFriendRequest(
    	@RequestParam("senderUsername") String senderUsername,
  		@RequestParam("receiverUsername") String receiverUsername){
   String response = friendRequestService.deleteFriendRequest(senderUsername, receiverUsername);
        return ResponseEntity.ok(response);
    }

	@GetMapping("/getRequest")
	public List<FriendRequestsDTO> getRequest(@RequestParam("receiverUsername") String receiverUsername) {

		return friendRequestService.getRequest(receiverUsername);

	}
	
	@DeleteMapping("/unFriend")
    public ResponseEntity<Object> deleteFriendList(
    	@RequestParam("senderUsername") String senderUsername,
  		@RequestParam("receiverUsername") String receiverUsername){
   String response = friendRequestService.deleteFriendList(senderUsername, receiverUsername);
        return ResponseEntity.ok(response);
    }

}