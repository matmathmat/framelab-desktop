package fr.framelab.dao;

import fr.framelab.models.Challenge;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeDAOTest {
    private ChallengeDAO challengeDAO;
    private Connection testConnection;

    private static final String TITLE = "Challenge hebdomadaire du samedi";
    private static final String DESC = "Ceci est le challenge hebdomadaire du samedi. Le dieu soleil Helios attend vos offrandes.";
    private static final String PHOTO = "https://i.imgur.com/v6xDssA.jpeg";
    private static final String START = "2025-11-29 12:00:00";
    private static final String END = "2026-02-28 12:00:00";

    @BeforeEach
    void setUp() throws SQLException {
        this.testConnection = DriverManager.getConnection("jdbc:sqlite::memory:");

        // Activer les FOREIGN KEY constraints
        try (Statement stmt = this.testConnection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        this.challengeDAO = new ChallengeDAO(this.testConnection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (this.testConnection != null && !this.testConnection.isClosed()) {
            this.testConnection.close();
        }
    }

    @Test
    void shouldSaveChallenge() {
        // ARRANGE - Préparer les données
        Challenge challenge = new Challenge(1, TITLE, DESC, PHOTO, START, END, 0);

        // ACT - Exécuter l'action à tester
        this.challengeDAO.save(challenge);

        // ASSERT - Vérifier le résultat
        assertEquals(1, challenge.getId());

        try (Statement stmt = this.testConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM challenges WHERE id = " + challenge.getId())) {

            assertTrue(rs.next());
            assertEquals(TITLE, rs.getString("title_theme"));
            assertEquals(PHOTO, rs.getString("photo_url"));
        } catch (SQLException e) {
            fail("Erreur SQL : " + e.getMessage());
        }
    }

    @Test
    void shouldFindChallengeById() {
        // ARRANGE - Préparer les données
        Challenge challenge = new Challenge(0, TITLE, DESC, PHOTO, START, END, 0);
        this.challengeDAO.save(challenge);

        // ACT - Exécuter l'action à tester
        Challenge found = this.challengeDAO.findById(challenge.getId());

        // ASSERT - Vérifier le résultat
        assertNotNull(found);
        assertEquals(challenge.getId(), found.getId());
        assertEquals(TITLE, found.getTitleTheme());
    }

    @Test
    void shouldFindAllChallenges() {
        // ARRANGE - Préparer les données
        this.challengeDAO.save(new Challenge(1, "Rêve de bleu", "Modifiez l'image source pour qu'elle évoque un rêve bleu.", "https://framelab/images/challenge1.png", START, END, 0));
        this.challengeDAO.save(new Challenge(2, "Géométrie parallèle", "Modifiez l'image source pour qu'elle évoque une géométrie parallèle.", "https://framelab/images/challenge2.png", START, END, 0));

        // ACT - Exécuter l'action à tester
        ArrayList<Challenge> challenges = this.challengeDAO.findAll();

        // ASSERT - Vérifier le résultat
        assertEquals(2, challenges.size());
        assertEquals("Rêve de bleu", challenges.get(0).getTitleTheme());
        assertEquals("https://framelab/images/challenge2.png", challenges.get(1).getPhotoUrl());
    }

    @Test
    void shouldUpdateChallenge() {
        // ARRANGE - Préparer les données
        Challenge challenge = new Challenge(0, TITLE, DESC, PHOTO, START, END, 0);
        this.challengeDAO.save(challenge);

        Challenge updatedData = new Challenge(challenge.getId(), "Titre Mis à Jour", DESC, PHOTO, START, END, 1);

        // ACT - Exécuter l'action à tester
        this.challengeDAO.update(updatedData);

        // ASSERT - Vérifier le résultat
        Challenge found = this.challengeDAO.findById(challenge.getId());
        assertNotNull(found);
        assertEquals("Titre Mis à Jour", found.getTitleTheme());
        assertTrue(found.getIsArchived());
    }

    @Test
    void shouldDeleteChallengeById() {
        // ARRANGE - Préparer les données
        Challenge challenge = new Challenge(0, TITLE, DESC, PHOTO, START, END, 0);
        this.challengeDAO.save(challenge);
        int savedId = challenge.getId();

        // ACT - Exécuter l'action à tester
        this.challengeDAO.deleteById(savedId);

        // ASSERT - Vérifier le résultat
        Challenge found = this.challengeDAO.findById(savedId);
        assertNull(found);
    }

    @Test
    void shouldReturnNullForNonExistentChallenge() {
        // ACT - Exécuter l'action à tester
        Challenge found = this.challengeDAO.findById(999);

        // ASSERT - Vérifier le résultat
        assertNull(found);
    }
}