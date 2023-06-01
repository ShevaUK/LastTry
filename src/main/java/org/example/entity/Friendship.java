package org.example.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "friendships")
public class Friendship {
    @Id
    private String id;


    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    private String senderUserId;
    private String receiverUserId;
    private FriendshipStatus status;
    @CreatedDate
    private Date createdAt;
    public Friendship() {

    }


    public Friendship(String senderUserId, String receiverUserId) {
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.status = FriendshipStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }


    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
