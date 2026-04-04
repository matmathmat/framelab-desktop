package fr.framelab.services;

import fr.framelab.models.Challenge;
import fr.framelab.dao.ChallengeDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class ChallengeServiceTest {
    private Connection connection;
    private ChallengeService challengeService;

    private final int CHALLENGE_ID = 1;
    private final String TITLE_THEME = "Challenge hebdomadaire du samedi";
    private final String DESCRIPTION_THEME = "Ceci est le challenge hebdomadaire du samedi. Le dieu soleil Helios attend vos offrandes pour cette semaine.";
    private final String PHOTO_URL = "https://i.imgur.com/v6xDssA.jpeg";
    private final String START_DATE = "2025-11-29 12:00:00";
    private final String END_DATE = "2026-02-28 12:00:00";
    private final int IS_ARCHIVED = 0;
    private final String NEW_TITLE_THEME = "Challenge hebdomadaire du samedi soir";

    @BeforeEach
    void setUp() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
        ChallengeDAO dao = new ChallengeDAO(this.connection);
        this.challengeService = new ChallengeService(dao);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (this.connection != null) this.connection.close();
    }

    @Test
    void shouldSaveAndRetrievChallenge() {
        // ARRANGE - Préparer les données
        Challenge challenge = new Challenge(CHALLENGE_ID, TITLE_THEME, DESCRIPTION_THEME, PHOTO_URL, START_DATE, END_DATE, IS_ARCHIVED);

        // ACT - Exécuter l'action à tester
        this.challengeService.saveChallenge(challenge);
        Challenge retrieveChallenge = this.challengeService.getChallenge(challenge.getId());

        // ASSERT - Vérifier le résultat
        assertEquals(challenge.getId(), retrieveChallenge.getId());
    }

    @Test
    void shouldUpdateExistingChallenge() {
        // ARRANGE - Préparer les données
        Challenge challenge = new Challenge(CHALLENGE_ID, TITLE_THEME, DESCRIPTION_THEME, PHOTO_URL, START_DATE, END_DATE, IS_ARCHIVED);
        this.challengeService.saveChallenge(challenge);
        int originalId = challenge.getId();
        challenge.setTitleTheme(NEW_TITLE_THEME);

        // ACT - Exécuter l'action à tester
        this.challengeService.saveChallenge(challenge);

        // ASSERT - Vérifier le résultat
        Challenge retrieveChallenge = this.challengeService.getChallenge(originalId);
        assertEquals(challenge.getTitleTheme(), retrieveChallenge.getTitleTheme());
    }

    @Test
    void shouldDeleteChallenge() {
        // ARRANGE - Préparer les données
        Challenge challenge = new Challenge(CHALLENGE_ID, TITLE_THEME, DESCRIPTION_THEME, PHOTO_URL, START_DATE, END_DATE, IS_ARCHIVED);
        this.challengeService.saveChallenge(challenge);

        // ACT - Exécuter l'action à tester
        this.challengeService.deleteChallenge(challenge.getId());

        // ASSERT - Vérifier le résultat
        Challenge retrieveChallenge = this.challengeService.getChallenge(challenge.getId());
        assertNull(retrieveChallenge);
    }

    @Test
    void shouldThrowExceptionWhenSavingNullChallenge() {
        // ARRANGE - Préparer les données
        Challenge challenge = null;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            this.challengeService.saveChallenge(challenge);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Challenge cannot be null", thrownException.getMessage());
    }
}