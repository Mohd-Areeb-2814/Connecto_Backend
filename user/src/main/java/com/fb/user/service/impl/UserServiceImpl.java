package com.fb.user.service.impl;

import com.fb.user.dto.*;
import com.fb.user.entity.User;
import com.fb.user.exception.UserException;
import com.fb.user.repository.UserRepository;
import com.fb.user.service.UserService;
import com.fb.user.utility.ExceptionConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

@Transactional

public class UserServiceImpl implements UserService {

    @Autowired

    private UserRepository userRepository;

    @Autowired

    private PasswordEncoder passwordEncoder;

    @Override

    public String registerUser(UserDTO userDTO) throws UserException {

        Optional<User> optional = userRepository.findByEmail(userDTO.getEmail());

        if (optional.isEmpty()) {

            String username = userDTO.getFirstName() + " " + userDTO.getSurname();

            if (userRepository.existsByUsername(username)) {

                throw new UserException(ExceptionConstants.USERNAME_ALREADY_PRESENT.toString());

            }

            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            User savedUser = userRepository.save(mapToEntity(userDTO));

            return "User Registered Successfully with email " + savedUser.getEmail();

        } else {

            throw new UserException(ExceptionConstants.USER_ALREADY_PRESENT.toString());

        }

    }

    //  09/01
    public String logoutUser(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setOnline(false);
            userRepository.save(user);

        }

        return "User logged out successfully";
    }

    @Override
    public String updateName(String email, @Valid UpdateNameDTO updateNameDTO) {
        Optional<User> mail = userRepository.findByEmail(email);
        if (mail.isPresent()) {
            User entity = mail.get();
            String uniqueUsername = generateUniqueUsername(updateNameDTO.getFirstName(), updateNameDTO.getSurname());
            entity.setUsername(uniqueUsername);
            entity.setFirstName(updateNameDTO.getFirstName());
            entity.setSurname(updateNameDTO.getSurname());
            userRepository.save(entity);

        }
        return "Username updated successfully";
    }

    @Override
    public String updateEmail(String email, @Valid UpdateEmailDTO updateEmailDTO) {
        Optional<User> mail=userRepository.findByEmail(email);
        if(mail.isPresent()) {
            User entity=mail.get();
            entity.setEmail(updateEmailDTO.getEmail());
            userRepository.save(entity);
        }
        return "Email Updated Successfully";
    }
    @Override

    public UserDTO getUserByEmail(String email) throws UserException {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {

            throw new UserException(ExceptionConstants.USER_NOT_FOUND.toString());

        }

        return mapToDTO(user.get());

    }

    @Override

    public String forgotPassword(ForgotPasswordDTO forgotPasswordDTO) throws UserException {

        Optional<User> optional = userRepository.findByEmail(forgotPasswordDTO.getEmail());

        if (optional.isEmpty()) {

            throw new UserException(ExceptionConstants.USER_NOT_FOUND.toString());

        }

        User user = optional.get();

        user.setPassword(passwordEncoder.encode(forgotPasswordDTO.getNewPassword()));

        userRepository.save(user);

        return "Password reset successfully";

    }

    @Override

    public String updateProfile(String email, String profilePictureUrl) throws UserException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ExceptionConstants.USER_NOT_FOUND.toString()));

        user.setProfilePictureUrl(profilePictureUrl);

        try {

            userRepository.save(user);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

        return "Profile Updated Successfully";

    }

    @Override
    public String updateCoverPhoto(String email, String coverPhotoUrl)  throws UserException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ExceptionConstants.USER_NOT_FOUND.toString()));

        user.setCoverPhotoUrl(coverPhotoUrl);

        try {

            userRepository.save(user);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

        return "Cover photo Updated Successfully";

    }

    @Override

    public String updateBio(String email, String bio) throws UserException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ExceptionConstants.USER_NOT_FOUND.toString()));

        user.setBio(bio);

        try {

            userRepository.save(user);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

        return "Bio Updated Successfully";

    }

    private UserDTO mapToDTO(User user) {

        UserDTO dto = new UserDTO();

        dto.setUserId(user.getUserId());

        dto.setFirstName(user.getFirstName());

        dto.setSurname(user.getSurname());

        dto.setEmail(user.getEmail());

        dto.setGender(user.getGender());

        dto.setDateOfBirth(user.getDateOfBirth());

        dto.setUsername(user.getUsername());

        dto.setProfilePictureUrl(user.getProfilePictureUrl());

        dto.setBio(user.getBio());

        dto.setOnline(user.getOnline());

        dto.setCoverPhotoUrl(user.getCoverPhotoUrl());
        return dto;

    }

    private User mapToEntity(UserDTO userDTO) {

        User entity = new User();

        entity.setUserId(userDTO.getUserId());

        entity.setFirstName(userDTO.getFirstName());

        entity.setSurname(userDTO.getSurname());

        entity.setEmail(userDTO.getEmail());

        entity.setGender(userDTO.getGender());

        entity.setPassword(userDTO.getPassword());

        entity.setDateOfBirth(userDTO.getDateOfBirth());

        entity.setUsername(generateUniqueUsername(userDTO.getFirstName(), userDTO.getSurname()));

        entity.setProfilePictureUrl(userDTO.getProfilePictureUrl());

        entity.setBio(userDTO.getBio());
        entity.setOnline(userDTO.getOnline());
        entity.setCoverPhotoUrl(userDTO.getCoverPhotoUrl());

        return entity;

    }

    private String generateUniqueUsername(String firstName, String surname) {

        String baseUsername = firstName.concat(surname);

        String uniqueUsername = baseUsername;

        int counter = 1;

        while (userRepository.existsByUsername(uniqueUsername)) {

            uniqueUsername = baseUsername + counter;

            counter++;

        }

        return uniqueUsername;

    }

    //12-23-2024

    @Override
    public List<FindNewFriendDTO> getAllUsernameAndPictures() {

        List<User> userList =  userRepository.findAll();

        List<FindNewFriendDTO> listFriends = new ArrayList<>();

        userList.forEach((user) -> {
            FindNewFriendDTO dto = new FindNewFriendDTO();
            dto.setUsername(user.getUsername());
            dto.setProfilePictureUrl(user.getProfilePictureUrl());
            listFriends.add(dto);
        });

        return listFriends;


    }

    @Override
    public List<AddFriendDTO> getAllUsernames() {
        List<String> userList=userRepository.findAllUsernames();


        List<AddFriendDTO> usernameList=new ArrayList<>();
        for (String username : userList) {
            AddFriendDTO dto = new AddFriendDTO();
            dto.setReceiverUsername(username);
            usernameList.add(dto);
        }
        return usernameList;

    }

}


