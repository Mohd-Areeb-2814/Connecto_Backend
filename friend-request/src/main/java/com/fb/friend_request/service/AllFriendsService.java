package com.fb.friend_request.service;

import com.fb.friend_request.dto.AllFriendsDTO;

import java.util.List;

public interface AllFriendsService {
	
	List<AllFriendsDTO> getAllFriends(String username);

}