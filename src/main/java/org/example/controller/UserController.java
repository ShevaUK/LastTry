package org.example.controller;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.entity.Tutorial;
import org.example.entity.User;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.UserRepository;
import org.example.service.TutorialService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
