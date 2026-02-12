package fr.framelab.controller;

import fr.framelab.modules.EditorModule;
import fr.framelab.modules.EnhancementModules;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    @FXML
    public void initialize() {
        // Charger les modules dans les combo boxes
        enhancementComboBox.setItems(EnhancementModules.getModules());


        // Lier la taille de l'ImageView à celle du ScrollPane
        baseImage.fitWidthProperty().bind(leftScrollPane.widthProperty());
        baseImage.fitHeightProperty().bind(leftScrollPane.heightProperty());

        // Lier la taille de l'ImageView à celle du ScrollPane
        editedImage.fitWidthProperty().bind(rightScrollPane.widthProperty());
        editedImage.fitHeightProperty().bind(rightScrollPane.heightProperty());
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setBaseImage(Image baseImage) {
        this.baseImage.setImage(baseImage);
        this.editedImage.setImage(baseImage);
    }

    @FXML
    private void handleEnhancement() {
        EditorModule module = enhancementComboBox.getValue();

        if (module != null)  {
            module.getOnTrigger().run();
        }
    }
}