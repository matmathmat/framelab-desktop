package fr.framelab.modules;

import fr.framelab.controller.EditorController;
import fr.framelab.controller.editor.enhancement.BrightnessController;
import fr.framelab.controller.editor.enhancement.ContrastController;
import fr.framelab.models.ImageLayer;
import fr.framelab.modules.image.BlacknWhiteOperation;
import fr.framelab.modules.image.InvertOperation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FilterModules {
    public static ObservableList<EditorModule> getModules(EditorController editorController) {

        return FXCollections.observableArrayList(
                new EditorModule(
                        "Noir et blanc",
                        "Appliquer un filtre noir et blanc",
                        () -> {
                            // C'est très moche façon de faire le code ici mais ça marche
                            BlacknWhiteOperation blacknWhiteOperation = new BlacknWhiteOperation();

                            // Ajouter l'opération au calque actif
                            ImageLayer activeLayer = editorController.getActiveLayer();
                            activeLayer.addImageOperation(blacknWhiteOperation);

                            // Mettre à jour l'affichage avec l'image du calque
                            editorController.updateEditedImage();
                        }
                ),
                new EditorModule(
                        "Inversion de couleur",
                        "Appliquer un filtre couleur inversé",
                        () -> {
                            // C'est très moche façon de faire le code ici mais ça marche
                            InvertOperation invertOperation = new InvertOperation();

                            // Ajouter l'opération au calque actif
                            ImageLayer activeLayer = editorController.getActiveLayer();
                            activeLayer.addImageOperation(invertOperation);

                            // Mettre à jour l'affichage avec l'image du calque
                            editorController.updateEditedImage();
                        }
                )
        );
    }
}