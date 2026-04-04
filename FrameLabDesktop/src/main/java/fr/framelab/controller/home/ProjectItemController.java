package fr.framelab.controller.home;

import fr.framelab.controller.MainController;
import fr.framelab.models.Project;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProjectItemController {
    @FXML
    private Label titleLabel;

    @FXML
    private Button openBtn;

    @FXML
    private Button deleteBtn;

    private Project project;
    private MainController mainController;
    private Runnable onDeleted; // callback pour rafraîchir le panneau parent

    public void setup(Project project, MainController mainController, Runnable onDeleted) {
        this.project = project;
        this.mainController = mainController;
        this.onDeleted = onDeleted;

        titleLabel.setText(project.getTitle());
    }

    @FXML
    private void handleOpen() {
        mainController.openExistingProject(project);
    }

    @FXML
    private void handleDelete() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("Supprimer \"" + project.getTitle() + "\" ?");
        confirm.showAndWait().ifPresent(btn -> {
            if (btn != ButtonType.OK) return;

            mainController.databaseManager.layerService.deleteProjectLayers(project.getId());
            deleteProjectFolder(project.getId());
            mainController.databaseManager.projectService.deleteProject(project.getId());

            onDeleted.run();
        });
    }

    private void deleteProjectFolder(int projectId) {
        var dir = Paths.get("data/project/" + projectId);

        if (!Files.exists(dir)) return;

        try {
            Files.walk(dir)
                    .sorted(java.util.Comparator.reverseOrder())
                    .forEach(p -> { try { Files.delete(p); } catch (IOException ignored) {} });
        } catch (IOException ignored) {

        }
    }
}