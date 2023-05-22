package org.example.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.*;
import org.example.entity.Friendship;
import org.example.entity.Tutorial;
import org.example.entity.User;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.TutorialRepository;
import org.example.repository.UserRepository;
import org.example.service.FriendshipService;
import org.example.service.TutorialService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/user")
public class UserController {
    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TutorialService tutorialService;
    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private TutorialRepository tutorialRepository;



    @PostMapping("/addTutorialToUser")
    public ResponseEntity<User> addTutorialToUser(@RequestBody AddTutorialDTO addTutorialDTO) {
        try {
            User updatedUser = userService.addTutorialById(addTutorialDTO.getUserId(), addTutorialDTO.getTutorialId());
            return ResponseEntity.ok(updatedUser);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/users/friends/tutorials")
    public List<Tutorial> getFriendTutorials(@RequestBody GetFriendTutorialsDTO getFriendTutorialsDTO) {
        return tutorialService.getFriendTutorials(getFriendTutorialsDTO.getUserId(), getFriendTutorialsDTO.getFriendId());
    }
    @PostMapping("/request")
    public ResponseEntity<Friendship> createFriendshipRequest(@RequestBody FriendshipDTO friendshipDTO) {
        Friendship friendship = friendshipService.createFriendshipRequest(friendshipDTO.getSenderUserId(), friendshipDTO.getReceiverUserId());
        return new ResponseEntity<>(friendship, HttpStatus.CREATED);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Friendship> confirmFriendshipRequest(@RequestBody ConfirmFriendshipDTO confirmFriendshipDTO) {
        Friendship friendship = friendshipService.confirmFriendshipRequest(confirmFriendshipDTO.getFriendshipId());
        return new ResponseEntity<>(friendship, HttpStatus.OK);
    }

    @PostMapping("/reject")
    public ResponseEntity<Friendship> rejectFriendshipRequest(@RequestBody RejectFriendshipDTO rejectFriendshipDTO) {
        Friendship friendship = friendshipService.rejectFriendshipRequest(rejectFriendshipDTO.getFriendshipId());
        return new ResponseEntity<>(friendship, HttpStatus.OK);
    }

    @PostMapping("/infoFriendship")
    public ResponseEntity<Friendship> getFriendshipRequest(@RequestBody FriendshipDTO friendshipDTO) {
        Friendship friendship = friendshipService.getFriendshipRequest(friendshipDTO.getSenderUserId(), friendshipDTO.getReceiverUserId());
        return new ResponseEntity<>(friendship, HttpStatus.OK);
    }

    @GetMapping("/currentUser")
    public ResponseEntity<User> getUser(HttpServletRequest request) {
        User currentUser = userService.getCurrentUser(request);
        return ResponseEntity.ok(currentUser);
    }
    @GetMapping("/{userId}/info")
    public ResponseEntity<User> getUser(@PathVariable("userId") String userId) {
        User userById = userService.getUserById(userId);
        if (userById == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userById);
    }
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String token, @RequestBody User userToUpdate) {
        User updatedUser = userService.updateUser( userToUpdate);
        return ResponseEntity.ok(updatedUser);
    }
    @PostMapping("/images")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image) {
        String avatarUrl = String.valueOf(userService.uploadImage(image));
        return ResponseEntity.ok(avatarUrl);
    }
    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
            Path imagePath = Path.of(uploadPath, fileName);
            byte[] imageBytes = Files.readAllBytes(imagePath);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/tutorials")
    public ResponseEntity<List<Tutorial>> getUserTutorials(
            HttpServletRequest request){
       return userService.getUserTutorials(request);

    }

    @GetMapping("/common-tutorials")
    public ResponseEntity<List<User>> getUsersWithCommonTutorials(@RequestParam("tutorialIds") List<String> tutorialIds) {
        List<User> users = userService.findUsersWithCommonTutorials(tutorialIds);
        if (users == null || users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }
    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<User>> getFriendsForUser(@PathVariable("userId") String userId) {
        List<User> users = userService.getFriendsForUser(userId);
        if (users == null || users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }


}
