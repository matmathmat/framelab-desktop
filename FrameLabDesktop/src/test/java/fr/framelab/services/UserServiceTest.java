package fr.framelab.services;

import fr.framelab.api.models.User;
import fr.framelab.dao.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private Connection connection;
    private UserService userService;

    private final int USER_ID = 1;
    private static final String FIRST_NAME = "Alice";
    private static final String LAST_NAME = "Dupont";
    private static final int IS_ADMIN = 0;
    private static final String EMAIL = "alice.dupont@email.com";
    private static final String TOKEN = "token";
    private static final String NEW_TOKEN = "new-token";

    @BeforeEach
    void setUp() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
        UserDAO dao = new UserDAO(this.connection);
        this.userService = new UserService(dao);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (this.connection != null) this.connection.close();
    }

    @Test
    void shouldSaveAndRetrieveUser() {
        // ARRANGE - Préparer les données
        User user = new User(USER_ID, FIRST_NAME, LAST_NAME, IS_ADMIN, EMAIL, TOKEN);

        // ACT - Exécuter l'action à tester
        this.userService.saveUser(user);
        User retrieveUser = this.userService.getUser(user.getId());

        // ASSERT - Vérifier le résultat
        assertEquals(user.getId(), retrieveUser.getId());
    }

    @Test
    void shouldUpdateExistingUser() {
        // ARRANGE - Préparer les données
        User user = new User(USER_ID, FIRST_NAME, LAST_NAME, IS_ADMIN, EMAIL, TOKEN);
        this.userService.saveUser(user);
        int originalId = user.getId();
        user.setToken(NEW_TOKEN);

        // ACT - Exécuter l'action à tester
        this.userService.saveUser(user);

        // ASSERT - Vérifier le résultat
        User retrieveUser = this.userService.getUser(originalId);
        assertEquals(user.getToken(), retrieveUser.getToken());
    }

    @Test
    void shouldDeleteUser() {
        // ARRANGE - Préparer les données
        User user = new User(USER_ID, FIRST_NAME, LAST_NAME, IS_ADMIN, EMAIL, TOKEN);
        this.userService.saveUser(user);

        // ACT - Exécuter l'action à tester
        this.userService.deleteUser(user.getId());

        // ASSERT - Vérifier le résultat
        User retrieveUser = this.userService.getUser(user.getId());
        assertNull(retrieveUser);
    }

    @Test
    void shouldThrowExceptionWhenSavingNullUser() {
        // ARRANGE - Préparer les données
        User user = null;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            this.userService.saveUser(user);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("User cannot be null", thrownException.getMessage());
    }
}