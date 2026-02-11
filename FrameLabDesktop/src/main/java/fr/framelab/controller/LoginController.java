package fr.framelab.controller;

import fr.framelab.api.FrameLabAPI;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class LoginController {
    private FrameLabAPI frameLabAPI;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleLogin() {
        System.out.println(this.emailTextField);

        try {
            frameLabAPI.login(this.emailTextField.getText(), this.passwordTextField.getText());
        }
        catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(e.getClass().getSimpleName());
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }

        System.out.println("Button cliqué");
    }

    public void setService(FrameLabAPI frameLabAPI) {
        this.frameLabAPI = frameLabAPI;
    }
}
