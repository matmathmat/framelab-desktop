package fr.framelab.dao;

import fr.framelab.models.Challenge;
import java.sql.*;
import java.util.ArrayList;

public class ChallengeDAO {
    private final Connection connection;

    public ChallengeDAO(Connection connection) {
        this.connection = connection;
        initializeTable();
    }

    private void initializeTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS challenges (
                id INTEGER PRIMARY KEY,
                title_theme TEXT NOT NULL,
                description_theme TEXT NOT NULL,
                photo_url TEXT NOT NULL,
                start_date TEXT NOT NULL,
                end_date TEXT NOT NULL,
                is_archived INTEGER NOT NULL DEFAULT 0
            )
        """;

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create challenges table: " + e.getMessage(), e);
        }
    }

    public void save(Challenge challenge) {
        String sql = "INSERT INTO challenges (id, title_theme, description_theme, photo_url, start_date, end_date, is_archived) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, challenge.getId());
            pstmt.setString(2, challenge.getTitleTheme());
            pstmt.setString(3, challenge.getDescriptionTheme());
            pstmt.setString(4, challenge.getPhotoUrl());
            pstmt.setString(5, challenge.getStartDate());
            pstmt.setString(6, challenge.getEndDate());
            pstmt.setInt(7, challenge.getIsArchived() ? 1 : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save challenge: " + e.getMessage(), e);
        }
    }

    public void update(Challenge challenge) {
        String sql = """
            UPDATE challenges 
            SET title_theme = ?, description_theme = ?, photo_url = ?, start_date = ?, end_date = ?, is_archived = ? 
            WHERE id = ?
        """;

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setString(1, challenge.getTitleTheme());
            pstmt.setString(2, challenge.getDescriptionTheme());
            pstmt.setString(3, challenge.getPhotoUrl());
            pstmt.setString(4, challenge.getStartDate());
            pstmt.setString(5, challenge.getEndDate());
            pstmt.setInt(6, challenge.getIsArchived() ? 1 : 0);
            pstmt.setInt(7, challenge.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update challenge " + challenge.getId() + ": " + e.getMessage(), e);
        }
    }

    public Challenge findById(int id) {
        String sql = "SELECT id, title_theme, description_theme, photo_url, start_date, end_date, is_archived FROM challenges WHERE id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRowToChallenge(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve challenge " + id + ": " + e.getMessage(), e);
        }

        return null;
    }

    public ArrayList<Challenge> findAll() {
        ArrayList<Challenge> challenges = new ArrayList<>();
        String sql = "SELECT id, title_theme, description_theme, photo_url, start_date, end_date, is_archived FROM challenges";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                challenges.add(mapRowToChallenge(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve all challenges: " + e.getMessage(), e);
        }

        return challenges;
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM challenges WHERE id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete challenge " + id + ": " + e.getMessage(), e);
        }
    }

    private Challenge mapRowToChallenge(ResultSet rs) throws SQLException {
        return new Challenge(
                rs.getInt("id"),
                rs.getString("title_theme"),
                rs.getString("description_theme"),
                rs.getString("photo_url"),
                rs.getString("start_date"),
                rs.getString("end_date"),
                rs.getInt("is_archived")
        );
    }
}