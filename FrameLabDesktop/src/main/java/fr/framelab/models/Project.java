package fr.framelab.models;

import java.time.LocalDateTime;

public class Project {
    protected int id;
    protected String title;
    protected int userId;
    protected int challengeId;
    protected LocalDateTime createdAt;
    protected LocalDateTime editedAt;

    // Constructors

    public Project(String title, int userId, int challengeId, LocalDateTime createdAt, LocalDateTime editedAt) {
        // On vérifie que le titre respecte notre règle métier
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (title.length() < 3) {
            throw new IllegalArgumentException("Title must be at least 3 characters long");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("Title cannot exceed 100 characters");
        }

        // On vérifie que l'id de l'utilisateur est supérieur à -1
        if (userId < 0) {
            throw new IllegalArgumentException("The user ID cannot be less than 0");
        }

        // On vérifie que l'id du challenge est supérieur à -1
        if (challengeId < 0) {
            throw new IllegalArgumentException("The challenge ID cannot be less than 0");
        }

        if (createdAt == null) {
            throw new IllegalArgumentException("The created at date time cannot be null");
        }

        if (editedAt == null) {
            throw new IllegalArgumentException("The edited at date time cannot be null");
        }

        this.title = title;
        this.userId = userId;
        this.challengeId = challengeId;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
        this.id = -1;
    }

    // Getters

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public int getUserId() {
        return this.userId;
    }

    public int getChallengeId() {
        return this.challengeId;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getEditedAt() {
        return this.editedAt;
    }

    // Setters

    public void setId(int id) {
        if (this.id > 0) {
            throw new UnsupportedOperationException("ID cannot be modified once set");
        }

        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }
}
