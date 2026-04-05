package fr.framelab.controller.home;

import fr.framelab.controller.MainController;
import fr.framelab.models.Challenge;
import fr.framelab.models.Project;
import fr.framelab.models.User;
import fr.framelab.services.ChallengeService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class CurrentChallengeController {
    @FXML
    private ImageView challengeImage;

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label datesLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Button participateButton;

    private MainController mainController;
    private Challenge activeChallenge;
    private Image baseImage;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        loadChallenge();
    }

    private void loadChallenge() {
        // Si getActiveChallenge() lève une IOException, on basculera en mode hors ligne
        try {
            // On récupère le challenge actif via l'api FrameLab et on le sauvegarde
            Challenge apiChallenge = mainController.frameLabService.getActiveChallenge();

            if (apiChallenge != null) {
                activeChallenge = mainController.databaseManager.challengeService.syncAndGetActiveChallenge(apiChallenge);
            } else {
                // On récupère le challenge actif via notre db local
                activeChallenge = mainController.databaseManager.challengeService.getActiveChallenge();
            }
        } catch (Exception e) {
            // Mode hors ligne
            activeChallenge = mainController.databaseManager.challengeService.getActiveChallenge();
        }

        // Si on n'est pas arrivé à afficher le challenge, on montre un message d'erreur et on quitte
        if (activeChallenge == null) {
            showError();
            return;
        }

        // Charger l'image locale si elle existe, sinon utiliser l'URL distante
        String localPath = ChallengeService.getLocalImagePath(activeChallenge.getId());
        File localFile = new File(localPath);

        if (localFile.exists()) {
            baseImage = new Image(localFile.toURI().toString());
        } else {
            baseImage = new Image(activeChallenge.getPhotoUrl(), true);
        }

        // Afficher les informations du challenge
        challengeImage.setImage(baseImage);
        titleLabel.setText(activeChallenge.getTitleTheme());
        descriptionLabel.setText(activeChallenge.getDescriptionTheme());
        datesLabel.setText(
                "Du " + activeChallenge.getStartDate()
                        + " au " + activeChallenge.getEndDate()
        );

        // rendre le bouton participer cliquable
        participateButton.setDisable(false);
    }

    private void showError() {
        challengeImage.setVisible(false);
        challengeImage.setManaged(false);
        titleLabel.setVisible(false);
        titleLabel.setManaged(false);
        descriptionLabel.setVisible(false);
        descriptionLabel.setManaged(false);
        datesLabel.setVisible(false);
        datesLabel.setManaged(false);
        participateButton.setDisable(true);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    @FXML
    private void handleParticipate() {
        // On vérifie qu'un challenge est actif et qu'une image de base est chargée
        if (activeChallenge == null || baseImage == null) return;

        // Création de la boîte de dialogue pour saisir le titre du projet
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nouveau projet");
        dialog.setHeaderText("Donnez un titre à votre projet");
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
            int userId = mainController.frameLabService.currentUser != null
                    ? mainController.frameLabService.currentUser.getId() : 0;

            // Création du projet et sauvegarde en local
            Project project = new Project(title.trim(), userId, activeChallenge.getId());
            mainController.databaseManager.projectService.saveProject(project);

            // Ouvre l'éditeur avec le projet et l'image de base
            mainController.showEditor(project, baseImage);
        });
    }
}