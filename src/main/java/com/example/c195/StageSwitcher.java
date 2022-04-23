package com.example.c195;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class holds the JavaFX stage switching functionality
 * @author Harold Alex McLeod
 * @version 1.0
 */
public final class StageSwitcher {

    /**
     * initializes INSTANCE as the singleton StageSwitcher
     */
    private final static StageSwitcher INSTANCE = new StageSwitcher();

    /**
     * Gets an instance of the singleton StageSwitcher
     * @return an instance
     */
    public static StageSwitcher getInstance() {
        return INSTANCE;
    }

    /**
     * This method switches between stages
     * @param stage a Stage object
     * @param stagename the fxml filename
     * @param title the title of window
     */
    public void SwitchStage (Stage stage, String stagename, String title) {
        try {
            AnchorPane root;
            root = (AnchorPane) FXMLLoader.load(getClass().getResource(stagename));
            Scene scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * StageSwitcher constructor
     */
    public StageSwitcher () {

    }
}
