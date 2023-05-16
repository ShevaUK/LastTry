package org.example.entity;

// без extends AuditMetadata


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "tutorials")
public class Tutorial {
    @Id
    private String id;

    @Size (min = 5,max = 100)
    @NotNull(message="title cannot be null")
    private String title;
    @Size(min = 5,max = 100)
    @NotNull(message=" author cannot be null")
    private String author;
    private boolean published;


    public Tutorial(String title, String author, boolean published) {
        this.title = title;
        this.author = author;
        this.published = published;
    }
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

}
