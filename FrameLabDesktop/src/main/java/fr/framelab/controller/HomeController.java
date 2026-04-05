package fr.framelab.controller;

import fr.framelab.controller.home.CurrentChallengeController;
import fr.framelab.controller.home.ProjectPanelController;
import fr.framelab.controller.home.TrainingPaneController;
import fr.framelab.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class HomeController {
    @FXML
    private VBox challengeContainer;

    @FXML
    private VBox trainingContainer;

    @FXML
    private VBox rightBar;

    @FXML
    private Label greetingLabel;

    @FXML
    private Label scoreLabel;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        loadUserInfo();
        loadChallengePanel();
        loadTrainingPanel();
        loadProjectPanel();
    }

    private void loadUserInfo() {
        User user = mainController.frameLabService.currentUser;

        if (user == null) {
            greetingLabel.setText("Bienvenue !");
            scoreLabel.setText("Score : 0");
            return;
        }

        if (user.getId() == 0) {
            greetingLabel.setText("Bienvenue, invité !");
            scoreLabel.setText("Score : —");
        } else {
            greetingLabel.setText("Bonjour " + user.getFirstName() + " " + user.getLastName() + " !");
            User dbUser = mainController.databaseManager.userService.getUser(user.getId());
            int score = (dbUser != null) ? dbUser.getScore() : user.getScore();
            scoreLabel.setText("Score : " + score);
        }
    }

    public void setGreeting(String firstName, String lastName) {
        greetingLabel.setText("Bonjour " + firstName + " " + lastName + " !");
    }

    private void loadChallengePanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/home/current_challenge.fxml"));
            Node node = loader.load();
            CurrentChallengeController ctrl = loader.getController();
            ctrl.setMainController(mainController);
            challengeContainer.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTrainingPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/home/training_pane.fxml"));
            Node node = loader.load();
            TrainingPaneController ctrl = loader.getController();
            ctrl.setMainController(mainController);
            trainingContainer.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProjectPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/home/project_panel.fxml"));
            Node node = loader.load();
            ProjectPanelController ctrl = loader.getController();
            ctrl.setMainController(mainController);
            rightBar.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}