package fr.framelab.services;

import fr.framelab.models.Challenge;
import fr.framelab.models.User;
import fr.framelab.dao.ChallengeDAO;
import fr.framelab.dao.LayerDAO;
import fr.framelab.dao.ProjectDAO;
import fr.framelab.dao.UserDAO;
import fr.framelab.models.Layer;
import fr.framelab.models.Project;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LayerServiceTest {
    private Connection connection;

    private LayerService layerService;
    private ProjectService projectService;
    private UserService userService;
    private ChallengeService challengeService;

    private final String TITLE = "Mon premier projet";
    private final int USER_ID = 1;
    private final int CHALLENGE_ID = 1;
    private final LocalDateTime CREATED_AT = LocalDateTime.of(2026, 1, 1, 0, 0, 0);
    private final LocalDateTime EDITED_AT = LocalDateTime.of(2026, 1, 1, 12, 0, 0);

    private final int LAYER_INDEX = 0;
    private final int NEW_LAYER_INDEX = 1;

    private Project testProject;

    @BeforeEach
    void setUp() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite::memory:");

        // Activer les FOREIGN KEY constraints
        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        // Créer le service utilisateur
        UserDAO userDAO = new UserDAO(this.connection);
        this.userService = new UserService(userDAO);

        // Insérer un utilisateur test pour satisfaire les clés étrangères
        User testUser = new User(
                1,
                "Alice",
                "Dupont",
                0,
                "alice.dupont@email.com",
                "token"
        );
        this.userService.saveUser(testUser);

        // Créer le service challenge
        ChallengeDAO challengeDAO = new ChallengeDAO(this.connection);
        this.challengeService = new ChallengeService(challengeDAO);

        // Insérer un challenge test pour satisfaire les clés étrangères
        Challenge testChallenge = new Challenge(
                1,
                "Challenge hebdomadaire du samedi",
                "Ceci est le challenge hebdomadaire du samedi. Le dieu soleil Helios attend vos offrandes pour cette semaine.",
                "https://i.imgur.com/v6xDssA.jpeg",
                "2025-11-29 12:00:00",
                "2026-02-28 12:00:00",
                0
        );
        this.challengeService.saveChallenge(testChallenge);

        // Créer le service project
        ProjectDAO projectDAO = new ProjectDAO(this.connection);
        this.projectService = new ProjectService(projectDAO);

        // Insérer un project test pour satisfaire les clés étrangères
        this.testProject = new Project(TITLE, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        this.projectService.saveProject(this.testProject);

        // Créer le service layer
        LayerDAO layerDAO = new LayerDAO(this.connection);
        this.layerService = new LayerService(layerDAO);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (this.connection != null) this.connection.close();
    }

    @Test
    void shouldSaveAndRetrieveLayer() {
        // ARRANGE - Préparer les données
        Layer layer = new Layer(this.testProject.getId(), LAYER_INDEX);

        // ACT - Exécuter l'action à tester
        this.layerService.saveLayer(layer);
        Layer retrievedLayer = this.layerService.getLayer(layer.getId());

        // ASSERT - Vérifier le résultat
        assertEquals(layer.getId(), retrievedLayer.getId());
    }

    @Test
    void shouldUpdateExistingLayer() {
        // ARRANGE - Préparer les données
        Layer layer = new Layer(this.testProject.getId(), LAYER_INDEX);
        this.layerService.saveLayer(layer);
        int originalId = layer.getId();
        layer.setIndex(NEW_LAYER_INDEX);

        // ACT - Exécuter l'action à tester
        this.layerService.saveLayer(layer);

        // ASSERT - Vérifier le résultat
        Layer retrievedLayer = this.layerService.getLayer(originalId);
        assertEquals(NEW_LAYER_INDEX, retrievedLayer.getIndex());
    }

    @Test
    void shouldReturnAllLayersForProject() {
        // ARRANGE - Préparer les données
        Layer layer1 = new Layer(this.testProject.getId(), LAYER_INDEX);
        Layer layer2 = new Layer(this.testProject.getId(), NEW_LAYER_INDEX);
        this.layerService.saveLayer(layer1);
        this.layerService.saveLayer(layer2);

        // ACT - Exécuter l'action à tester
        List<Layer> layers = this.layerService.getProjectLayers(this.testProject.getId());

        // ASSERT - Vérifier le résultat
        assertEquals(2, layers.size());
    }

    @Test
    void shouldReturnEmptyListWhenProjectHasNoLayers() {
        // ARRANGE - Préparer les données
        int unknownProjectId = 999;

        // ACT - Exécuter l'action à tester
        List<Layer> layers = this.layerService.getProjectLayers(unknownProjectId);

        // ASSERT - Vérifier le résultat
        assertNotNull(layers);
        assertEquals(0, layers.size());
    }

    @Test
    void shouldDeleteLayer() {
        // ARRANGE - Préparer les données
        Layer layer = new Layer(this.testProject.getId(), LAYER_INDEX);
        this.layerService.saveLayer(layer);

        // ACT - Exécuter l'action à tester
        this.layerService.deleteLayer(layer.getId());

        // ASSERT - Vérifier le résultat
        Layer retrievedLayer = this.layerService.getLayer(layer.getId());
        assertNull(retrievedLayer);
    }

    @Test
    void shouldDeleteAllLayersForProject() {
        // ARRANGE - Préparer les données
        Layer layer1 = new Layer(this.testProject.getId(), LAYER_INDEX);
        Layer layer2 = new Layer(this.testProject.getId(), NEW_LAYER_INDEX);
        this.layerService.saveLayer(layer1);
        this.layerService.saveLayer(layer2);

        // ACT - Exécuter l'action à tester
        this.layerService.deleteProjectLayers(this.testProject.getId());

        // ASSERT - Vérifier le résultat
        List<Layer> layers = this.layerService.getProjectLayers(this.testProject.getId());
        assertEquals(0, layers.size());
    }

    @Test
    void shouldThrowExceptionWhenSavingNullLayer() {
        // ARRANGE - Préparer les données
        Layer layer = null;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            this.layerService.saveLayer(layer);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Layer cannot be null", thrownException.getMessage());
    }
}