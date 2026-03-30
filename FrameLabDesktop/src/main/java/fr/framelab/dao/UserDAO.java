package fr.framelab.dao;

import fr.framelab.models.User;
import java.sql.*;
import java.util.ArrayList;

public class UserDAO {
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
        initializeTable();
    }

    private void initializeTable() {
        String createSql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                is_admin INTEGER NOT NULL DEFAULT 0,
                email TEXT UNIQUE NOT NULL,
                token TEXT NOT NULL
            )
        """;

        String guestSql = """
            INSERT OR IGNORE INTO users (id, first_name, last_name, is_admin, email, token)
            VALUES (0, 'Guest', 'Guest', 0, 'guest@guest.com', 'guestToken')
        """;

        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute(createSql);
            stmt.execute(guestSql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create users table: " + e.getMessage(), e);
        }
    }

    public void save(User user) {
        String sql = "INSERT INTO users (id, first_name, last_name, is_admin, email, token) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, user.getId());
            pstmt.setString(2, user.getFirstName());
            pstmt.setString(3, user.getLastName());
            pstmt.setInt(4, user.getIsAdmin() ? 1 : 0);
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getToken());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
    }

    public void update(User user) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, token = ? WHERE id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getToken());
            pstmt.setInt(5, user.getId());
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