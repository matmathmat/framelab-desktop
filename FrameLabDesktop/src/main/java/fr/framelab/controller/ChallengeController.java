package fr.framelab.controller;

import fr.framelab.api.model.Challenge;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ChallengeController {
    @FXML
    private ImageView challengeImage;

    @FXML
    private Label titleThemeLabel;

    @FXML
    private Label descriptionThemeLabel;

    @FXML
    private Label startDateLabel;

    @FXML
    private Label endDateLabel;

    private MainController mainController;

    private Challenge activeChallenge;

    private void loadActiveChallenge() {
        try {
            activeChallenge = this.mainController.frameLabAPI.getActiveChallenge();

            Image image = new Image(activeChallenge.getPhotoUrl(), true);
            challengeImage.setImage(image);

            titleThemeLabel.setText(activeChallenge.getTitleTheme());
            descriptionThemeLabel.setText(activeChallenge.getDescriptionTheme());
            startDateLabel.setText("Start date: " + activeChallenge.getStartDate());
            endDateLabel.setText("End date: " + activeChallenge.getEndDate());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        loadActiveChallenge();
    }

    @FXML
    private void handleParticipate() {

    }
}
