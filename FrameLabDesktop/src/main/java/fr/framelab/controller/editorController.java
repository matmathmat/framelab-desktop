package fr.framelab.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class editorController {
    @FXML
    private VBox rightBar;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
