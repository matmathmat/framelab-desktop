package fr.framelab.controller;

import fr.framelab.api.models.Challenge;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    private Image baseImage;

    private void loadActiveChallenge() {
        try {
            activeChallenge = this.mainController.frameLabAPI.getActiveChallenge();

            baseImage = new Image(activeChallenge.getPhotoUrl(), true);
            challengeImage.setImage(baseImage);

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
        this.mainController.showEditor(baseImage);
    }
}
