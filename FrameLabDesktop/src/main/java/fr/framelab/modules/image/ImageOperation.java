package fr.framelab.modules.image;

import javafx.scene.image.WritableImage;

public interface ImageOperation {

    void handle(WritableImage image);

    default String getName() {
        return this.getClass().getSimpleName();
    }

    default Integer getParameterValue() {
        return null;
    }
}