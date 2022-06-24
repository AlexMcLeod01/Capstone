package com.example.c195;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
 * This class controls the Customer Records window
 * <p>
 * For the ChangeListener listener:
 * Lambda was the clearest way to make a listener for the single listener that was needed,
 * and I didn't want to change the declaration when I made it a class-level private variable
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class CustomerController {
    /**
     * Declaring the various resources needed
     */
    private DBAccessor dba;
    private CustomerDatabaseAccessor cda;
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
     * A ChangeListener for the Country ComboBox. It was originally in the setUpDropMenu()
     * function, but was needed in the clearForm() function as well to remove errors
     * At the time, lambda was the clearest way to make a listener, and I didn't want to change
     * the declaration when I made it a class-level private variable
     */
    private final ChangeListener listener = (observableValue, old, now) -> {
        int cty = 0;
        firstLevelCombo.getItems().clear();
        for (Countries c : cda.getCountries()) {
            if (now.equals(c.getName())) {
                cty = c.getID();
            }
        }
        for (Divisions d : cda.getDivisions()) {
            if (d.getCountryID() == cty) {
                firstLevelCombo.getItems().add(d.getName());
            }
        }
    };

    /**
     * This is a helper function to set up the drop menu in the add customer functionality
     */
    private void setUpDropMenu() {
        //Populate Combo Boxes
        countryCombo.getItems().clear();
        for (Countries c : cda.getCountries()) {
            countryCombo.getItems().add(c.getName());
        }
        countryCombo.getSelectionModel().selectedItemProperty().addListener(listener);
    }

    /**
     * This method activates the form below the table with nothing in the fields
     */
    @FXML private void addClicked() {
        idField.setText(Integer.toString(cda.getNewCustomerID()));
        nameField.setDisable(false);
        addressField.setDisable(false);
        postalField.setDisable(false);
        phoneField.setDisable(false);
        countryCombo.setDisable(false);
        firstLevelCombo.setDisable(false);
        setUpDropMenu();
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
        setUpDropMenu();
        setFormState();
    }

    /**
     * Deletes the Customer record if there are no attached appointments
     */
    @FXML private void deleteClicked() {
        cda.setSelectedCustomer(custTable.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(C195App.class.getResource("ConfirmDelete.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(msg.getString("ConfirmDelete"));
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        customers = cda.getAllCustomers();
        custTable.getItems().setAll(customers);
    }

    /**
     * Checks the Customer add/update form to see if it's filled out
     * @return true if everything is filled out
     */
    private boolean isFormValid() {
        boolean valid = true;
        if (nameField.getText().isEmpty()) {
            nameField.setPromptText(msg.getString("Required"));
            valid = false;
        }
        if (addressField.getText().isEmpty()) {
            addressField.setPromptText(msg.getString("Required"));
            valid = false;
        }
        if (postalField.getText().isEmpty()) {
            postalField.setPromptText(msg.getString("Required"));
            valid = false;
        }
        if (phoneField.getText().isEmpty()) {
            phoneField.setPromptText(msg.getString("Required"));
            valid = false;
        }
        if (firstLevelCombo.getSelectionModel().isEmpty()) {
            firstLevelCombo.setPromptText(msg.getString("Required"));
            valid = false;
        }
        if (countryCombo.getSelectionModel().isEmpty()) {
            countryCombo.setPromptText(msg.getString("Required"));
            valid = false;
        }
        return valid;
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

    private void clearForm() {
        nameField.clear();
        nameField.setDisable(true);
        addressField.clear();
        addressField.setDisable(true);
        postalField.clear();
        postalField.setDisable(true);
        phoneField.clear();
        phoneField.setDisable(true);
        countryCombo.setDisable(true);
        firstLevelCombo.setDisable(true);
        countryCombo.getSelectionModel().selectedItemProperty().removeListener(listener);
        countryCombo.getItems().clear();
        firstLevelCombo.getItems().clear();
        saveButton.setDisable(true);
    }

    /**
     * This is a helper function to search through the customers on the table
     * @param customerID id to search for
     * @return a customer object of the original customer of that ID
     */
    private Customer searchCustomers(int customerID) {
        Customer customer = new Customer(-1, "a", "a", "a", "a", "a", "a");
        for (Customer allCustomers : this.customers) {
            if (allCustomers.getID() == customerID) {
                customer = allCustomers;
            }
        }
        return customer;
    }

    /**
     * Saves the Customer record if no errors in the fields
     */
    @FXML private void saveClicked() {
        if (isFormValid()) {
            Customer customer = getFormState();
            Customer customerOrig = searchCustomers(customer.getID());
            if(customerOrig.getID() != -1 && customers.contains(customerOrig)) {
                cda.updateCustomer(customerOrig, customer);
            } else {
                cda.addCustomer(customer);
            }
            customers = cda.getAllCustomers();
            custTable.getItems().setAll(customers);
            clearForm();
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
     * Gets an instance of the DBAccessor for various operations and calls localize() to translate the GUI
     */
    @FXML private void initialize() {
        dba = DBAccessor.getInstance();
        cda = CustomerDatabaseAccessor.getInstance();
        msg = dba.getMsg();
        localize();
        switcher = StageSwitcher.getInstance();
        customers = cda.getAllCustomers();
        //Populate Customer Table
        custIDCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("ID"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        custAddCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        custPostCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("postCode"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        custFLDCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("division"));
        custCountryCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));
        custTable.getItems().setAll(customers);
    }
}
