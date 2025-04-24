package com.fb.friend_request.repository;

import com.fb.friend_request.entity.AllFriends;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AllFriendsRepository extends JpaRepository<AllFriends, Long> {
	
	//List<AllFriends> findByReceiverUsername(String receiverUsername);

	List<AllFriends> findByReceiverUsernameOrSenderUsername(String receiverUsername, String senderUsername);

	Optional<AllFriends> findBySenderUsernameAndReceiverUsername(String senderUsername, String receiverUsername);

}