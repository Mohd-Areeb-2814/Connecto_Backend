package com.fb.friend_request.service.impl;

import com.fb.friend_request.dto.AddFriendDTO;
import com.fb.friend_request.dto.FindNewFriendDTO;
import com.fb.friend_request.entity.AddFriend;
import com.fb.friend_request.entity.AllFriends;
import com.fb.friend_request.repository.AddFriendRepository;
import com.fb.friend_request.repository.AllFriendsRepository;
import com.fb.friend_request.service.FindNewFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FindNewFriendServiceImpl implements FindNewFriendService {

	@Autowired
	private final RestTemplate restTemplate;

	@Autowired
	private AllFriendsRepository allFriendsRepository;

	@Autowired
	private AddFriendRepository addFriendRepository;

	public FindNewFriendServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;

	}

	@Override
	@Transactional
	public Object friendRequestSend(String senderUsername, String receiverUsername) {
		AddFriendDTO request = new AddFriendDTO();

		// AddFriend addFriendEntity=new AddFriend();

		AddFriend addFriend = addFriendRepository.findBySenderUsernameAndReceiverUsername(senderUsername,
				receiverUsername);

		if (addFriend != null) {

			addFriendRepository.delete(addFriend);

			return "add friend";
		}

		AddFriend newRequest = new AddFriend();
		newRequest.setSenderUsername(senderUsername);
		newRequest.setReceiverUsername(receiverUsername);
		newRequest.setStatus("pending");
		// newRequest.setStatus(status);
		// String senderProfilePictureUrl =
		// restTemplate.getForEntity("http://localhost:3336/user-api/v1/users/getProfileByUsername?username="
		// + senderUsername, String.class).getBody();
		// newRequest.setSenderProfilePictureUrl(senderProfilePictureUrl);
		addFriendRepository.saveAndFlush(newRequest);

		request.setSenderUsername(newRequest.getSenderUsername());
		request.setReceiverUsername(newRequest.getReceiverUsername());
		// request.setSenderProfilePictureUrl(newRequest.getSenderProfilePictureUrl());
		// request.setStatus(newRequest.getStatus());
		request.setId(newRequest.getId());

		return "cancel request";

	}

	@Override
	public List<FindNewFriendDTO> getUsernameProfile(String loggedInUsername) {
		String url = "http://localhost:3336/user-api/v1/users/username-and-profile";
		ResponseEntity<List<FindNewFriendDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<FindNewFriendDTO>>() {
				});

		List<FindNewFriendDTO> findNewFriendDTOList = response.getBody();

		//String loggedInUsername = "MohdAreeb";
		List<AllFriends> allFriendListOfLoggedInUser = allFriendsRepository
				.findByReceiverUsernameOrSenderUsername(loggedInUsername, loggedInUsername);

		return filterNewFriends(allFriendListOfLoggedInUser, findNewFriendDTOList);
	}

	//12-8-2024, Areeb
	public List<FindNewFriendDTO> filterNewFriends(List<AllFriends> allFriendListOfLoggedInUser,
			List<FindNewFriendDTO> findNewFriendDTOList) {
         // Step 1: Extract sender and receiver usernames into a Set
		Set<String> existingFriendUsernames = allFriendListOfLoggedInUser.stream()
	            .flatMap(friend -> Stream.of(friend.getSenderUsername(), friend.getReceiverUsername()))
	            .collect(Collectors.toSet());

        // Step 2: Filter findNewFriendDTOList based on existing usernames
		List<FindNewFriendDTO> filteredNewFriends = findNewFriendDTOList.stream()
				.filter(newFriend -> !existingFriendUsernames.contains(newFriend.getUsername()))
				.collect(Collectors.toList());

		return filteredNewFriends;
	}

}