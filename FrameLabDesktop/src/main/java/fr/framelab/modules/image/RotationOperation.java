package fr.framelab.modules.image;

import javafx.scene.image.WritableImage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class RotationOperation implements ImageOperation {
    protected double rotationAngle;

    public RotationOperation(double rotationAngle) {
        this.rotationAngle = rotationAngle;

        while (this.rotationAngle > 180) {
            this.rotationAngle = 180;
        }
        while (this.rotationAngle < -180) {
            this.rotationAngle = -180;
        }
    }

    @Override
    public void handle(WritableImage image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        // Créer un canvas temporaire
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Sauvegarder l'état actuel
        gc.save();

        // Rotation autour du centre
        gc.translate(width / 2.0, height / 2.0);
        gc.rotate(this.rotationAngle);
        gc.translate(-width / 2.0, -height / 2.0);

        // Dessiner l'image
        gc.drawImage(image, 0, 0);

        // Restaurer l'état
        gc.restore();

        // Copier le résultat dans l'image
        canvas.snapshot(null, image);
    }

    @Override
    public String getName() {
        return "Rotation (" + (int) this.rotationAngle + ")";
    }
}