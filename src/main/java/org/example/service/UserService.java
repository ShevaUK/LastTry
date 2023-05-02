package org.example.service;


import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.utils.BaseResponseDTO;

public interface UserService {
    BaseResponseDTO registerAccount(UserDTO userDTO);
    User getUserById(String userId);

    User addUser(User user);

    User updateUser(User user);
    User addTutorialById(String userId, String tutorialId);
}
