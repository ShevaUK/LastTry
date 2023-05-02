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
    @NotNull(message=" description cannot be null")
    private String description;
    private boolean published;


    public Tutorial(String title, String description, boolean published) {
        this.title = title;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

}
