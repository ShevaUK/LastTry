package org.example.repository;

import org.example.entity.Tutorial;
import org.example.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User,String> {
    User findByUsername(String username);
    List<User> findByTutorials(Tutorial tutorial);
}
