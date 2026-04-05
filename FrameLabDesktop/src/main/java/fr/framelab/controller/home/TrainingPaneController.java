package fr.framelab.controller.home;

import fr.framelab.controller.MainController;
import fr.framelab.models.*;
import fr.framelab.utils.validation.DateValidator;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class TrainingPaneController {
    @FXML
    private ImageView trainingImage;

    @FXML
    private Label countdownLabel;

    @FXML
    private Label completedBadge;

    @FXML
    private ListView<String> operationsList;

    @FXML
    private Button startButton;

    private MainController mainController;
    private Training currentTraining;
    private Image image;
    private String today;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        this.today = LocalDate.now().toString();
        initPanel();
        startCountdown();
    }

    private void initPanel() {
        User user = mainController.frameLabService.currentUser;
        int userId = (user != null) ? user.getId() : 0;

        currentTraining = mainController.databaseManager.trainingService.getOrCreateTraining(today, userId);
        image = mainController.databaseManager.trainingService.getOrDownloadTrainingImage(today);
        trainingImage.setImage(image);

        List<TrainingOperation> ops = mainController.databaseManager.trainingService.generateOperations(today);
        operationsList.getItems().clear();
        for (TrainingOperation op : ops) {
            operationsList.getItems().add("- " + op.getDisplayName() + (op.getParam() != null ? " (" + op.getParam() + ")" : ""));
        }

        if (currentTraining.isCompleted()) {
            completedBadge.setVisible(true);
            completedBadge.setManaged(true);
            startButton.setText("Recommencer l'entraînement");
        }
    }

    private void startCountdown() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime midnight = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT);

            java.time.Duration diff = java.time.Duration.between(now, midnight);

            long h = diff.toHours();
            long m = diff.toMinutesPart();
            long s = diff.toSecondsPart();

            countdownLabel.setText(String.format("Réinitialisation dans : %02d:%02d:%02d", h, m, s));
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void handleStart() {

        User user = mainController.frameLabService.currentUser;
        int userId = (user != null) ? user.getId() : 0;

        // Génération des dates
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime end   = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        String startDate = start.format(DateValidator.DATETIME_FORMATTER);
        String endDate   = end.format(DateValidator.DATETIME_FORMATTER);

        // Pas très beau de créer un challenge mais bon c'est le rush
        Challenge challengeProject = new Challenge(
                9999,
                "Entraînement " + today,
                "Travaillez sur un calque. Parfait pour s'entraîner.",
                "https://localhost",
                startDate,
                endDate,
                0
        );

        mainController.databaseManager.challengeService.saveChallenge(challengeProject);

        Project trainingProject = new Project("Entraînement " + today, userId, 9999);
        trainingProject.setIsTraining(1);

        mainController.databaseManager.projectService.saveProject(trainingProject);

        mainController.showTrainingEditor(trainingProject, image, currentTraining);
    }
}