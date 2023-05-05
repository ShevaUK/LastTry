package org.example.service;

import org.example.entity.Friendship;

public interface FriendshipService {
    Friendship createFriendshipRequest(String senderUserId, String receiverUserId);

    Friendship confirmFriendshipRequest(String friendshipId,String receiverUserId);
    Friendship rejectFriendshipRequest(String friendshipId,String rejectedBy);

    Friendship getFriendshipRequest(String senderUserId, String receiverUserId);
}
