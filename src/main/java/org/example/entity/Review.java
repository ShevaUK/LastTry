package org.example.entity;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.dto.Reviewer;


public class Review {
    @Size(min = 2,max = 100)
    @NotNull(message="comment cannot be null")
    private String comment;
    private int rating;
    private Reviewer reviewer;

    public Review(String comment, int rating,Reviewer reviewer) {
        this.comment = comment;
        this.rating = rating;
        this.reviewer = reviewer;
    }

    public Reviewer getReviewer() {
        return reviewer;
    }

    public void setReviewer(Reviewer reviewer) {
        this.reviewer = reviewer;
    }

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
