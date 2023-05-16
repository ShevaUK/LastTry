package org.example.service;

import org.example.entity.Friendship;

public interface FriendshipService {
    Friendship createFriendshipRequest(String senderUserId, String receiverUserId);

    Friendship confirmFriendshipRequest(String friendshipId);
    Friendship rejectFriendshipRequest(String friendshipId);

    Friendship getFriendshipRequest(String senderUserId, String receiverUserId);
}
