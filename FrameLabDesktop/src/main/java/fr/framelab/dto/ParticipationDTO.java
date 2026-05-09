package fr.framelab.dto;

public class ParticipationDTO {
    protected int id;
    protected String photoUrl;
    protected String submissionDate;
    protected int challengeId;

    public int getId() {
        return id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public int getChallengeId() {
        return challengeId;
    }
}