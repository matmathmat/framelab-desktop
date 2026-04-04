package fr.framelab.controller.editor;

import fr.framelab.controller.EditorController;
import fr.framelab.models.ImageLayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

public class LayerRowController {

    @FXML
    private HBox root;

    @FXML
    private CheckBox visibleCheckBox;

    @FXML
    private Label layerNameLabel;

    @FXML
    private Slider opacitySlider;

    @FXML
    private Label opacityLabel;

    @FXML
    private Button upButton;

    @FXML
    private Button downButton;

    @FXML
    private Button deleteButton;

    private ImageLayer layer;
    private int index;
    private EditorController editorController;

    public void setup(ImageLayer layer, int index, int totalLayers,
                      boolean isActive, EditorController editorController) {
        this.layer = layer;
        this.index = index;
        this.editorController = editorController;

        String typeLabel = layer.isDrawable() ? "Transp." : "Image";
        layerNameLabel.setText(typeLabel + " " + index);

        visibleCheckBox.setSelected(layer.isVisible());
        visibleCheckBox.setOnAction(e -> {
            layer.setVisible(visibleCheckBox.isSelected());
            editorController.refreshEditedImage();
        });

        int pct = (int) Math.round(layer.getOpacity() * 100);
        opacitySlider.setValue(pct);
        opacityLabel.setText(pct + "%");

        opacitySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int v = newVal.intValue();
            layer.setOpacity(v / 100.0);
            opacityLabel.setText(v + "%");
            editorController.refreshEditedImage();
        });

        upButton.setDisable(index == totalLayers - 1);
        downButton.setDisable(index == 0);
        deleteButton.setDisable(totalLayers <= 1);

        setActive(isActive);
        root.setOnMouseClicked(e -> editorController.selectLayer(index));
    }

    public void setActive(boolean active) {
        root.setStyle(active
                ? "-fx-background-color: #cce5ff; -fx-border-color: #99caff; -fx-border-radius: 4;"
                : "-fx-background-color: white;");
    }

    @FXML private void handleUp()     {
        editorController.moveLayer(index, index + 1);
    }

    @FXML private void handleDown()   {
        editorController.moveLayer(index, index - 1);
    }

    @FXML private void handleDelete() {
        editorController.deleteLayer(index);
    }
}