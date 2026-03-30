package fr.framelab.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HomeController {
    @FXML
    private VBox rightBar;

    @FXML
    private Label greetingLabel;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setGreeting(String firstName, String lastName) {
        this.greetingLabel.setText("Bonjour " + firstName + " " + lastName);
    }
}
