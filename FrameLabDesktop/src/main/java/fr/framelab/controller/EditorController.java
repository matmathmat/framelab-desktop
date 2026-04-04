package fr.framelab.controller;

import fr.framelab.controller.editor.LayerRowController;
import fr.framelab.models.ImageLayer;
import fr.framelab.models.Layer;
import fr.framelab.models.Project;
import fr.framelab.modules.EditorModule;
import fr.framelab.modules.EnhancementModules;
import fr.framelab.modules.FilterModules;
import fr.framelab.modules.TransformationModules;
import fr.framelab.services.EditorService;
import fr.framelab.utils.image.ImageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;

public class EditorController {
    @FXML private ImageView baseImage;
    @FXML private ImageView editedImage;
    @FXML private ScrollPane leftScrollPane;
    @FXML private ScrollPane rightScrollPane;
    @FXML private ComboBox<EditorModule> enhancementComboBox;
    @FXML private ComboBox<EditorModule> filterComboBox;
    @FXML private ComboBox<EditorModule> transformComboBox;
    @FXML private VBox layersContainer;

    private MainController mainController;
    private Project project;

    private final List<ImageLayer> layers = new ArrayList<>();
    private int activeLayerIndex = -1;

    private final EditorService editorService = new EditorService();

    // Initialisation

    @FXML
    public void initialize() {
        // Charger les modules dans les combo boxes
        enhancementComboBox.setItems(EnhancementModules.getModules(this));
        filterComboBox.setItems(FilterModules.getModules(this));
        transformComboBox.setItems(TransformationModules.getModules(this));

        // Lier la taille de l'ImageView à celle du ScrollPane
        baseImage.fitWidthProperty().bind(leftScrollPane.widthProperty());
        baseImage.fitHeightProperty().bind(leftScrollPane.heightProperty());

        editedImage.fitWidthProperty().bind(rightScrollPane.widthProperty());
        editedImage.fitHeightProperty().bind(rightScrollPane.heightProperty());
    }

    public void setMainController(MainController mainController) {

        this.mainController = mainController;
    }

    // Ouverture projet

    public void setNewProject(Project project, Image challengeImg) {
        this.project = project;

        List<ImageLayer> newLayers = editorService.prepareNewProject(challengeImg);

        this.layers.clear();
        this.layers.addAll(newLayers);
        this.activeLayerIndex = 0;

        this.baseImage.setImage(challengeImg);
        refreshLayersUI();
        refreshEditedImage();
    }

    public void setExistingProject(Project project) {
        this.project = project;

        List<ImageLayer> loadedLayers = editorService.loadProjectLayers(project, mainController.databaseManager);

        if (loadedLayers.isEmpty()) {
            System.err.println("Erreur : Impossible de charger les calques.");
            return;
        }

        this.layers.clear();
        this.layers.addAll(loadedLayers);
        this.activeLayerIndex = 0;

        Image base = layers.get(0).getBaseImage();
        this.baseImage.setImage(base);

        refreshLayersUI();
        refreshEditedImage();
    }

    // Sauvegarde

    @FXML
    private void handleSave() {
        try {
            editorService.saveProjectFull(project, layers, mainController.databaseManager);
            new Alert(Alert.AlertType.INFORMATION, "Projet sauvegardé !").showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(e.getClass().getSimpleName());
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
    }

    // Gestion des calques

    @FXML
    private void handleAddTransparentLayer() {
        if (layers.isEmpty()) return;
        int w = (int) layers.get(0).getBaseImage().getWidth();
        int h = (int) layers.get(0).getBaseImage().getHeight();
        layers.add(new ImageLayer(w, h));
        activeLayerIndex = layers.size() - 1;
        refreshLayersUI();
        refreshEditedImage();
    }

    @FXML
    private void handleAddImageLayer() {
        if (layers.isEmpty()) return;
        WritableImage wi = ImageUtil.toWritable(baseImage.getImage());
        layers.add(new ImageLayer(wi));
        activeLayerIndex = layers.size() - 1;
        refreshLayersUI();
        refreshEditedImage();
    }

    @FXML
    private void handleMergeVisibleLayers() {
        List<ImageLayer> visible = new ArrayList<>();
        for (ImageLayer l : layers) {
            if (l.isVisible()) visible.add(l);
        }
        if (visible.size() < 2) return;

        ImageLayer base = visible.get(0);
        for (int i = 1; i < visible.size(); i++) {
            base.merge(visible.get(i));
        }

        layers.removeIf(l -> l.isVisible() && l != base);
        activeLayerIndex = Math.min(activeLayerIndex, layers.size() - 1);

        refreshLayersUI();
        refreshEditedImage();
    }

    public void moveLayer(int fromIndex, int toIndex) {
        if (toIndex < 0 || toIndex >= layers.size()) return;
        ImageLayer layer = layers.remove(fromIndex);
        layers.add(toIndex, layer);
        if (activeLayerIndex == fromIndex) activeLayerIndex = toIndex;
        refreshLayersUI();
        refreshEditedImage();
    }

    public void deleteLayer(int index) {
        if (layers.size() <= 1) return;
        layers.remove(index);
        if (activeLayerIndex >= layers.size()) activeLayerIndex = layers.size() - 1;
        refreshLayersUI();
        refreshEditedImage();
    }

    public void selectLayer(int index) {
        activeLayerIndex = index;
        refreshLayersUI();
        refreshEditedImage();
    }

    // UI

    private void refreshLayersUI() {
        layersContainer.getChildren().clear();

        for (int i = layers.size() - 1; i >= 0; i--) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/editor/layer_row.fxml"));
                loader.load();
                LayerRowController rowController = loader.getController();
                rowController.setup(layers.get(i), i, layers.size(), i == activeLayerIndex, this);
                layersContainer.getChildren().add(loader.getRoot());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Render

    public void refreshEditedImage() {
        this.editedImage.setImage(ImageUtil.render(layers));
    }

    public void updateEditedImage() {
        refreshEditedImage();
    }

    public ImageLayer getActiveLayer() {
        if (activeLayerIndex < 0 || activeLayerIndex >= layers.size()) return null;
        return layers.get(activeLayerIndex);
    }

    public ImageView getEditedImageView() {
        return editedImage;
    }

    // Gestion module

    private void comboboxSelectedIndexChange(ComboBox<EditorModule> comboBox) {
        if (comboBox == null) return;

        // on obtient la valeur sélectionnée par la combobox
        EditorModule module = comboBox.getValue();

        // Si le module n'est pas nul on lance l'action
        if (module != null)  {
            module.getOnTrigger().run();
        }

        // Quand javafx voudra bien traiter notre demande
//        Platform.runLater(() -> {
//            // On désélectionne l'item sélectionné
//            comboBox.getSelectionModel().clearSelection();
//            // On force l'affichage de la combobox par défaut pour réafficher le text prompt
//            comboBox.setSkin(new ComboBoxListViewSkin<>(comboBox));
//        });
    }

    @FXML
    private void handleEnhancement() {

        comboboxSelectedIndexChange(this.enhancementComboBox);
    }

    @FXML
    private void handleFilter() {

        comboboxSelectedIndexChange(this.filterComboBox);
    }

    @FXML
    private void handleTransformation() {

        comboboxSelectedIndexChange(this.transformComboBox);
    }
}