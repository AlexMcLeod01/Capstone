package com.example.c195;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * This class controls the pop up window: ConfirmAppointmentDelete
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class ConfirmAppointmentDeleteController {
    private DBAccessor dba;
    private AppointmentsDatabaseAccessor ada;
    private ResourceBundle msg;

    //Stuff for the pop up
    //Buttons for the pop up
    @FXML
    private Button confirmButton;
    @FXML private Button cancelButton;
    //Labels
    @FXML private Label confirmLabel;

    /**
     * This method deletes the customer selected
     */
    @FXML private void confirmClicked() {
        ada.deleteAppointment(ada.getSelectedAppointment());
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Closes out of window without deleting Appointment
     */
    @FXML private void cancelClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    /**
     * Initializes all the data to desired values
     */
    @FXML private void initialize() {
        //Get instance of DBAccessor and the ResourceBundle
        dba = DBAccessor.getInstance();
        ada = AppointmentsDatabaseAccessor.getInstance();
        msg = dba.getMsg();

        //Localize messages
        confirmButton.setText(msg.getString("Confirm"));
        cancelButton.setText(msg.getString("Cancel"));
        confirmLabel.setText(msg.getString("SureDeleteAppointment") + "\nAppointment ID: " + ada.getSelectedAppointment().getAppointmentID()
            + "\nType: " + ada.getSelectedAppointment().getType());
    }
}
