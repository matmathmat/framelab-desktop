package fr.framelab.controller;

import fr.framelab.models.Challenge;
import fr.framelab.services.ChallengeService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
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

    private Image baseImage;

    private void loadActiveChallenge() {
        // Si getActiveChallenge() lève une IOException, on basculera en mode hors ligne
        try {
            // On récupère le challenge actif via l'api FrameLab et on le sauvegarde
            Challenge apiChallenge = this.mainController.frameLabService.getActiveChallenge();
            activeChallenge = this.mainController.databaseManager.challengeService.syncAndGetActiveChallenge(apiChallenge);
        } catch (IOException e) {
            // On récupère le challenge actif via notre db local
            activeChallenge = this.mainController.databaseManager.challengeService.getActiveChallenge();

            if (activeChallenge == null) {
                return;
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(e.getClass().getSimpleName());
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
            return;
        }

        // Charger l'image locale si elle existe, sinon utiliser l'URL distante
        String localPath = ChallengeService.getLocalImagePath(activeChallenge.getId());
        File localImageFile = new File(localPath);

        if (localImageFile.exists()) {
            baseImage = new Image(localImageFile.toURI().toString());
        } else {
            baseImage = new Image(activeChallenge.getPhotoUrl(), true);
        }

        challengeImage.setImage(baseImage);
        titleThemeLabel.setText(activeChallenge.getTitleTheme());
        descriptionThemeLabel.setText(activeChallenge.getDescriptionTheme());
        startDateLabel.setText("Start date: " + activeChallenge.getStartDate());
        endDateLabel.setText("End date: " + activeChallenge.getEndDate());
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
