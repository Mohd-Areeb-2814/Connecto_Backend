package com.fb.friend_profile.service;

import com.fb.friend_profile.dto.AllFriendsDTO;
import com.fb.friend_profile.dto.CommentDTO;
import com.fb.friend_profile.dto.FriendPostDTO;
import com.fb.friend_profile.dto.ProfileDTO;

import java.util.List;

public interface FriendProfileService {

	//public ProfileDTO getFriendProfile(String token);
	public ProfileDTO getFriendProfile(String username);
	
	public List<FriendPostDTO> getFriendPost(String username);
	
	public List<CommentDTO> getCommentsByPostId(Long postId);
	
	public List<AllFriendsDTO> getFriendsOfFriend(String username);
}
