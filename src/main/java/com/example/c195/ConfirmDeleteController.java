package com.example.c195;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * A controller for the Confirm Delete pop up window
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class ConfirmDeleteController {
    DBAccessor dba;
    CustomerDatabaseAccessor cda;
    ResourceBundle msg;

    //Stuff for the pop up
    //Buttons for the pop up
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;
    //Labels
    @FXML private Label confirmLabel;
    @FXML private Label warningLabel;

    /**
     * This method deletes the customer selected
     */
    @FXML private void confirmClicked() {
        cda.deleteCustomer(cda.getSelectedCustomer());
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    /**
     * This method closes the pop up without deleting
     */
    @FXML private void cancelClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    @FXML private void initialize() {
        dba = DBAccessor.getInstance();
        cda = CustomerDatabaseAccessor.getInstance();
        msg = dba.getMsg();

        confirmButton.setText(msg.getString("Confirm"));
        cancelButton.setText(msg.getString("Cancel"));
        confirmLabel.setText(msg.getString("SureDelete"));
        warningLabel.setText(msg.getString("WarnDelete"));
        User user = dba.getCurrentUser();
        if (user instanceof Rep) {
            confirmButton.setVisible(false);
            confirmLabel.setText("You are not authorized to delete Customer Records");
        }
    }
}
