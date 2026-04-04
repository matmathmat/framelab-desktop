package fr.framelab.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeTest {
    private Challenge challenge;
    private final int CHALLENGE_ID = 1;
    private final String TITLE_THEME = "Challenge hebdomadaire du samedi";
    private final String DESCRIPTION_THEME = "Ceci est le challenge hebdomadaire du samedi. Le dieu soleil Helios attend vos offrandes pour cette semaine.";
    private final String PHOTO_URL = "https://i.imgur.com/v6xDssA.jpeg";
    private final String START_DATE = "2025-11-29 12:00:00";
    private final String END_DATE = "2026-02-28 12:00:00";
    private final int IS_ARCHIVED = 0;

    /**
     * Prépare un challenge valide avant chaque test
     */
    @BeforeEach
    void setUp() {
        challenge = new Challenge(CHALLENGE_ID, TITLE_THEME, DESCRIPTION_THEME, PHOTO_URL, START_DATE, END_DATE, IS_ARCHIVED);
    }

    @Test
    void shouldRejectLessthan0ChallengeId() {
        // ARRANGE - Préparer les données
        int challengeId = -1;
        String titleTheme = "Challenge hebdomadaire du samedi";
        String descriptionTheme = "Ceci est le challenge hebdomadaire du samedi. Le dieu soleil Helios attend vos offrandes pour cette semaine.";
        String photoUrl = "https://i.imgur.com/v6xDssA.jpeg";
        String startDate = "2025-11-29 12:00:00";
        String endDate = "2026-02-28 12:00:00";
        int isArchived = 0;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            Challenge challenge = new Challenge(challengeId, titleTheme, descriptionTheme, photoUrl, startDate, endDate, isArchived);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Negative id is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectNullTitleTheme() {
        // ARRANGE - Préparer les données
        int challengeId = 1;
        String titleTheme = null;
        String descriptionTheme = "Ceci est le challenge hebdomadaire du samedi. Le dieu soleil Helios attend vos offrandes pour cette semaine.";
        String photoUrl = "https://i.imgur.com/v6xDssA.jpeg";
        String startDate = "2025-11-29 12:00:00";
        String endDate = "2026-02-28 12:00:00";
        int isArchived = 0;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            Challenge challenge = new Challenge(challengeId, titleTheme, descriptionTheme, photoUrl, startDate, endDate, isArchived);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Null titleTheme is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectEmptyTitleTheme() {
        // ARRANGE - Préparer les données
        int challengeId = 1;
        String titleTheme = " ";
        String descriptionTheme = "Ceci est le challenge hebdomadaire du samedi. Le dieu soleil Helios attend vos offrandes pour cette semaine.";
        String photoUrl = "https://i.imgur.com/v6xDssA.jpeg";
        String startDate = "2025-11-29 12:00:00";
        String endDate = "2026-02-28 12:00:00";
        int isArchived = 0;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            Challenge challenge = new Challenge(challengeId, titleTheme, descriptionTheme, photoUrl, startDate, endDate, isArchived);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Empty titleTheme is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectNullDescriptionTheme() {
        // ARRANGE - Préparer les données
        int challengeId = 1;
        String titleTheme = "Challenge hebdomadaire du samedi";
        String descriptionTheme = null;
        String photoUrl = "https://i.imgur.com/v6xDssA.jpeg";
        String startDate = "2025-11-29 12:00:00";
        String endDate = "2026-02-28 12:00:00";
        int isArchived = 0;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            Challenge challenge = new Challenge(challengeId, titleTheme, descriptionTheme, photoUrl, startDate, endDate, isArchived);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Null descriptionTheme is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectEmptyDescriptionTheme() {
        // ARRANGE - Préparer les données
        int challengeId = 1;
        String titleTheme = "Challenge hebdomadaire du samedi";
        String descriptionTheme = " ";
        String photoUrl = "https://i.imgur.com/v6xDssA.jpeg";
        String startDate = "2025-11-29 12:00:00";
        String endDate = "2026-02-28 12:00:00";
        int isArchived = 0;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            Challenge challenge = new Challenge(challengeId, titleTheme, descriptionTheme, photoUrl, startDate, endDate, isArchived);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Empty descriptionTheme is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectNullPhotoUrl() {
        // ARRANGE - Préparer les données
        int challengeId = 1;
        String titleTheme = "Challenge hebdomadaire du samedi";
        String descriptionTheme = "Ceci est le challenge hebdomadaire du samedi. Le dieu soleil Helios attend vos offrandes pour cette semaine.";
        String photoUrl = null;
        String startDate = "2025-11-29 12:00:00";
        String endDate = "2026-02-28 12:00:00";
        int isArchived = 0;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            Challenge challenge = new Challenge(challengeId, titleTheme, descriptionTheme, photoUrl, startDate, endDate, isArchived);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Null photoUrl is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectEmptyPhotoUrl() {
        // ARRANGE - Préparer les données
        int challengeId = 1;
        String titleTheme = "Challenge hebdomadaire du samedi";
        String descriptionTheme = "Ceci est le challenge hebdomadaire du samedi. Le dieu soleil Helios attend vos offrandes pour cette semaine.";
        String photoUrl = " ";
        String startDate = "2025-11-29 12:00:00";
        String endDate = "2026-02-28 12:00:00";
        int isArchived = 0;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            Challenge challenge = new Challenge(challengeId, titleTheme, descriptionTheme, photoUrl, startDate, endDate, isArchived);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Empty photoUrl is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectNullStartDate() {
        // ARRANGE - Préparer les données
        int challengeId = 1;
        String titleTheme = "Challenge hebdomadaire du samedi";
        String descriptionTheme = "Ceci est le challenge hebdomadaire du samedi. Le dieu soleil Helios attend vos offrandes pour cette semaine.";
        String photoUrl = "https://i.imgur.com/v6xDssA.jpeg";
        String startDate = null;
        String endDate = "2026-02-28 12:00:00";
        int isArchived = 0;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            Challenge challenge = new Challenge(challengeId, titleTheme, descriptionTheme, photoUrl, startDate, endDate, isArchived);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Null date is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectEmptyStartDate() {
        // ARRANGE - Préparer les données
        int challengeId = 1;
        String titleTheme = "Challenge hebdomadaire du samedi";
        String descriptionTheme = "Ceci est le challenge hebdomadaire du samedi. Le dieu soleil Helios attend vos offrandes pour cette semaine.";
        String photoUrl = "https://i.imgur.com/v6xDssA.jpeg";
        String startDate = " ";
        String endDate = "2026-02-28 12:00:00";
        int isArchived = 0;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            Challenge challenge = new Challenge(challengeId, titleTheme, descriptionTheme, photoUrl, startDate, endDate, isArchived);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Empty date is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectInvalidStartDate() {
        // ARRANGE - Préparer les données
        int challengeId = 1;
        String titleTheme = "Challenge hebdomadaire du samedi";
        String descriptionTheme = "Ceci est le challenge hebdomadaire du samedi. Le dieu soleil Helios attend vos offrandes pour cette semaine.";
        String photoUrl = "https://i.imgur.com/v6xDssA.jpeg";
        String startDate = "2025-11-29-12:00:00";
        String endDate = "2026-02-28 12:00:00";
        int isArchived = 0;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            Challenge challenge = new Challenge(challengeId, titleTheme, descriptionTheme, photoUrl, startDate, endDate, isArchived);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Invalid format. Expected format: yyyy-MM-dd HH:mm:ss", thrownException.getMessage());
    }

    @Test
    void shouldRejectNullEndDate() {
        // ARRANGE - Préparer les données
        int challengeId = 1;
        String titleTheme = "Challenge hebdomadaire du samedi";
        String descriptionTheme = "Ceci est le challenge hebdomadaire du samedi. Le dieu soleil Helios attend vos offrandes pour cette semaine.";
        String photoUrl = "https://i.imgur.com/v6xDssA.jpeg";
        String startDate = "2025-11-29 12:00:00";
        String endDate = null;
        int isArchived = 0;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            Challenge challenge = new Challenge(challengeId, titleTheme, descriptionTheme, photoUrl, startDate, endDate, isArchived);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Null date is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectEmptyEndDate() {
        // ARRANGE - Préparer les données
        int challengeId = 1;
        String titleTheme = "Challenge hebdomadaire du samedi";
        String descriptionTheme = "Ceci est le challenge hebdomadaire du samedi. Le dieu soleil Helios attend vos offrandes pour cette semaine.";
        String photoUrl = "https://i.imgur.com/v6xDssA.jpeg";
        String startDate = "2025-11-29 12:00:00";
        String endDate = " ";
        int isArchived = 0;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            Challenge challenge = new Challenge(challengeId, titleTheme, descriptionTheme, photoUrl, startDate, endDate, isArchived);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Empty date is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectInvalidEndDate() {
        // ARRANGE - Préparer les données
        int challengeId = 1;
        String titleTheme = "Challenge hebdomadaire du samedi";
        String descriptionTheme = "Ceci est le challenge hebdomadaire du samedi. Le dieu soleil Helios attend vos offrandes pour cette semaine.";
        String photoUrl = "https://i.imgur.com/v6xDssA.jpeg";
        String startDate = "2025-11-29 12:00:00";
        String endDate = "2026-02-28?12:00:00";
        int isArchived = 0;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            Challenge challenge = new Challenge(challengeId, titleTheme, descriptionTheme, photoUrl, startDate, endDate, isArchived);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Invalid format. Expected format: yyyy-MM-dd HH:mm:ss", thrownException.getMessage());
    }
}