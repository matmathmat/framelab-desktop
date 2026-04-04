package fr.framelab.controller;

import fr.framelab.DatabaseManager;
import fr.framelab.models.Project;
import fr.framelab.models.User;
import fr.framelab.services.FrameLabService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

public class MainController {
    @FXML
    private StackPane contentPane;

    @FXML
    private HBox topBar;

    @FXML
    private VBox leftBar;

    private final BooleanProperty homeVisible = new SimpleBooleanProperty(false);

    public FrameLabService frameLabService;
    public DatabaseManager databaseManager;

    public void initialize() {
        // Rend visible uniquement si l'home est visible
        this.topBar.visibleProperty().bind(homeVisible);
        this.leftBar.visibleProperty().bind(homeVisible);
    }

    public void setServices(FrameLabService frameLabService, DatabaseManager databaseManager) {
        this.frameLabService = frameLabService;
        this.databaseManager = databaseManager;

        User loggedInUser = this.databaseManager.userService.getLoggedInUser();

        if (loggedInUser != null) {
            try {
                this.frameLabService.setToken(loggedInUser.getToken());
                User me = this.frameLabService.getMe();
                this.frameLabService.currentUser = me;
                showHome();
                return;
            } catch (Exception e) {
                this.databaseManager.userService.deleteUser(loggedInUser.getId());
            }
        }

        showLogin();
    }

    public void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/login.fxml"));
            Parent view = loader.load();

            LoginController controller = loader.getController();
            controller.setMainController(this);

            this.contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showHome() {
        try {
            this.homeVisible.set(true);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/home.fxml"));
            Parent view = loader.load();

            HomeController controller = loader.getController();
            controller.setMainController(this);

            if (this.frameLabService.currentUser != null) {
                controller.setGreeting(
                        this.frameLabService.currentUser.getFirstName(),
                        this.frameLabService.currentUser.getLastName()
                );
            }

            this.contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void challengeHandle() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/challenge.fxml"));
            Parent view = loader.load();

            ChallengeController controller = loader.getController();
            controller.setMainController(this);

            this.contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showEditor(Project project, Image challengeImage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/editor.fxml"));
            Parent view = loader.load();

            EditorController controller = loader.getController();
            controller.setMainController(this);
            controller.setNewProject(project, challengeImage);

            this.contentPane.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openExistingProject(Project project) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/editor.fxml"));
            Parent view = loader.load();

            EditorController controller = loader.getController();
            controller.setMainController(this);
            controller.setExistingProject(project);

            this.contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
