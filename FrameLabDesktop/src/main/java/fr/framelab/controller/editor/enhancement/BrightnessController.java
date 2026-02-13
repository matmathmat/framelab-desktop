package fr.framelab.controller.editor.enhancement;

import fr.framelab.controller.EditorController;
import fr.framelab.models.ImageLayer;
import fr.framelab.modules.image.BrightnessOperation;
import fr.framelab.utils.image.ImageUtil;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
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

    private boolean applyChange;

    public void show(EditorController editorController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/editor/enhancement/brightness.fxml"));
            BorderPane root = loader.load();

            // lier le stage et le controller
            BrightnessController controller = loader.getController();

            // Créer la fenêtre
            Stage stage = new Stage();
            stage.setTitle("Contraste");
            stage.setScene(new Scene(root, 300, 150));

            // Paramétrer le mode modal pour la fenêtre
            stage.initModality(Modality.APPLICATION_MODAL);

            // Rend la modification de la fenêtre impossible
            stage.resizableProperty().setValue(Boolean.FALSE);

            // On passe le stage après l'avoir créé
            controller.setStage(stage);
            controller.setEditorController(editorController);

            // Afficher la fenêtre
            stage.showAndWait();

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
            this.previewImage = ImageUtil.copyImage(activeLayer.getEditedImage());
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
        this.previewImage = ImageUtil.copyImage(activeLayer.getEditedImage());

        // Appliquer temporairement l'opération de luminosité
        BrightnessOperation tempOperation = new BrightnessOperation(brightnessValue);
        tempOperation.handle(this.previewImage);

        // Mettre à jour l'affichage
        editorController.getEditedImageView().setImage(this.previewImage);
    }

    private void resetImage() {
        // Restaurer l'image originale sans appliquer les changements
        if (editorController != null) {
            editorController.updateEditedImage();
        }
    }

    @FXML
    private void handleApply() {
        if (editorController != null) {
            // On confirme qu'on veut appliquer le changemet
            this.applyChange = true;

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
        this.resetImage();
        this.stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        // Créer un évenèment onclose pour le stage
        this.stage.setOnCloseRequest(event -> {
            if (!this.applyChange) {
                resetImage();
            }
        });
    }
}