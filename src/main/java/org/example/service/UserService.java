package org.example.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.utils.BaseResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    public User getCurrentUser(HttpServletRequest request,
                               HttpServletResponse response);
    BaseResponseDTO registerAccount(UserDTO userDTO);
    User getUserById(String userId);

    User addUser(User user);
    public User updateUser( User userToUpdate);
    User addTutorialById(String userId, String tutorialId);
    String saveAvatar(MultipartFile file)throws IOException;
}
