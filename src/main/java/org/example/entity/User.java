package org.example.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotBlank
    @Size(max = 20)
    private String username;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @Size(max = 120)
    @NotBlank
    @JsonIgnore
    private String password;
    private String firstName;
    private String lastName;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    @DBRef
    private List<Friendship> pendingFriendships;



    @DBRef
    @JsonIgnoreProperties("friends")
    private List<User> friends;
    @DBRef
    private List<Tutorial> tutorials;

    private String avatarUrl;
    public User() {
    }
    public User(String username,String email, String password,List<Tutorial> tutorials,String firstName,String lastName,String avatarUrl) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.tutorials = tutorials;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Friendship> getPendingFriendships() {return pendingFriendships;}

    public void setPendingFriendships(List<Friendship> pendingFriendships) {this.pendingFriendships = pendingFriendships;}

    public List<Tutorial> getTutorials() {
        return tutorials;
    }

    public void setTutorials(List<Tutorial> tutorials) {
        this.tutorials = tutorials;
    }

    public String getFirstName() {
        return firstName;
    }

    public List<User> getFriends() {return friends;}

    public void setFriends(List<User> friends) {this.friends = friends;}

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatarUrl() {return avatarUrl;}

    public void setAvatarUrl(String avatarUrl) {this.avatarUrl = avatarUrl;}
}
