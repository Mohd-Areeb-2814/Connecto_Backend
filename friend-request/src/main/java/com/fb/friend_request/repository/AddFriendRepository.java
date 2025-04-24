package com.fb.friend_request.repository;

import com.fb.friend_request.entity.AddFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddFriendRepository  extends JpaRepository<AddFriend, Long> {
	

	AddFriend findBySenderUsernameAndReceiverUsername(String senderUsername, String receiverUsername);

	Optional<AddFriend> findByReceiverUsername(String receiverUsername);

	Optional<AddFriend> findBySenderUsername(String senderUsername);

}