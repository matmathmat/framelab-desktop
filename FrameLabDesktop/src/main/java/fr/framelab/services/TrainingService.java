package fr.framelab.services;

import fr.framelab.dao.TrainingDAO;
import fr.framelab.models.Training;
import fr.framelab.models.TrainingOperation;
import fr.framelab.modules.image.ImageOperation;
import fr.framelab.modules.image.ImageOperationFactory;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrainingService {
    private final TrainingDAO trainingDAO;

    public TrainingService(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    public Training getTraining(int trainingId) {
        return this.trainingDAO.findById(trainingId);
    }

    public Training getTrainingByDate(String date, int userId) {
        return this.trainingDAO.findByDateAndUserId(date, userId);
    }

    public Training getOrCreateTraining(String date, int userId) {
        Training training = this.trainingDAO.findByDateAndUserId(date, userId);

        if (training == null) {
            training = new Training(date, userId);
            this.trainingDAO.save(training);
        }

        return training;
    }

    public List<Training> getUserTrainings(int userId) {
        return this.trainingDAO.findByUserId(userId);
    }

    public void saveTraining(Training training) {
        if (training == null) {
            throw new IllegalArgumentException("Training cannot be null");
        }

        if (training.getId() == -1) {
            this.trainingDAO.save(training);
        } else {
            this.trainingDAO.update(training);
        }
    }

    public void deleteTraining(int id) {
        this.trainingDAO.deleteById(id);
    }

    public void incrementAttempt(Training training) {
        if (training == null) {
            throw new IllegalArgumentException("Training cannot be null");
        }

        training.setAttemptCount(training.getAttemptCount() + 1);
        this.trainingDAO.update(training);
    }

    public void markCompleted(Training training) {
        if (training == null) {
            throw new IllegalArgumentException("Training cannot be null");
        }

        training.setCompleted(1);
        this.trainingDAO.update(training);
    }

    public String getTrainingImagePath(String date) {
        return "data/trainings/" + date + "/image.png";
    }

    public List<TrainingOperation> generateOperations(String date) {
        Random rng = new Random(LocalDate.parse(date).toEpochDay());
        int count = 5 + rng.nextInt(6);

        List<String> types = ImageOperationFactory.getAvailableTypes();
        List<TrainingOperation> ops = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String type = types.get(rng.nextInt(types.size()));
            Integer param = null;

            if (type.equals("BrightnessOperation"))  {
                param = -100 + rng.nextInt(201);
            }
            else if (type.equals("ContrastOperation"))  {
                param = -100 + rng.nextInt(201);
            }
            else if (type.equals("RotationOperation")) {
                param = -180 + rng.nextInt(361);
            }

            ops.add(new TrainingOperation(type, param));
        }
        return ops;
    }

    public Image getOrDownloadTrainingImage(String date) {
        String savePath = getTrainingImagePath(date);
        File savedFile = new File(savePath);

        // Si l'image existe déjà localement
        if (savedFile.exists()) {
            return new Image(savedFile.toURI().toString());
        }

        // Sinon on tente de la télécharger
        try {
            Path imagePath = Paths.get(savePath);
            Files.createDirectories(imagePath.getParent());

            // Cette url renvoie une image aléatoire de 800x800
            URL url = new URL("https://picsum.photos/800");
            try (InputStream in = url.openStream()) {
                Files.copy(in, imagePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return new Image(savedFile.toURI().toString());

        } catch (IOException e) {
            // On utilise l'image démo en cours
            return loadDemoImage(savePath);
        }
    }

    private Image loadDemoImage(String savePath) {
        try (InputStream demoStream = TrainingService.class.getResourceAsStream("/fr/framelab/demo/demo_image.png")) {
            if (demoStream != null) {
                Path imagePath = Paths.get(savePath);
                Files.createDirectories(imagePath.getParent());
                Files.copy(demoStream, imagePath, StandardCopyOption.REPLACE_EXISTING);

                File cached = new File(savePath);
                if (cached.exists()) {
                    return new Image(cached.toURI().toString());
                }
            }
        } catch (IOException ignored) {

        }

        InputStream stream = TrainingService.class.getResourceAsStream("/fr/framelab/demo/demo_image.png");

        return (stream != null) ? new Image(stream) : null;
    }

    public boolean validateOperations(List<ImageOperation> applied, List<TrainingOperation> expected) {
        if (applied.size() != expected.size()) return false;

        for (int i = 0; i < expected.size(); i++) {
            ImageOperation opApplied = applied.get(i);
            TrainingOperation opExpected = expected.get(i);

            // On compare le nom de la classe
            if (!opApplied.getClass().getSimpleName().equals(opExpected.getType())) {
                return false;
            }

            // On compare le paramètre
            Integer appliedParam = opApplied.getParameterValue();
            if (opExpected.getParam() != null && !opExpected.getParam().equals(appliedParam)) {
                return false;
            }
        }

        return true;
    }

    public int computeScore(int attempts) {
        if (attempts <= 0) {
            return 0;
        }

        return 1000 / attempts;
    }
}