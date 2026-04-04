package fr.framelab;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {
    private DatabaseManager databaseManager;

    @AfterEach
    void tearDown() throws SQLException {
        if (this.databaseManager != null) {
            this.databaseManager.close();
        }
    }

    @Test
    void shouldInitializeWithoutThrowing() {
        // ARRANGE - Préparer les données
        Exception thrownException = null;

        // ACT - Exécuter l'action à tester
        try {
            this.databaseManager = new DatabaseManager();
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNull(thrownException);
    }

    @Test
    void shouldExposeNonNullServices() throws SQLException {
        // ARRANGE - Préparer les données
        this.databaseManager = new DatabaseManager();

        // ASSERT - Vérifier le résultat
        assertNotNull(this.databaseManager.userService);
        assertNotNull(this.databaseManager.challengeService);
        assertNotNull(this.databaseManager.projectService);
        assertNotNull(this.databaseManager.layerService);
    }

    @Test
    void shouldHaveForeignKeysEnabled() throws SQLException {
        // ARRANGE - Préparer les données
        this.databaseManager = new DatabaseManager();

        // ACT - Exécuter l'action à tester
        int foreignKeysEnabled = this.databaseManager.connection
                .createStatement()
                .executeQuery("PRAGMA foreign_keys")
                .getInt(1);

        // ASSERT - Vérifier le résultat
        assertEquals(1, foreignKeysEnabled);
    }

    @Test
    void shouldCloseConnectionWithoutThrowing() throws SQLException {
        // ARRANGE - Préparer les données
        this.databaseManager = new DatabaseManager();
        Exception thrownException = null;

        // ACT - Exécuter l'action à tester
        try {
            this.databaseManager.close();
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNull(thrownException);
    }

    @Test
    void shouldHaveConnectionClosedAfterClose() throws SQLException {
        // ARRANGE - Préparer les données
        this.databaseManager = new DatabaseManager();

        // ACT - Exécuter l'action à tester
        this.databaseManager.close();

        // ASSERT - Vérifier le résultat
        assertTrue(this.databaseManager.connection.isClosed());
    }
}