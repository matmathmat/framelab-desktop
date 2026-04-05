package fr.framelab.dao;

import fr.framelab.models.User;
import fr.framelab.models.Training;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDAOTest {
    private TrainingDAO trainingDAO;
    private UserDAO userDAO;
    private Connection testConnection;

    private int testUserId;
    private final String TEST_DATE = "2026-04-05";

    @BeforeEach
    void setUp() throws SQLException {
        this.testConnection = DriverManager.getConnection("jdbc:sqlite::memory:");

        // Activer les FOREIGN KEY constraints
        try (Statement stmt = this.testConnection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        // Créer les tables via les DAO
        this.userDAO = new UserDAO(this.testConnection);
        this.trainingDAO = new TrainingDAO(this.testConnection);

        // Insérer un utilisateur test pour les clés étrangères
        User user = new User(1, "Alice", "Dupont", 0, "alice@email.com", "token");
        this.userDAO.save(user);
        this.testUserId = user.getId();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (this.testConnection != null && !this.testConnection.isClosed()) {
            this.testConnection.close();
        }
    }

    @Test
    void shouldSaveTrainingAndAssignId() {
        // ARRANGE - Préparer les données
        Training training = new Training(TEST_DATE, testUserId, 3, 1);

        // ACT - Exécuter l'action à tester
        this.trainingDAO.save(training);

        // ASSERT - Vérifier le résultat
        assertNotEquals(-1, training.getId());

        try (Statement stmt = this.testConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM trainings WHERE id = " + training.getId())) {

            assertTrue(rs.next());
            assertEquals(TEST_DATE, rs.getString("date"));
            assertEquals(testUserId, rs.getInt("user_id"));
            assertEquals(3, rs.getInt("attempt_count"));
            assertEquals(1, rs.getInt("completed"));
        } catch (SQLException e) {
            fail("Erreur SQL : " + e.getMessage());
        }
    }

    @Test
    void shouldFindTrainingById() {
        // ARRANGE - Préparer les données
        Training training = new Training(TEST_DATE, testUserId, 5, 0);
        this.trainingDAO.save(training);

        // ACT - Exécuter l'action à tester
        Training found = this.trainingDAO.findById(training.getId());

        // ASSERT - Vérifier le résultat
        assertNotNull(found);
        assertEquals(training.getId(), found.getId());
        assertEquals(TEST_DATE, found.getDate());
        assertEquals(5, found.getAttemptCount());
    }

    @Test
    void shouldFindTrainingByDateAndUserId() {
        // ARRANGE - Préparer les données
        Training training = new Training(TEST_DATE, testUserId, 2, 1);
        this.trainingDAO.save(training);

        // ACT - Exécuter l'action à tester
        Training found = this.trainingDAO.findByDateAndUserId(TEST_DATE, testUserId);

        // ASSERT - Vérifier le résultat
        assertNotNull(found);
        assertEquals(training.getId(), found.getId());
        assertEquals(testUserId, found.getUserId());
    }

    @Test
    void shouldFindTrainingsByUserId() {
        // ARRANGE - Préparer les données
        this.trainingDAO.save(new Training("2026-04-05", testUserId, 1, 0));
        this.trainingDAO.save(new Training("2026-04-06", testUserId, 2, 1));

        // ACT - Exécuter l'action à tester
        ArrayList<Training> results = this.trainingDAO.findByUserId(testUserId);

        // ASSERT - Vérifier le résultat
        assertEquals(2, results.size());
    }

    @Test
    void shouldUpdateTrainingAttemptsAndCompletion() {
        // ARRANGE - Préparer les données
        Training training = new Training(TEST_DATE, testUserId, 1, 0);
        this.trainingDAO.save(training);

        training.setAttemptCount(10);
        training.setCompleted(1);

        // ACT - Exécuter l'action à tester
        this.trainingDAO.update(training);

        // ASSERT - Vérifier le résultat
        Training updated = this.trainingDAO.findById(training.getId());
        assertNotNull(updated);
        assertEquals(10, updated.getAttemptCount());
        assertTrue(updated.isCompleted());
    }

    @Test
    void shouldDeleteTrainingById() {
        // ARRANGE - Préparer les données
        Training training = new Training(TEST_DATE, testUserId, 1, 0);
        this.trainingDAO.save(training);
        int savedId = training.getId();

        // ACT - Exécuter l'action à tester
        this.trainingDAO.deleteById(savedId);

        // ASSERT - Vérifier le résultat
        Training found = this.trainingDAO.findById(savedId);
        assertNull(found);
    }

    @Test
    void shouldThrowExceptionWhenSavingDuplicateDateForSameUser() {
        // ARRANGE - Préparer les données
        Training t1 = new Training(TEST_DATE, testUserId);
        this.trainingDAO.save(t1);

        Training t2 = new Training(TEST_DATE, testUserId);

        // ACT & ASSERT
        assertThrows(RuntimeException.class, () -> this.trainingDAO.save(t2));
    }

    @Test
    void shouldReturnNullForNonExistentTraining() {
        // ACT - Exécuter l'action à tester
        Training found = this.trainingDAO.findByDateAndUserId("1990-01-01", 999);

        // ASSERT - Vérifier le résultat
        assertNull(found);
    }
}