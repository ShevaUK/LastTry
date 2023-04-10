package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LastController {
    @GetMapping("/welcome")
    public String welcome(){
            return "Welcome to spring boot heroku demo";
        }


    }