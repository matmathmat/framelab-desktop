package fr.framelab.modules;

import fr.framelab.controller.EditorController;
import fr.framelab.controller.editor.enhancement.ContrastController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import fr.framelab.controller.editor.enhancement.BrightnessController;

public class EnhancementModules {
    public static ObservableList<EditorModule> getModules(EditorController editorController) {

        return FXCollections.observableArrayList(
                new EditorModule(
                        "Luminosité",
                        "Ajuster la luminosité de l'image",
                        () -> {
                            BrightnessController controller = new BrightnessController();
                            controller.show(editorController);
                        }
                ),
                new EditorModule(
                        "Contraste",
                        "Modifier le contraste",
                        () -> {
                            ContrastController controller = new ContrastController();
                            controller.show();
                        }
                )
        );
    }
}