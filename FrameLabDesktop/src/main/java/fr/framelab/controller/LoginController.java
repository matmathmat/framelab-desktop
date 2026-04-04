package fr.framelab.controller;

import fr.framelab.models.User;
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

    @FXML
    public Button guestButton;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleLogin() {
        try {
            boolean success = this.mainController.frameLabService.login(
                    this.emailTextField.getText(),
                    this.passwordTextField.getText()
            );

            if (success) {
                User me = this.mainController.frameLabService.getMe();
                this.mainController.databaseManager.userService.saveUser(me);
                this.mainController.showHome();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(e.getClass().getSimpleName());
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleGuest() {
        User guest = this.mainController.databaseManager.userService.getUser(0);
        this.mainController.frameLabService.currentUser = guest;
        this.mainController.showHome();
    }
}
