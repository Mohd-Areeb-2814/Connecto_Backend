package com.fb.user.controller;

import com.fb.user.dto.*;
import com.fb.user.entity.User;
import com.fb.user.exception.UserException;
import com.fb.user.repository.UserRepository;
import com.fb.user.service.UserService;
import com.fb.user.service.impl.AuthService;
import com.fb.user.service.impl.JwtService;
import com.fb.user.utility.ExceptionConstants;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/user-api/v1/users")
@Validated
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired

    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // Register User

    // 12-23-2024

    @GetMapping("/username-and-profile")
    public ResponseEntity<List<FindNewFriendDTO>> getAllUsernameAndPictures() {
        // return new
        // ResponseEntity<FindNewFriendDTO>(userService.getAllFirstNameAndPictures(),
        // HttpStatus.OK);
        return new ResponseEntity<>(userService.getAllUsernameAndPictures(), HttpStatus.OK);
    }


    @GetMapping("/check-email")

    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {

        boolean emailExists = userRepository.existsByEmail(email);

        Map<String, Object> response = new HashMap<>();

        response.put("exists", emailExists);

        return ResponseEntity.ok(response);

    }



    @GetMapping("/getAllUsername")
    public ResponseEntity<List<AddFriendDTO>> getAllId() {
        // return new
        // ResponseEntity<FindNewFriendDTO>(userService.getAllFirstNameAndPictures(),
        // HttpStatus.OK);
        return new ResponseEntity<>(userService.getAllUsernames(), HttpStatus.OK);
    }
//

    @GetMapping("/getProfileByUsername")
    public ResponseEntity<String> getProfileByUsername(@RequestParam("username") String username) throws UserException {
        // return new
        // ResponseEntity<FindNewFriendDTO>(userService.getAllFirstNameAndPictures(),
        // HttpStatus.OK);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(ExceptionConstants.USER_NOT_FOUND.toString()));

        return new ResponseEntity<>(user.getProfilePictureUrl(), HttpStatus.OK);
    }



    @GetMapping("/getStatusByUsername")
    public ResponseEntity<Boolean> getStatusByUsername(@RequestParam("username") String username) throws UserException {
        // return new
        // ResponseEntity<FindNewFriendDTO>(userService.getAllFirstNameAndPictures(),
        // HttpStatus.OK);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(ExceptionConstants.USER_NOT_FOUND.toString()));

        return new ResponseEntity<>(user.getOnline(), HttpStatus.OK);
    }

    @GetMapping("/getFriendProfileByUsername")
    public ResponseEntity<User> getFriendProfileByUsername(@RequestParam("username") String username) throws UserException {
        // return new
        // ResponseEntity<FindNewFriendDTO>(userService.getAllFirstNameAndPictures(),
        // HttpStatus.OK);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(ExceptionConstants.USER_NOT_FOUND.toString()));

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/register")

    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDTO userDTO) throws UserException {

        String res = userService.registerUser(userDTO);

        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }

    // Forgot Password

    @PutMapping("/forgot-password")

    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO)
            throws UserException {

        String response = userService.forgotPassword(forgotPasswordDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }




    // Generate Token (Login)

    @CircuitBreaker(name = "userService", fallbackMethod = "loginFallBack")

    @PostMapping("/login")

    public ResponseEntity<String> getToken(@RequestBody AuthRequest authRequest) throws BadCredentialsException {

        try {


            System.out.println("Starting authentication for email:" + authRequest.getEmail());


            Authentication authenticate = authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())

            );



            if (authenticate.isAuthenticated()) {

                User loggedInUser = userRepository.findByEmail(authRequest.getEmail()).get(); // Fetch the User object



                // Extract username and email from the User object

                String loggedInUsername = loggedInUser.getUsername();

                String loggedInEmail = loggedInUser.getEmail();



                // Generate the token with both username and email

                String token = authService.generateToken(loggedInUsername, loggedInEmail); // Pass both email and username


                //  online status 1-9
                Optional<User> response=userRepository.findByEmail(loggedInEmail);
                User entity=response.get();
                entity.setOnline(true);
                userRepository.save(entity);


                return new ResponseEntity<>(token, HttpStatus.OK);

            } else {

                throw new BadCredentialsException("Authentication failed: Invalid email or password.");

            }

        } catch (BadCredentialsException e) {

            System.out.println("Authentication failed for email: " + authRequest.getEmail());

            throw new BadCredentialsException("Authentication failed: Invalid email or password.");

        }

    }


    public ResponseEntity<String> loginFallBack(AuthRequest authRequest, Throwable exception) {

        String message = "";

        //12-24-2-24
        if (exception.getMessage().equals("Authentication failed: Invalid email or password.")) {

            message = "Authentication failed: Invalid email or password.";

        } else if (exception.getMessage()
                .equals("CircuitBreaker 'userService' is OPEN and does not permit further calls")) {
            message = "You have entered maximum attempts of login. Please hold for 1 min.";

        }

        return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);

    }



    //09/01
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Bearer <token>
        String email = jwtService.extractUsername(token);
        String message = userService.logoutUser(email);
        return ResponseEntity.ok(message);
    }

    // Validate Token

    @GetMapping("/validate")

    public String validateToken(@RequestParam("token") String token) {

        authService.validateToken(token);

        return "Token is valid";

    }

    // Get User Profile

    @GetMapping("/profile")

    public ResponseEntity<UserDTO> getUser(@RequestHeader("Authorization") String authHeader) throws UserException {

        String token = authHeader.substring(7); // Bearer <token>

        String username = jwtService.extractUsername(token);

        UserDTO userDTO = userService.getUserByEmail(username);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);

    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<String> getProfileUrl(@RequestHeader("Authorization") String authHeader,
                                                @PathVariable String username) throws UserException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserException("user not found"));
        return new ResponseEntity<String>(user.getProfilePictureUrl(), HttpStatus.OK);
    }

    // Update User Profile

