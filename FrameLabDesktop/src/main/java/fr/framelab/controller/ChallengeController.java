package fr.framelab.controller;

import fr.framelab.models.Challenge;
import fr.framelab.models.Project;
import fr.framelab.services.ChallengeService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

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
        // On vérifie qu'un challenge est actif et qu'une image de base est chargée
        if (activeChallenge == null || baseImage == null) return;

        // Création de la boîte de dialogue pour saisir le titre du projet
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nouveau projet");
        dialog.setHeaderText("Donner un titre à votre projet");
        dialog.setContentText("Titre :");

        // Pour attraper la réponse de l'utilisateur
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(title -> {
            // On affiche une erreur si la longeur du titre du projet est inférieur à 3
            if (title.trim().length() < 3) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Le titre doit faire au moins 3 caractères.");
                alert.showAndWait();
                return;
            }

            // Récupère l'ID de l'utilisateur courant (0 si non connecté)
            int userId = this.mainController.frameLabService.currentUser != null
                    ? this.mainController.frameLabService.currentUser.getId()
                    : 0;

            // Création du projet et sauvegarde en local
            Project project = new Project(title.trim(), userId, activeChallenge.getId());
            this.mainController.databaseManager.projectService.saveProject(project);


            // Ouvre l'éditeur avec le projet et l'image de base
            this.mainController.showEditor(project, baseImage);
        });
    }
}
