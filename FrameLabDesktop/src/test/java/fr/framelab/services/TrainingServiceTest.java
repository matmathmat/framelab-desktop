package fr.framelab.services;

import fr.framelab.dao.TrainingDAO;
import fr.framelab.dao.UserDAO;
import fr.framelab.models.Training;
import fr.framelab.models.TrainingOperation;
import fr.framelab.models.User;
import fr.framelab.modules.image.BrightnessOperation;
import fr.framelab.modules.image.ImageOperation;
import fr.framelab.modules.image.RotationOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingServiceTest {
    private Connection connection;

    private TrainingService trainingService;
    private UserService userService;

    private final String DATE = "2026-04-01";
    private final String OTHER_DATE = "2026-04-02";
    private final int USER_ID = 1;
    private final int ATTEMPT_COUNT = 3;

    @BeforeEach
    void setUp() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite::memory:");

        // Activer les FOREIGN KEY constraints
        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        // Créer le service utilisateur et un utilisateur de test
        UserDAO userDAO = new UserDAO(this.connection);
        this.userService = new UserService(userDAO);

        User testUser = new User(USER_ID, "Alice", "Dupont", 0, "alice@email.com", "token");
        this.userService.saveUser(testUser);

        // Créer le service training
        TrainingDAO trainingDAO = new TrainingDAO(this.connection);
        this.trainingService = new TrainingService(trainingDAO);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (this.connection != null) this.connection.close();
    }

    @Test
    void shouldSaveAndRetrieveTraining() {
        // ARRANGE - Préparer les données
        Training training = new Training(DATE, USER_ID);

        // ACT - Exécuter l'action à tester
        this.trainingService.saveTraining(training);
        Training retrieved = this.trainingService.getTraining(training.getId());

        // ASSERT - Vérifier le résultat
        assertNotNull(retrieved);
        assertEquals(DATE, retrieved.getDate());
        assertEquals(USER_ID, retrieved.getUserId());
    }

    @Test
    void shouldGetTrainingByDateAndUser() {
        // ARRANGE - Préparer les données
        Training training = new Training(DATE, USER_ID);
        this.trainingService.saveTraining(training);

        // ACT - Exécuter l'action à tester
        Training retrieved = this.trainingService.getTrainingByDate(DATE, USER_ID);

        // ASSERT - Vérifier le résultat
        assertNotNull(retrieved);
        assertEquals(training.getId(), retrieved.getId());
    }

    @Test
    void shouldCreateTrainingIfDoesNotExist() {
        // ACT - Exécuter l'action
        Training training = this.trainingService.getOrCreateTraining(DATE, USER_ID);

        // ASSERT - Vérifier le résultat
        assertNotNull(training);
        assertTrue(training.getId() > 0);
        assertEquals(DATE, training.getDate());
    }

    @Test
    void shouldReturnExistingTrainingInGetOrCreate() {
        // ARRANGE - Préparer les données
        Training existing = new Training(DATE, USER_ID);
        this.trainingService.saveTraining(existing);

        // ACT - Exécuter l'action à tester
        Training result = this.trainingService.getOrCreateTraining(DATE, USER_ID);

        // ASSERT - Vérifier le résultat
        assertEquals(existing.getId(), result.getId());
    }

    @Test
    void shouldIncrementAttemptCount() {
        // ARRANGE - Préparer les données
        Training training = new Training(DATE, USER_ID);
        this.trainingService.saveTraining(training);

        // ACT - Exécuter l'action à tester
        this.trainingService.incrementAttempt(training);

        // ASSERT - Vérifier le résultat
        Training retrieved = this.trainingService.getTraining(training.getId());
        assertEquals(1, retrieved.getAttemptCount());
    }

    @Test
    void shouldMarkTrainingAsCompleted() {
        // ARRANGE - Préparer les données
        Training training = new Training(DATE, USER_ID);
        this.trainingService.saveTraining(training);

        // ACT - Exécuter l'action à tester
        this.trainingService.markCompleted(training);

        // ASSERT - Vérifier le résultat
        Training retrieved = this.trainingService.getTraining(training.getId());
        assertTrue(retrieved.isCompleted());
    }

    @Test
    void shouldGenerateConsistentOperationsForSameDate() {
        // ARRANGE - Préparer les données
        String testDate = "2026-05-20";

        // ACT - Exécuter l'action à tester
        List<TrainingOperation> ops1 = this.trainingService.generateOperations(testDate);
        List<TrainingOperation> ops2 = this.trainingService.generateOperations(testDate);

        // ASSERT - Vérifier le résultat
        assertEquals(ops1.size(), ops2.size());
        for (int i = 0; i < ops1.size(); i++) {
            assertEquals(ops1.get(i).getType(), ops2.get(i).getType());
            assertEquals(ops1.get(i).getParam(), ops2.get(i).getParam());
        }
    }

    @Test
    void shouldValidateCorrectOperations() {
        // ARRANGE - Préparer les données
        List<TrainingOperation> expected = List.of(
                new TrainingOperation("BrightnessOperation", 50),
                new TrainingOperation("RotationOperation", 90)
        );
        List<ImageOperation> applied = List.of(
                new BrightnessOperation(50),
                new RotationOperation(90)
        );

        // ASSERT - Vérifier le résultat
        boolean isValid = this.trainingService.validateOperations(applied, expected);

        // ASSERT - Doit être vrai
        assertTrue(isValid);
    }

    @Test
    void shouldInvalidateWrongParameter() {
        // ARRANGE - Préparer les données
        List<TrainingOperation> expected = List.of(new TrainingOperation("BrightnessOperation", 50));
        List<ImageOperation> applied = List.of(new BrightnessOperation(40));

        // ACT - Exécuter l'action à tester
        boolean isValid = this.trainingService.validateOperations(applied, expected);

        // ASSERT - Vérifier le résultat
        assertFalse(isValid);
    }

    @Test
    void shouldComputeScoreCorrectly() {
        // ARRANGE - Préparer les données
        int attempts1 = 1;
        int attempts2 = 4;
        int attemptsZero = 0;

        // ACT - Exécuter l'action à tester
        int score1 = this.trainingService.computeScore(attempts1);
        int score2 = this.trainingService.computeScore(attempts2);
        int scoreZero = this.trainingService.computeScore(attemptsZero);

        // ASSERT - Vérifier le résultat
        assertEquals(1000, score1);
        assertEquals(250, score2);
        assertEquals(0, scoreZero);
    }

    @Test
    void shouldThrowExceptionWhenSavingNullTraining() {
        // ARRANGE - Préparer les données
        Training training = null;

        // ACT - Exécuter l'action à tester
        Exception thrown = null;
        try {
            this.trainingService.saveTraining(training);
        } catch (Exception e) {
            thrown = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrown);
        assertInstanceOf(IllegalArgumentException.class, thrown);
        assertEquals("Training cannot be null", thrown.getMessage());
    }

    @Test
    void shouldDeleteTraining() {
        // ARRANGE - Préparer les données
        Training training = new Training(DATE, USER_ID);
        this.trainingService.saveTraining(training);

        // ACT - Exécuter l'action à tester
        this.trainingService.deleteTraining(training.getId());

        // ASSERT - Vérifier le résultat
        assertNull(this.trainingService.getTraining(training.getId()));
    }
}