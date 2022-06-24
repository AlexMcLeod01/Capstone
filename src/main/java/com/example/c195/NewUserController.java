package com.example.c195;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * The controller for the New User form
 */
public class NewUserController {
    //Database stuff
    private DBAccessor dba;
    private ResourceBundle msg;
    private StageSwitcher switcher;


    //FXML variables
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passField;
    @FXML
    private TextField passConfirmField;
    @FXML
    private TextField nameField;
    @FXML
    private Label errorLabel;

    /**
     * Returns to the login form
     */
    @FXML
    private void cancelClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        switcher.SwitchStage(stage, "LoginForm.fxml", msg.getString("LoginLabel"));
    }

    /**
     * saves form info if valid
     */
    @FXML
    private void saveClicked() {
        if(validate()) {
            //Collect form info
            String username = usernameField.getText();
            String password = passField.getText();
            String name = nameField.getText();
            String email = name + "@Sales4You.com";

            //Create and add user and contact to database
            int id = dba.getNewUserId();
            Rep rep = new Rep(username, id, id);
            dba.addUser(rep, password);
            Contact c = new Contact(id, name);
            dba.addContact(c, email);

            //Return to login form
            Stage stage = (Stage) saveButton.getScene().getWindow();
            switcher.SwitchStage(stage, "LoginForm.fxml", msg.getString("LoginLabel"));
        }
    }

    @FXML
    private boolean validate() {
        if (usernameField.getText().isEmpty()) {
            errorLabel.setText("Username is Required");
            errorLabel.setVisible(true);
            return false;
        }
        if (passField.getText().isEmpty()) {
            errorLabel.setText("Password is Required");
            errorLabel.setVisible(true);
            return false;
        }
        if (passConfirmField.getText().isEmpty()) {
            errorLabel.setText("Password Confirmation is Required");
            errorLabel.setVisible(true);
            return false;
        }
        if (nameField.getText().isEmpty()) {
            errorLabel.setText("Name is Required");
            errorLabel.setVisible(true);
            return false;
        }
        if (!passField.getText().equals(passConfirmField.getText())) {
            errorLabel.setText("Password And Password Confirmation Fields Must Match");
            errorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    /**
     * Initializes stuff that need initializing
     */
    @FXML
    private void initialize() {
        dba = DBAccessor.getInstance();
        msg = dba.getMsg();
        switcher = StageSwitcher.getInstance();
    }
}
