package fr.framelab.dao;

import fr.framelab.models.Layer;
import java.sql.*;
import java.util.ArrayList;

public class LayerDAO {
    private final Connection connection;

    public LayerDAO(Connection connection) {
        this.connection = connection;
        initializeTable();
    }

    private void initializeTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS layers (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                project_id INTEGER NOT NULL,
                layer_index INTEGER NOT NULL,
                layer_type INTEGER NOT NULL DEFAULT 0,
                FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
                UNIQUE(project_id, layer_index)
            )
        """;

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create layers table: " + e.getMessage(), e);
        }
    }

    public void save(Layer layer) {
        String sql = "INSERT INTO layers (project_id, layer_index, layer_type) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, layer.getProjectId());
            pstmt.setInt(2, layer.getIndex());
            pstmt.setInt(3, layer.getLayerType());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    layer.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save layer: " + e.getMessage(), e);
        }
    }

    public void update(Layer layer) {
        String sql = "UPDATE layers SET layer_index = ?, layer_type = ? WHERE id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, layer.getIndex());
            pstmt.setInt(2, layer.getLayerType());
            pstmt.setInt(3, layer.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update layer " + layer.getId() + ": " + e.getMessage(), e);
        }
    }

    public Layer findById(int id) {
        String sql = "SELECT id, project_id, layer_index, layer_type FROM layers WHERE id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRowToLayer(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve layer " + id + ": " + e.getMessage(), e);
        }

        return null;
    }

    public ArrayList<Layer> findByProjectId(int projectId) {
        ArrayList<Layer> layers = new ArrayList<>();

        String sql = "SELECT id, project_id, layer_index, layer_type FROM layers WHERE project_id = ? ORDER BY layer_index ASC";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, projectId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                layers.add(mapRowToLayer(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve layers for project " + projectId + ": " + e.getMessage(), e);
        }

        return layers;
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM layers WHERE id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete layer " + id + ": " + e.getMessage(), e);
        }
    }

    public void deleteByProjectId(int projectId) {
        String sql = "DELETE FROM layers WHERE project_id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, projectId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete layers for project " + projectId + ": " + e.getMessage(), e);
        }
    }

    private Layer mapRowToLayer(ResultSet rs) throws SQLException {
        Layer layer = new Layer(
                rs.getInt("project_id"),
                rs.getInt("layer_index"),
                rs.getInt("layer_type")
        );

        layer.setId(rs.getInt("id"));

        return layer;
    }
}