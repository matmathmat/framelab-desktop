package fr.framelab.controller;

import fr.framelab.api.FrameLabAPI;
import javafx.fxml.FXML;

public class MainController {
    @FXML
    private LoginController loginController;

    public void setServices(FrameLabAPI frameLabAPI) {
        this.loginController.setService(frameLabAPI);
    }
}
