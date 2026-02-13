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
                    // On obtient le canal de la couleur et on lui applique une formule pondérée (l'oeil perçoit le vert plus intensément)
                    double r = Math.clamp(sourceColor.getRed() * 0.299, 0, 1);
                    double g = Math.clamp(sourceColor.getGreen() * 0.587, 0, 1);
                    double b = Math.clamp(sourceColor.getBlue() * 0.114, 0, 1);

                    // On écrit le nouveau pixel avec notre niveau de gris
                    writer.setColor(x, y, new Color(r, g, b, sourceColor.getOpacity()));
                }
            }
        }
    }
}
