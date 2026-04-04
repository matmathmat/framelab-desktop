package fr.framelab.controller.home;

import fr.framelab.controller.MainController;
import fr.framelab.models.Challenge;
import fr.framelab.models.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class ProjectPanelController {
    @FXML
    private Accordion accordion;

    @FXML
    private Label emptyLabel;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        load();
    }

    public void load() {
        accordion.getPanes().clear();

        int userId = (mainController.frameLabService != null && mainController.frameLabService.currentUser != null)
                ? mainController.frameLabService.currentUser.getId()
                : 0;

        List<Challenge> allChallenges = mainController.databaseManager.challengeService.getAllChallenges();
        boolean hasAnyProject = false;

        for (Challenge challenge : allChallenges) {
            List<Project> projects = mainController.databaseManager.projectService
                    .getUserProjectsByChallenge(challenge.getId(), userId);

            if (projects == null || projects.isEmpty()) continue;

            projects.sort(Comparator.comparing(Project::getEditedAt).reversed());

            hasAnyProject = true;

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/home/challenge_pane.fxml"));
                TitledPane pane = loader.load();

                ChallengePaneController paneController = loader.getController();
                paneController.setup(challenge, projects, mainController, this::load);

                accordion.getPanes().add(pane);
            } catch (IOException e) {
                System.err.println("Erreur de chargement challenge_pane.fxml : " + e.getMessage());
                e.printStackTrace();
            }
        }

        emptyLabel.setVisible(!hasAnyProject);
        emptyLabel.setManaged(!hasAnyProject);
        accordion.setVisible(hasAnyProject);
        accordion.setManaged(hasAnyProject);
    }
}