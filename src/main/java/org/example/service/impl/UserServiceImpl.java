package org.example.service.impl;



import lombok.RequiredArgsConstructor;
import org.example.dto.UserDTO;
import org.example.entity.Role;
import org.example.entity.Tutorial;
import org.example.entity.User;
import org.example.exception.BaseException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.RoleRepository;
import org.example.repository.TutorialRepository;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.example.utils.BaseResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final TutorialRepository tutorialRepository;

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
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(userDTO.getRole()));
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


        //validate role
        List<String> roles = roleRepository.findAll().stream().map(Role::getName).toList();
        if (!roles.contains(userDTO.getRole())) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Invalid role");
        }
    }
    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }
    @Override
    public User updateUser(User user) {
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

}
