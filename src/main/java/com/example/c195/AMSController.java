package com.example.c195;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * This class controls the AppointmentManagementSystem view
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class AMSController {
    private DBAccessor dba;
    private ResourceBundle msg;
    private StageSwitcher switcher;

    //Declare FXML elements
    @FXML private Button exitButton;
    @FXML private Button appointButton;
    @FXML private Button customerButton;
    @FXML private Button reportButton;
    @FXML private Label mainHeader;
    @FXML private TableColumn timeCol;
    @FXML private TableColumn dateCol;
    @FXML private TableColumn appointCol;
    @FXML private Label alert;

    /**
     * This method exits the application upon click
     */
    @FXML
    private void exitClicked() {
        Platform.exit();
    }

    /**
     * This method changes the screen to the Customer records screen when
     * the Customers button is clicked
     */
    @FXML
    private void customerClicked() {
        Stage stage = (Stage) customerButton.getScene().getWindow();
        switcher.SwitchStage(stage, "CustomerRecords.fxml", msg.getString("CustomerTitle"));
    }

    /**
     * This method changes the screen to the Appointment Records screen
     * when the Appointments button is clicked
     */
    @FXML
    private void appointmentClicked() {
        Stage stage = (Stage) appointButton.getScene().getWindow();
        switcher.SwitchStage(stage, "AppointmentRecords.fxml", msg.getString("AppointmentTitle"));
    }

    /**
     * This method localizes the view language
     */
    @FXML
    private void localize() {
        exitButton.setText(msg.getString("MainExit"));
        appointButton.setText(msg.getString("MainAppoint"));
        customerButton.setText(msg.getString("MainCustomer"));
        reportButton.setText(msg.getString("MainReport"));
        mainHeader.setText(msg.getString("MainTitle"));
        timeCol.setText(msg.getString("Time"));
        dateCol.setText(msg.getString("Date"));
        appointCol.setText(msg.getString("Appointment"));
        alert.setText(msg.getString("MainAlert"));
    }

    /**
     * This method initializes data that needs to be accessed
     */
    @FXML
    private void initialize() {
        dba = DBAccessor.getInstance();
        msg = dba.getMsg();
        localize();
        switcher = StageSwitcher.getInstance();
    }
}
