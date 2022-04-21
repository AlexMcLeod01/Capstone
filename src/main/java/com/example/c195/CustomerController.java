package com.example.c195;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ResourceBundle;

/**
 * This class controls the Customer Records window
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class CustomerController {
    private DBAccessor dba;
    private ResourceBundle msg;

    /**
     * declare various FXML elements
     */
    @FXML private Button backButton;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button delButton;
    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label addressLabel;
    @FXML private Label postalLabel;
    @FXML private Label phoneLabel;
    @FXML private Label countryLabel;
    @FXML private Label firstLevelLabel;

    /**
     * This method switches the scene from current back to the AppointmentManagementSystem view
     */
    @FXML private void backClicked() {

    }

    /**
     * This method translates the GUI elements into French or English
     */
    @FXML private void localize() {
        backButton.setText(msg.getString("Back"));
        addButton.setText(msg.getString("Add"));
        updateButton.setText(msg.getString("Update"));
        delButton.setText(msg.getString("Delete"));
        idLabel.setText(msg.getString("CustID"));
        nameLabel.setText(msg.getString("CustName") + ": ");
        addressLabel.setText(msg.getString("CustAdd") + ": ");
        postalLabel.setText(msg.getString("CustPost") + ": ");
        phoneLabel.setText(msg.getString("CustPhone"));
        countryLabel.setText(msg.getString("custCountry") + ": ");
        firstLevelLabel.setText(msg.getString("CustFirstLevel") + ": ");
    }

    /**
     * Gets an instance of the DBAccessor for various operations and calls localize() to translate the GUI
     */
    @FXML private void initialize() {
        dba = DBAccessor.getInstance();
        msg = dba.getMsg();
        localize();
    }
}
