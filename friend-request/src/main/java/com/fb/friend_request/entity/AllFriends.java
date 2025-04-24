package com.fb.friend_request.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="AllFriends")
public class AllFriends {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String senderUsername;
	private String receiverUsername;

		
}