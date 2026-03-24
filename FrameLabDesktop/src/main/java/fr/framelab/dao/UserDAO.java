package fr.framelab.dao;

import fr.framelab.api.models.User;
import java.sql.*;
import java.util.ArrayList;

public class UserDAO {
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
        initializeTable();
    }

    private void initializeTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                is_admin INTEGER NOT NULL DEFAULT 0,
                email TEXT UNIQUE,
                token TEXT
            )
        """;

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create users table: " + e.getMessage(), e);
        }
    }

    public void save(User user) {
        String sql = "INSERT INTO users (first_name, last_name, is_admin, email, token) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setInt(3, user.getIsAdmin() ? 1 : 0);
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getToken());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
    }

    public void update(User user) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ? WHERE id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getEmail());
            pstmt.setInt(4, user.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user " + user.getId() + ": " + e.getMessage(), e);
        }
    }

    public User findById(int id) {
        String sql = "SELECT id, first_name, last_name, is_admin, email, token FROM users WHERE id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRowToUser(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve user " + id + ": " + e.getMessage(), e);
        }

        return null;
    }

    public User findByEmail(String email) {
        String sql = "SELECT id, first_name, last_name, is_admin, email, token FROM users WHERE email = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRowToUser(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve user with email " + email + ": " + e.getMessage(), e);
        }

        return null;
    }

    public ArrayList<User> findAll() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT id, first_name, last_name, is_admin, email, token FROM users";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve all users: " + e.getMessage(), e);
        }

        return users;
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user " + id + ": " + e.getMessage(), e);
        }
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getInt("is_admin"),
                rs.getString("email"),
                rs.getString("token")
        );
    }
}