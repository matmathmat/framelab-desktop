package fr.framelab;

import fr.framelab.services.FrameLabService;
import fr.framelab.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FrameLabDesktop extends Application {
    private FrameLabService frameLabService;
    private DatabaseManager databaseManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.frameLabService = new FrameLabService("http://localhost:3000");
        this.databaseManager = new DatabaseManager();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/main.fxml"));
        BorderPane root = loader.load();

        MainController mainController = loader.getController();
        mainController.setServices(frameLabService, databaseManager);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("FrameLabDesktop");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (this.databaseManager != null) {
            this.databaseManager.close();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
