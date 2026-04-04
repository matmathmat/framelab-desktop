package fr.framelab.controller;

import fr.framelab.controller.editor.HistoryController;
import fr.framelab.controller.editor.LayerRowController;
import fr.framelab.models.ImageLayer;
import fr.framelab.models.Layer;
import fr.framelab.models.Project;
import fr.framelab.modules.EditorModule;
import fr.framelab.modules.EnhancementModules;
import fr.framelab.modules.FilterModules;
import fr.framelab.modules.TransformationModules;
import fr.framelab.modules.image.DrawOperation;
import fr.framelab.modules.image.EraseOperation;
import fr.framelab.services.EditorService;
import fr.framelab.utils.image.ImageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;

import javafx.scene.input.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;

public class EditorController {
    @FXML private ImageView baseImage;
    @FXML private ImageView editedImage;
    @FXML private ScrollPane leftScrollPane;
    @FXML private ScrollPane rightScrollPane;
    @FXML private SplitPane centerSplitPane;
    @FXML private ComboBox<EditorModule> enhancementComboBox;
    @FXML private ComboBox<EditorModule> filterComboBox;
    @FXML private ComboBox<EditorModule> transformComboBox;
    @FXML private VBox layersContainer;
    @FXML private Button pencilButton;
    @FXML private Button eraserButton;
    @FXML private Button toggleReferenceButton;
    @FXML private Label zoomLabel;

    private MainController mainController;
    private Project project;

    private final List<ImageLayer> layers = new ArrayList<>();
    private int activeLayerIndex = -1;

    private final EditorService editorService = new EditorService();

    private enum Tool { NONE, PENCIL, ERASER }
    private Tool activeTool = Tool.NONE;

    private final List<int[]> currentStrokePoints = new ArrayList<>();
    private WritableImage preStrokeSnapshot = null;

    private static final int   PENCIL_SIZE  = 8;
    private static final Color PENCIL_COLOR = Color.BLACK;
    private static final int   ERASER_SIZE  = 20;

    private double zoomFactor = 1.0;
    private static final double ZOOM_STEP = 0.10;
    private static final double ZOOM_MIN  = 0.01;   // 1 %
    private static final double ZOOM_MAX  = 10.0;   // 1 000

    private Node referencePane = null;

    // Initialisation

