package fr.framelab.utils.image;

import fr.framelab.models.ImageLayer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

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

    public static WritableImage mergeImages(WritableImage bottom, WritableImage top, double topLayerOpacity) {
        if (bottom == null) return copyImage(top);
        if (top    == null) return copyImage(bottom);

        // Obtenir les dimensions de l'image
        int width  = (int) bottom.getWidth();
        int height = (int) bottom.getHeight();

        WritableImage merged = new WritableImage(width, height);

        PixelReader bottomReader = bottom.getPixelReader();
        PixelReader topReader    = top.getPixelReader();
        PixelWriter mergedWriter = merged.getPixelWriter();

        // On va fusionner chaque pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color bottomColor = bottomReader.getColor(x, y);
                Color topColor    = topReader.getColor(x, y);

                // L'opacité effective du pixel = opacité pixel × opacité calque
                double alpha = topColor.getOpacity() * topLayerOpacity;

                double red   = topColor.getRed()   * alpha + bottomColor.getRed()   * (1 - alpha);
                double green = topColor.getGreen() * alpha + bottomColor.getGreen() * (1 - alpha);
                double blue  = topColor.getBlue()  * alpha + bottomColor.getBlue()  * (1 - alpha);
                double opacity = alpha + bottomColor.getOpacity() * (1 - alpha);

                mergedWriter.setColor(x, y, new Color(
                        Math.min(1, red),
                        Math.min(1, green),
                        Math.min(1, blue),
                        Math.min(1, opacity)
                ));
            }
        }
        return merged;
    }

    public static WritableImage toWritable(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        writableImage.getPixelWriter().setPixels(
                0,
                0,
                width,
                height,
                image.getPixelReader(),
                0,
                0
        );

        return writableImage;
    }

    public static void saveToDisk(Image image, String filePath) {
        try {
            File file = new File(filePath);
            BufferedImage buf = SwingFXUtils.fromFXImage(image, null);
            ImageIO.write(buf, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Image render(List<ImageLayer> layers) {
        if (layers.isEmpty()) return null;

        int w = (int) layers.get(0).getBaseImage().getWidth();
        int h = (int) layers.get(0).getBaseImage().getHeight();

        Canvas canvas = new Canvas(w, h);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (ImageLayer layer : layers) {
            if (layer.isVisible()) {
                gc.setGlobalAlpha(layer.getOpacity());
                gc.drawImage(layer.getEditedImage(), 0, 0);
            }
        }

        gc.setGlobalAlpha(1.0);

        WritableImage result = new WritableImage(w, h);
        canvas.snapshot(null, result);
        return result;
    }
}
