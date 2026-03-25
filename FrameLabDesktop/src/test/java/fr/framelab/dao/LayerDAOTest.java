package fr.framelab.dao;

import fr.framelab.api.models.Challenge;
import fr.framelab.api.models.User;
import fr.framelab.models.Layer;
import fr.framelab.models.Project;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LayerDAOTest {
    private LayerDAO layerDAO;
    private ProjectDAO projectDAO;
    private UserDAO userDAO;
    private ChallengeDAO challengeDAO;
    private Connection testConnection;

    private int testProjectId;
    private final int LAYER_INDEX = 0;

    @BeforeEach
    void setUp() throws SQLException {
        this.testConnection = DriverManager.getConnection("jdbc:sqlite::memory:");

        // Activer les FOREIGN KEY constraints
        try (Statement stmt = this.testConnection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        // Créer les tables parentes via leurs DAO respectifs
        this.userDAO = new UserDAO(this.testConnection);
        this.challengeDAO = new ChallengeDAO(this.testConnection);
        this.projectDAO = new ProjectDAO(this.testConnection);
        this.layerDAO = new LayerDAO(this.testConnection);

        // Insérer un utilisateur test pour satisfaire les clés étrangères
        User user = new User(1, "Alice", "Dupont", 0, "alicia.durand@email.com", "token");
        this.userDAO.save(user);

        // Insérer un challenge test pour satisfaire les clés étrangères
        Challenge challenge = new Challenge(1, "Challenge hebdomadaire du samedi", "Ceci est le challenge hebdomadaire du samedi. Le dieu soleil Helios attend vos offrandes.", "https://i.imgur.com/v6xDssA.jpeg", "2025-11-29 12:00:00", "2026-02-28 12:00:00", 0);
        this.challengeDAO.save(challenge);

        // Insérer un projet test pour satisfaire les clés étrangères
        Project project = new Project("Mon Projet", user.getId(), challenge.getId(), LocalDateTime.now(), LocalDateTime.now());
        this.projectDAO.save(project);
        this.testProjectId = project.getId();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (this.testConnection != null && !this.testConnection.isClosed()) {
            this.testConnection.close();
        }
    }

    @Test
    void shouldSaveLayerAndAssignId() {
        // ARRANGE - Préparer les données
        Layer layer = new Layer(testProjectId, LAYER_INDEX);

        // ACT - Exécuter l'action à tester
        this.layerDAO.save(layer);

        // ASSERT - Vérifier le résultat
        assertNotEquals(-1, layer.getId());

        try (Statement stmt = this.testConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM layers WHERE id = " + layer.getId())) {

            assertTrue(rs.next());
            assertEquals(testProjectId, rs.getInt("project_id"));
            assertEquals(LAYER_INDEX, rs.getInt("layer_index"));
        } catch (SQLException e) {
            fail("Erreur SQL : " + e.getMessage());
        }
    }

    @Test
    void shouldFindLayerById() {
        // ARRANGE - Préparer les données
        Layer layer = new Layer(testProjectId, LAYER_INDEX);
        this.layerDAO.save(layer);

        // ACT - Exécuter l'action à tester
        Layer found = this.layerDAO.findById(layer.getId());

        // ASSERT - Vérifier le résultat
        assertNotNull(found);
        assertEquals(layer.getId(), found.getId());
        assertEquals(testProjectId, found.getProjectId());
        assertEquals(LAYER_INDEX, found.getIndex());
    }

    @Test
    void shouldFindLayersByProjectId() {
        // ARRANGE - Préparer les données
        this.layerDAO.save(new Layer(testProjectId, 0));
        this.layerDAO.save(new Layer(testProjectId, 1));

        // ACT - Exécuter l'action à tester
        ArrayList<Layer> layers = this.layerDAO.findByProjectId(testProjectId);

        // ASSERT - Vérifier le résultat
        assertEquals(2, layers.size());
        assertEquals(0, layers.get(0).getIndex());
        assertEquals(1, layers.get(1).getIndex());
    }

    @Test
    void shouldUpdateLayerIndex() {
        // ARRANGE - Préparer les données
        Layer layer = new Layer(testProjectId, 0);
        this.layerDAO.save(layer);
        layer.setIndex(5);

        // ACT - Exécuter l'action à tester
        this.layerDAO.update(layer);

        // ASSERT - Vérifier le résultat
        Layer updated = this.layerDAO.findById(layer.getId());
        assertNotNull(updated);
        assertEquals(5, updated.getIndex());
    }

    @Test
    void shouldDeleteLayerById() {
        // ARRANGE - Préparer les données
        Layer layer = new Layer(testProjectId, 0);
        this.layerDAO.save(layer);
        int savedId = layer.getId();

        // ACT - Exécuter l'action à tester
        this.layerDAO.deleteById(savedId);

        // ASSERT - Vérifier le résultat
        Layer found = this.layerDAO.findById(savedId);
        assertNull(found);
    }

    @Test
    void shouldDeleteLayersByProjectId() {
        // ARRANGE - Préparer les données
        this.layerDAO.save(new Layer(testProjectId, 0));
        this.layerDAO.save(new Layer(testProjectId, 1));

        // ACT - Exécuter l'action à tester
        this.layerDAO.deleteByProjectId(testProjectId);

        // ASSERT - Vérifier le résultat
        ArrayList<Layer> layers = this.layerDAO.findByProjectId(testProjectId);
        assertTrue(layers.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenSavingDuplicateIndexForSameProject() {
        // ARRANGE - Préparer les données
        Layer layer1 = new Layer(testProjectId, 0);
        this.layerDAO.save(layer1);

        Layer layer2 = new Layer(testProjectId, 0);

        // ACT & ASSERT
        assertThrows(RuntimeException.class, () -> this.layerDAO.save(layer2));
    }

    @Test
    void shouldAllowSameIndexForDifferentProjects() throws SQLException {
        // ARRANGE - Préparer un second projet
        Project project2 = new Project("Autre Projet", 1, 1, LocalDateTime.now(), LocalDateTime.now());
        this.projectDAO.save(project2);

        Layer layer1 = new Layer(testProjectId, 10);
        Layer layer2 = new Layer(project2.getId(), 10);

        // ACT & ASSERT
        assertDoesNotThrow(() -> {
            this.layerDAO.save(layer1);
            this.layerDAO.save(layer2);
        });
    }

    @Test
    void shouldReturnNullForNonExistentId() {
        // ACT - Exécuter l'action à tester
        Layer found = this.layerDAO.findById(9999);

        // ASSERT - Vérifier le résultat
        assertNull(found);
    }
}