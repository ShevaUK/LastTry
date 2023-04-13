package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


import org.bson.Document;




@RestController
public class LastController {
    @PostMapping("/DDD")
    public String create(){
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

        return "Перший запит пішов";
    }

    @GetMapping("/WWW")
    public String welcome() {
        return "Welcome to spring boot heroku demo";
    }



}