package fr.framelab.dao;

import fr.framelab.models.User;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    private UserDAO userDAO;
    private Connection testConnection;

    private static final String FIRST_NAME = "Alice";
    private static final String LAST_NAME = "Dupont";
    private static final int IS_ADMIN = 0;
    private static final String EMAIL = "alice.dupont@email.com";
    private static final String TOKEN = "token";

    @BeforeEach
    void setUp() throws SQLException {
        this.testConnection = DriverManager.getConnection("jdbc:sqlite::memory:");

        // Activer les FOREIGN KEY constraints
        try (Statement stmt = this.testConnection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        this.userDAO = new UserDAO(this.testConnection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (this.testConnection != null && !this.testConnection.isClosed()) {
            this.testConnection.close();
        }
    }

    @Test
    void shouldSaveUser() {
        // ARRANGE - Préparer les données
        User user = new User(0, FIRST_NAME, LAST_NAME, IS_ADMIN, EMAIL, TOKEN);

        // ACT - Exécuter l'action à tester
        this.userDAO.save(user);

        // ASSERT - Vérifier le résultat
        assertEquals(0, user.getId());

        try (Statement stmt = this.testConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE id = " + user.getId())) {

            assertTrue(rs.next());
            assertEquals(FIRST_NAME, rs.getString("first_name"));
            assertEquals(LAST_NAME, rs.getString("last_name"));
            assertEquals(IS_ADMIN, rs.getInt("is_admin"));
            assertEquals(EMAIL, rs.getString("email"));
            assertEquals(TOKEN, rs.getString("token"));
        } catch (SQLException e) {
            fail("Erreur SQL : " + e.getMessage());
        }
    }

    @Test
    void shouldAssignDifferentIdsToMultipleUsers() {
        // ARRANGE - Préparer les données
        User user1 = new User(1, "Jean", "Jean", 0, "jean.jean@email.com", "token");
        User user2 = new User(2, "Pascalou", "Pascalou", 0, "pascalou-pascalou@email.com", "token");

        // ACT - Exécuter l'action à tester
        this.userDAO.save(user1);
        this.userDAO.save(user2);

        // ASSERT - Vérifier le résultat
        assertNotEquals(user1.getId(), user2.getId());
    }

    @Test
    void shouldFindUserById() {
        // ARRANGE - Préparer les données
        User user = new User(0, FIRST_NAME, LAST_NAME, IS_ADMIN, EMAIL, TOKEN);
        this.userDAO.save(user);

        // ACT - Exécuter l'action à tester
        User found = this.userDAO.findById(user.getId());

        // ASSERT - Vérifier le résultat
        assertNotNull(found);
        assertEquals(user.getId(), found.getId());
        assertEquals(FIRST_NAME, found.getFirstName());
        assertEquals(LAST_NAME, found.getLastName());
        assertEquals(EMAIL, found.getEmail());
    }

    @Test
    void shouldFindUserByEmail() {
        // ARRANGE - Préparer les données
        User user = new User(0, FIRST_NAME, LAST_NAME, IS_ADMIN, EMAIL, TOKEN);
        this.userDAO.save(user);

        // ACT - Exécuter l'action à tester
        User found = this.userDAO.findByEmail(EMAIL);

        // ASSERT - Vérifier le résultat
        assertNotNull(found);
        assertEquals(EMAIL, found.getEmail());
        assertEquals(user.getId(), found.getId());
    }

    @Test
    void shouldReturnNullForNonExistentId() {
        // ARRANGE - Préparer les données
        int nonExistentId = 9999;

        // ACT - Exécuter l'action à tester
        User found = this.userDAO.findById(nonExistentId);

        // ASSERT - Vérifier le résultat
        assertNull(found);
    }

    @Test
    void shouldFindAllUsers() {
        // ARRANGE - Préparer les données
        this.userDAO.save(new User(1, "Bob", "Martin", 0, "bob.martin@test.fr", "token"));
        this.userDAO.save(new User(2, "Charlie", "Dubois", 1, "charlie.admin@test.fr", "token"));

        // ACT - Exécuter l'action à tester
        ArrayList<User> users = this.userDAO.findAll();

        // ASSERT - Vérifier le résultat
        assertEquals(2, users.size());
        assertEquals("Bob", users.get(0).getFirstName());
        assertEquals("Charlie", users.get(1).getFirstName());
    }

    @Test
    void shouldUpdateUserNamesAndEmail() {
        // ARRANGE - Préparer les données
        User user = new User(0, FIRST_NAME, LAST_NAME, IS_ADMIN, EMAIL, TOKEN);
        this.userDAO.save(user);

        user.setFirstName("Alicia");
        user.setLastName("Durand");
        user.setEmail("alicia.durand@newmail.com");

        // ACT - Exécuter l'action à tester
        this.userDAO.update(user);

        // ASSERT - Vérifier le résultat
        User updated = this.userDAO.findById(user.getId());
        assertNotNull(updated);
        assertEquals("Alicia", updated.getFirstName());
        assertEquals("Durand", updated.getLastName());
        assertEquals("alicia.durand@newmail.com", updated.getEmail());
    }

    @Test
    void shouldDeleteUserById() {
        // ARRANGE - Préparer les données
        User user = new User(0, FIRST_NAME, LAST_NAME, IS_ADMIN, EMAIL, TOKEN);
        this.userDAO.save(user);
        int savedId = user.getId();

        // ACT - Exécuter l'action à tester
        this.userDAO.deleteById(savedId);

        // ASSERT - Vérifier le résultat
        User deleted = this.userDAO.findById(savedId);
        assertNull(deleted);
    }

    @Test
    void shouldThrowExceptionWhenSavingDuplicateEmail() {
        // ARRANGE - Préparer les données
        User user1 = new User(0, "Premier", "User", 0, "unique@compte.com", "token");
        User user2 = new User(0, "Second", "User", 0, "unique@compte.com", "token");
        this.userDAO.save(user1);

        // ACT & ASSERT
        assertThrows(RuntimeException.class, () -> this.userDAO.save(user2));
    }
}