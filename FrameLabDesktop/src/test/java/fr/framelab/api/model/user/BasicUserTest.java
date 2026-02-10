package fr.framelab.api.model.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicUserTest {
    private BasicUser basicUser;
    private final int USER_ID = 1;
    private final String FIRST_NAME = "Alice";
    private final String LAST_NAME = "Dupont";
    private final boolean IS_ADMIN = false;

    /**
     * Prépare un basic user valide avant chaque test
     */
    @BeforeEach
    void setUp() {
        basicUser = new BasicUser(USER_ID, FIRST_NAME, LAST_NAME, IS_ADMIN);
    }

    @Test
    void shouldRejectLessthan0UserId() {
        // ARRANGE - Préparer les données
        int userId = -1;
        String firstName = "Alice";
        String lastName = "Dupont";
        boolean isAdmin = false;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            BasicUser basicUser = new BasicUser(userId, firstName, lastName, isAdmin);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Negative id is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectNullFirstName() {
        // ARRANGE - Préparer les données
        int userId = 1;
        String firstName = null;
        String lastName = "Dupont";
        boolean isAdmin = false;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            BasicUser basicUser = new BasicUser(userId, firstName, lastName, isAdmin);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Null firstName is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectEmptyFirstName() {
        // ARRANGE - Préparer les données
        int userId = 1;
        String firstName = " ";
        String lastName = "Dupont";
        boolean isAdmin = false;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            BasicUser basicUser = new BasicUser(userId, firstName, lastName, isAdmin);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Empty firstName is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectNullLastName() {
        // ARRANGE - Préparer les données
        int userId = 1;
        String firstName = "Alice";
        String lastName = null;
        boolean isAdmin = false;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            BasicUser basicUser = new BasicUser(userId, firstName, lastName, isAdmin);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Null lastName is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectEmptyLastName() {
        // ARRANGE - Préparer les données
        int userId = 1;
        String firstName = "Alice";
        String lastName = " ";
        boolean isAdmin = false;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            BasicUser basicUser = new BasicUser(userId, firstName, lastName, isAdmin);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Empty lastName is not allowed", thrownException.getMessage());
    }
}