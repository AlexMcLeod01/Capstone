package com.example.c195;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ResourceBundle;


/**
 * This class is the controller for the login form
 * @author Harold Alex Mcleod
 * @version 1.0
 */
public class LoginFormController {
    private DBAccessor dba;
    private ResourceBundle msg;
    private StageSwitcher switcher;

    //Declaring FXML elements
    @FXML
    private Label header;
    @FXML
    private Label userLabel;
    @FXML
    private Label passLabel;
    @FXML
    private Label timeZoneLabel;
    @FXML
    private Label timeZoneDisplay;
    @FXML
    private Label errorMessageDisplay;
    @FXML
    private TextField userField;
    @FXML
    private TextField passField;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button newUserButton;

    /**
     * This method exits the application upon click
     */
    @FXML
    private void exitClicked() {
        Platform.exit();
    }

    /**
     * This method checks the login information against
     * the login information in the database. Returns appropriate
     * error if invalid information
     */
    private int checkCreds () {
        String use = userField.getText();
        String pas = passField.getText();
        if (dba.userExists(use)) {
            if (dba.userPass(use, pas)) {
                return 0;
            } else {
                return 2;
            }
        } else {
            return 1;
        }
    }

    /**
     * Displays error messages if error code is returned from checkCreds()
     */
    @FXML
    private void submitClicked() {
        int i = checkCreds();
        switch (i) {
            case 0:
                Stage stage = (Stage) submitButton.getScene().getWindow();
                switcher.SwitchStage(stage, "AppointmentManagementSystem.fxml", msg.getString("MainTitle"));
                break;
            case 1:
                errorMessageDisplay.setText(msg.getString("LoginErrorUser"));
                errorMessageDisplay.setVisible(true);
                break;
            case 2:
                errorMessageDisplay.setText(msg.getString("LoginErrorPass"));
                errorMessageDisplay.setVisible(true);
                break;
        }
    }

    /**
     * Opens the new user form
     */
    @FXML
    private void newUserClicked() {
        Stage stage = (Stage) newUserButton.getScene().getWindow();
        switcher.SwitchStage(stage, "NewUser.fxml", msg.getString("NewUserTitle"));
    }

    /**
     * Does localization stuff
     */
    @FXML
    private void localize() {
        submitButton.setText(msg.getString("LoginSubmit"));
        cancelButton.setText(msg.getString("Cancel"));
        timeZoneLabel.setText(msg.getString("LoginZone"));
        userLabel.setText(msg.getString("LoginUser"));
        passLabel.setText(msg.getString("LoginPass"));
        header.setText(msg.getString("LoginLabel"));
        newUserButton.setText(msg.getString("NewUser"));
    }

    /**
     * This method initializes data that should be visible
     */
    @FXML
    private void initialize() {
        dba = DBAccessor.getInstance();
        timeZoneDisplay.setText(dba.getZone().toString());
        msg = dba.getMsg();
        localize();
        switcher = StageSwitcher.getInstance();
    }
}