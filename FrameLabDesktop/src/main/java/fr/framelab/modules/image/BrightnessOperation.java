package fr.framelab.modules.image;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class BrightnessOperation implements ImageOperation {
    protected double brightnessFactor;

    public BrightnessOperation(double brightnessFactor) {
        this.brightnessFactor = brightnessFactor;

        if (this.brightnessFactor < -100) {
            this.brightnessFactor = -100;
        }

        if (this.brightnessFactor > 100) {
            this.brightnessFactor = 100;
        }
    }

    public void handle(WritableImage image) {
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = image.getPixelWriter();

        // On normalise le facteur luminosité de (-100..100) à (-1..1)
        double delta = brightnessFactor / 100.0;

        // Pour chaque pixel sur l'axe X
        for (int x = 0; x < image.getWidth(); x++) {
            // pour chaque pixel sur l'axe Y
            for (int y = 0; y < image.getHeight(); y++) {
                // On obtient le pixel
                Color sourceColor = reader.getColor(x, y);

                // Si on a obtenu un pixel
                if (sourceColor != null) {
                    // On obtient le canal de la couleur et on lui applique le facteur de luminosité
                    double r = Math.clamp(sourceColor.getRed() + delta, 0, 1);
                    double g = Math.clamp(sourceColor.getGreen() + delta, 0, 1);
                    double b = Math.clamp(sourceColor.getBlue() + delta, 0, 1);

                    // On écrit le nouveau pixel avec notre nouvelle luminosité
                    writer.setColor(x, y, new Color(r, g, b, sourceColor.getOpacity()));
                }
            }
        }
    }
}
