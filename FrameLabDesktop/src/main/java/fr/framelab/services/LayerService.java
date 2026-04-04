package fr.framelab.services;

import fr.framelab.dao.LayerDAO;
import fr.framelab.models.Layer;

import java.util.List;

public class LayerService {
    private final LayerDAO layerDAO;

    public LayerService(LayerDAO layerDAO) {
        this.layerDAO = layerDAO;
    }

    public Layer getLayer(int layerId) {
        return this.layerDAO.findById(layerId);
    }

    public List<Layer> getProjectLayers(int projectId) {
        return this.layerDAO.findByProjectId(projectId);
    }

    public void saveLayer(Layer layer) {
        if (layer == null) {
            throw new IllegalArgumentException("Layer cannot be null");
        }

        if (layer.getId() == -1) {
            this.layerDAO.save(layer);
        } else {
            this.layerDAO.update(layer);
        }
    }

    public void deleteLayer(int id) {
        this.layerDAO.deleteById(id);
    }

    public void deleteProjectLayers(int projectId) {
        this.layerDAO.deleteByProjectId(projectId);
    }
}