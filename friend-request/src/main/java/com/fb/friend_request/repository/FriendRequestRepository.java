package com.fb.friend_request.repository;

import com.fb.friend_request.entity.AddFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<AddFriend, Long> {
	
	List<AddFriend> findByReceiverUsername(String receiverUsername);
	Optional<AddFriend> findBySenderUsername(String senderUsername);
	
	Optional<AddFriend> findBySenderUsernameAndReceiverUsername(String senderUsername, String receiverUsername);
	
	


}