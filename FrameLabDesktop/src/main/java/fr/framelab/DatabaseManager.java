package fr.framelab;

import fr.framelab.dao.*;
import fr.framelab.services.*;

import java.sql.*;

public class DatabaseManager {
    final Connection connection;

    public final UserService userService;
    public final ChallengeService challengeService;
    public final ProjectService projectService;
    public final LayerService layerService;

    public DatabaseManager() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:framelab.db");

        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        UserDAO userDAO = new UserDAO(this.connection);
        ChallengeDAO challengeDAO = new ChallengeDAO(this.connection);
        ProjectDAO projectDAO = new ProjectDAO(this.connection);
        LayerDAO layerDAO = new LayerDAO(this.connection);

        this.userService = new UserService(userDAO);
        this.challengeService = new ChallengeService(challengeDAO);
        this.projectService = new ProjectService(projectDAO);
        this.layerService = new LayerService(layerDAO);
    }

    public void close() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }
}