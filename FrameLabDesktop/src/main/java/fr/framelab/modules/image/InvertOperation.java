package fr.framelab.modules.image;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class InvertOperation implements ImageOperation {
    public InvertOperation() {

    }

    public void handle(WritableImage image) {
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = image.getPixelWriter();

        // Pour chaque pixel sur l'axe X
        for (int x = 0; x < image.getWidth(); x++) {
            // pour chaque pixel sur l'axe Y
            for (int y = 0; y < image.getHeight(); y++) {
                // On obtient le pixel
                Color sourceColor = reader.getColor(x, y);

                // Si on a obtenu un pixel
                if (sourceColor != null) {
                    // Inversion de couleur = x - composante couleur où x est 1 ici car on a un maping [0:1]
                    double r = Math.clamp(1 - sourceColor.getRed() , 0, 1);
                    double g = Math.clamp(1 - sourceColor.getGreen(), 0, 1);
                    double b = Math.clamp(1 - sourceColor.getBlue(), 0, 1);

                    // On écrit le nouveau pixel avec notre inversion
                    writer.setColor(x, y, new Color(r, g, b, sourceColor.getOpacity()));
                }
            }
        }
    }
}