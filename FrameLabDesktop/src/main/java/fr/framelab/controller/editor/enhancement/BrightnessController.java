package fr.framelab.controller.editor.enhancement;

import fr.framelab.controller.EditorController;
import fr.framelab.models.ImageLayer;
import fr.framelab.modules.image.BrightnessOperation;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class BrightnessController {

    @FXML
    private Slider slider;

    @FXML
    private Label valueLabel;

    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;

    private Stage stage;
    private EditorController editorController;
    private WritableImage previewImage;

    public void show(EditorController editorController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/editor/enhancement/brightness.fxml"));
            BorderPane root = loader.load();

            // Créer la fenêtre
            this.stage = new Stage();
            this.stage.setTitle("Contraste");
            this.stage.setScene(new Scene(root, 300, 150));

            // lier le stage et le controller
            BrightnessController controller = loader.getController();
            controller.setStage(this.stage);
            controller.setEditorController(editorController);

            // Afficher la fenêtre
            this.stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Lier le label à la valeur du slider
        this.valueLabel.textProperty().bind(
                Bindings.format("%.2f", slider.valueProperty())
        );

        // Ajouter un écouteur sur le slider pour l'aperçu en temps réel
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            updatePreview(newValue.doubleValue());
        });
    }

    public void setEditorController(EditorController editorController) {
        this.editorController = editorController;

        // Créer une copie de l'image actuelle pour l'aperçu
        ImageLayer activeLayer = editorController.getActiveLayer();
        if (activeLayer != null) {
            this.previewImage = copyImage(activeLayer.getEditedImage());
        }
    }

    private void updatePreview(double brightnessValue) {
        if (editorController == null) {
            return;
        }

        ImageLayer activeLayer = editorController.getActiveLayer();
        if (activeLayer == null) {
            return;
        }

        // Réinitialiser l'image d'aperçu avec l'image originale du calque
        this.previewImage = copyImage(activeLayer.getEditedImage());

        // Appliquer temporairement l'opération de luminosité
        BrightnessOperation tempOperation = new BrightnessOperation(brightnessValue);
        tempOperation.handle(this.previewImage);

        // Mettre à jour l'affichage
        editorController.getEditedImageView().setImage(this.previewImage);
    }

    @FXML
    private void handleApply() {
        if (editorController != null) {
            // Créer l'opération finale avec la valeur du slider
            double brightnessValue = slider.getValue();
            BrightnessOperation operation = new BrightnessOperation(brightnessValue);

            // Ajouter l'opération au calque actif
            ImageLayer activeLayer = editorController.getActiveLayer();
            activeLayer.addImageOperation(operation);

            // Mettre à jour l'affichage avec l'image du calque
            editorController.updateEditedImage();
        }

        this.stage.close();
    }

    @FXML
    private void handleCancel() {
        // Restaurer l'image originale sans appliquer les changements
        if (editorController != null) {
            editorController.updateEditedImage();
        }

        this.stage.close();
    }

    private WritableImage copyImage(WritableImage image) {
        if (image == null) {
            return null;
        }

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage copy = new WritableImage(width, height);
        copy.getPixelWriter().setPixels(
                0, 0, width, height,
                image.getPixelReader(),
                0, 0
        );

        return copy;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}