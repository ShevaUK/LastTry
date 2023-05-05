package org.example.service.impl;

import jakarta.annotation.security.PermitAll;
import org.example.entity.Friendship;
import org.example.entity.FriendshipStatus;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.FriendshipRepository;
import org.example.repository.TutorialRepository;
import org.example.repository.UserRepository;
import org.example.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FriendshipServiceImpl implements FriendshipService {
    @Autowired
    private TutorialRepository tutorialRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Override
    public Friendship createFriendshipRequest(String senderUserId,String receiverUserId){
        if (friendshipRepository.findBySenderUserIdAndReceiverUserId(senderUserId, receiverUserId).isPresent()) {
            throw new IllegalArgumentException("Friendship request already sent");
        }

        Friendship friendship = new Friendship();
        friendship.setSenderUserId(senderUserId);
        friendship.setReceiverUserId(receiverUserId);
        friendship.setStatus(FriendshipStatus.PENDING);

        return friendshipRepository.save(friendship);
    }
    @Override
    public Friendship confirmFriendshipRequest(String friendshipId,String receiverUserId) {
        Optional<Friendship> friendship = friendshipRepository.findById(friendshipId);
        if (friendship.isPresent()) {
            Friendship request = friendship.get();
            if (request.getStatus() == FriendshipStatus.PENDING) {
                if (request.getReceiverUserId().equals(receiverUserId)) {
                    request.setStatus(FriendshipStatus.ACCEPTED);
                    friendshipRepository.save(request);

                    return request;
                }else {
                    throw new IllegalArgumentException("Only receiver user can confirm this friendship request.");
                }
            } else {
                throw new IllegalArgumentException("Friend request has already been accepted or rejected.");
            }
        } else {
            throw new IllegalArgumentException("Friend request not found.");
        }
    }
    @Override
    public Friendship rejectFriendshipRequest(String friendshipId,String rejectedById) {
        Optional<Friendship> friendship = friendshipRepository.findById(friendshipId);
        if (friendship.isPresent()) {
            Friendship request = friendship.get();
            if (request.getStatus() == FriendshipStatus.PENDING) {
                if (rejectedById.equals(request.getReceiverUserId()) || rejectedById.equals(request.getSenderUserId())) {
                request.setStatus(FriendshipStatus.REJECTED);
                friendshipRepository.save(request);
                return request;
                } else {
                    throw new IllegalArgumentException("Invalid rejectedBy parameter. It must be equal to either senderUserId or receiverUserId.");
                }
            } else {
                throw new IllegalArgumentException("Friend request has already been accepted or rejected.");
            }
        } else {
            throw new IllegalArgumentException("Friend request not found.");
        }
    }
    @Override
    public Friendship getFriendshipRequest(String senderUserId, String receiverUserId) {
        return friendshipRepository.findBySenderUserIdAndReceiverUserId(senderUserId, receiverUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Friendship request not found"));
    }
}
