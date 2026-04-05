package fr.framelab.models;

import fr.framelab.utils.validation.DateValidator;

public class Training {
    protected int id;
    protected String date;
    protected int userId;
    protected int attemptCount;
    protected int completed;

    // Constructors

    public Training(String date, int userId) {
        // On vérifie que la date respecte le format yyyy-MM-dd
        DateValidator.validateDate(date);

        // On vérifie que l'id de l'utilisateur est supérieur à -1
        if (userId < 0) {
            throw new IllegalArgumentException("The user ID cannot be less than 0");
        }

        this.date = date;
        this.userId = userId;
        this.attemptCount = 0;
        this.completed = 0;
        this.id = -1;
    }

    public Training(String date, int userId, int attemptCount, int completed) {
        this(date, userId);

        // On vérifie que le nombre de tentatives est supérieur à -1
        if (attemptCount < 0) {
            throw new IllegalArgumentException("The attempt count cannot be less than 0");
        }

        this.attemptCount = attemptCount;

        if (completed <= 0) {
            this.completed = 0;
        } else {
            this.completed = 1;
        }
    }

    // Getters

    public int getId() {
        return this.id;
    }

    public String getDate() {
        return this.date;
    }

    public int getUserId() {
        return this.userId;
    }

    public int getAttemptCount() {
        return this.attemptCount;
    }

    public boolean isCompleted() {
        return this.completed == 1;
    }

    public int getCompletedValue() {
        return this.completed;
    }

    // Setters

    public void setId(int id) {
        if (this.id > 0) {
            throw new UnsupportedOperationException("ID cannot be modified once set");
        }

        this.id = id;
    }

    public void setDate(String date) {
        DateValidator.validateDate(date);
        this.date = date;
    }

    public void setAttemptCount(int attemptCount) {
        if (attemptCount < 0) {
            throw new IllegalArgumentException("The attempt count cannot be less than 0");
        }

        this.attemptCount = attemptCount;
    }

    public void setCompleted(int completed) {
        if (completed <= 0) {
            this.completed = 0;
        } else {
            this.completed = 1;
        }
    }
}