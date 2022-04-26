package com.example.c195;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * This is the controller for the appointment records view
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class AppointmentController {
    //Model stuff
    DBAccessor dba;
    ResourceBundle msg;
    StageSwitcher switcher;
    ObservableList<Appointments> appointments;

    //FXML stuff
    //Table View
    @FXML private TableView<Appointments> appointTable;

    //Table Columns
    @FXML private TableColumn appointIdCol;
    @FXML private TableColumn titleCol;
    @FXML private TableColumn descrCol;
    @FXML private TableColumn locCol;
    @FXML private TableColumn contactCol;
    @FXML private TableColumn typeCol;
    @FXML private TableColumn sDateTimeCol;
    @FXML private TableColumn eDateTimeCol;
    @FXML private TableColumn custIdCol;
    @FXML private TableColumn userIdCol;

    //Labels
    @FXML private Label appointmentLabel;
    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label locationLabel;
    @FXML private Label contactLabel;
    @FXML private Label typeLabel;
    @FXML private Label sDateLabel;
    @FXML private Label sTimeLabel;
    @FXML private Label eTimeLabel;
    @FXML private Label customerLabel;
    @FXML private Label userLabel;
    @FXML private Label headingLabel;

    //Text Fields
    @FXML private TextField appointmentField;
    @FXML private TextField titleField;
    @FXML private TextField descriptionField;
    @FXML private TextField locationField;
    @FXML private TextField typeField;

    //Combo Boxes
    @FXML private ComboBox contactCombo;
    @FXML private ComboBox sTimeCombo;
    @FXML private ComboBox eTimeCombo;
    @FXML private ComboBox customerCombo;
    //@FXML private ComboBox userCombo;

    //Date Picker
    @FXML private DatePicker sDateSelector;

    //Buttons
    @FXML private Button backButton;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button saveButton;

    /**
     * This method switches stages back to the main view when back button is clicked
     */
    @FXML private void backClicked() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        switcher.SwitchStage(stage, "AppointmentManagementSystem.fxml", msg.getString("MainTitle"));
    }

    /**
     * This method sets the form to enabled (false) or disabled (true) based on param
     * @param disabled true if want disabled
     */
    @FXML private void disableFields(boolean disabled) {
        titleField.setDisable(disabled);
        descriptionField.setDisable(disabled);
        locationField.setDisable(disabled);
        typeField.setDisable(disabled);
        contactCombo.setDisable(disabled);
        sDateSelector.setDisable(disabled);
        sTimeCombo.setDisable(disabled);
        eTimeCombo.setDisable(disabled);
        customerCombo.setDisable(disabled);
        //userCombo.setDisable(disabled);
        saveButton.setDisable(disabled);
    }

    /**
     * Populate the combo boxes with data from database
     */
    @FXML private void populateCombos() {
        ObservableList<Contact> contacts = dba.getContactList();
        contactCombo.getItems().clear();
        for (Contact c : contacts)
            contactCombo.getItems().add(c.getName());
        ObservableList<Customer> customers = dba.getAllCustomers();
        customerCombo.getItems().clear();
        for (Customer c : customers)
            customerCombo.getItems().add(c.getName());
    }

    /**
     * The user has clicked Add button, so turn on form
     */
    @FXML private void addClicked() {
        appointmentField.setText(Integer.toString(dba.getNewAppointmentID()));
        disableFields(false);
        populateCombos();
    }

    /**
     * The user has clicked Update button, so turn on form
     * and fill it out with selected appointment
     */
    @FXML private void updateClicked() {
        disableFields(false);
    }

    @FXML private void deleteClicked() {
        dba.setSelectedAppointment(appointTable.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(C195App.class.getResource("ConfirmAppointmentDelete.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(msg.getString("ConfirmDelete"));
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        appointments = dba.getAllAppointments();
        appointTable.getItems().setAll(appointments);
    }

    /**
     * This method saves the form data when clicked, if form data is valid, or error message is displayed if not
     */
    @FXML private void saveClicked() {
        disableFields(true);
    }

    /**
     * This method loads the interface in either English or French with English as default
     */
    @FXML private void localize() {
        backButton.setText(msg.getString("Back"));
        addButton.setText(msg.getString("Add"));
        updateButton.setText(msg.getString("Update"));
        deleteButton.setText(msg.getString("Delete"));
        saveButton.setText(msg.getString("Save"));
        custIdCol.setText(msg.getString("CustID"));
        appointIdCol.setText(msg.getString("AppointID"));
        titleCol.setText(msg.getString("Title"));
        descrCol.setText(msg.getString("Description"));
        locCol.setText(msg.getString("Location"));
        contactCol.setText(msg.getString("Contact"));
        typeCol.setText(msg.getString("Type"));
        sDateTimeCol.setText(msg.getString("StartDateTime"));
        eDateTimeCol.setText(msg.getString("EndDateTime"));
        userIdCol.setText(msg.getString("UserID"));
        appointmentLabel.setText(msg.getString("AppointID") + ":");
        titleLabel.setText(msg.getString("Title") + ":");
        descriptionLabel.setText(msg.getString("Description") + ":");
        locationLabel.setText(msg.getString("Location") + ":");
        contactLabel.setText(msg.getString("Contact") + ":");
        typeLabel.setText(msg.getString("Type") + ":");
        sDateLabel.setText(msg.getString("StartDate") + ":");
        sTimeLabel.setText(msg.getString("StartTime") + ":");
        eTimeLabel.setText(msg.getString("EndTime") + ":");
        customerLabel.setText(msg.getString("CustID") + ":");
        //userLabel.setText(msg.getString("UserID") + ":");
        headingLabel.setText(msg.getString("AppointHeading"));
    }
    /**
     * This method initializes data that needs to be accessed
     */
    @FXML private void initialize() {
        dba = DBAccessor.getInstance();
        msg = dba.getMsg();
        switcher = StageSwitcher.getInstance();
        localize();
        appointments = dba.getAllAppointments();
        //Setup TableView
        appointIdCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("title"));
        descrCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("description"));
        locCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("location"));
        sDateTimeCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("start"));
        eDateTimeCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("end"));
        custIdCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("customerID"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("userID"));
        contactCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("contactID"));
        appointTable.getItems().setAll(appointments);
    }
}
