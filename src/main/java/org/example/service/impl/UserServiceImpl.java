package org.example.service.impl;



import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserDTO;
import org.example.entity.*;
import org.example.exception.BaseException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.FriendshipRepository;
import org.example.repository.RoleRepository;
import org.example.repository.TutorialRepository;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.example.utils.BaseResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final FriendshipRepository friendshipRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final TutorialRepository tutorialRepository;
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public BaseResponseDTO registerAccount(UserDTO userDTO) {
        BaseResponseDTO response = new BaseResponseDTO();

        validateAccount(userDTO);

        User user = insertUser(userDTO);

        try {
            userRepository.save(user);
            response.setCode(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("Create account successfully");
        } catch (Exception e) {
            response.setCode(String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()));
            response.setMessage("Service unavailable");
        }
        return response;
    }

    private User insertUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("USER"));
        user.setRoles(roles);
        return user;
    }

    private void validateAccount(UserDTO userDTO) {
        //validate null data
        if (ObjectUtils.isEmpty(userDTO)) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Data must not empty");
        }

        //validate duplicate username
        User user = userRepository.findByUsername(userDTO.getUsername());
        if (!ObjectUtils.isEmpty(user)) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Username had existed");
        }
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Passwords do not match");
        }


        //validate role
//        List<String> roles = roleRepository.findAll().stream().map(Role::getName).toList();
//        if (!roles.contains(userDTO.getRole())) {
//            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Invalid role");
//        }
    }
    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }
    @Override
    public User getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        User userWithoutPassword = new User();
        userWithoutPassword.setId(user.getId());
        userWithoutPassword.setUsername(user.getUsername());
        userWithoutPassword.setEmail(user.getEmail());
        userWithoutPassword.setFirstName(user.getFirstName());
        userWithoutPassword.setLastName(user.getLastName());
        userWithoutPassword.setRoles(user.getRoles());
        userWithoutPassword.setFriends(user.getFriends());
        userWithoutPassword.setTutorials(user.getTutorials());
        userWithoutPassword.setAvatarUrl(user.getAvatarUrl());
        return userWithoutPassword;}
    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User addTutorialById(String userId, String tutorialId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Tutorial tutorial = tutorialRepository.findById(tutorialId)
                .orElseThrow(() -> new ResourceNotFoundException("Tutorial", "id", tutorialId));
        // create tutorials list if it doesn't exist
        List<Tutorial> tutorials = user.getTutorials();
        if (tutorials == null) {
            tutorials = new ArrayList<>();
        }

        // add tutorial to user's tutorial list
        tutorials.add(tutorial);
        user.setTutorials(tutorials);
        userRepository.save(user);

        return user;
    }
    @Override
    public User addReviewToTutorial(String tutorialId,Review review) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("Invalid token");
        }
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username);
//        String userId = currentUser.getId();
        Tutorial tutorial = tutorialRepository.findById(tutorialId)
                .orElseThrow(() -> new ResourceNotFoundException("Tutorial", "id", tutorialId));
        if (review.getRating() < 1 || review.getRating() > 10) {
            throw new IllegalArgumentException("Rating must be between 1 and 10.");
        }

        // Create userReviews map if it doesn't exist
        Map<String, Review> userReviews = tutorial.getUserReviews();
        if (userReviews == null) {
            userReviews = new HashMap<>();
        }

        // Add review to tutorial's userReviews map
        userReviews.put(username, review);
        tutorial.setUserReviews(userReviews);
        tutorialRepository.save(tutorial);
        Tutorial updatedTutorial = tutorialRepository.save(tutorial);
        if (updatedTutorial == null) {
            throw new RuntimeException("Failed to add review to tutorial.");
        }
        User updateUser = userRepository.findByUsername(username);


        return updateUser;
    }

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        User userWithoutPassword = new User();
        userWithoutPassword.setId(user.getId());
        userWithoutPassword.setUsername(user.getUsername());
        userWithoutPassword.setEmail(user.getEmail());
        userWithoutPassword.setFirstName(user.getFirstName());
        userWithoutPassword.setLastName(user.getLastName());
        userWithoutPassword.setRoles(user.getRoles());
        userWithoutPassword.setFriends(user.getFriends());
        userWithoutPassword.setTutorials(user.getTutorials());
        userWithoutPassword.setAvatarUrl(user.getAvatarUrl());
        return userWithoutPassword;
    }

    @Override
    public String uploadImage(@RequestParam("image") MultipartFile image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("Invalid token");
        }
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username);
        if (currentUser == null) {
            throw new ResourceNotFoundException("User not found");
        }
        try {
            // create file name
            String fileName = System.currentTimeMillis() + "-" + image.getOriginalFilename();
            Path filePath = Path.of(uploadPath, fileName);

            File directory = new File(uploadPath);
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    System.out.println("Directory created: " + uploadPath);
                } else {
                    throw new ResourceNotFoundException("Failed to create directory: " + uploadPath);
                }
            }

            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "/user/avatar/" + fileName;
            currentUser.setAvatarUrl(imageUrl);
            userRepository.save(currentUser);
            return imageUrl;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResourceNotFoundException("Error download image");
        }

    }

    @Override
    public User updateUser(User userToUpdate){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("Invalid token");
        }
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username);
        if (currentUser == null) {
            throw new ResourceNotFoundException("User not found");
        }

        // update

        currentUser.setUsername(userToUpdate.getUsername());
        currentUser.setEmail(userToUpdate.getEmail());
        currentUser.setFirstName(userToUpdate.getFirstName());
        currentUser.setLastName(userToUpdate.getLastName());
        currentUser.setPassword(userToUpdate.getPassword());
        return userRepository.save(currentUser);
    }
    @Override
    public ResponseEntity<List<Tutorial>> getUserTutorials(
            HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);
            List<Tutorial> tutorials = new ArrayList<>();

            tutorials = user.getTutorials();

            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @Override
    public List<User> findUsersWithCommonTutorials(List<String> tutorialIds) {
        List<User> usersWithCommonTutorials = new ArrayList<>();

        for (String tutorialId : tutorialIds) {
            Optional<Tutorial> tutorial = tutorialRepository.findById(tutorialId);
            if (tutorial.isPresent()) {
                List<User> users = userRepository.findByTutorials(tutorial.get());
                usersWithCommonTutorials.addAll(users);
            }
        }

        Set<User> uniqueUsers = new HashSet<>(usersWithCommonTutorials);
        usersWithCommonTutorials.clear();
        usersWithCommonTutorials.addAll(uniqueUsers);

        return usersWithCommonTutorials;
    }
    @Override
    public List<User> getFriendsForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("Invalid token");
        }
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username);
        String userId = currentUser.getId();
        List<Friendship> sentFriendships = friendshipRepository.findAllBySenderUserIdAndStatus(userId, FriendshipStatus.ACCEPTED);
        List<Friendship> receivedFriendships = friendshipRepository.findAllByReceiverUserIdAndStatus(userId, FriendshipStatus.ACCEPTED);
        List<User> myFriends = new ArrayList<>();

        for (Friendship friendship : sentFriendships) {
            User friend = userRepository.findById(friendship.getReceiverUserId()).orElse(null);
            if (friend != null) {
                myFriends.add(friend);
            }
        }

        for (Friendship friendship : receivedFriendships) {
            User friend = userRepository.findById(friendship.getSenderUserId()).orElse(null);
            if (friend != null) {
                myFriends.add(friend);
            }
        }

        return myFriends;
    }
}


