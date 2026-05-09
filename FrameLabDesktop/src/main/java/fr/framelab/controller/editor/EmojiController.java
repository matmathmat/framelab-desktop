package fr.framelab.controller.editor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.framelab.utils.emoji.EmojiUtils;
import fr.framelab.utils.emoji.EmojiList;

public class EmojiController {

    @FXML private TextField charInput;
    @FXML private FlowPane paletteContainer;
    @FXML private Label pageLabel;

    private static final int PAGE_SIZE = 25;

    private Stage stage;
    private String selectedEmoji = null;
    private List<String> emojis;
    private int currentPage = 0;

    @FXML
    public void initialize() {
        // Remplir la palette depuis EmojiList
        emojis = new ArrayList<>(EmojiList.EMOJIS);
        updatePage();

        // Limiter le TextField à 1 seul caractère
        charInput.textProperty().addListener((obs, oldV, newV) -> {
            if (newV != null && newV.codePoints().count() > 1) {
                charInput.setText(oldV);
            }
        });
    }

    private int getTotalPages() {
        return (int) Math.ceil((double) emojis.size() / PAGE_SIZE);
    }

    private void updatePage() {
        paletteContainer.getChildren().clear();

        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, emojis.size());

        for (int i = start; i < end; i++) {
            String emoji = emojis.get(i);
            Button btn = new Button(emoji);
            btn.getStyleClass().add("emoji-btn");
            btn.setOnAction(e -> applyEmoji(emoji));
            paletteContainer.getChildren().add(btn);
        }

        pageLabel.setText((currentPage + 1) + " / " + getTotalPages());
    }

    @FXML
    private void handlePrev() {
        currentPage = (currentPage - 1 + getTotalPages()) % getTotalPages();
        updatePage();
    }

    @FXML
    private void handleNext() {
        currentPage = (currentPage + 1) % getTotalPages();
        updatePage();
    }

    @FXML
    private void handleConfirm() {
        String input = charInput.getText();
        if (input == null || input.isEmpty()) {
            showError("Veuillez entrer un caractère.");
            return;
        }

        int codePoint = input.codePointAt(0);
        if (!EmojiUtils.isEmoji(codePoint)) {
            showError("Le caractère saisi n'est pas un caractère dessinable valide !");
            return;
        }

        applyEmoji(input);
    }

    private void applyEmoji(String emoji) {
        this.selectedEmoji = emoji;
        this.stage.close();
    }

    @FXML
    private void handleCancel() {
        this.selectedEmoji = null;
        this.stage.close();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.setHeaderText("Caractère invalide");
        alert.showAndWait();
    }

    public String showAndWait() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/fxml/editor/emoji.fxml"));
            BorderPane root = loader.load();
            EmojiController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Choisir un caractère");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

            controller.stage = stage;
            stage.showAndWait();

            return controller.selectedEmoji;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}