// @PutMapping("/profile")

// public ResponseEntity<String> updateProfile(@RequestHeader("Authorization") String authHeader,

// @Valid @RequestBody UserDTO userDTO) throws UserException {

// String token = authHeader.substring(7); // Bearer <token>

// String username = jwtService.extractUsername(token);

//

// Optional<User> user = userService.getUserByEmail(username);

// if (user.isPresent()) {

// userService.updateProfile(userDTO);

// return new ResponseEntity<>("Profile updated successfully", HttpStatus.OK);

// } else {

// throw new UserException(ExceptionConstants.USER_NOT_FOUND.toString());

// }

// }

    @PutMapping("profile/update")

    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String authHeader,

                                           @RequestParam(value = "image", required = false) MultipartFile image) throws UserException {

        String token = authHeader.substring(7); // Bearer <token>

        String email = jwtService.extractUsername(token); // extractUsername() means extracting email from token

        try {

            // Ensure the upload directory exists

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {

                Files.createDirectories(uploadPath);

            }

            // Handle image upload

            String profilePictureUrl = null;

            if (image != null && !image.isEmpty()) {

                String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename();

                Path imagePath = uploadPath.resolve(imageName);

                Files.write(imagePath, image.getBytes());

                profilePictureUrl = "/uploads/" + imageName; // Save relative path of the image

            }

            String message = userService.updateProfile(email, profilePictureUrl);

            return ResponseEntity.ok(message);

        } catch (IOException e) {

            e.printStackTrace();

            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    //09-01 cover image update

    @PutMapping("cover/update")

    public ResponseEntity<?> updateCoverPhoto(@RequestHeader("Authorization") String authHeader,

                                              @RequestParam(value = "image", required = false) MultipartFile image) throws UserException {

        String token = authHeader.substring(7); // Bearer <token>

        String email = jwtService.extractUsername(token); // extractUsername() means extracting email from token

        try {

            // Ensure the upload directory exists

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {

                Files.createDirectories(uploadPath);

            }

            // Handle image upload

            String coverPhotoUrl = null;

            if (image != null && !image.isEmpty()) {

                String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename();

                Path imagePath = uploadPath.resolve(imageName);

                Files.write(imagePath, image.getBytes());

                coverPhotoUrl = "/uploads/" + imageName; // Save relative path of the image

            }

            String message = userService.updateCoverPhoto(email, coverPhotoUrl);

            return ResponseEntity.ok(message);

        } catch (IOException e) {

            e.printStackTrace();

            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }


    @GetMapping("/uploads/{imageName}")

    public ResponseEntity<Resource> getProfile(@PathVariable String imageName) {

        try {

            Path imagePath = Paths.get(uploadDir).resolve(imageName);

            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {

                return ResponseEntity.ok()

                        .header(HttpHeaders.CONTENT_DISPOSITION,

                                "attachment; filename=\"" + resource.getFilename() + "\"")

                        .contentType(MediaType.IMAGE_JPEG) // You can set the correct content type for different image

                        // formats (e.g., PNG, JPEG)

                        .body(resource);

            } else {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

            }

        } catch (MalformedURLException e) {

            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }

    }
    @PutMapping("/updateFirstnameAndSurname")
    public ResponseEntity<?> updateName(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody UpdateNameDTO updateNameDTO){
        String token = authHeader.substring(7); // Bearer <token>

        String email = jwtService.extractUsername(token); // extractUsername() means extracting email from token
        String message = userService.updateName(email, updateNameDTO);
        return ResponseEntity.ok(message);

    }

    @PutMapping("/updateEmail")
    public ResponseEntity<?> updateEmail(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody UpdateEmailDTO updateEmailDTO){
        String token=authHeader.substring(7);
        String email=jwtService.extractUsername(token);
        String response=userService.updateEmail(email,updateEmailDTO);
        return ResponseEntity.ok(response);
    }


    @PutMapping("bio/update")

    public ResponseEntity<?> updateBio(@RequestHeader("Authorization") String authHeader, @RequestParam String bio)
            throws UserException {

        String token = authHeader.substring(7); // Bearer <token>

        String email = jwtService.extractUsername(token); // extractUsername() means extracting email from token

        String message = userService.updateBio(email, bio);

        return ResponseEntity.ok(message);

    }
}
