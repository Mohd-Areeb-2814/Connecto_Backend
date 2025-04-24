package com.fb.friend_request.service.impl;

import com.fb.friend_request.dto.AllFriendsDTO;
import com.fb.friend_request.entity.AllFriends;
import com.fb.friend_request.repository.AllFriendsRepository;
import com.fb.friend_request.service.AllFriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AllFriendsServiceImpl  implements AllFriendsService {
	
	@Autowired
	private AllFriendsRepository allFriendsRepository;

	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public List<AllFriendsDTO> getAllFriends(String username) {
       
		//String receiverUsername=new String(username);
		List<AllFriends> response=allFriendsRepository.findByReceiverUsernameOrSenderUsername(username,username);
		
		List<AllFriendsDTO> allFriendsDTOs=new ArrayList<>();
		
		if(response!=null) {
			for(AllFriends entity:response) {
				AllFriendsDTO dto=new AllFriendsDTO();
				dto.setId(entity.getId());
				dto.setSenderUsername(entity.getSenderUsername());
				dto.setReceiverUsername(entity.getReceiverUsername());
				
				String senderProfilePictureUrl = restTemplate.getForEntity("http://localhost:3336/user-api/v1/users/getProfileByUsername?username=" + entity.getSenderUsername(), String.class).getBody();
				String receiverProfilePictureUrl = restTemplate.getForEntity("http://localhost:3336/user-api/v1/users/getProfileByUsername?username=" + entity.getReceiverUsername(), String.class).getBody();
				
				Boolean senderStatus = restTemplate.getForEntity("http://localhost:3336/user-api/v1/users/getStatusByUsername?username=" + entity.getSenderUsername(), Boolean.class).getBody();
				Boolean receiverStatus = restTemplate.getForEntity("http://localhost:3336/user-api/v1/users/getStatusByUsername?username=" + entity.getReceiverUsername(), Boolean.class).getBody();
				
				
//				String status = restTemplate.getForEntity("", null)
				dto.setSenderProfilePictureUrl(senderProfilePictureUrl);
				dto.setReceiverProfilePictureUrl(receiverProfilePictureUrl);
				
				dto.setSenderStatus(senderStatus);
				dto.setReceiverStatus(receiverStatus);
				
				
				allFriendsDTOs.add(dto);
				
			}
			
		}
		
 
		return allFriendsDTOs;
	}

}