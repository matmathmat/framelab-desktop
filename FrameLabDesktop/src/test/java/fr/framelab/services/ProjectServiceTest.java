package fr.framelab.services;

import fr.framelab.api.models.Challenge;
import fr.framelab.api.models.User;
import fr.framelab.dao.ChallengeDAO;
import fr.framelab.dao.ProjectDAO;
import fr.framelab.dao.UserDAO;
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

public class ProjectServiceTest {
    private Connection connection;

    private ProjectService projectService;
    private UserService userService;
    private ChallengeService challengeService;

    private final String TITLE = "Mon premier projet";
    private final int USER_ID = 1;
    private final int CHALLENGE_ID = 1;
    private final LocalDateTime CREATED_AT = LocalDateTime.of(2026, 1, 1, 0, 0, 0);
    private final LocalDateTime EDITED_AT = LocalDateTime.of(2026, 1, 1, 12, 0, 0);
    private final String NEW_TITLE = "Mon plus gros projet";

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

        // on crée maintenant le service project
        ProjectDAO projectDAO = new ProjectDAO(this.connection);
        this.projectService = new ProjectService(projectDAO);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (this.connection != null) this.connection.close();
    }

    @Test
    void shouldSaveAndRetrieveProject() {
        // ARRANGE - Préparer les données
        Project project = new Project(TITLE, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);

        // ACT - Exécuter l'action à tester
        this.projectService.saveProject(project);
        Project retrieveProject = this.projectService.getProject(project.getId());

        // ASSERT - Vérifier le résultat
        assertEquals(project.getId(), retrieveProject.getId());
    }

    @Test
    void shouldReturnAllProjectsForUser() {
        // ARRANGE - Préparer les données
        Project project1 = new Project(TITLE, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        Project project2 = new Project(NEW_TITLE, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        this.projectService.saveProject(project1);
        this.projectService.saveProject(project2);

        // ACT - Exécuter l'action à tester
        List<Project> projects = this.projectService.getUserProjects(USER_ID);

        // ASSERT - Vérifier le résultat
        assertEquals(2, projects.size());
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoProjects() {
        // ARRANGE - Préparer les données
        int unknownUserId = 999;

        // ACT - Exécuter l'action à tester
        List<Project> projects = this.projectService.getUserProjects(unknownUserId);

        // ASSERT - Vérifier le résultat
        assertNotNull(projects);
        assertEquals(0, projects.size());
    }

    @Test
    void shouldReturnOnlyProjectsMatchingChallengeAndUser() {
        // ARRANGE - Préparer les données
        Challenge testChallenge = new Challenge(
                2,
                "Challenge hebdomadaire du mercredi",
                "Ceci est le challenge hebdomadaire du mercredi.",
                "https://i.imgur.com/v6xDssA.jpeg",
                "2025-11-29 12:00:00",
                "2026-02-28 12:00:00",
                0
        );
        this.challengeService.saveChallenge(testChallenge);
        int otherChallengeId = 2;
        Project project1 = new Project(TITLE, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        Project project2 = new Project(NEW_TITLE, USER_ID, otherChallengeId, CREATED_AT, EDITED_AT);
        this.projectService.saveProject(project1);
        this.projectService.saveProject(project2);

        // ACT - Exécuter l'action à tester
        List<Project> projects = this.projectService.getUserProjectsByChallenge(CHALLENGE_ID, USER_ID);

        // ASSERT - Vérifier le résultat
        assertEquals(1, projects.size());
        assertEquals(CHALLENGE_ID, projects.get(0).getChallengeId());
    }

    @Test
    void shouldReturnEmptyListWhenNoChallengeMatchForUser() {
        // ARRANGE - Préparer les données
        Project project = new Project(TITLE, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        this.projectService.saveProject(project);
        int unknownChallengeId = 999;

        // ACT - Exécuter l'action à tester
        List<Project> projects = this.projectService.getUserProjectsByChallenge(unknownChallengeId, USER_ID);

        // ASSERT - Vérifier le résultat
        assertNotNull(projects);
        assertEquals(0, projects.size());
    }

    @Test
    void shouldUpdateExistingProject() {
        // ARRANGE - Préparer les données
        Project project = new Project(TITLE, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        this.projectService.saveProject(project);
        int originalId = project.getId();
        project.setTitle(NEW_TITLE);

        // ACT - Exécuter l'action à tester
        this.projectService.saveProject(project);

        // ASSERT - Vérifier le résultat
        Project retrieveProject = this.projectService.getProject(originalId);
        assertEquals(project.getTitle(), retrieveProject.getTitle());
    }

    @Test
    void shouldDeleteProject() {
        // ARRANGE - Préparer les données
        Project project = new Project(TITLE, USER_ID, CHALLENGE_ID, CREATED_AT, EDITED_AT);
        this.projectService.saveProject(project);

        // ACT - Exécuter l'action à tester
        this.projectService.deleteProject(project.getId());

        // ASSERT - Vérifier le résultat
        Project retrieveProject = this.projectService.getProject(project.getId());
        assertNull(retrieveProject);
    }

    @Test
    void shouldThrowExceptionWhenSavingNullProject() {
        // ARRANGE - Préparer les données
        Project project = null;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            this.projectService.saveProject(project);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("Project cannot be null", thrownException.getMessage());
    }
}