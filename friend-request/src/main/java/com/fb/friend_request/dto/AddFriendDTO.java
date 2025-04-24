package com.fb.friend_request.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class AddFriendDTO {

	
	
	    private Long id;
	    
	    
	    private String senderUsername;
	    
	    //@JsonProperty("userId")
	    private String receiverUsername;

	   // private String status; // PENDING, ACCEPTED, DECLINED

	    private String senderProfilePictureUrl;
	    
	    private String status;
}