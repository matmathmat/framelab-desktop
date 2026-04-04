package fr.framelab.models;

import fr.framelab.models.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectTest {
    private Project project;
    private final String TITLE = "Mon premier projet";
    private final int USER_ID = 1;
    private final int CHALLENGE_ID = 1;
    private final LocalDateTime CREATED_AT = LocalDateTime.of(2026, 1, 1, 0, 0, 0);
    private final LocalDateTime EDITED_AT = LocalDateTime.of(2026, 1, 1, 12, 0, 0);

    @BeforeEach
    void setUp() {
        project = new Project(TITLE, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
    }

    @Test
    void shouldNotAllowIdModification() {
        // ARRANGE - Préparer les données
        project.setId(1);
        int originalId = project.getId();

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            project.setId(99);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(UnsupportedOperationException.class, thrownException);
        assertEquals("ID cannot be modified once set", thrownException.getMessage());
        assertEquals(originalId, project.getId());
    }

    @Test
    void shouldRejectNullTitle() {
        // ARRANGE - Préparer les données
        String title = null;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            new Project(title, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Title cannot be null or empty", thrownException.getMessage());
    }

    @Test
    void shouldRejectEmptyTitle() {
        // ARRANGE - Préparer les données
        String title = "";

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            new Project(title, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Title cannot be null or empty", thrownException.getMessage());
    }

    @Test
    void shouldRejectShortTitle() {
        // ARRANGE - Préparer les données
        String title = "bb";

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            new Project(title, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Title must be at least 3 characters long", thrownException.getMessage());
    }

    @Test
    void shouldRejectTooLongTitle() {
        // ARRANGE - Préparer les données
        String title = "A".repeat(101);

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            new Project(title, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Title cannot exceed 100 characters", thrownException.getMessage());
    }

    @Test
    void shouldAcceptValidTitle() {
        // ARRANGE - Préparer les données
        String title = "Mon second projet";

        // ACT - Exécuter l'action à tester
        Project slide = new Project(title, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);

        // ASSERT - Vérifier le résultat
        assertEquals(title, slide.getTitle());
    }

    @Test
    void shouldRejectNegativeUserId() {
        // ARRANGE - Préparer les données
        int userId = -1;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            new Project(TITLE, userId, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("The user ID cannot be less than 0", thrownException.getMessage());
    }

    @Test
    void shouldAcceptValidUserId() {
        // ARRANGE - Préparer les données
        int userId = 1;

        // ACT - Exécuter l'action à tester
        Project project = new Project(TITLE, userId, CHALLENGE_ID, CREATED_AT, EDITED_AT);

        // ASSERT - Vérifier le résultat
        assertEquals(userId, project.getUserId());
    }

    @Test
    void shouldAcceptZeroUserId() {
        // ARRANGE - Préparer les données
        int userId = 0;

        // ACT - Exécuter l'action à tester
        Project project = new Project(TITLE, userId, CHALLENGE_ID, CREATED_AT, EDITED_AT);

        // ASSERT - Vérifier le résultat
        assertEquals(userId, project.getUserId());
    }

    @Test
    void shouldRejectNegativeChallengeId() {
        // ARRANGE - Préparer les données
        int challengeId = -1;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            new Project(TITLE, USER_ID, challengeId, CREATED_AT, EDITED_AT);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("The challenge ID cannot be less than 0", thrownException.getMessage());
    }

    @Test
    void shouldAcceptValidChallengeId() {
        // ARRANGE - Préparer les données
        int challengeId = 1;

        // ACT - Exécuter l'action à tester
        Project project = new Project(TITLE, USER_ID, challengeId, CREATED_AT, EDITED_AT);

        // ASSERT - Vérifier le résultat
        assertEquals(challengeId, project.getChallengeId());
    }

    @Test
    void shouldAcceptZeroChallengeId() {
        // ARRANGE - Préparer les données
        int challengeId = 0;

        // ACT - Exécuter l'action à tester
        Project project = new Project(TITLE, USER_ID, challengeId, CREATED_AT, EDITED_AT);

        // ASSERT - Vérifier le résultat
        assertEquals(challengeId, project.getChallengeId());
    }

    @Test
    void shouldRejectNullCreatedAt() {
        // ARRANGE - Préparer les données
        LocalDateTime createdAt = null;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            new Project(TITLE, USER_ID, CHALLENGE_ID, createdAt, EDITED_AT);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("The created at date time cannot be null", thrownException.getMessage());
    }

    @Test
    void shouldAcceptValidCreatedAt() {
        // ARRANGE - Préparer les données
        LocalDateTime createdAt = LocalDateTime.of(2025, 6, 15, 10, 30, 0);

        // ACT - Exécuter l'action à tester
        Project project = new Project(TITLE, USER_ID, CHALLENGE_ID, createdAt, EDITED_AT);

        // ASSERT - Vérifier le résultat
        assertEquals(createdAt, project.getCreatedAt());
    }

    @Test
    void shouldRejectNullEditedAt() {
        // ARRANGE - Préparer les données
        LocalDateTime editedAt = null;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            new Project(TITLE, USER_ID, CHALLENGE_ID, CREATED_AT, editedAt);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("The edited at date time cannot be null", thrownException.getMessage());
    }

    @Test
    void shouldAcceptValidEditedAt() {
        // ARRANGE - Préparer les données
        LocalDateTime editedAt = LocalDateTime.of(2025, 6, 15, 14, 0, 0);

        // ACT - Exécuter l'action à tester
        Project project = new Project(TITLE, USER_ID, CHALLENGE_ID, CREATED_AT, editedAt);

        // ASSERT - Vérifier le résultat
        assertEquals(editedAt, project.getEditedAt());
    }
}