    @FXML
    public void initialize() {
        // Charger les modules dans les combo boxes
        enhancementComboBox.setItems(EnhancementModules.getModules(this));
        filterComboBox.setItems(FilterModules.getModules(this));
        transformComboBox.setItems(TransformationModules.getModules(this));

        // Appliquer le zoom au redimensionnement de la fenêtre
        leftScrollPane.widthProperty().addListener((o, prev, next)  -> applyZoom());
        leftScrollPane.heightProperty().addListener((o, prev, next) -> applyZoom());
        rightScrollPane.widthProperty().addListener((o, prev, next) -> applyZoom());
        rightScrollPane.heightProperty().addListener((o, prev, next)-> applyZoom());

        // Evenement souris
        editedImage.setOnMousePressed(this::onMousePressed);
        editedImage.setOnMouseDragged(this::onMouseDragged);
        editedImage.setOnMouseReleased(this::onMouseReleased);

        // On récupère le panneau de référence après l'injection FXML
        javafx.application.Platform.runLater(() ->
                referencePane = centerSplitPane.getItems().isEmpty()
                        ? null
                        : centerSplitPane.getItems().get(0)
        );
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

        // On charge l'image du challenge qui sert de référence
        String challengePath = fr.framelab.services.ChallengeService.getLocalImagePath(project.getChallengeId());
        File challengeFile = new File(challengePath);
        if (challengeFile.exists()) {
            this.baseImage.setImage(new Image(challengeFile.toURI().toString()));
        } else {
            // Si l'image challenge n'existe plus on prend le premier layer
            this.baseImage.setImage(layers.get(0).getBaseImage());
        }

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

    @FXML
    private void handleReset() {
        ImageLayer layer = getActiveLayer();
        if (layer == null) return;
        layer.reset();
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

    // Outils crayon & gomme

    @FXML
    private void handlePencil() {
        activeTool = (activeTool == Tool.PENCIL) ? Tool.NONE : Tool.PENCIL;
        updateToolUI();
    }

    @FXML
    private void handleEraser() {
        activeTool = (activeTool == Tool.ERASER) ? Tool.NONE : Tool.ERASER;
        updateToolUI();
    }

    private void updateToolUI() {
        String activeStyle = "-fx-background-radius: 5; -fx-background-color: #b3d9ff;";
        String inactiveStyle = "-fx-background-radius: 5;";

        pencilButton.setStyle(activeTool == Tool.PENCIL ? activeStyle : inactiveStyle);
        eraserButton.setStyle(activeTool == Tool.ERASER ? activeStyle : inactiveStyle);
    }

    // Événements souris

    private void onMousePressed(MouseEvent e) {
        if (activeTool == Tool.NONE) return;

        ImageLayer layer = getActiveLayer();
        if (layer == null) return;

        // Le crayon ne peut fonctionner que sur un calque de dessin
        if (activeTool == Tool.PENCIL && !layer.isDrawable()) return;

        // On sauvegarde l'état du calque avant le tracé
        preStrokeSnapshot = ImageUtil.copyImage(layer.getEditedImage());
        currentStrokePoints.clear();

        int[] pt = toImageCoords(e.getX(), e.getY());
        if (pt != null) currentStrokePoints.add(pt);
    }

    private void onMouseDragged(MouseEvent e) {
        if (activeTool == Tool.NONE || preStrokeSnapshot == null) return;

        ImageLayer layer = getActiveLayer();
        if (layer == null) return;
        if (activeTool == Tool.PENCIL && !layer.isDrawable()) return;

        int[] pt = toImageCoords(e.getX(), e.getY());
        if (pt != null) currentStrokePoints.add(pt);

        layer.restoreFromSnapshot(preStrokeSnapshot);
        applyCurrentStrokePreview(layer);
        refreshEditedImage();
    }

    private void onMouseReleased(MouseEvent e) {
        if (activeTool == Tool.NONE || preStrokeSnapshot == null) return;

        ImageLayer layer = getActiveLayer();
        if (layer == null) { preStrokeSnapshot = null; return; }
        if (activeTool == Tool.PENCIL && !layer.isDrawable()) { preStrokeSnapshot = null; return; }

        int[] pt = toImageCoords(e.getX(), e.getY());
        if (pt != null) currentStrokePoints.add(pt);

        if (!currentStrokePoints.isEmpty()) {
            // On remet le calque à son état avant le tracé
            layer.restoreFromSnapshot(preStrokeSnapshot);

            // On crée et enregistre l'opération
            if (activeTool == Tool.PENCIL) {
                layer.addImageOperation(new DrawOperation(currentStrokePoints, PENCIL_COLOR, PENCIL_SIZE));
            } else {
                layer.addImageOperation(new EraseOperation(currentStrokePoints, ERASER_SIZE));
            }
            refreshEditedImage();
        }

        preStrokeSnapshot = null;
        currentStrokePoints.clear();
    }

    private void applyCurrentStrokePreview(ImageLayer layer) {
        if (currentStrokePoints.isEmpty()) return;

        if (activeTool == Tool.PENCIL) {
            new DrawOperation(currentStrokePoints, PENCIL_COLOR, PENCIL_SIZE)
                    .handle(layer.getEditedImage());
        } else {
            new EraseOperation(currentStrokePoints, ERASER_SIZE)
                    .handle(layer.getEditedImage());
        }
    }

    private int[] toImageCoords(double mouseX, double mouseY) {
        Image img = editedImage.getImage();
        if (img == null) return null;

        double viewW = editedImage.getBoundsInLocal().getWidth();
        double viewH = editedImage.getBoundsInLocal().getHeight();
        double imgW  = img.getWidth();
        double imgH  = img.getHeight();

        if (viewW <= 0 || viewH <= 0) return null;

        double scale = Math.min(viewW / imgW, viewH / imgH);

        // Taille réellement rendue
        double renderedW = imgW * scale;
        double renderedH = imgH * scale;

        // Décalage
        double offsetX = (viewW - renderedW) / 2.0;
        double offsetY = (viewH - renderedH) / 2.0;

        int imgX = (int) ((mouseX - offsetX) / scale);
        int imgY = (int) ((mouseY - offsetY) / scale);

        imgX = Math.max(0, Math.min(imgX, (int) imgW - 1));
        imgY = Math.max(0, Math.min(imgY, (int) imgH - 1));

        return new int[]{imgX, imgY};
    }

    // Gestion de l'historique

    @FXML
    private void handleHistory() {
        if (getActiveLayer() == null) return;
        HistoryController.show(this);
    }

    // Gestion image de référence

    @FXML
    private void handleToggleReference() {
        if (referencePane == null) return;

        boolean visible = centerSplitPane.getItems().contains(referencePane);
        if (visible) {
            centerSplitPane.getItems().remove(referencePane);
            toggleReferenceButton.setText("Afficher image de référence");
        } else {
            centerSplitPane.getItems().add(0, referencePane);
            centerSplitPane.setDividerPositions(0.5);
            toggleReferenceButton.setText("Masquer image de référence");
        }
    }

    // Ma partie préfére le zoom

    @FXML
    private void handleZoomIn()  { zoomIn(); }

    @FXML
    private void handleZoomOut() { zoomOut(); }

    private void zoomIn() {
        zoomFactor = Math.min(ZOOM_MAX, zoomFactor + ZOOM_STEP);
        applyZoom();
    }

    private void zoomOut() {
        zoomFactor = Math.max(ZOOM_MIN, zoomFactor - ZOOM_STEP);
        applyZoom();
    }

    private void applyZoom() {
        // Taille de la zone visible dans chaque ScrollPane
        double lw = leftScrollPane.getWidth()   > 0 ? leftScrollPane.getWidth()   : leftScrollPane.getPrefWidth();
        double lh = leftScrollPane.getHeight()  > 0 ? leftScrollPane.getHeight()  : leftScrollPane.getPrefHeight();
        double rw = rightScrollPane.getWidth()  > 0 ? rightScrollPane.getWidth()  : rightScrollPane.getPrefWidth();
        double rh = rightScrollPane.getHeight() > 0 ? rightScrollPane.getHeight() : rightScrollPane.getPrefHeight();

        baseImage.setFitWidth(lw * zoomFactor);
        baseImage.setFitHeight(lh * zoomFactor);
        editedImage.setFitWidth(rw * zoomFactor);
        editedImage.setFitHeight(rh * zoomFactor);

        int pct = (int) Math.round(zoomFactor * 100);

        if (zoomLabel != null) zoomLabel.setText(pct + "%");
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