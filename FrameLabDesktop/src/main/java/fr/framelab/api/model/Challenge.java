package fr.framelab.api.model;

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
}
