package fr.framelab.controller;

import fr.framelab.api.FrameLabAPI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {
    @FXML
    private StackPane contentPane;

    public FrameLabAPI frameLabAPI;

    public void initialize() {
        showLogin();
    }

    public void setServices(FrameLabAPI frameLabAPI) {
        this.frameLabAPI = frameLabAPI;
    }

    public void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/login.fxml"));
            Parent view = loader.load();

            LoginController controller = loader.getController();
            controller.setMainController(this);

            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/home.fxml"));
            Parent view = loader.load();

            HomeController controller = loader.getController();
            controller.setMainController(this);

            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
