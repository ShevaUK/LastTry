package org.example.service;


import org.example.dto.UserDTO;
import org.example.utils.BaseResponseDTO;

public interface UserService {
    BaseResponseDTO registerAccount(UserDTO userDTO);
}
