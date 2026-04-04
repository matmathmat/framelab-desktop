package fr.framelab.controller.home;

import fr.framelab.controller.MainController;
import fr.framelab.models.Challenge;
import fr.framelab.models.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class ChallengePaneController {

    @FXML
    private TitledPane root;

    @FXML
    private VBox projectList;

    private MainController mainController;
    private Runnable onDeleted;

    public void setup(Challenge challenge, List<Project> projects, MainController mainController, Runnable onDeleted) {
        this.mainController = mainController;
        this.onDeleted = onDeleted;

        root.setText(challenge.getTitleTheme());

        for (Project project : projects) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/home/project_item.fxml"));
                loader.load();

                ProjectItemController itemController = loader.getController();
                itemController.setup(project, mainController, onDeleted);

                projectList.getChildren().add(loader.getRoot());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}