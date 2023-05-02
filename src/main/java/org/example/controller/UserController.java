package org.example.controller;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.annotation.security.PermitAll;
import org.bson.Document;
import org.example.dto.FriendshipDTO;
import org.example.entity.Friendship;
import org.example.entity.Tutorial;
import org.example.entity.User;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.UserRepository;
import org.example.service.FriendshipService;
import org.example.service.TutorialService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TutorialService tutorialService;
    @Autowired
    private FriendshipService friendshipService;

    @GetMapping("/index")
    public ResponseEntity<String> index(Principal principal){
        return ResponseEntity.ok("Welcome to user page : " + principal.getName());
    }
    @PostMapping("/DDD")
    public void create(){
        String mongoUri = "mongodb+srv://sheva:sheva@cluster1.xkwwqu6.mongodb.net/test";
        MongoClient mongoClient = MongoClients.create(mongoUri);
        MongoDatabase database = mongoClient.getDatabase("bezkoder_db");


        MongoCollection<Document> collection = database.getCollection("my_documents");
        System.out.println("Connected to database");
        Document document = new Document();
        document.put("priority","Lowwwww");
        document.put("author","func");
        document.put("content","some extra text like this");

        collection.insertOne(document);
        mongoClient.close();

    }

    @GetMapping("WWW")
    public String welcome() {
        return "Welcome to spring boot heroku demo";
    }
    @PostMapping("/{userId}/tutorials/{tutorialId}")
    public ResponseEntity<User> addTutorialToUser(@PathVariable String userId, @PathVariable String tutorialId) {
        try {
            User updatedUser = userService.addTutorialById(userId, tutorialId);
            return ResponseEntity.ok(updatedUser);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/users/{userId}/friends/{friendId}/tutorials")
    public List<Tutorial> getFriendTutorials(@PathVariable String userId, @PathVariable String friendId) {
        return tutorialService.getFriendTutorials(userId, friendId);
    }
    @PostMapping("/request")
    public ResponseEntity<Friendship> createFriendshipRequest(@RequestBody FriendshipDTO friendshipDTO) {
        Friendship friendship = friendshipService.createFriendshipRequest(friendshipDTO.getSenderUserId(), friendshipDTO.getReceiverUserId());
        return new ResponseEntity<>(friendship, HttpStatus.CREATED);
    }

    @PostMapping("/confirm/{friendshipId}")
    public ResponseEntity<Friendship> confirmFriendshipRequest(@PathVariable("friendshipId") String friendshipId) {
        Friendship friendship = friendshipService.confirmFriendshipRequest(friendshipId);
        return new ResponseEntity<>(friendship, HttpStatus.OK);
    }

    @PostMapping("/reject/{friendshipId}")
    public ResponseEntity<Friendship> rejectFriendshipRequest(@PathVariable("friendshipId") String friendshipId) {
        Friendship friendship = friendshipService.rejectFriendshipRequest(friendshipId);
        return new ResponseEntity<>(friendship, HttpStatus.OK);
    }

    @PermitAll
    @GetMapping("/{senderUserId}/{receiverUserId}")
    public ResponseEntity<Friendship> getFriendshipRequest(@PathVariable("senderUserId") String senderUserId,
                                                           @PathVariable("receiverUserId") String receiverUserId) {
        Friendship friendship = friendshipService.getFriendshipRequest(senderUserId, receiverUserId);
        return new ResponseEntity<>(friendship, HttpStatus.OK);
    }


}
