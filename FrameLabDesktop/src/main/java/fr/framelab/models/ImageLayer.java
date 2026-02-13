package fr.framelab.models;

import fr.framelab.modules.image.ImageOperation;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ImageLayer {
    protected int zIndex;
    protected boolean isDrawable;
    protected boolean isVisible;
    protected List<ImageOperation> operations;
    protected int operationIndex;
    protected WritableImage baseImage;
    protected WritableImage editedImage;

    private static final int MAX_OPERATIONS = 30;

    public ImageLayer() {
        this.zIndex = 0;
        this.isDrawable = false;
        this.isVisible = true;
        this.operations = new ArrayList<ImageOperation>();
        this.operationIndex = 0;
    }

    public ImageLayer(WritableImage baseImage) {
        this();
        this.baseImage = copyImage(baseImage);
        this.editedImage = copyImage(baseImage);
    }

    public ImageLayer(int width, int height) {
        this();
        this.isDrawable = true;
        this.baseImage = new WritableImage(width, height);

        // On crée une image transparente pour les calques dessinables
        PixelWriter pw = this.baseImage.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pw.setColor(x, y, Color.TRANSPARENT);
            }
        }
        this.editedImage = copyImage(this.baseImage);
    }

    public void addImageOperation(ImageOperation imageOperation) {
        // Si on n'est pas à la fin de la liste, on supprime les opérations suivantes
        if (this.operationIndex < this.operations.size()) {
            this.operations = new ArrayList<>(this.operations.subList(0, this.operationIndex));
        }

        imageOperation.handle(editedImage);
        this.operations.add(imageOperation);
        this.operationIndex += 1;

        // On vérifie si on dépasse la limite de 30 opérations
        if (this.operations.size() > MAX_OPERATIONS) {
            // BaseImage devient editedImage actuelle
            this.baseImage = copyImage(this.editedImage);

            // On réinitialise les opérations
            this.operations = new ArrayList<>();
            this.operationIndex = 0;
        }
    }

    private void resetEditedImage() {
        this.editedImage = copyImage(this.baseImage);
    }

    public void renderImage(int operationIndex) {
        if (operationIndex > this.operations.size()) {
            return;
        }

        this.resetEditedImage();

        // On applique les opérations depuis le début jusqu'à operationIndex
        for (int i = 0; i < operationIndex; i++) {
            ImageOperation imageOperation = this.operations.get(i);
            imageOperation.handle(this.editedImage);
        }
    }

    public void revertImageOperation() {
        if (this.operationIndex == 0) {
            return;
        }

        this.operationIndex -= 1;
        renderImage(this.operationIndex);
    }

    public void restoreImageOperation() {
        if (this.operationIndex >= this.operations.size()) {
            return;
        }

        this.operationIndex += 1;
        renderImage(this.operationIndex);
    }

    public void merge(ImageLayer imageLayer) {
        // Fusionner les deux editedImage
        this.editedImage = mergeImages(this.editedImage, imageLayer.getEditedImage());

        // Mettre à jour baseImage avec le résultat de la fusion
        this.baseImage = copyImage(this.editedImage);

        // Réinitialiser les opérations
        this.operations = new ArrayList<>();
        this.operationIndex = 0;
    }

    private WritableImage copyImage(WritableImage image) {
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

    private WritableImage mergeImages(WritableImage bottom, WritableImage top) {
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
    
    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public boolean isDrawable() {
        return isDrawable;
    }

    public void setDrawable(boolean drawable) {
        isDrawable = drawable;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public WritableImage getBaseImage() {
        return baseImage;
    }

    public WritableImage getEditedImage() {
        return editedImage;
    }

    public List<ImageOperation> getOperations() {
        return operations;
    }

    public int getOperationIndex() {
        return operationIndex;
    }
}