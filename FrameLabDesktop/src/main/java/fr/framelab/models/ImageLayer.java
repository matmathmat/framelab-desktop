package fr.framelab.models;

import fr.framelab.modules.image.ImageOperation;
import fr.framelab.utils.image.ImageUtil;
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
        this.baseImage = ImageUtil.copyImage(baseImage);
        this.editedImage = ImageUtil.copyImage(baseImage);
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

        this.editedImage = ImageUtil.copyImage(this.baseImage);
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
            this.baseImage = ImageUtil.copyImage(this.editedImage);

            // On réinitialise les opérations
            this.operations = new ArrayList<>();
            this.operationIndex = 0;
        }
    }

    private void resetEditedImage() {
        this.editedImage = ImageUtil.copyImage(this.baseImage);
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
        this.editedImage = ImageUtil.mergeImages(this.editedImage, imageLayer.getEditedImage());

        // Mettre à jour baseImage avec le résultat de la fusion
        this.baseImage = ImageUtil.copyImage(this.editedImage);

        // Réinitialiser les opérations
        this.operations = new ArrayList<>();
        this.operationIndex = 0;
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