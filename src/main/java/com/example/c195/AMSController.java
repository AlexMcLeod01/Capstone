package com.example.c195;


import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * This class controls the AppointmentManagementSystem view
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class AMSController {
    private ResourceBundle msg;
    private StageSwitcher switcher;
    private ObservableList<Appointments> weekAppoint;

    //Declare FXML elements
    @FXML private Button exitButton;
    @FXML private Button appointButton;
    @FXML private Button customerButton;
    @FXML private Button reportButton;

    @FXML private Label mainHeader;
    @FXML private Label alert;

    @FXML private Tab monthTab;
    @FXML private Tab weekTab;

    //Table and Column Information for Month Tab
    @FXML private TableView<Appointments> appointmentTable;

    @FXML private TableColumn<Appointments, String> appointCol;
    @FXML private TableColumn<Appointments, String> titleCol;
    @FXML private TableColumn<Appointments, String> descrCol;
    @FXML private TableColumn<Appointments, String> locCol;
    @FXML private TableColumn<Appointments, String> contactCol;
    @FXML private TableColumn<Appointments, String> typeCol;
    @FXML private TableColumn<Appointments, String> sDateTimeCol;
    @FXML private TableColumn<Appointments, String> eDateTimeCol;
    @FXML private TableColumn<Appointments, String> custCol;
    @FXML private TableColumn<Appointments, String> userCol;

    //Table and Column Information for Week Tab
    @FXML private TableView<Appointments> appointmentTable1;

    @FXML private TableColumn<Appointments, String> appointCol1;
    @FXML private TableColumn<Appointments, String> titleCol1;
    @FXML private TableColumn<Appointments, String> descrCol1;
    @FXML private TableColumn<Appointments, String> locCol1;
    @FXML private TableColumn<Appointments, String> contactCol1;
    @FXML private TableColumn<Appointments, String> typeCol1;
    @FXML private TableColumn<Appointments, String> sDateTimeCol1;
    @FXML private TableColumn<Appointments, String> eDateTimeCol1;
    @FXML private TableColumn<Appointments, String> custCol1;
    @FXML private TableColumn<Appointments, String> userCol1;

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
     * This method changes the screen to the Reports screen
     */
    @FXML
    private void reportClicked() {
        Stage stage = (Stage) reportButton.getScene().getWindow();
        switcher.SwitchStage(stage, "Reports.fxml", msg.getString("MainReport"));
    }

    private void checkUpcoming() {
        for (Appointments a : weekAppoint) {
            LocalDateTime start = LocalDateTime.parse(a.getStart());
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(now, start);
            if (duration.toMinutes() <= 15 && duration.toMinutes() >= -15) {
                alert.setText(msg.getString("UpcomingAlert") + "\nAppointment ID: " + a.getAppointmentID() + " Date: " +
                        LocalDate.parse(a.getStart().substring(0,10)) + " Time: " + LocalTime.parse(a.getStart().substring(11)));
            }
        }
    }

    /**
     * This method localizes the view language
     */
    @FXML
    private void localize() {
        //Buttons
        exitButton.setText(msg.getString("MainExit"));
        appointButton.setText(msg.getString("MainAppoint"));
        customerButton.setText(msg.getString("MainCustomer"));
        reportButton.setText(msg.getString("MainReport"));
        mainHeader.setText(msg.getString("MainTitle"));

        //Tab 1 Table and Columns
        monthTab.setText(msg.getString("Month"));
        appointCol.setText(msg.getString("Appointment"));
        titleCol.setText(msg.getString("Title"));
        descrCol.setText(msg.getString("Description"));
        locCol.setText(msg.getString("Location"));
        contactCol.setText(msg.getString("Contact"));
        typeCol.setText(msg.getString("Type"));
        sDateTimeCol.setText(msg.getString("StartDateTime"));
        eDateTimeCol.setText(msg.getString("EndDateTime"));
        custCol.setText(msg.getString("CustID"));
        userCol.setText(msg.getString("UserID"));
        alert.setText(msg.getString("MainAlert"));

        //Tab 2 Table and Columns
        weekTab.setText(msg.getString("Week"));
        appointCol1.setText(msg.getString("Appointment"));
        titleCol1.setText(msg.getString("Title"));
        descrCol1.setText(msg.getString("Description"));
        locCol1.setText(msg.getString("Location"));
        contactCol1.setText(msg.getString("Contact"));
        typeCol1.setText(msg.getString("Type"));
        sDateTimeCol1.setText(msg.getString("StartDateTime"));
        eDateTimeCol1.setText(msg.getString("EndDateTime"));
        custCol1.setText(msg.getString("CustID"));
        userCol1.setText(msg.getString("UserID"));

        //Alert Label
        alert.setText(msg.getString("MainAlert"));
    }

    /**
     * This method initializes data that needs to be accessed
     */
    @FXML
    private void initialize() {
        DBAccessor dba = DBAccessor.getInstance();
        AppointmentsDatabaseAccessor ada = AppointmentsDatabaseAccessor.getInstance();
        msg = dba.getMsg();
        localize();
        switcher = StageSwitcher.getInstance();
        weekAppoint = ada.getWeekAppointments();
        ObservableList<Appointments> monthAppoint = ada.getMonthAppointments();

        //Set up Table 1
        appointCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descrCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        sDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        eDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        custCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        appointmentTable.getItems().setAll(monthAppoint);

        //Set up Table 2
        appointCol1.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol1.setCellValueFactory(new PropertyValueFactory<Appointments, String>("title"));
        descrCol1.setCellValueFactory(new PropertyValueFactory<>("description"));
        locCol1.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol1.setCellValueFactory(new PropertyValueFactory<>("type"));
        sDateTimeCol1.setCellValueFactory(new PropertyValueFactory<>("start"));
        eDateTimeCol1.setCellValueFactory(new PropertyValueFactory<>("end"));
        custCol1.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userCol1.setCellValueFactory(new PropertyValueFactory<>("userID"));
        contactCol1.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        appointmentTable1.getItems().setAll(weekAppoint);

        checkUpcoming();
    }
}
