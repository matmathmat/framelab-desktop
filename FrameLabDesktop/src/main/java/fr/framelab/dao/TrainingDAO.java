package fr.framelab.dao;

import fr.framelab.models.Training;
import java.sql.*;
import java.util.ArrayList;

public class TrainingDAO {
    private final Connection connection;

    public TrainingDAO(Connection connection) {
        this.connection = connection;
        initializeTable();
    }

    private void initializeTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS trainings (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date TEXT NOT NULL,
                user_id INTEGER NOT NULL,
                attempt_count INTEGER NOT NULL DEFAULT 0,
                completed INTEGER NOT NULL DEFAULT 0,
                UNIQUE(date, user_id),
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            )
        """;

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create trainings table: " + e.getMessage(), e);
        }
    }

    public void save(Training training) {
        String sql = "INSERT INTO trainings (date, user_id, attempt_count, completed) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, training.getDate());
            pstmt.setInt(2, training.getUserId());
            pstmt.setInt(3, training.getAttemptCount());
            pstmt.setInt(4, training.getCompletedValue());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    training.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save training: " + e.getMessage(), e);
        }
    }

    public void update(Training training) {
        String sql = "UPDATE trainings SET attempt_count = ?, completed = ? WHERE id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, training.getAttemptCount());
            pstmt.setInt(2, training.getCompletedValue());
            pstmt.setInt(3, training.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update training " + training.getId() + ": " + e.getMessage(), e);
        }
    }

    public Training findById(int id) {
        String sql = "SELECT id, date, user_id, attempt_count, completed FROM trainings WHERE id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRowToTraining(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve training " + id + ": " + e.getMessage(), e);
        }

        return null;
    }

    public Training findByDateAndUserId(String date, int userId) {
        String sql = "SELECT id, date, user_id, attempt_count, completed FROM trainings WHERE date = ? AND user_id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setInt(2, userId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRowToTraining(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve training for user " + userId + " on date " + date + ": " + e.getMessage(), e);
        }

        return null;
    }

    public ArrayList<Training> findByUserId(int userId) {
        ArrayList<Training> trainings = new ArrayList<>();

        String sql = "SELECT id, date, user_id, attempt_count, completed FROM trainings WHERE user_id = ? ORDER BY date DESC";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                trainings.add(mapRowToTraining(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve trainings for user " + userId + ": " + e.getMessage(), e);
        }

        return trainings;
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM trainings WHERE id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete training " + id + ": " + e.getMessage(), e);
        }
    }

    private Training mapRowToTraining(ResultSet rs) throws SQLException {
        Training training = new Training(
                rs.getString("date"),
                rs.getInt("user_id"),
                rs.getInt("attempt_count"),
                rs.getInt("completed")
        );

        training.setId(rs.getInt("id"));

        return training;
    }
}