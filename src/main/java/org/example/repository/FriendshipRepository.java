package org.example.repository;

import org.example.entity.Friendship;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface FriendshipRepository extends MongoRepository<Friendship, String> {
    Optional<Friendship> findBySenderUserIdAndReceiverUserId(String senderUserId, String receiverUserId);
}
