package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;



@RestController
public class LastController {
    @GetMapping("/WWW")
    public String welcome() {
        return "Welcome to spring boot heroku demo";
    }


    @PostMapping("/AAA")
    public void myCurrency() throws IOException {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    // Setting URL
                    String url_str = "https://v6.exchangerate-api.com/v6/5ffe763b6b76d0fa7e7fb034/latest/USD";

                    // Making Request
                    URL url = new URL(url_str);
                    HttpURLConnection request = (HttpURLConnection) url.openConnection();
                    request.connect();

                    // Convert to JSON

                    JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
                    JsonObject jsonobj = root.getAsJsonObject();

                    // Create a MongoClient instance and establish a connection to the database
                    String mongoUri = "mongodb+srv://sheva:sheva@cluster1.xkwwqu6.mongodb.net/test"; // Replace with your own database URL
                    MongoClient mongoClient = MongoClients.create(mongoUri);
                    MongoDatabase database = mongoClient.getDatabase("bezkoder_db");

                    // Create a collection to store the data
                    MongoCollection<Document> collection = database.getCollection("exchange_rates");

                    // Convert JsonObject to Document and insert into the collection
                    Document document = Document.parse(jsonobj.toString());
                    collection.insertOne(document);

                    // Close the connection to the database
                    mongoClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5 * 60 * 1000); // Run every 5 minutes
    }
}