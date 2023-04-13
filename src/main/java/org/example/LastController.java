package org.example;

import org.springframework.web.bind.annotation.*;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


import org.bson.Document;




@RestController
@RequestMapping("/")
public class LastController {
    @PostMapping ("DDD")
    public void create(){
        String mongoUri = "mongodb+srv://sheva:sheva@cluster1.xkwwqu6.mongodb.net/test";
        MongoClient mongoClient = MongoClients.create(mongoUri);
        MongoDatabase database = mongoClient.getDatabase("bezkoder_db");


        MongoCollection<Document> collection = database.getCollection("my_documents");
        System.out.println("Connected to database");
        Document document = new Document();
        document.put("priority","HIGHE");
        document.put("author","func");
        document.put("content","some extra text like this");

        collection.insertOne(document);
        mongoClient.close();

    }

    @GetMapping("WWW")
    public String welcome() {
        return "Welcome to spring boot heroku demo";
    }



}