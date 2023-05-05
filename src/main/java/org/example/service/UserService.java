package org.example.service;


import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.utils.BaseResponseDTO;

public interface UserService {
    public User getCurrentUser(HttpServletRequest request);
    BaseResponseDTO registerAccount(UserDTO userDTO);
    User getUserById(String userId);

    User addUser(User user);
    public User updateUser(String token, User userToUpdate);
    User addTutorialById(String userId, String tutorialId);
}
