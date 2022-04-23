package com.example.c195;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * This class controls the Customer Records window
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class CustomerController {
    /**
     * Declaring the various resources needed
     */
    private DBAccessor dba;
    private ResourceBundle msg;
    private StageSwitcher switcher;
    private ObservableList<Customer> customers;

    /**
     * declare various FXML elements
     */
    //The Table
    @FXML private TableView<Customer> custTable;
    //Then column names
    @FXML private TableColumn<Customer, String> custIDCol;
    @FXML private TableColumn<Customer, String> custNameCol;
    @FXML private TableColumn<Customer, String> custAddCol;
    @FXML private TableColumn<Customer, String> custPostCol;
    @FXML private TableColumn<Customer, String> custFLDCol;
    @FXML private TableColumn<Customer, String> custPhoneCol;
    @FXML private TableColumn<Customer, String> custCountryCol;
    //Then Buttons
    @FXML private Button backButton;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button delButton;
    @FXML private Button saveButton;
    //Then Labels
    @FXML private Label custHTitle;
    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label addressLabel;
    @FXML private Label postalLabel;
    @FXML private Label phoneLabel;
    @FXML private Label countryLabel;
    @FXML private Label firstLevelLabel;
    //Then Text Fields
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField postalField;
    @FXML private TextField phoneField;
    //Combo Boxes
    @FXML private ComboBox countryCombo;
    @FXML private ComboBox firstLevelCombo;

    /**
     * This method switches the scene from current back to the AppointmentManagementSystem view
     */
    @FXML private void backClicked() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        switcher.SwitchStage(stage, "AppointmentManagementSystem.fxml", msg.getString("MainTitle"));
    }

    /**
     * This method activates the form below the table with nothing in the fields
     */
    @FXML private void addClicked() {
        idField.setText(Integer.toString(dba.getNewCustomerID()));
        nameField.setDisable(false);
        addressField.setDisable(false);
        postalField.setDisable(false);
        phoneField.setDisable(false);
        countryCombo.setDisable(false);
        firstLevelCombo.setDisable(false);
        saveButton.setDisable(false);
    }

    /**
     * This method sets the form state based upon selected row in custTable
     */
    private void setFormState() {
        Customer customer = custTable.getSelectionModel().getSelectedItem();
        idField.setText(Integer.toString(customer.getID()));
        nameField.setText(customer.getName());
        addressField.setText(customer.getAddress());
        postalField.setText(customer.getPostCode());
        phoneField.setText(customer.getPhone());
        countryCombo.setValue(customer.getCountry());
        firstLevelCombo.setValue(customer.getDivision());
    }

    /**
     * This method activates the form below the table with
     * the highlighted customer info filled in the fields
     */
    @FXML private void updateClicked() {
        nameField.setDisable(false);
        addressField.setDisable(false);
        postalField.setDisable(false);
        phoneField.setDisable(false);
        countryCombo.setDisable(false);
        firstLevelCombo.setDisable(false);
        saveButton.setDisable(false);
        setFormState();
    }

    /**
     * Deletes the Customer record if there are no attached appointments
     */
    @FXML private void deleteClicked() {
        Customer customer = custTable.getSelectionModel().getSelectedItem();
        if (true) { //This if statement should be a call to a pop-up asking for affirmation
            dba.deleteCustomer(customer);
            customers = dba.getAllCustomers();
            custTable.getItems().setAll(customers); //Additional statement should be added to update the database
        }
    }

    /**
     * Checks the Customer add/update form to see if it's filled out
     * @return true if everything is filled out
     */
    private boolean isFormValid() {
        if (nameField.getText().isEmpty()) {
            nameField.setPromptText(msg.getString("Required"));
            return false;
        }
        if (addressField.getText().isEmpty()) {
            addressField.setPromptText(msg.getString("Required"));
            return false;
        }
        if (postalField.getText().isEmpty()) {
            postalField.setPromptText(msg.getString("Required"));
            return false;
        }
        if (phoneField.getText().isEmpty()) {
            phoneField.setPromptText(msg.getString("Required"));
            return false;
        }
        if (firstLevelCombo.getSelectionModel().isEmpty()) {
            firstLevelCombo.setPromptText(msg.getString("Required"));
            return false;
        }
        if (countryCombo.getSelectionModel().isEmpty()) {
            countryCombo.setPromptText(msg.getString("Required"));
            return false;
        }
        return true;
    }

    private Customer getFormState() {
        Customer customer;
        int ID = Integer.parseInt(idField.getText());
        String name = nameField.getText();
        String addr = addressField.getText();
        String post = postalField.getText();
        String phone = phoneField.getText();
        String div = firstLevelCombo.getValue().toString();
        String country = countryCombo.getValue().toString();
        customer = new Customer(ID, name, addr, post, phone, div, country);
        return customer;
    }

    /**
     * Saves the Customer record if no errors in the fields
     */
    @FXML private void saveClicked() {
        if (isFormValid()) {
            Customer customer = getFormState();
            dba.addCustomer(customer);
            customers = dba.getAllCustomers();
            custTable.getItems().setAll(customers);
        }
    }

    /**
     * This method translates the GUI elements into French or English
     */
    @FXML private void localize() {
        custHTitle.setText(msg.getString("CustRec"));
        backButton.setText(msg.getString("Back"));
        addButton.setText(msg.getString("Add"));
        updateButton.setText(msg.getString("Update"));
        delButton.setText(msg.getString("Delete"));
        idLabel.setText(msg.getString("CustID") + ": ");
        nameLabel.setText(msg.getString("CustName") + ": ");
        addressLabel.setText(msg.getString("CustAdd") + ": ");
        postalLabel.setText(msg.getString("CustPost") + ": ");
        phoneLabel.setText(msg.getString("CustPhone"));
        countryLabel.setText(msg.getString("CustCountry") + ": ");
        firstLevelLabel.setText(msg.getString("CustFirstLevel") + ": ");
        custIDCol.setText(msg.getString("CustID"));
        custNameCol.setText(msg.getString("CustName"));
        custAddCol.setText(msg.getString("Address"));
        custPostCol.setText(msg.getString("CustPost"));
        custFLDCol.setText(msg.getString("CustFirstLevel"));
        custPhoneCol.setText(msg.getString("CustPhone"));
        custCountryCol.setText(msg.getString("CustCountry"));
        saveButton.setText(msg.getString("Save"));
    }

    /**
     * This is a helper function to set up the drop menu in the add customer functionality
     * Uses a lambda function inside the .addListener() call to create a new ChangeListener
     */
    private void setUpDropMenuListener() {
        //Populate Combo Boxes
        countryCombo.getItems().clear();
        countryCombo.getItems().addAll("USA", "France", "Canada");
        countryCombo.getSelectionModel().selectedItemProperty().addListener((observableValue, old, now) -> {
            if(now.equals("USA")) {
                firstLevelCombo.getItems().clear();
                firstLevelCombo.getItems().addAll("Florida", "Utah", "Washington", "Oregon", "Texas", "New York", "California");
            } else if (now.equals("France")) {
                firstLevelCombo.getItems().clear();
                firstLevelCombo.getItems().addAll("Brittany", "Normandy", "Occitania", "Centre-Val de Loire");
            } else {
                firstLevelCombo.getItems().clear();
                firstLevelCombo.getItems().addAll("Ontario", "Quebec", "New Brunswick",  "Nova Scotia");
            }
        });
    }

    /**
     * Gets an instance of the DBAccessor for various operations and calls localize() to translate the GUI
     */
    @FXML private void initialize() {
        dba = DBAccessor.getInstance();
        msg = dba.getMsg();
        localize();
        switcher = StageSwitcher.getInstance();
        customers = dba.getAllCustomers();
        //Populate Customer Table
        custIDCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("ID"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        custAddCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        custPostCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("postCode"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        custFLDCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("division"));
        custCountryCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));
        custTable.getItems().setAll(customers);
        setUpDropMenuListener();
    }
}
