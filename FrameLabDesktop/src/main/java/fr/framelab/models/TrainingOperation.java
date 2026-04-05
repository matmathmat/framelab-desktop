package fr.framelab.models;

import fr.framelab.modules.image.ImageOperationFactory;

public class TrainingOperation {

    protected String type;
    protected Integer param;

    // Constructors

    public TrainingOperation(String type) {

        if (type == null) {
            throw new IllegalArgumentException("Null type is not allowed");
        }

        if (type.isBlank()) {
            throw new IllegalArgumentException("Empty type is not allowed");
        }

        this.type = type;
        this.param = null;
    }

    public TrainingOperation(String type, Integer param) {

        if (type == null) {
            throw new IllegalArgumentException("Null type is not allowed");
        }

        if (type.isBlank()) {
            throw new IllegalArgumentException("Empty type is not allowed");
        }

        this.type = type;
        this.param = param;
    }

    // Getters

    public String getType() {
        return this.type;
    }

    public Integer getParam() {
        return this.param;
    }

    public String getDisplayName() {
        return ImageOperationFactory.create(this.type, this.param).getName();
    }

    // Setters

    public void setType(String type) {

        if (type == null) {
            throw new IllegalArgumentException("Null type is not allowed");
        }

        if (type.isBlank()) {
            throw new IllegalArgumentException("Empty type is not allowed");
        }

        this.type = type;
    }

    public void setParam(Integer param) {
        this.param = param;
    }
}