package fr.framelab.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrainingTest {
    private Training training;
    private final String DATE = "2026-04-05";
    private final int USER_ID = 1;
    private final int ATTEMPT_COUNT = 5;
    private final int COMPLETED = 1;

    @BeforeEach
    void setUp() {
        training = new Training(DATE, USER_ID, ATTEMPT_COUNT, COMPLETED);
    }

    @Test
    void shouldNotAllowIdModification() {
        // ARRANGE - Préparer les données
        training.setId(1);
        int originalId = training.getId();

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            training.setId(99);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(UnsupportedOperationException.class, thrownException);
        assertEquals("ID cannot be modified once set", thrownException.getMessage());
        assertEquals(originalId, training.getId());
    }

    @Test
    void shouldRejectInvalidDateFormat() {
        // ARRANGE - Préparer les données
        String invalidDate = "05-04-2026";

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            new Training(invalidDate, USER_ID);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertTrue(thrownException.getMessage().contains("Invalid format"));
    }

    @Test
    void shouldRejectNegativeUserId() {
        // ARRANGE - Préparer les données
        int userId = -1;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            new Training(DATE, userId);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("The user ID cannot be less than 0", thrownException.getMessage());
    }

    @Test
    void shouldRejectNegativeAttemptCount() {
        // ARRANGE - Préparer les données
        int attemptCount = -5;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            new Training(DATE, USER_ID, attemptCount, COMPLETED);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("The attempt count cannot be less than 0", thrownException.getMessage());
    }

    @Test
    void shouldNormalizeCompletedValueToZero() {
        // ARRANGE - Préparer les données
        int completedInput = -1;

        // ACT - Exécuter l'action à tester
        Training newTraining = new Training(DATE, USER_ID, ATTEMPT_COUNT, completedInput);

        // ASSERT - Vérifier le résultat
        assertFalse(newTraining.isCompleted());
        assertEquals(0, newTraining.getCompletedValue());
    }

    @Test
    void shouldNormalizeCompletedValueToOne() {
        // ARRANGE - Préparer les données
        int completedInput = 50;

        // ACT - Exécuter l'action à tester
        Training newTraining = new Training(DATE, USER_ID, ATTEMPT_COUNT, completedInput);

        // ASSERT - Vérifier le résultat
        assertTrue(newTraining.isCompleted());
        assertEquals(1, newTraining.getCompletedValue());
    }

    @Test
    void shouldAcceptValidData() {
        // ARRANGE - Préparer les données
        String validDate = "2026-04-06";
        int validUser = 10;

        // ACT - Exécuter l'action à tester
        Training newTraining = new Training(validDate, validUser);

        // ASSERT - Vérifier le résultat
        assertEquals(validDate, newTraining.getDate());
        assertEquals(validUser, newTraining.getUserId());
        assertEquals(0, newTraining.getAttemptCount());
        assertFalse(newTraining.isCompleted());
    }
}