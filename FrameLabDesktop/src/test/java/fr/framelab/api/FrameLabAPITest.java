package fr.framelab.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FrameLabAPITest {
    private FrameLabAPI frameLabAPI;
    private final String DOMAINE_NAME = "localhost:3000";
    private final boolean IS_HTTPS = false;

    /**
     * Prépare une instance api valide avant chaque test
     */
    @BeforeEach
    void setUp() {
        frameLabAPI = new FrameLabAPI(DOMAINE_NAME, IS_HTTPS);
    }

    @Test
    void shouldLogginWithValidCredential() throws IOException, InterruptedException {
        // ARRANGE - Préparer les données
        String email = "hawana@gmail.com";
        String password = "password123";
        boolean logged = false;

        // ACT - Exécuter l'action à tester
        logged = frameLabAPI.login(email, password);

        // ASSERT - Vérifier le résultat
        assertTrue(logged);
    }
}