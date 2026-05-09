package fr.framelab.modules.image;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class EmojiOperation implements ImageOperation {
    private final String emoji;
    private final int x;
    private final int y;
    private final double size;
    private final javafx.scene.paint.Color color;

    public EmojiOperation(String emoji, int x, int y, double size, javafx.scene.paint.Color color) {
        this.emoji = emoji;
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    @Override
    public void handle(WritableImage image) {
        Canvas canvas = new Canvas(image.getWidth(), image.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // On redessine l'image de base
        gc.drawImage(image, 0, 0);

        // On configure et on pose l'emoji
        gc.setFont(Font.font("Arial", size));

        // On configure la couleur de l'emoji
        gc.setFill(color);

        // On centre le texte sur les coordonnées cliquées
        gc.fillText(emoji, x - (size / 3.0), y + (size / 3.0));

        // On fusionne le résultat dans l'image
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        canvas.snapshot(params, image);
    }

    @Override
    public String getName() {
        return "Ajout Emoji : " + emoji;
    }
}