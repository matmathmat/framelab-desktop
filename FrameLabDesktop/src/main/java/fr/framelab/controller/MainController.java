package fr.framelab.controller;

import fr.framelab.api.FrameLabAPI;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {
    @FXML
    private StackPane contentPane;

    @FXML
    private HBox topBar;

    @FXML
    private VBox leftBar;

    private final BooleanProperty homeVisible = new SimpleBooleanProperty(false);

    public FrameLabAPI frameLabAPI;

    public void initialize() {
        // Rend visible top et left bar uniquement si l'home est visible
        topBar.visibleProperty().bind(homeVisible);
        leftBar.visibleProperty().bind(homeVisible);

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
            homeVisible.set(true);

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
