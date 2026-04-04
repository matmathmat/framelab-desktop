package fr.framelab.controller;

import fr.framelab.controller.home.ProjectPanelController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.io.IOException;

public class HomeController {
    @FXML
    private VBox rightBar;

    @FXML
    private Label greetingLabel;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        loadProjectPanel();
    }

    public void setGreeting(String firstName, String lastName) {
        greetingLabel.setText("Bonjour " + firstName + " " + lastName);
    }

    private void loadProjectPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/home/project_panel.fxml"));
            loader.load();

            ProjectPanelController panelController = loader.getController();
            panelController.setMainController(mainController);

            rightBar.getChildren().setAll((javafx.scene.Node) loader.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}