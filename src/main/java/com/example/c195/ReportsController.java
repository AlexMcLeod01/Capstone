package com.example.c195;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * This class controls the Reports.fxml view
 * Primary purpose of this view is to generate
 * and display reports
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class ReportsController {
    //Begin with the model and view resources
    private ResourceBundle msg;
    private StageSwitcher switcher;

    //FX items
    //Button
    @FXML private Button backButton;

    //TableViews
    @FXML private TableView<TypeReport> totalTypeReportTable;
    @FXML private TableView<Appointments> contactScheduleTable;
    @FXML private TableView<UserReport> userModReportTable;

    //TableColumns
    @FXML private TableColumn<TypeReport, String> typeReportTypeCol;
    @FXML private TableColumn<TypeReport, String> typeReportMonthCol;
    @FXML private TableColumn<TypeReport, String> typeReportNumCol;
    @FXML private TableColumn<Appointments, String> contactIDCol;
    @FXML private TableColumn<Appointments, String> appointIDCol;
    @FXML private TableColumn<Appointments, String> titleCol;
    @FXML private TableColumn<Appointments, String> typeCol;
    @FXML private TableColumn<Appointments, String> descrCol;
    @FXML private TableColumn<Appointments, String> startCol;
    @FXML private TableColumn<Appointments, String> endCol;
    @FXML private TableColumn<Appointments, String> custID;
    @FXML private TableColumn<UserReport, String> userIDCol;
    @FXML private TableColumn<UserReport, String> userTypeCol;
    @FXML private TableColumn<UserReport, String> userMonthCol;
    @FXML private TableColumn<UserReport, String> userNumCol;


    @FXML private void backClicked() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        switcher.SwitchStage(stage, "AppointmentManagementSystem.fxml", msg.getString("MainTitle"));
    }

    @FXML private void localize() {
        backButton.setText(msg.getString("Back"));
        typeReportNumCol.setText(msg.getString("Number"));
        typeReportMonthCol.setText(msg.getString("Month"));
        typeReportTypeCol.setText(msg.getString("Type"));
        contactIDCol.setText(msg.getString("Contact"));
        appointIDCol.setText(msg.getString("AppointID"));
        titleCol.setText(msg.getString("Title"));
        typeCol.setText(msg.getString("Type"));
        descrCol.setText(msg.getString("Description"));
        startCol.setText(msg.getString("StartDateTime"));
        endCol.setText(msg.getString("EndDateTime"));
        custID.setText(msg.getString("CustID"));
        userIDCol.setText(msg.getString("UserID"));
        userTypeCol.setText(msg.getString("Type"));
        userMonthCol.setText(msg.getString("Month"));
        userNumCol.setText(msg.getString("Number"));
    }

    /**
     * Initializes the information the page requires to function
     */
    @FXML private void initialize() {
        DBAccessor dba = DBAccessor.getInstance();
        msg = dba.getMsg();
        switcher = StageSwitcher.getInstance();
        localize();

        //Populate Report Tables
        //Number per month per type
        typeReportNumCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        typeReportTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeReportMonthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        totalTypeReportTable.getItems().setAll(dba.getTypeReport());

        //Contact Schedules
        contactIDCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        appointIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descrCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        custID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        contactScheduleTable.getItems().setAll(dba.getScheduleReport());

        //User number Appointments set/edit
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        userTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        userMonthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        userNumCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        userModReportTable.getItems().setAll(dba.getUserReport());

    }
}
