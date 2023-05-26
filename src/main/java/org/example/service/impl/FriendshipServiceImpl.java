package org.example.service.impl;

import jakarta.annotation.security.PermitAll;
import org.example.entity.Friendship;
import org.example.entity.FriendshipStatus;
import org.example.entity.Tutorial;
import org.example.entity.User;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.FriendshipRepository;
import org.example.repository.TutorialRepository;
import org.example.repository.UserRepository;
import org.example.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
        Optional<Friendship> existingFriendship = friendshipRepository.findBySenderUserIdAndReceiverUserId(senderUserId, receiverUserId);
        if (existingFriendship.isPresent()) {
            Friendship friendship = existingFriendship.get();
            friendship.setStatus(FriendshipStatus.PENDING);
            return friendshipRepository.save(friendship);
        }else {

            Friendship friendship = new Friendship();
            friendship.setSenderUserId(senderUserId);
            friendship.setReceiverUserId(receiverUserId);
            friendship.setStatus(FriendshipStatus.PENDING);

            return friendshipRepository.save(friendship);
        }
    }
    @Override
    public Friendship confirmFriendshipRequest(String friendshipId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        System.out.println(currentUsername);

        Optional<Friendship> friendship = friendshipRepository.findById(friendshipId);
        if (friendship.isPresent()) {
            Friendship request = friendship.get();
            if (request.getStatus() == FriendshipStatus.PENDING) {
                User receiverUser = userRepository.findById(request.getReceiverUserId()).orElse(null);
                User senderUser = userRepository.findById(request.getSenderUserId()).orElse(null);

                System.out.println(receiverUser);
                if (receiverUser != null && receiverUser.getUsername().equals(currentUsername)) {
                    request.setStatus(FriendshipStatus.ACCEPTED);
                    friendshipRepository.save(request);
                    List<String> receiverUserFriends = receiverUser.getFriendIds();
                    if (receiverUserFriends == null) {
                        receiverUserFriends = new ArrayList<>();
                    }


                    receiverUserFriends.add(senderUser.getId());
                    receiverUser.setFriendIds(receiverUserFriends);
                    userRepository.save(receiverUser);

                    List<String> senderUserFriends = senderUser.getFriendIds();
                    if (senderUserFriends == null) {
                        senderUserFriends = new ArrayList<>();
                    }

                    senderUserFriends.add(receiverUser.getId());
                    senderUser.setFriendIds(senderUserFriends);
                    userRepository.save(senderUser);

                    return request;
                } else {
                    throw new IllegalArgumentException("Only the receiver user can confirm this friendship request.");
                }
            } else {
                throw new IllegalArgumentException("Friend request has already been accepted or rejected.");
            }
        } else {
            throw new IllegalArgumentException("Friend request not found.");
        }
    }
//        Optional<Friendship> friendship = friendshipRepository.findById(friendshipId);
//        if (friendship.isPresent()) {
//            Friendship request = friendship.get();
//            if (request.getStatus() == FriendshipStatus.PENDING) {
//                if (request.getReceiverUserId().equals(receiverUserId)) {
//                    request.setStatus(FriendshipStatus.ACCEPTED);
//                    friendshipRepository.save(request);
//
//                    return request;
//                }else {
//                    throw new IllegalArgumentException("Only receiver user can confirm this friendship request.");
//                }
//            } else {
//                throw new IllegalArgumentException("Friend request has already been accepted or rejected.");
//            }
//        } else {
//            throw new IllegalArgumentException("Friend request not found.");
//        }
//    }
    @Override
    public Friendship rejectFriendshipRequest(String friendshipId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<Friendship> friendship = friendshipRepository.findById(friendshipId);
        if (friendship.isPresent()) {
            Friendship request = friendship.get();
            if (request.getStatus() == FriendshipStatus.PENDING||request.getStatus() == FriendshipStatus.ACCEPTED) {
                User receiverUser = userRepository.findById(request.getReceiverUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("Receiver user not found"));
                User senderUser = userRepository.findById(request.getSenderUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("Sender user not found"));

                if (receiverUser.getUsername().equals(currentUsername) || senderUser.getUsername().equals(currentUsername)) {
                    request.setStatus(FriendshipStatus.REJECTED);
                    friendshipRepository.save(request);



                    List<String> receiverUserFriends = receiverUser.getFriendIds();
                    if (receiverUserFriends != null) {
                        receiverUserFriends.remove(senderUser.getId());
                        receiverUser.setFriendIds(receiverUserFriends);
                        userRepository.save(receiverUser);
                    }

                    List<String> senderUserFriends = senderUser.getFriendIds();
                    if (senderUserFriends != null) {
                        senderUserFriends.remove(receiverUser.getId());
                        senderUser.setFriendIds(senderUserFriends);
                        userRepository.save(senderUser);
                    }
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
//    public Friendship rejectFriendshipRequest(String friendshipId,String rejectedById) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentUsername = authentication.getName();
//        System.out.println(currentUsername);
//
//        Optional<Friendship> friendship = friendshipRepository.findById(friendshipId);
//        if (friendship.isPresent()) {
//            Friendship request = friendship.get();
//            if (request.getStatus() == FriendshipStatus.PENDING) {
//                if (rejectedById.equals(request.getReceiverUserId()) || rejectedById.equals(request.getSenderUserId())) {
//                request.setStatus(FriendshipStatus.REJECTED);
//                friendshipRepository.save(request);
//                return request;
//                } else {
//                    throw new IllegalArgumentException("Invalid rejectedBy parameter. It must be equal to either senderUserId or receiverUserId.");
//                }
//            } else {
//                throw new IllegalArgumentException("Friend request has already been accepted or rejected.");
//            }
//        } else {
//            throw new IllegalArgumentException("Friend request not found.");
//        }
//    }
    @Override
    public Friendship getFriendshipRequest(String senderUserId, String receiverUserId) {
        return friendshipRepository.findBySenderUserIdAndReceiverUserId(senderUserId, receiverUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Friendship request not found"));
    }
}
