package org.example.controller;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.Document;
import org.example.dto.*;
import org.example.entity.Friendship;
import org.example.entity.Tutorial;
import org.example.entity.User;
import org.example.exception.GlobalException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.UserRepository;
import org.example.service.FriendshipService;
import org.example.service.TutorialService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ServletContext servletContext;
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

    @GetMapping("/WWW")
    public String welcome() {
        return "Welcome to spring boot heroku demo";
    }
    @PostMapping("/addTutorialToUser")
    public ResponseEntity<User> addTutorialToUser(@RequestBody AddTutorialDTO addTutorialDTO) {
        try {
            User updatedUser = userService.addTutorialById(addTutorialDTO.getUserId(), addTutorialDTO.getTutorialId());
            return ResponseEntity.ok(updatedUser);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/users/friends/tutorials")
    public List<Tutorial> getFriendTutorials(@RequestBody GetFriendTutorialsDTO getFriendTutorialsDTO) {
        return tutorialService.getFriendTutorials(getFriendTutorialsDTO.getUserId(), getFriendTutorialsDTO.getFriendId());
    }
    @PostMapping("/request")
    public ResponseEntity<Friendship> createFriendshipRequest(@RequestBody FriendshipDTO friendshipDTO) {
        Friendship friendship = friendshipService.createFriendshipRequest(friendshipDTO.getSenderUserId(), friendshipDTO.getReceiverUserId());
        return new ResponseEntity<>(friendship, HttpStatus.CREATED);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Friendship> confirmFriendshipRequest(@RequestBody ConfirmFriendshipDTO confirmFriendshipDTO) {
        Friendship friendship = friendshipService.confirmFriendshipRequest(confirmFriendshipDTO.getFriendshipId(),confirmFriendshipDTO.getReceiverUserId());
        return new ResponseEntity<>(friendship, HttpStatus.OK);
    }

    @PostMapping("/reject/")
    public ResponseEntity<Friendship> rejectFriendshipRequest(@RequestBody RejectFriendshipDTO rejectFriendshipDTO) {
        Friendship friendship = friendshipService.rejectFriendshipRequest(rejectFriendshipDTO.getFriendshipId(),rejectFriendshipDTO.getRejectedById());
        return new ResponseEntity<>(friendship, HttpStatus.OK);
    }

    @PostMapping("/infoFriendship")
    public ResponseEntity<Friendship> getFriendshipRequest(@RequestBody FriendshipDTO friendshipDTO) {
        Friendship friendship = friendshipService.getFriendshipRequest(friendshipDTO.getSenderUserId(), friendshipDTO.getReceiverUserId());
        return new ResponseEntity<>(friendship, HttpStatus.OK);
    }

    @GetMapping("/currentUser")
    public ResponseEntity<User> getUser(HttpServletRequest request) {
        User currentUser = userService.getCurrentUser(request);
        return ResponseEntity.ok(currentUser);
    }
    @PutMapping("/me")
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String token, @RequestBody User userToUpdate) {
        User updatedUser = userService.updateUser(token, userToUpdate);
        return ResponseEntity.ok(updatedUser);
    }
    @PostMapping("upload-file")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String resourcesDirectory = servletContext.getRealPath("/resources");
        String imagesDirectory = resourcesDirectory + "/static/image";

        // Остаточний шлях до файлу
        String filePath = imagesDirectory + "/" + file.getOriginalFilename();

        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        return "Successfully uploaded the image";
    }


//    @PostMapping("upload-file")
//
//    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
//        System.out.println(file.getOriginalFilename());
//        System.out.println(file.getName());
//        System.out.println(file.getContentType());
//        System.out.println(file.getSize());
//
//        String projectDirectory = System.getProperty("user.dir"); // Отримуємо шлях до кореневої папки проекту
//        String imagesDirectory = projectDirectory + "/src/main/resources/static/image";
//
//        // Остаточний шлях до файлу
//        String filePath = imagesDirectory + "/" + file.getOriginalFilename();
//
//        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
//
//        return "Successfully uploaded the image";
//    }
//    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
//        System.out.println(file.getOriginalFilename());
//        System.out.println(file.getName());
//        System.out.println(file.getContentType());
//        System.out.println(file.getSize());
//
//        String Path_Directory = "/Users/macbook/IdeaProjects/LastTry/src/main/resources/static/image";
//        Files.copy(file.getInputStream(), Paths.get(Path_Directory+ File.separator+file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
//
//        return "Succesessfully Image is uploaded";
//
//
//    }




}
