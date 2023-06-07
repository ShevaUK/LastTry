package org.example.service.impl;

import jakarta.validation.ConstraintViolationException;
import org.example.entity.Friendship;
import org.example.entity.FriendshipStatus;
import org.example.entity.Tutorial;
import org.example.entity.User;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.TutorialCollectionException;
import org.example.repository.FriendshipRepository;
import org.example.repository.TutorialRepository;
import org.example.repository.UserRepository;
import org.example.service.TutorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class TutorialServiceImp implements TutorialService {
    @Autowired
    TutorialRepository tutorialRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Override
    public void createTutorial(Tutorial tutorial) throws ConstraintViolationException, TutorialCollectionException {
        Optional<Tutorial> tutorialOptional = tutorialRepository.findByTitel(tutorial.getTitle());
        if(tutorialOptional.isPresent()){
            throw new TutorialCollectionException(TutorialCollectionException.TutorialAlreadyExists());
        }else {
            tutorialRepository.save(tutorial);
        }

    }
    @Override
    public List<Tutorial> getFriendTutorials(String senderUserId, String receiverUserId) {
        User friend = userRepository.findById(receiverUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", receiverUserId));
        Friendship friendship = friendshipRepository.findBySenderUserIdAndReceiverUserId(senderUserId, receiverUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Friendship", "id", receiverUserId + "-" + senderUserId));
        FriendshipStatus status = friendship.getStatus();
        if (status != FriendshipStatus.ACCEPTED) {
            // перевіряємо чи є спільний файл friendship з статусом accepted
            Friendship reverseFriendship = friendshipRepository.findBySenderUserIdAndReceiverUserId(receiverUserId, senderUserId)
                    .orElseThrow(() -> new ResourceNotFoundException("Friendship", "id", senderUserId + "-" + receiverUserId));
            if (reverseFriendship.getStatus() != FriendshipStatus.ACCEPTED) {
                throw new ResourceNotFoundException("You are not authorized to view this friend's tutorials");
            }
        }
        return friend.getTutorials();
    }

}
