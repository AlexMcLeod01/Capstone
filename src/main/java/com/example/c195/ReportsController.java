package com.example.c195;


import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private DBAccessor dba;
    private ResourceBundle msg;
    private StageSwitcher switcher;

    //FX items
    //Button
    @FXML private Button backButton;

    //TableViews
    @FXML private TableView<TypeReport> totalTypeReportTable;

    //TableColumns
    @FXML private TableColumn<TypeReport, String> typeReportTypeCol;
    @FXML private TableColumn<TypeReport, LocalDate> typeReportMonthCol;
    @FXML private TableColumn<TypeReport, String> typeReportNumCol;


    @FXML private void backClicked() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        switcher.SwitchStage(stage, "AppointmentManagementSystem.fxml", msg.getString("MainTitle"));
    }

    @FXML private void localize() {
        backButton.setText(msg.getString("Back"));
        typeReportNumCol.setText(msg.getString("Number"));
        typeReportMonthCol.setText(msg.getString("Month"));
        typeReportTypeCol.setText(msg.getString("Type"));
    }

    /**
     * Initializes the information the page requires to function
     */
    @FXML private void initialize() {
        dba = DBAccessor.getInstance();
        msg = dba.getMsg();
        switcher = StageSwitcher.getInstance();
        localize();

        //Populate Report Tables
        typeReportNumCol.setCellValueFactory(new PropertyValueFactory<TypeReport, String>("number"));
        typeReportTypeCol.setCellValueFactory(new PropertyValueFactory<TypeReport, String>("type"));
        //currently not working
        typeReportMonthCol.setCellFactory(col -> {
            TableCell<TypeReport, LocalDate> cell = new TableCell<TypeReport, LocalDate>() {
                private final DateTimeFormatter format = DateTimeFormatter.ofPattern("LLL");

                @Override
                protected void updateItem(LocalDate month, boolean empty) {
                    super.updateItem(month, empty);
                    if(empty || month == null) {
                        setText(null);
                    } else {
                        this.setText(format.format(month));
                    }
                }
            };
            return cell;
        });
        totalTypeReportTable.getItems().setAll(dba.getTypeReport());
    }
}