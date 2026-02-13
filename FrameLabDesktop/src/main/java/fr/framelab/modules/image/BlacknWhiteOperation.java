package fr.framelab.modules.image;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class BlacknWhiteOperation implements ImageOperation {
    public BlacknWhiteOperation() {

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
                    // Formule pondérée (l'oeil perçoit le vert plus intensément)
                    double lum = Math.clamp(0.299 * sourceColor.getRed() + 0.587 * sourceColor.getGreen() + 0.114 * sourceColor.getBlue(), 0, 1);

                    // On écrit le nouveau pixel avec notre niveau de gris
                    writer.setColor(x, y, new Color(lum, lum, lum, sourceColor.getOpacity()));
                }
            }
        }
    }
}
