package fr.framelab.modules.image;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ContrastOperation implements ImageOperation {
    protected double contrastFactor;

    public ContrastOperation(double contrastFactor) {
        this.contrastFactor = contrastFactor;

        if (this.contrastFactor < -100) {
            this.contrastFactor = -100;
        }

        if (this.contrastFactor > 100) {
            this.contrastFactor = 100;
        }
    }

    public void handle(WritableImage image) {
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = image.getPixelWriter();

        // On normalise le facteur contraste de (-100..100) à (0..4)
        double factor = (this.contrastFactor + 100.0) / 100.0;

        // Pour chaque pixel sur l'axe X
        for (int x = 0; x < image.getWidth(); x++) {
            // Pour chaque pixel sur l'axe Y
            for (int y = 0; y < image.getHeight(); y++) {
                // On obtient le pixel
                Color sourceColor = reader.getColor(x, y);

                // Si on a obtenu un pixel
                if (sourceColor != null) {
                    // On centre chaque canal autour de 0.5, on applique le facteur, puis on recentre
                    double r = Math.clamp((sourceColor.getRed()   - 0.5) * factor + 0.5, 0, 1);
                    double g = Math.clamp((sourceColor.getGreen() - 0.5) * factor + 0.5, 0, 1);
                    double b = Math.clamp((sourceColor.getBlue()  - 0.5) * factor + 0.5, 0, 1);

                    // On écrit le nouveau pixel avec notre nouveau contraste
                    writer.setColor(x, y, new Color(r, g, b, sourceColor.getOpacity()));
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Contraste (" + (int) this.contrastFactor + ")";
    }

    @Override
    public Integer getParameterValue() {
        return (int) this.contrastFactor;
    }
}