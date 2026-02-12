package fr.framelab.modules;

import fr.framelab.controller.editor.enhancement.ContrastController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import fr.framelab.controller.editor.enhancement.BrightnessController;

public class EnhancementModules {
    public static ObservableList<EditorModule> getModules() {

        return FXCollections.observableArrayList(
                new EditorModule(
                        "Luminosité",
                        "Ajuster la luminosité de l'image",
                        new BrightnessController()::show
                ),
                new EditorModule(
                        "Contraste",
                        "Modifier le contraste",
                        new ContrastController()::show
                )
        );
    }
}
