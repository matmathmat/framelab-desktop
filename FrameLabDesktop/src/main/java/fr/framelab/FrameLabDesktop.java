package fr.framelab;

import fr.framelab.api.FrameLabAPI;
import fr.framelab.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FrameLabDesktop extends Application {
    private FrameLabAPI frameLabAPI;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.frameLabAPI = new FrameLabAPI("localhost:3000", false);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/framelab/view/main-view.fxml"));
        BorderPane root = loader.load();

        MainController mainController = loader.getController();
        mainController.setServices(frameLabAPI);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("FrameLabDesktop");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
