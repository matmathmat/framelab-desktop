package fr.framelab.controller.editor.enhancement;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ContrastController {

    @FXML
    private Slider slider;

    @FXML
    private Label valueLabel;

    @FXML
    private Button applyButton;

    private Stage stage;

    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/editor/enhancement/contrast.fxml"));
            BorderPane root = loader.load();

            // Créer la fenêtre
            this.stage = new Stage();
            this.stage.setTitle("Contraste");
            this.stage.setScene(new Scene(root, 300, 150));

            // lier le stage et le controller
            ContrastController controller = loader.getController();
            controller.setStage(this.stage);

            // Afficher la fenêtre
            this.stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        this.valueLabel.textProperty().bind(
                Bindings.format(
                        "%.2f",
                        slider.valueProperty()
                )
        );
    }

    @FXML
    private void handleApply() {
        this.stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
