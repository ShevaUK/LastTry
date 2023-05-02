package org.example.service.impl;

import jakarta.validation.ConstraintViolationException;
import org.example.entity.Tutorial;
import org.example.exception.TutorialCollectionException;
import org.example.repository.TutorialRepository;
import org.example.repository.UserRepository;
import org.example.service.TutorialService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class TutorialServiceImp implements TutorialService {
    @Autowired
    TutorialRepository tutorialRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public void createTutorial(Tutorial tutorial) throws ConstraintViolationException, TutorialCollectionException {
        Optional<Tutorial> tutorialOptional = tutorialRepository.findByTitel(tutorial.getTitle());
        if(tutorialOptional.isPresent()){
            throw new TutorialCollectionException(TutorialCollectionException.TutorialAlreadyExists());
        }else {
            tutorialRepository.save(tutorial);
        }

    }
}
