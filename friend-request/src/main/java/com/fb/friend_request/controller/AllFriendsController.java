package com.fb.friend_request.controller;

import com.fb.friend_request.dto.AllFriendsDTO;
import com.fb.friend_request.service.AllFriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/allFriends")
public class AllFriendsController {
	
	@Autowired
	private AllFriendsService allFriendsService;
	
	@GetMapping("/getAllFriends")
	public List<AllFriendsDTO> getAllFriends(@RequestParam("username") String username){
		
		return allFriendsService.getAllFriends(username);
		
	}

}