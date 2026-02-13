package fr.framelab.modules;

import fr.framelab.controller.EditorController;
import fr.framelab.controller.editor.enhancement.BrightnessController;
import fr.framelab.controller.editor.enhancement.ContrastController;
import fr.framelab.controller.editor.enhancement.RotationController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TransformationModules {
    public static ObservableList<EditorModule> getModules(EditorController editorController) {

        return FXCollections.observableArrayList(
                new EditorModule(
                        "Rotation libre",
                        "Ajuster la rotation de l'image librements",
                        () -> {
                            RotationController controller = new RotationController();
                            controller.show(editorController);
                        }
                )
        );
    }
}