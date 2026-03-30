package fr.framelab.services;

import fr.framelab.api.models.Challenge;
import fr.framelab.api.models.User;
import fr.framelab.dao.ChallengeDAO;
import fr.framelab.dao.UserDAO;

public class ChallengeService {
    private final ChallengeDAO challengeDAO;

    public ChallengeService(ChallengeDAO challengeDAO) {
        this.challengeDAO = challengeDAO;
    }

    public Challenge getChallenge(int challengeId) {
        return this.challengeDAO.findById(challengeId);
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
}
