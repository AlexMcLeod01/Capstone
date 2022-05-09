package com.example.c195;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.transform.Result;
import java.sql.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.Date;

/**
 * This class is used to abstract the Database functions from the controller.
 * I wanted to build everything separately, test that it is working first, and
 * then change the code here to actually pull data from the database.
 * @author Harold Alex McLeod
 * @version 1.0
 */
public final class DBAccessor {
    //DataBase settings
    private final String path = "jdbc:mysql://localhost:3306/client_schedule";
    private final String username = "sqlUser";
    private final String pass = "Passw0rd!";

    //Login screen variables
    private ZoneId zone;
    private Locale local;
    private ResourceBundle bundle;
    private SimpleDateFormat format;
    private File loginFile;
    private User currentUser;


    /**
     * initializes INSTANCE as the singleton DBAccessor
     */
    private final static DBAccessor INSTANCE = new DBAccessor();

    /**
     * Gets an instance of the singleton DBAccessor
     * @return an instance
     */
    public static DBAccessor getInstance() {
        return INSTANCE;
    }

    /**
     * Getter for Path variable
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * Getter for username variable
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for pass variable
     * @return pass
     */
    public String getPass() {
        return pass;
    }

    /*********************************************************************************************************
     ************************************USER*****************************************************************
     ************************************FUNCTIONS************************************************************
     */


    /**
     * gets the timezone
     * @return user's timezone setting
     */
    public ZoneId getZone() {
        return zone;
    }

    /**
     * gets the displayed text in user's language
     * @return ResourceBundle with user's language
     */
    public ResourceBundle getMsg() { return bundle; }

    /**
     * Helper function to write to tracking file
     * @param message - message to append to tracking file
     */
    private void Write(String message) {
        try {
            FileWriter writer = new FileWriter(loginFile, true);
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks to see if username is in database; if false, write to login_activity.txt
     * @param user - username
     * @return true if username is in database
     */
    public boolean userExists(String user) {
        if (user.equals("Admin")) {
            return true;
        } else {
            Date date = new Date(System.currentTimeMillis());
            String err = "\nFailed Login Attempt:\nUsername: " + user + "\nDate: " + format.format(date);
            Write(err);
            return false;
        }
    }

    /**
     * Checks to see if username and password pair are in database
     * Also writes the activity to login_activity.txt
     * @param user - username
     * @param pass - password
     * @return true if username and password match pair in database
     */
    public boolean userPass(String user, String pass) {
        Date date = new Date(System.currentTimeMillis());
        if (user.equals("Admin") && pass.equals("12345")) {
            String mes = "\nSuccessful Login:\nUsername: " + user + "\nDate: " + format.format(date);
            Write(mes);
            currentUser = new User(user, 1);
            return true;
        } else {
            String err = "\nFailed Login Attempt:\nUsername: " + user + "\nDate: " + format.format(date);
            Write(err);
            return false;
        }
    }

    /**
     * This is a getter for the currentUser who is logged in
     * @return currentUser
     */
    public User getCurrentUser() {
        return currentUser;
    }


    /******************************************************************************************************
     ******************************************REPORT******************************************************
     ******************************************FUNCTIONS***************************************************
     ******************************************************************************************************/

    /**
     * This function returns the type/month count report
     * It is easiest to use SQL versus searching the data in java
     * @return ObservableList of TypeReport objects
     */
    public ObservableList<TypeReport> getTypeReport() {
        List<TypeReport> rep = new ArrayList<>();
        ObservableList<TypeReport> report = FXCollections.observableList(rep);
        try {
            Connection connection = DriverManager.getConnection(path, username, pass);
            String sql = "SELECT Type, MONTH(Start), COUNT(*) FROM appointments";
            ResultSet result = queryDatabase(sql, connection);
            while (result.next()) {
                report.add(new TypeReport(result.getString(1), LocalDate.of(2022, (int) result.getLong(2), 1), result.getInt(3)));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return report;
    }


    /*******************************************************************************************************
     ******************************************DBACCESSOR***************************************************
     ******************************************CONSTRUCTOR**************************************************
     *******************************************************************************************************/

    ResultSet queryDatabase(String sql, Connection connection) {
        ResultSet resultSet = null;
        try {
            Statement statement = (Statement) connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * The constructor initializes the information from database
     * and any global data that needs to be passed around
     */
    public DBAccessor () {
        zone = ZoneId.systemDefault();
        local = Locale.getDefault();
        bundle = ResourceBundle.getBundle("com.example.c195/MessagesBundle", local);
        format = new SimpleDateFormat("MM/dd/yyyy 'at' HH:mm:ss z");
        String path = System.getProperty("user.dir") + "\\login_activity.txt";
        loginFile = new File(path);
        if(!loginFile.exists()) {
            try {
                loginFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
