package com.fb.friend_request.service;

import com.fb.friend_request.dto.FriendRequestsDTO;

import java.util.List;

public interface FriendRequestService {

	

	List<FriendRequestsDTO> getRequest(String receiverUsername);

	FriendRequestsDTO confirmFriendRequest(String senderUsername, String receiverUsername);

	String deleteFriendRequest(String senderUsername, String receiverUsername);
	
	String deleteFriendList(String senderUsername, String receiverUsername);

	

}