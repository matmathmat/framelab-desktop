package fr.framelab.controller;

import fr.framelab.models.ImageLayer;
import fr.framelab.modules.EditorModule;
import fr.framelab.modules.EnhancementModules;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;
import java.util.List;

public class EditorController {
    @FXML
    private ImageView baseImage;

    @FXML
    private ImageView editedImage;

    @FXML
    private ScrollPane leftScrollPane;

    @FXML
    private ScrollPane rightScrollPane;

    @FXML
    private ComboBox<EditorModule> enhancementComboBox;

    @FXML
    private ComboBox<EditorModule> filterComboBox;

    @FXML
    private ComboBox<EditorModule> transformComboBox;

    private MainController mainController;

    // Gestion des calques
    private List<ImageLayer> layers;
    private ImageLayer activeLayer;

    @FXML
    public void initialize() {
        // Charger les modules dans les combo boxes
        enhancementComboBox.setItems(EnhancementModules.getModules(this));

        // Lier la taille de l'ImageView à celle du ScrollPane
        baseImage.fitWidthProperty().bind(leftScrollPane.widthProperty());
        baseImage.fitHeightProperty().bind(leftScrollPane.heightProperty());

        editedImage.fitWidthProperty().bind(rightScrollPane.widthProperty());
        editedImage.fitHeightProperty().bind(rightScrollPane.heightProperty());

        // Initialiser la liste de calques
        this.layers = new ArrayList<>();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setBaseImage(Image baseImage) {
        this.baseImage.setImage(baseImage);

        // Créer une WritableImage à partir de l'Image
        WritableImage writableImage = convertToWritableImage(baseImage);

        // Créer le calque par défaut avec l'image de référence
        this.activeLayer = new ImageLayer(writableImage);
        this.layers.add(activeLayer);

        // Afficher l'image éditée
        this.editedImage.setImage(activeLayer.getEditedImage());
    }

    private WritableImage convertToWritableImage(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        writableImage.getPixelWriter().setPixels(
                0, 0, width, height,
                image.getPixelReader(),
                0, 0
        );

        return writableImage;
    }


    public void updateEditedImage() {
        // Mettre à jour l'affichage de l'image éditée
        if (activeLayer != null) {
            this.editedImage.setImage(activeLayer.getEditedImage());
        }
    }

    public ImageLayer getActiveLayer() {
        return activeLayer;
    }

    public ImageView getEditedImageView() {
        return editedImage;
    }

    @FXML
    private void handleEnhancement() {
        EditorModule module = enhancementComboBox.getValue();

        if (module != null)  {
            module.getOnTrigger().run();
        }

        Platform.runLater(() -> {
            enhancementComboBox.getSelectionModel().clearSelection();
            enhancementComboBox.setSkin(new ComboBoxListViewSkin<>(enhancementComboBox));
        });
    }
}