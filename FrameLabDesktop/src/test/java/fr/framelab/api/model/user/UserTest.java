package fr.framelab.api.model.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user;
    private final int USER_ID = 1;
    private final String FIRST_NAME = "Alice";
    private final String LAST_NAME = "Dupont";
    private final int IS_ADMIN = 1;
    private final String EMAIL = "alice.dupont@email.com";

    /**
     * Prépare un basic user valide avant chaque test
     */
    @BeforeEach
    void setUp() {
        user = new User(USER_ID, FIRST_NAME, LAST_NAME, IS_ADMIN, EMAIL);
    }

    @Test
    void shouldRejectLessthan0UserId() {
        // ARRANGE - Préparer les données
        int userId = -1;
        String firstName = "Alice";
        String lastName = "Dupont";
        int isAdmin = 0;
        String email = "alice.dupont@email.com";

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            User user = new User(userId, firstName, lastName, isAdmin, email);
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
        int isAdmin = 0;
        String email = "alice.dupont@email.com";

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            User user = new User(userId, firstName, lastName, isAdmin, email);
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
        int isAdmin = 0;
        String email = "alice.dupont@email.com";

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            User user = new User(userId, firstName, lastName, isAdmin, email);
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
        int isAdmin = 0;
        String email = "alice.dupont@email.com";

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            User user = new User(userId, firstName, lastName, isAdmin, email);
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
        int isAdmin = 0;
        String email = "alice.dupont@email.com";

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            User user = new User(userId, firstName, lastName, isAdmin, email);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Empty lastName is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectNullEmail() {
        // ARRANGE - Préparer les données
        int userId = 1;
        String firstName = "Alice";
        String lastName = "Dupont";
        int isAdmin = 0;
        String email = null;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            User user = new User(userId, firstName, lastName, isAdmin, email);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Null email is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectEmptyEmail() {
        // ARRANGE - Préparer les données
        int userId = 1;
        String firstName = "Alice";
        String lastName = "Dupont";
        int isAdmin = 0;
        String email = " ";

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            User user = new User(userId, firstName, lastName, isAdmin, email);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Empty email is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectNullToken() {
        // ARRANGE - Préparer les données
        int userId = 1;
        String firstName = "Alice";
        String lastName = "Dupont";
        int isAdmin = 0;
        String email = "alice.dupont@email.com";
        String token = null;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            User user = new User(userId, firstName, lastName, isAdmin, email, token);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Null token is not allowed", thrownException.getMessage());
    }

    @Test
    void shouldRejectEmptyToken() {
        // ARRANGE - Préparer les données
        int userId = 1;
        String firstName = "Alice";
        String lastName = "Dupont";
        int isAdmin = 0;
        String email = "alice.dupont@email.com";
        String token = " ";

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            User user = new User(userId, firstName, lastName, isAdmin, email, token);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Empty token is not allowed", thrownException.getMessage());
    }
}