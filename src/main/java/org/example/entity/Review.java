package org.example.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Review {
    @Size(min = 2,max = 100)
    @NotNull(message="comment cannot be null")
    private String comment;
    private int rating;

    public Review(String comment, int rating) {
        this.comment = comment;
        this.rating = rating;
    }

    // Getters and setters

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
