package fr.framelab.dao;

import fr.framelab.models.Project;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ProjectDAO {
    private final Connection connection;

    public ProjectDAO(Connection connection) {
        this.connection = connection;
        initializeTable();
    }

    private void initializeTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS projects (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                user_id INTEGER NOT NULL,
                challenge_id INTEGER NOT NULL,
                created_at TEXT NOT NULL,
                edited_at TEXT NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                FOREIGN KEY (challenge_id) REFERENCES challenges(id) ON DELETE CASCADE
            )
        """;

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create projects table: " + e.getMessage(), e);
        }
    }

    public void save(Project project) {
        String sql = "INSERT INTO projects (title, user_id, challenge_id, created_at, edited_at) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, project.getTitle());
            pstmt.setInt(2, project.getUserId());
            pstmt.setInt(3, project.getChallengeId());
            pstmt.setString(4, project.getCreatedAt().toString());
            pstmt.setString(5, project.getEditedAt().toString());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    project.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save project: " + e.getMessage(), e);
        }
    }

    public void update(Project project) {
        String sql = "UPDATE projects SET title = ?, edited_at = ? WHERE id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setString(1, project.getTitle());
            pstmt.setString(2, project.getEditedAt().toString());
            pstmt.setInt(3, project.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update project " + project.getId() + ": " + e.getMessage(), e);
        }
    }

    public Project findById(int id) {
        String sql = "SELECT id, title, user_id, challenge_id, created_at, edited_at FROM projects WHERE id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRowToProject(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve project " + id + ": " + e.getMessage(), e);
        }

        return null;
    }

    public ArrayList<Project> findByUserId(int userId) {
        ArrayList<Project> projects = new ArrayList<>();

        String sql = "SELECT id, title, user_id, challenge_id, created_at, edited_at FROM projects WHERE user_id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                projects.add(mapRowToProject(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve projects for user " + userId + ": " + e.getMessage(), e);
        }

        return projects;
    }

    public ArrayList<Project> findByChallengeId(int challengeId) {
        ArrayList<Project> projects = new ArrayList<>();

        String sql = "SELECT id, title, user_id, challenge_id, created_at, edited_at FROM projects WHERE challenge_id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, challengeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                projects.add(mapRowToProject(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve projects for challenge " + challengeId + ": " + e.getMessage(), e);
        }

        return projects;
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM projects WHERE id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete project " + id + ": " + e.getMessage(), e);
        }
    }

    public void deleteByUserId(int userId) {
        String sql = "DELETE FROM projects WHERE user_id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete projects for user " + userId + ": " + e.getMessage(), e);
        }
    }

    private Project mapRowToProject(ResultSet rs) throws SQLException {
        Project project = new Project(
                rs.getString("title"),
                rs.getInt("user_id"),
                rs.getInt("challenge_id"),
                LocalDateTime.parse(rs.getString("created_at")),
                LocalDateTime.parse(rs.getString("edited_at"))
        );

        project.setId(rs.getInt("id"));

        return project;
    }
}