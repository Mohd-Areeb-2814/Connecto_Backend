package com.fb.user.service;

import com.fb.user.dto.*;
import com.fb.user.exception.UserException;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {

    public String registerUser(UserDTO userDTO) throws UserException;

    public String updateName(String email, UpdateNameDTO updateNameDTO);

    public String forgotPassword(ForgotPasswordDTO forgotPasswordDTO) throws UserException;

    public UserDTO getUserByEmail(String email) throws UserException;

    //	12-19-2024
    public String updateProfile(String email, String profilePictureUrl) throws UserException;

    public String updateBio(String email, String bio) throws UserException;

    //12-23-2024


    public List<FindNewFriendDTO> getAllUsernameAndPictures();


    public List<AddFriendDTO> getAllUsernames();

    public String updateEmail(String email, @Valid UpdateEmailDTO updateEmailDTO);

    public String logoutUser(String email);

    public String updateCoverPhoto(String email, String coverPhotoUrl) throws UserException;


}


