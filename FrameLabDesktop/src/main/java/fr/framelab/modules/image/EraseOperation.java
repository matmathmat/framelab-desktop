package fr.framelab.modules.image;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class EraseOperation implements ImageOperation {

    private final List<int[]> points;
    private final int brushSize;

    public EraseOperation(List<int[]> points, int brushSize) {
        this.points = new ArrayList<>(points);

        // On s'assure que la taille de la gomme est au moins de 1
        this.brushSize = Math.max(1, brushSize);
    }

    @Override
    public void handle(WritableImage image) {
        // Si on n'a pas de points, on ne fait rien
        if (points.isEmpty()) return;

        PixelWriter pw = image.getPixelWriter();
        int w = (int) image.getWidth();
        int h = (int) image.getHeight();

        // On efface le premier point
        eraseCircle(pw, points.get(0)[0], points.get(0)[1], w, h);

        // On efface entre chaque point consécutif pour un rendu fluide
        for (int i = 0; i < points.size() - 1; i++) {
            eraseLine(pw, points.get(i), points.get(i + 1), w, h);
        }
    }

    private void eraseLine(PixelWriter pw, int[] p1, int[] p2, int w, int h) {
        int x0 = p1[0], y0 = p1[1];
        int x1 = p2[0], y1 = p2[1];
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        // Algorithme de Bresenham pour effacer de façon continue
        while (true) {
            eraseCircle(pw, x0, y0, w, h);

            if (x0 == x1 && y0 == y1) break;

            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x0 += sx; }
            if (e2 < dx)  { err += dx; y0 += sy; }
        }
    }

    private void eraseCircle(PixelWriter pw, int cx, int cy, int w, int h) {
        // On définit le rayon à partir de la taille de la gomme
        int r = brushSize / 2;

        // Pour chaque pixel sur l'axe X dans la zone de la gomme
        for (int x = cx - r; x <= cx + r; x++) {
            // pour chaque pixel sur l'axe Y dans la zone de la gomme
            for (int y = cy - r; y <= cy + r; y++) {
                // On vérifie si on est bien dans les limites de l'image
                if (x >= 0 && y >= 0 && x < w && y < h) {
                    // On vérifie si le pixel est à l'intérieur du cercle
                    if ((x - cx) * (x - cx) + (y - cy) * (y - cy) <= r * r) {
                        // On écrit de la transparence pour simuler la gomme
                        pw.setColor(x, y, Color.TRANSPARENT);
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Gomme";
    }
}