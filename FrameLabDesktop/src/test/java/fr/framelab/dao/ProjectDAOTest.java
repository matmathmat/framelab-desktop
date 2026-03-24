package fr.framelab.dao;

import fr.framelab.models.Project;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProjectDAOTest {
    private ProjectDAO projectDAO;
    private Connection testConnection;

    private static final String TITLE = "Mon premier projet";
    private static final int USER_ID = 1;
    private static final int CHALLENGE_ID = 1;
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2026, 1, 1, 0, 0, 0);
    private static final LocalDateTime EDITED_AT = LocalDateTime.of(2026, 1, 1, 12, 0, 0);

    @BeforeEach
    void setUp() throws SQLException {
        this.testConnection = DriverManager.getConnection("jdbc:sqlite::memory:");

        // Activer les FOREIGN KEY constraints
        try (Statement stmt = this.testConnection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        // Créer les tables parentes pour respecter les contraintes de clés étrangères
        try (Statement stmt = this.testConnection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                )
            """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS challenges (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                )
            """);
            stmt.execute("INSERT INTO users (name) VALUES ('Test User')");
            stmt.execute("INSERT INTO challenges (name) VALUES ('Test Challenge')");
        }

        this.projectDAO = new ProjectDAO(this.testConnection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (this.testConnection != null && !this.testConnection.isClosed()) {
            this.testConnection.close();
        }
    }

    @Test
    void shouldSaveProjectAndAssignId() {
        // ARRANGE - Préparer les données
        Project project = new Project(TITLE, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);

        // ACT - Exécuter l'action à tester
        this.projectDAO.save(project);

        // ASSERT - Vérifier le résultat
        assertNotEquals(-1, project.getId());

        try (Statement stmt = this.testConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM projects WHERE id = " + project.getId())) {

            assertTrue(rs.next());
            assertEquals(TITLE, rs.getString("title"));
            assertEquals(USER_ID, rs.getInt("user_id"));
            assertEquals(CHALLENGE_ID, rs.getInt("challenge_id"));
            assertEquals(CREATED_AT.toString(), rs.getString("created_at"));
            assertEquals(EDITED_AT.toString(), rs.getString("edited_at"));
        } catch (SQLException e) {
            fail("Erreur SQL : " + e.getMessage());
        }
    }

    @Test
    void shouldAssignDifferentIdsToMultipleProjects() {
        // ARRANGE - Préparer les données
        Project project1 = new Project("Premier projet", USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        Project project2 = new Project("Deuxième projet", USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);

        // ACT - Exécuter l'action à tester
        this.projectDAO.save(project1);
        this.projectDAO.save(project2);

        // ASSERT - Vérifier le résultat
        assertNotEquals(project1.getId(), project2.getId());
    }

    @Test
    void shouldFindProjectById() {
        // ARRANGE - Préparer les données
        Project project = new Project(TITLE, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        this.projectDAO.save(project);

        // ACT - Exécuter l'action à tester
        Project found = this.projectDAO.findById(project.getId());

        // ASSERT - Vérifier le résultat
        assertNotNull(found);
        assertEquals(project.getId(), found.getId());
        assertEquals(TITLE, found.getTitle());
        assertEquals(USER_ID, found.getUserId());
        assertEquals(CHALLENGE_ID, found.getChallengeId());
        assertEquals(CREATED_AT, found.getCreatedAt());
        assertEquals(EDITED_AT, found.getEditedAt());
    }

    @Test
    void shouldReturnNullForNonExistentId() {
        // ARRANGE - Préparer les données
        int nonExistentId = 9999;

        // ACT - Exécuter l'action à tester
        Project found = this.projectDAO.findById(nonExistentId);

        // ASSERT - Vérifier le résultat
        assertNull(found);
    }

    @Test
    void shouldFindProjectsByUserId() {
        // ARRANGE - Préparer les données
        Project project1 = new Project("Premier projet", USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        Project project2 = new Project("Deuxième projet", USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        this.projectDAO.save(project1);
        this.projectDAO.save(project2);

        // ACT - Exécuter l'action à tester
        ArrayList<Project> projects = this.projectDAO.findByUserId(USER_ID);

        // ASSERT - Vérifier le résultat
        assertEquals(2, projects.size());
        assertEquals("Premier projet", projects.get(0).getTitle());
        assertEquals("Deuxième projet", projects.get(1).getTitle());
    }

    @Test
    void shouldReturnEmptyListForNonExistentUserId() {
        // ARRANGE - Préparer les données
        int nonExistentUserId = 9999;

        // ACT - Exécuter l'action à tester
        ArrayList<Project> projects = this.projectDAO.findByUserId(nonExistentUserId);

        // ASSERT - Vérifier le résultat
        assertNotNull(projects);
        assertTrue(projects.isEmpty());
    }

    @Test
    void shouldFindProjectsByChallengeId() {
        // ARRANGE - Préparer les données
        Project project1 = new Project("Premier projet", USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        Project project2 = new Project("Deuxième projet", USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        this.projectDAO.save(project1);
        this.projectDAO.save(project2);

        // ACT - Exécuter l'action à tester
        ArrayList<Project> projects = this.projectDAO.findByChallengeId(CHALLENGE_ID);

        // ASSERT - Vérifier le résultat
        assertEquals(2, projects.size());
    }

    @Test
    void shouldReturnEmptyListForNonExistentChallengeId() {
        // ARRANGE - Préparer les données
        int nonExistentChallengeId = 9999;

        // ACT - Exécuter l'action à tester
        ArrayList<Project> projects = this.projectDAO.findByChallengeId(nonExistentChallengeId);

        // ASSERT - Vérifier le résultat
        assertNotNull(projects);
        assertTrue(projects.isEmpty());
    }

    @Test
    void shouldUpdateProjectTitleAndEditedAt() {
        // ARRANGE - Préparer les données
        Project project = new Project(TITLE, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        this.projectDAO.save(project);
        project.setTitle("Titre modifié");
        LocalDateTime newEditedAt = LocalDateTime.of(2026, 6, 1, 8, 0, 0);
        project.setEditedAt(newEditedAt);

        // ACT - Exécuter l'action à tester
        this.projectDAO.update(project);

        // ASSERT - Vérifier le résultat
        Project updated = this.projectDAO.findById(project.getId());
        assertNotNull(updated);
        assertEquals("Titre modifié", updated.getTitle());
        assertEquals(newEditedAt, updated.getEditedAt());
    }

    @Test
    void shouldNotUpdateCreatedAt() {
        // ARRANGE - Préparer les données
        Project project = new Project(TITLE, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        this.projectDAO.save(project);
        project.setTitle("Titre modifié");
        project.setEditedAt(LocalDateTime.of(2026, 6, 1, 8, 0, 0));

        // ACT - Exécuter l'action à tester
        this.projectDAO.update(project);

        // ASSERT - Vérifier le résultat
        Project updated = this.projectDAO.findById(project.getId());
        assertNotNull(updated);
        assertEquals(CREATED_AT, updated.getCreatedAt());
    }

    @Test
    void shouldDeleteProjectById() {
        // ARRANGE - Préparer les données
        Project project = new Project(TITLE, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        this.projectDAO.save(project);
        int savedId = project.getId();

        // ACT - Exécuter l'action à tester
        this.projectDAO.deleteById(savedId);

        // ASSERT - Vérifier le résultat
        Project deleted = this.projectDAO.findById(savedId);
        assertNull(deleted);
    }

    @Test
    void shouldDeleteAllProjectsByUserId() {
        // ARRANGE - Préparer les données
        Project project1 = new Project("Premier projet", USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        Project project2 = new Project("Deuxième projet", USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        this.projectDAO.save(project1);
        this.projectDAO.save(project2);

        // ACT - Exécuter l'action à tester
        this.projectDAO.deleteByUserId(USER_ID);

        // ASSERT - Vérifier le résultat
        ArrayList<Project> projects = this.projectDAO.findByUserId(USER_ID);
        assertTrue(projects.isEmpty());
    }

    @Test
    void shouldNotDeleteProjectsFromOtherUsers() throws SQLException {
        // ARRANGE - Préparer les données
        try (Statement stmt = this.testConnection.createStatement()) {
            stmt.execute("INSERT INTO users (name) VALUES ('Other User')");
        }
        int otherUserId = 2;

        Project project1 = new Project("Projet user 1", USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        Project project2 = new Project("Projet user 2", otherUserId, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        this.projectDAO.save(project1);
        this.projectDAO.save(project2);

        // ACT - Exécuter l'action à tester
        this.projectDAO.deleteByUserId(USER_ID);

        // ASSERT - Vérifier le résultat
        ArrayList<Project> remainingProjects = this.projectDAO.findByUserId(otherUserId);
        assertEquals(1, remainingProjects.size());
        assertEquals("Projet user 2", remainingProjects.getFirst().getTitle());
    }
}