package com.fb.friend_request.service;

import com.fb.friend_request.dto.FindNewFriendDTO;

import java.util.List;

public interface FindNewFriendService {

	List<FindNewFriendDTO> getUsernameProfile(String loggedInUsername);


	Object friendRequestSend(String senderUsername, String receiverUsername);



	
	

}