package org.example.dto;

public class RejectFriendshipDTO {
    private String friendshipId;
    private String rejectedById;

    public String getFriendshipId() {
        return friendshipId;
    }

    public void setFriendshipId(String friendshipId) {
        this.friendshipId = friendshipId;
    }

    public String getRejectedById() {
        return rejectedById;
    }

    public void setRejectedById(String rejectedById) {
        this.rejectedById = rejectedById;
    }
}
