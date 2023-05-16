package org.example.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.*;
import org.example.entity.Friendship;
import org.example.entity.Tutorial;
import org.example.entity.User;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.UserRepository;
import org.example.service.FriendshipService;
import org.example.service.TutorialService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Value("${file.upload-dir}")
    private String fileUploadDir;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TutorialService tutorialService;
    @Autowired
    private FriendshipService friendshipService;



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
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String token, @RequestBody User userToUpdate) {
        User updatedUser = userService.updateUser( userToUpdate);
        return ResponseEntity.ok(updatedUser);
    }
    @PostMapping("/avatars")
    public ResponseEntity<String> uploadAvatar(@RequestParam("avatar") MultipartFile file) {
        try {
            String avatarUrl = userService.saveAvatar(file);
            return ResponseEntity.ok(avatarUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload avatar");
        }
    }

}
