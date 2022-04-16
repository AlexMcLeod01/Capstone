package com.example.c195;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.ZoneId;


/**
 * This class is the controller for the login form
 * @author Harold Alex Mcleod
 * @version 1.0
 */
public class LoginFormController {
    //Declaring FXML elements
    @FXML
    private Label timeZoneDisplay;

    /**
     * This method exits the application upon click
     */
    @FXML
    private void exitClicked() {
        Platform.exit();
    }

    /**
     * This method initializes data that should be visible
     */
    @FXML
    private void initialize() {
        timeZoneDisplay.setText(ZoneId.systemDefault().toString());
    }
}