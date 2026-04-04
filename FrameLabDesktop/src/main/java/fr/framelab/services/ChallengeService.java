package fr.framelab.services;

import fr.framelab.models.Challenge;
import fr.framelab.dao.ChallengeDAO;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class ChallengeService {
    private final ChallengeDAO challengeDAO;

    public ChallengeService(ChallengeDAO challengeDAO) {

        this.challengeDAO = challengeDAO;
    }

    public Challenge getChallenge(int challengeId) {

        return this.challengeDAO.findById(challengeId);
    }

    public List<Challenge> getAllChallenges() {
        return this.challengeDAO.findAll();
    }

    public Challenge getActiveChallenge() {
        return this.challengeDAO.findActive();
    }

    public Challenge syncAndGetActiveChallenge(Challenge apiChallenge) throws IOException {
        Challenge existing = this.getChallenge(apiChallenge.getId());

        if (existing == null) {
            this.challengeDAO.save(apiChallenge);
            downloadImage(apiChallenge);
        } else {
            boolean metadataChanged =
                    !existing.getTitleTheme().equals(apiChallenge.getTitleTheme()) ||
                            !existing.getDescriptionTheme().equals(apiChallenge.getDescriptionTheme()) ||
                            !existing.getStartDate().equals(apiChallenge.getStartDate()) ||
                            !existing.getEndDate().equals(apiChallenge.getEndDate()) ||
                            existing.getIsArchived() != apiChallenge.getIsArchived();

            boolean imageUrlChanged = !existing.getPhotoUrl().equals(apiChallenge.getPhotoUrl());

            if (metadataChanged || imageUrlChanged) {
                this.challengeDAO.update(apiChallenge);
            }

            // On télécharge uniquement si l'URL a changé ou si le fichier local est absent
            if (imageUrlChanged || !Files.exists(Paths.get(getLocalImagePath(apiChallenge.getId())))) {
                downloadImage(apiChallenge);
            }
        }

        return apiChallenge;
    }

    public void saveChallenge(Challenge challenge) {
        if (challenge == null) {
            throw new IllegalArgumentException("Challenge cannot be null");
        }

        try {
            if (this.getChallenge(challenge.getId()) != null) {
                this.challengeDAO.update(challenge);
            } else {
                this.challengeDAO.save(challenge);
            }
        } catch (Exception e) {
            this.challengeDAO.save(challenge);
        }
    }

    public void deleteChallenge(int id) {
        this.challengeDAO.deleteById(id);
    }

    public static String getLocalImagePath(int challengeId) {
        return "data/challenges/" + challengeId + "/image.png";
    }

    private void downloadImage(Challenge challenge) throws IOException {
        Path imagePath = Paths.get(getLocalImagePath(challenge.getId()));
        Files.createDirectories(imagePath.getParent());

        URL url = new URL(challenge.getPhotoUrl());
        try (InputStream in = url.openStream()) {
            Files.copy(in, imagePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
