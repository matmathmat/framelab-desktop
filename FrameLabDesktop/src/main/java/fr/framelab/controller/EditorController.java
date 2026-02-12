package fr.framelab.controller;

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
    private ComboBox<String> enhancementComboBox;

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private ComboBox<String> transformComboBox;

    private MainController mainController;

    @FXML
    public void initialize() {
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
}