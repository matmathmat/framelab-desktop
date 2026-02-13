package fr.framelab.utils.image;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageUtil {
    public static WritableImage copyImage(WritableImage image) {
        if (image == null) {
            return null;
        }

        // Obtenir les dimensions de l'image
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage copy = new WritableImage(width, height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = copy.getPixelWriter();

        // On va copier chaque pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelWriter.setArgb(x, y, pixelReader.getArgb(x, y));
            }
        }

        return copy;
    }

    public static WritableImage mergeImages(WritableImage bottom, WritableImage top) {
        if (bottom == null) return copyImage(top);
        if (top == null) return copyImage(bottom);

        // Obtenir les dimensions de l'image
        int width = (int) bottom.getWidth();
        int height = (int) bottom.getHeight();
        WritableImage merged = new WritableImage(width, height);

        PixelReader bottomReader = bottom.getPixelReader();
        PixelReader topReader = top.getPixelReader();
        PixelWriter mergedWriter = merged.getPixelWriter();

        // On va fusionner chaque pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color bottomColor = bottomReader.getColor(x, y);
                Color topColor = topReader.getColor(x, y);

                double alpha = topColor.getOpacity();
                double red = topColor.getRed() * alpha + bottomColor.getRed() * (1 - alpha);
                double green = topColor.getGreen() * alpha + bottomColor.getGreen() * (1 - alpha);
                double blue = topColor.getBlue() * alpha + bottomColor.getBlue() * (1 - alpha);
                double opacity = alpha + bottomColor.getOpacity() * (1 - alpha);

                mergedWriter.setColor(x, y, new Color(red, green, blue, opacity));
            }
        }

        return merged;
    }
}
