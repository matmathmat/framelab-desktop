package fr.framelab.services;

import fr.framelab.DatabaseManager;
import fr.framelab.models.*;
import fr.framelab.utils.image.ImageUtil;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class EditorService {
    public static String getProjectDir(int projectId) {
        return "data/project/" + projectId;
    }

    public List<ImageLayer> prepareNewProject(Image challengeImg) {
        List<ImageLayer> layers = new ArrayList<>();

        WritableImage wi = ImageUtil.toWritable(challengeImg);
        layers.add(new ImageLayer(wi));

        return layers;
    }

    public List<ImageLayer> loadProjectLayers(Project project, DatabaseManager db) {
        List<ImageLayer> layers = new ArrayList<>();

        // On récupère les calques depuis notre db local
        List<Layer> dbLayers = db.layerService.getProjectLayers(project.getId());

        // Si la liste est vide -> c'est la première fois qu'on ouvre le projet après sa création
        if (dbLayers.isEmpty()) {
            // On doit donc récupérer l'image de base du challenge
            String localPath = ChallengeService.getLocalImagePath(project.getChallengeId());
            File localImageFile = new File(localPath);

            if (localImageFile.exists()) {
                // On utilise prepareNewProject qui va créer automatiquement le premier calque avec l'image
                Image challengeImg = new Image(localImageFile.toURI().toString());
                return prepareNewProject(challengeImg);
            }

            return layers;
        }

        // Sinon -> Le projet a au moins un calque, donc il a déjà été sauvegardé
        String dir = getProjectDir(project.getId());
        for (Layer dbLayer : dbLayers) {
            // Pour chaque image valide (elle doit exister) on va l'ajouter à notre liste de calque
            File f = new File(dir + "/layer_" + dbLayer.getIndex() + ".png");

            if (f.exists()) {
                Image img = new Image(f.toURI().toString());
                WritableImage wi = ImageUtil.toWritable(img);

                boolean isTransparent = (dbLayer.getLayerType() == Layer.TYPE_TRANSPARENT);

                layers.add(new ImageLayer(wi, isTransparent));
            }
        }

        return layers;
    }

    public void saveProjectFull(Project project, List<ImageLayer> layers, DatabaseManager db) throws IOException {
        // On Sauvegarde le projet en db
        db.projectService.saveProject(project);

        // On crée le répertoire data/project/{id}/
        String dir = getProjectDir(project.getId());
        Files.createDirectories(Paths.get(dir));

        // On supprime les anciens fichiers layer_*.png
        Files.list(Paths.get(dir))
                .filter(p -> p.getFileName().toString().matches("layer_\\d+\\.png"))
                .forEach(p -> { try { Files.delete(p); } catch (IOException ignored) {} });

        // On clear les layers en db
        db.layerService.deleteProjectLayers(project.getId());

        for (int i = 0; i < layers.size(); i++) {
            // On écrit chaque ImageLayer en PNG sur le disque
            ImageLayer il = layers.get(i);
            String path = dir + "/layer_" + i + ".png";
            ImageUtil.saveToDisk(il.getEditedImage(), path);

            // Et on sauvegarde le calque dans la db
            int type = il.isDrawable() ? Layer.TYPE_TRANSPARENT : Layer.TYPE_IMAGE;
            db.layerService.saveLayer(new Layer(project.getId(), i, type));
        }
    }
}