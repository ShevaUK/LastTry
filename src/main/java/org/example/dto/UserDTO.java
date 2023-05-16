package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String username;

    private String password;
    private String confirmPassword;

    private String role;
    private String email;
    private String firstName;
    private String lastName;
}
