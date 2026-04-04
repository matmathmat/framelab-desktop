package fr.framelab.models;

import fr.framelab.utils.validation.DateValidator;

public class Challenge {
    protected int id;
    protected String titleTheme;
    protected String descriptionTheme;
    protected String photoUrl;
    protected String startDate;
    protected String endDate;
    protected int isArchived;

    public Challenge(int id, String titleTheme, String descriptionTheme, String photoUrl, String startDate, String endDate, int isArchived) {
        if (id < 0) {
            throw new IllegalArgumentException("Negative id is not allowed");
        }

        if (titleTheme == null) {
            throw new IllegalArgumentException("Null titleTheme is not allowed");
        }
        if (titleTheme.isBlank()) {
            throw new IllegalArgumentException("Empty titleTheme is not allowed");
        }

        if (descriptionTheme == null) {
            throw new IllegalArgumentException("Null descriptionTheme is not allowed");
        }
        if (descriptionTheme.isBlank()) {
            throw new IllegalArgumentException("Empty descriptionTheme is not allowed");
        }

        if (photoUrl == null) {
            throw new IllegalArgumentException("Null photoUrl is not allowed");
        }
        if (photoUrl.isBlank()) {
            throw new IllegalArgumentException("Empty photoUrl is not allowed");
        }

        startDate = DateValidator.normalize(startDate);
        endDate   = DateValidator.normalize(endDate);

        DateValidator.validate(startDate);
        DateValidator.validate(endDate);

        this.id = id;
        this.titleTheme = titleTheme;
        this.descriptionTheme = descriptionTheme;
        this.photoUrl = photoUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isArchived = isArchived;
    }

    // Getters

    public int getId() {
        return this.id;
    }

    public String getTitleTheme() {
        return this.titleTheme;
    }

    public String getDescriptionTheme() {
        return this.descriptionTheme;
    }

    public String getPhotoUrl() {
        return this.photoUrl;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public boolean getIsArchived() {
        return (this.isArchived != 0);
    }

    // Setters

    public void setTitleTheme(String titleTheme) {
        this.titleTheme = titleTheme;
    }

    public void setDescriptionTheme(String descriptionTheme) {
        this.descriptionTheme = descriptionTheme;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setIsArchived(int isArchived) {
        this.isArchived = isArchived;
    }
}
