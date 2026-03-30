package fr.framelab.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class LoginController {
    private MainController mainController;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button loginButton;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleLogin() {
        try {
            boolean success = this.mainController.frameLabAPI.login(this.emailTextField.getText(), this.passwordTextField.getText());

            if (success) {
                this.mainController.showHome();
            }
        }
        catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(e.getClass().getSimpleName());
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
    }
}
