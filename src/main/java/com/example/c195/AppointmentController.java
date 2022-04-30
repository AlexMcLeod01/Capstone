package com.example.c195;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.ResourceBundle;

import static java.time.temporal.ChronoUnit.HOURS;

/**
 * This is the controller for the appointment records view
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class AppointmentController {
    //Model stuff
    private DBAccessor dba;
    private ResourceBundle msg;
    private StageSwitcher switcher;
    private ObservableList<Appointments> appointments;


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
    @FXML private Label eDateLabel;
    @FXML private Label eTimeLabel;
    @FXML private Label customerLabel;
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
    @FXML private DatePicker eDateSelector;

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
        eDateSelector.setDisable(disabled);
        eTimeCombo.setDisable(disabled);
        customerCombo.setDisable(disabled);
        saveButton.setDisable(disabled);
    }

    /**
     * Populate the combo boxes with data from database
     * And the datepickers with dates today forward
     * Implemented through a lambda function because
     * the callback function is quite messy otherwise
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
        sTimeCombo.getItems().clear();
        sTimeCombo.getItems().addAll(dba.getTimes());
        eTimeCombo.getItems().clear();
        eTimeCombo.getItems().addAll(dba.getTimes());
        //Disable unsuitable (past) dates for appointments
        sDateSelector.setDayCellFactory(appointmentDayPicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // < 0 to put all appointments starting at least today
                setDisable(empty || date.compareTo(LocalDate.now()) < 0);
            }
        });
        eDateSelector.setDayCellFactory(appointmentDayPicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // < 0 to put all appointments starting at least today
                setDisable(empty || date.compareTo(LocalDate.now()) < 0);
            }
        });
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
     * Sets the fields to match the selected appointment
     */
    @FXML private void setFields() {
        Appointments selected = appointTable.getSelectionModel().getSelectedItem();
        appointmentField.setText(Integer.toString(selected.getAppointmentID()));
        titleField.setText(selected.getTitle());
        descriptionField.setText(selected.getDescription());
        locationField.setText(selected.getLocation());
        typeField.setText(selected.getType());
        contactCombo.setValue(dba.getContactByID(selected.getContactID()).getName());
        sDateSelector.setValue(LocalDate.parse(selected.getStart().substring(0, 10), DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        sTimeCombo.setValue(LocalTime.parse(selected.getStart().substring(11)));
        eDateSelector.setValue(LocalDate.parse(selected.getEnd().substring(0, 10), DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        eTimeCombo.setValue(LocalTime.parse(selected.getEnd().substring(11)));
        customerCombo.setValue(dba.getCustomerByID(selected.getCustomerID()).getName());

    }

    /**
     * Clears the fields
     */
    @FXML private void clearFields() {
        appointmentField.clear();
        titleField.clear();
        titleField.setPromptText(null);
        descriptionField.clear();
        descriptionField.setPromptText(null);
        locationField.clear();
        locationField.setPromptText(null);
        typeField.clear();
        typeField.setPromptText(null);
        contactCombo.getItems().clear();
        contactCombo.setPromptText(null);
        sDateSelector.setValue(null);
        sDateSelector.setPromptText(null);
        sTimeCombo.getItems().clear();
        sTimeCombo.setPromptText(null);
        eDateSelector.setValue(null);
        eDateSelector.setPromptText(null);
        eTimeCombo.getItems().clear();
        eTimeCombo.setPromptText(null);
        customerCombo.getItems().clear();
        customerCombo.setPromptText(null);
    }

    /**
     * The user has clicked Update button, so turn on form
     * and fill it out with selected appointment
     */
    @FXML private void updateClicked() {
        disableFields(false);
        populateCombos();
        setFields();
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
     * Validates text fields
     * @param field a TextField object
     * @return true if valid, false if not
     */
    @FXML private boolean validText(TextField field) {
        if (field.getText().isEmpty()) {
            field.setPromptText(msg.getString("Required"));
            return false;
        }
        return true;
    }

    /**
     * Validates combo boxes
     * @param combo a ComboBox object
     * @return true if a selection was made
     */
    @FXML private boolean validCombo(ComboBox combo) {
        if (combo.getSelectionModel().isEmpty()) {
            combo.setPromptText(msg.getString("Required"));
            return false;
        }
        return true;
    }

    /**
     * Make sure that appointment times fall within specs
     * @return true if appointment is acceptable
     */
    @FXML private boolean validateDateTime() {
        boolean valid = true;
        boolean timeValid = true;
        if (!validText(typeField)) {
            valid = false;
            timeValid = false;
        }
        if (sDateSelector.getValue() == null) {
            sDateSelector.setPromptText(msg.getString("Required"));
            valid = false;
            timeValid = false;
        }
        if (!validCombo(sTimeCombo)) {
            valid = false;
            timeValid = false;
        }
        if (eDateSelector.getValue() == null) {
            eDateSelector.setPromptText(msg.getString("Required"));
            valid = false;
            timeValid = false;
        }
        if (!validCombo(eTimeCombo)) {
            valid = false;
            timeValid = false;
        }
        if (timeValid) {
            LocalDateTime begin = sDateSelector.getValue().atTime((LocalTime) sTimeCombo.getSelectionModel().getSelectedItem());
            ZonedDateTime beginZoned = begin.atZone(dba.getZone());
            ZonedDateTime beginEST = beginZoned.withZoneSameInstant(ZoneId.of("America/New_York"));
            LocalDateTime end = eDateSelector.getValue().atTime((LocalTime) eTimeCombo.getSelectionModel().getSelectedItem());
            ZonedDateTime endZoned = end.atZone(dba.getZone());
            ZonedDateTime endEST = endZoned.withZoneSameInstant(ZoneId.of("America/New_York"));
            if (begin.isAfter(end)) {
                eDateSelector.setValue(null);
                eDateSelector.setPromptText(msg.getString("MustBeAfter"));
                eDateSelector.setStyle("color: red; -fx-font-weight: bold;");
                eTimeCombo.getSelectionModel().clearSelection();
                eTimeCombo.setPromptText(msg.getString("AfterStartTime"));
                eTimeCombo.setStyle("color: red; -fx-font-weight: bold;");
                valid = false;
            }
            if (end.minus(14, HOURS).isAfter(begin)) {
                System.out.println("Too Long");
                valid = false;
            } else if (beginEST.getHour() < 8 || beginEST.getHour() > 21) {
                sDateSelector.setValue(null);
                sDateSelector.setPromptText(msg.getString("OutsideHours"));
                valid = false;
            } else if (endEST.getHour() < 9 || endEST.getHour() > 22) {
                eDateSelector.setValue(null);
                eDateSelector.setPromptText(msg.getString("OutsideHours"));
                valid = false;
            }
        }
        return valid;
    }

    /**
     * Validates the form information before sending to database
     * @return true if form information is valid
     */
    @FXML private boolean validateInput() {
        boolean valid = true;
        if (!validText(titleField)) {
            valid = false;
        }
        if (!validText(descriptionField)) {
            valid = false;
        }
        if (!validText(locationField)) {
            valid = false;
        }
        if (!validCombo(contactCombo)) {
            valid = false;
        }
        if (!validateDateTime()) {
            valid = false;
        }
        if (customerCombo.getValue() == null) {
            customerCombo.setPromptText(msg.getString("Required"));
            valid = false;
        }
        return valid;
    }

    /**
     * This method saves the form data when clicked, if form data is valid, or error message is displayed if not
     */
    @FXML private void saveClicked() {
        if(validateInput()) {
            //saveData();
            clearFields();
            disableFields(true);
        }
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
        eDateLabel.setText(msg.getString("EndDate") + ":");
        eTimeLabel.setText(msg.getString("EndTime") + ":");
        customerLabel.setText(msg.getString("CustID") + ":");
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
        typeCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("type"));
        sDateTimeCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("start"));
        eDateTimeCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("end"));
        custIdCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("customerID"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("userID"));
        contactCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("contactID"));
        appointTable.getItems().setAll(appointments);
        //Fix prompt text visibility for combo boxes
        eTimeCombo.setButtonCell(new ListCell<LocalTime>() {
            @Override
            protected void updateItem(LocalTime item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(eTimeCombo.getPromptText());
                } else {
                    setText(item.toString());
                }
            }
        });
    }
}
