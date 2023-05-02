package org.example.service;
//newww

import jakarta.validation.ConstraintViolationException;
import org.example.entity.Tutorial;
import org.example.exception.TutorialCollectionException;

import java.util.List;
public interface TutorialService {
    public void createTutorial(Tutorial tutorial)throws ConstraintViolationException, TutorialCollectionException;
    public List<Tutorial> getFriendTutorials(String userId, String friendId);
}
