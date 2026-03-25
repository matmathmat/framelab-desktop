package fr.framelab.models;

public class Layer {
    protected int id;
    protected int projectId;
    protected int index;

    // Constructors

    public Layer(int projectId, int index) {
        // On vérifie que l'id du projet est supérieur à -1
        if (projectId < 0) {
            throw new IllegalArgumentException("The project ID cannot be less than 0");
        }

        // On vérifie que l'index est supérieur à -1
        if (index < 0) {
            throw new IllegalArgumentException("The index cannot be less than 0");
        }

        this.projectId = projectId;
        this.index = index;
        this.id = -1;
    }

    // Getters

    public int getId() {
        return this.id;
    }

    public int getProjectId() {
        return this.projectId;
    }

    public int getIndex() {
        return this.index;
    }

    // Setters

    public void setId(int id) {
        if (this.id > 0) {
            throw new UnsupportedOperationException("ID cannot be modified once set");
        }

        this.id = id;
    }

    public void setIndex(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("The index cannot be less than 0");
        }

        this.index = index;
    }
}