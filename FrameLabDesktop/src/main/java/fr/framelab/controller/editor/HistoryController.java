package fr.framelab.controller.editor;

import fr.framelab.controller.EditorController;
import fr.framelab.models.ImageLayer;
import fr.framelab.modules.image.ImageOperation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HistoryController {

    @FXML private ListView<String> historyList;
    @FXML private Button okButton;
    @FXML private Button cancelButton;

    private Stage stage;
    private EditorController editorController;
    private int originalOperationIndex;

    public static void show(EditorController editorController) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HistoryController.class.getResource("/fr/framelab/view/editor/history.fxml"));
            BorderPane root = loader.load();

            HistoryController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Historique des actions");
            stage.setScene(new Scene(root, 420, 320));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            controller.setStage(stage);
            controller.setEditorController(editorController);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setEditorController(EditorController editorController) {
        this.editorController = editorController;
        ImageLayer layer = editorController.getActiveLayer();
        if (layer == null) return;

        this.originalOperationIndex = layer.getOperationIndex();

        // Construire la liste des opérations
        ObservableList<String> items = FXCollections.observableArrayList();
        items.add("0  —  État initial");

        List<ImageOperation> ops = layer.getOperations();
        for (int i = 0; i < ops.size(); i++) {
            items.add((i + 1) + "  —  " + ops.get(i).getName());
        }

        historyList.setItems(items);

        // Sélectionner l'état courant
        historyList.getSelectionModel().select(layer.getOperationIndex());
        historyList.scrollTo(layer.getOperationIndex());

        historyList.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int idx = newVal.intValue();
            if (idx >= 0) {
                layer.jumpToOperation(idx);
                editorController.refreshEditedImage();
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setOnCloseRequest(event -> revert());
    }

    @FXML
    private void handleOk() {
        stage.close();
    }

    @FXML
    private void handleCancel() {
        revert();
        stage.close();
    }

    private void revert() {
        ImageLayer layer = editorController.getActiveLayer();
        if (layer != null) {
            layer.jumpToOperation(originalOperationIndex);
            editorController.refreshEditedImage();
        }
    }
}