package com.example.c195;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is the launcher for the application
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class C195App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(C195App.class.getResource("LoginForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Please Log In");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}