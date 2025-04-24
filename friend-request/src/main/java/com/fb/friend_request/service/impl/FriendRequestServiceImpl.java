package com.fb.friend_request.service.impl;

import com.fb.friend_request.dto.FriendRequestsDTO;
import com.fb.friend_request.entity.AddFriend;
import com.fb.friend_request.entity.AllFriends;
import com.fb.friend_request.repository.AddFriendRepository;
import com.fb.friend_request.repository.AllFriendsRepository;
import com.fb.friend_request.repository.FriendRequestRepository;
import com.fb.friend_request.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {
	@Autowired
	private FriendRequestRepository friendRequestRepository;
	
	@Autowired
	private AddFriendRepository addFriendRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private AllFriendsRepository friendListRepository;
	
	@Override
	public List<FriendRequestsDTO> getRequest(String receiverUsername) {
	    List<FriendRequestsDTO> friendRequestsDTOs = new ArrayList<>();
	    
	    // Assuming findByReceiverUsername returns a List<AddFriend>
	    List<AddFriend> addFriends = friendRequestRepository.findByReceiverUsername(receiverUsername);
	    
	    for (AddFriend addFriend : addFriends) {
	        FriendRequestsDTO dto = new FriendRequestsDTO();
	        dto.setSenderUsername(addFriend.getSenderUsername());
	        //dto.setReceiverUsername(addFriend.getReceiverUsername());
	       // dto.setStatus(addFriend.getStatus());
	        String senderProfilePictureUrl = restTemplate.getForEntity("http://localhost:3336/user-api/v1/users/getProfileByUsername?username=" + addFriend.getSenderUsername(), String.class).getBody();
	        dto.setSenderProfilePictureUrl(senderProfilePictureUrl);
	        friendRequestsDTOs.add(dto);
	    }
	    
	    return friendRequestsDTOs;
	}
	
	@Transactional
	public FriendRequestsDTO confirmFriendRequest(String senderUsername, String receiverUsername) {
		try {
			Optional<AddFriend> response = friendRequestRepository
					.findBySenderUsernameAndReceiverUsername(senderUsername, receiverUsername);
 
			if (response.isPresent() ) {
				AddFriend addFriend = response.get();
//				addFriend.setStatus("confirm");
//				addFriendRepository.save(addFriend);
 
				AllFriends friendList = new AllFriends();
				friendList.setSenderUsername(senderUsername);
				friendList.setReceiverUsername(receiverUsername);
				
				String senderProfilePictureUrl = restTemplate.getForEntity("http://localhost:3336/user-api/v1/users/getProfileByUsername?username=" + senderUsername, String.class).getBody();
				//friendList.setSenderProfilePictureUrl(senderProfilePictureUrl);
				friendListRepository.save(friendList);
 
				FriendRequestsDTO friendListDTO = new FriendRequestsDTO();
				//friendListDTO.setId(friendList.getId());
				friendListDTO.setSenderUsername(friendList.getSenderUsername());
				//friendListDTO.setReceiverUsername(friendList.getReceiverUsername());
				friendListDTO.setSenderProfilePictureUrl(senderProfilePictureUrl);
 
				// Delete the friend request
				addFriendRepository.delete(addFriend);
 
				return friendListDTO;
			} else {
				throw new Exception("Invalid request status or no pending request found");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
   
	
	
	
	@Transactional
	public String deleteFriendRequest(String senderUsername, String receiverUsername) {
	    try {
	        Optional<AddFriend> response = friendRequestRepository.findBySenderUsernameAndReceiverUsername(senderUsername, receiverUsername);
 
	        if (response.isPresent()) {
	        	AddFriend addFriend = response.get();
	            addFriendRepository.delete(addFriend);
	            return "Friend request deleted successfully";
	        } else {
	            throw new Exception("No pending friend request found or invalid request status");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error deleting friend request: " + e.getMessage();
	    }
 
            
	}

	@Transactional
    public String deleteFriendList(String senderUsername, String receiverUsername) {
        try {
            Optional<AllFriends> response = friendListRepository.findBySenderUsernameAndReceiverUsername(senderUsername, receiverUsername);

            if (response.isPresent()) {
            	AllFriends friend = response.get();
                friendListRepository.delete(friend); // Assuming friendListRepository is the correct repository for the friendList table
                return "Unfriend successfully";
            } else {
                throw new Exception("No pending friend request found or invalid request status");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error deleting friend request: " + e.getMessage();
        }
    }
    }



	


	
	




