package org.example.service;


import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.UserDTO;
import org.example.entity.Tutorial;
import org.example.entity.User;
import org.example.utils.BaseResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


public interface UserService {
    public User getCurrentUser(HttpServletRequest request);
    BaseResponseDTO registerAccount(UserDTO userDTO);
    User getUserById(String userId);

    User addUser(User user);
    public User updateUser( User userToUpdate);
    User addTutorialById(String userId, String tutorialId);

    public ResponseEntity<List<Tutorial>> getUserTutorials(HttpServletRequest request);
    public String uploadImage(@RequestParam("image") MultipartFile image);
    List<User> findUsersWithCommonTutorials(List<String> tutorialIds);
    public List<User> getFriendsForUser(String userId);
}
