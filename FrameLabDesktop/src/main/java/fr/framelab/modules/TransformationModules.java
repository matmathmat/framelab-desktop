package fr.framelab.modules;

import fr.framelab.controller.EditorController;
import fr.framelab.controller.editor.enhancement.BrightnessController;
import fr.framelab.controller.editor.enhancement.ContrastController;
import fr.framelab.controller.editor.enhancement.RotationController;
import fr.framelab.models.ImageLayer;
import fr.framelab.modules.image.InvertOperation;
import fr.framelab.modules.image.RotationOperation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TransformationModules {
    public static ObservableList<EditorModule> getModules(EditorController editorController) {

        return FXCollections.observableArrayList(
                new EditorModule(
                        "Faire pivoter de 90° à droite",
                        "Appliquer une rotation de 90° à droite",
                        () -> {
                            // C'est très moche façon de faire le code ici mais ça marche
                            RotationOperation rotationOperation = new RotationOperation(90);

                            // Ajouter l'opération au calque actif
                            ImageLayer activeLayer = editorController.getActiveLayer();
                            activeLayer.addImageOperation(rotationOperation);

                            // Mettre à jour l'affichage avec l'image du calque
                            editorController.updateEditedImage();
                        }
                ),
                new EditorModule(
                        "Faire pivoter de 90° à gauche",
                        "Appliquer une rotation de 90° à gauche",
                        () -> {
                            // C'est très moche façon de faire le code ici mais ça marche
                            RotationOperation rotationOperation = new RotationOperation(-90);

                            // Ajouter l'opération au calque actif
                            ImageLayer activeLayer = editorController.getActiveLayer();
                            activeLayer.addImageOperation(rotationOperation);

                            // Mettre à jour l'affichage avec l'image du calque
                            editorController.updateEditedImage();
                        }
                ),
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