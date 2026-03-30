package fr.framelab.api;

import fr.framelab.exceptions.http.HttpUnauthorizedException;
import fr.framelab.models.Challenge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FrameLabAPITest {
    private FrameLabAPI frameLabAPI;
    private final String DOMAINE_NAME = "localhost:3000";
    private final boolean IS_HTTPS = false;

    private final String VALID_EMAIL = "hawana@gmail.com";
    private final String VALID_PASSWORD = "password123";

    /**
     * Prépare une instance api valide avant chaque test
     */
    @BeforeEach
    void setUp() {
        this.frameLabAPI = new FrameLabAPI(DOMAINE_NAME, IS_HTTPS);
    }

    @Test
    void shouldLoginWithValidCredential() throws IOException, InterruptedException {
        // ARRANGE - Préparer les données
        boolean logged = false;

        // ACT - Exécuter l'action à tester
        logged = this.frameLabAPI.login(VALID_EMAIL, VALID_PASSWORD);

        // ASSERT - Vérifier le résultat
        assertTrue(logged);
    }

    @Test
    void shouldRejectInvalidCredential() {
        // ARRANGE - Préparer les données
        String email = "hawana@gmail.com";
        String password = "password1234";
        boolean logged = false;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            logged = this.frameLabAPI.login(email, password);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(HttpUnauthorizedException.class, thrownException);
        assertEquals("Email ou mot de passe incorrect", thrownException.getMessage());
    }

    @Test
    void shouldGetActiveChallenge() throws IOException, InterruptedException {
        // ARRANGE - Préparer les données
        this.frameLabAPI.login(VALID_EMAIL, VALID_PASSWORD);
        Challenge activeChallenge = null;

        // ACT - Exécuter l'action à tester
        activeChallenge = this.frameLabAPI.getActiveChallenge();

        // ASSERT - Vérifier le résultat
        assertNotNull(activeChallenge);
    }

    void shouldRejectNotLoggedAccont() {
        // ARRANGE - Préparer les données
        Challenge activeChallenge = null;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            activeChallenge = this.frameLabAPI.getActiveChallenge();
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(HttpUnauthorizedException.class, thrownException);
        assertEquals("Token manquant, invalide ou expiré", thrownException.getMessage());
    }
}