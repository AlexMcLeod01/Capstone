package com.example.c195;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.*;
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
    private ResourceBundle bundle;
    private SimpleDateFormat format;
    private File loginFile;
    private User currentUser;

    //New User screen variables
    private int newUserId;


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

    /**
     * Getter for newUserId
     * @return newUserId
     */
    public int getNewUserId() {
        int id = newUserId;
        newUserId++;
        return id;
    }

    /**
     * Sets the newUserId based upon the highest int user_id in users
     */
    private void setNewUserId() {
        int id = 1;
        try {
            Connection connection = DriverManager.getConnection(path, username, pass);
            String sql = "SELECT MAX(user_id) FROM users";
            ResultSet result = queryDatabase(sql, connection);
            while (result.next()) {
                id = result.getInt(1) + 1;
            }
            sql = "SELECT MAX(contact_id) FROM contacts";
            ResultSet result1 = queryDatabase(sql, connection);
            while (result1.next()) {
                int cId = result1.getInt(1);
                if (cId > id) {
                    id = cId;
                }
            }
            newUserId = id;
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            Connection connection = DriverManager.getConnection(path, username, pass);
            String sql = "SELECT user_name FROM users";
            ResultSet result = queryDatabase(sql, connection);
            while (result.next()) {
                String r = result.getString(1);
                if (r.length() > user.length() && user.equals(r.substring(0, user.length()))) {
                    return true;
                }
                if (user.equals(r) && !(user.contains("/p") || user.contains("/c"))) {
                    return true;
                }
            }
            Date date = new Date(System.currentTimeMillis());
            String err = "\nFailed Login Attempt:\nUsername: " + user + "\nDate: " + format.format(date);
            Write(err);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks to see if username and password pair are in database
     * Also writes the activity to login_activity.txt
     * @param user - username
     * @param password - password
     * @return true if username and password match pair in database
     */
    public boolean userPass(String user, String password) {
        String admin = "/p";
        String contact = "/c";
        try {
            Connection connection = DriverManager.getConnection(path, username, pass);
            String sql = "SELECT user_name, password, user_id FROM users";
            ResultSet result = queryDatabase(sql, connection);
            Date date = new Date(System.currentTimeMillis());
            while (result.next()) {
                String r = result.getString(1);
                if ((r.length() > user.length()) && (user.equals(r.substring(0, user.length())))) {
                    if(password.equals(result.getString(2))) {
                        String type = r.substring(user.length());
                        int id = result.getInt(3);

                        String mes = "\nSuccessful Login:\nUsername: " + user + "\nDate: " + format.format(date);
                        Write(mes);

                        if (type.equals(admin)) {
                            currentUser = new Admin(user, id);
                        }
                        if (contact.equals(type.substring(0, 2))) {
                            currentUser = new Rep(user, id, Integer.parseInt(type.substring(2)));
                        }
                        return true;

                    }
                }
                //This portion is only in here to correct older style username saves in the database, if they exist
                if (user.equals(r)) {
                    if(password.equals(result.getString(2))) {
                        int id = result.getInt(3);

                        String mes = "\nSuccessful Login:\nUsername: " + user + "\nDate: " + format.format(date);
                        Write(mes);

                        currentUser = new Admin(user, id);
                        updateUser(user + admin, id);
                        return true;
                    }
                }
            }
            String err = "\nFailed Login Attempt:\nUsername: " + user + "\nDate: " + format.format(date);
            Write(err);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
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

    /**
     * This function just updates the username of a user, mostly for backward compatibility
     * @param updatedUsername
     * @param ID
     */
    private void updateUser(String updatedUsername, int ID) {
        try {
            Connection connection = DriverManager.getConnection(path, username, pass);
            String sql = "UPDATE users SET user_name = ? WHERE user_id = ?";
            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setString(1, updatedUsername);
            prepared.setInt(2, ID);
            prepared.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a user to the users table
     * @param user
     * @param pwd
     */
    public void addUser(User user, String pwd) {
        boolean valid = true;
        String name = "";
        if (user instanceof Rep) {
            name = user.getName() + "/c" + ((Rep) user).getContactID();
        }
        if (user instanceof Admin) {
            name = user.getName() + "/p";
        }
        try {
            Connection connection = DriverManager.getConnection(path, username, pass);
            String sql = "SELECT user_name FROM users";
            ResultSet result = queryDatabase(sql, connection);
            while (result.next()) {
                if (user.getName().equals(result.getString(1))) {
                    valid = false;
                }
            }
            if (valid) {
                sql = "INSERT INTO users (user_name, password, user_id," +
                        "Create_Date, Created_By, Last_Update, Last_Updated_By) " +
                        " values (?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement prepared = connection.prepareStatement(sql);
                prepared.setString(1, name);
                prepared.setString(2, pwd);
                prepared.setInt(3, user.getID());
                prepared.setTimestamp(4, Timestamp.valueOf(convertToUTC(LocalDateTime.now())));
                prepared.setString(5, user.getName());
                prepared.setTimestamp(6, Timestamp.valueOf(convertToUTC(LocalDateTime.now())));
                prepared.setString(7, user.getName());
                prepared.executeUpdate();
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a contact to the contact table
     * @param contact
     * @param email
     */
    public void addContact(Contact contact, String email) {
        try {
            Connection connection = DriverManager.getConnection(path, username, pass);
            String sql = "INSERT INTO contacts (contact_id, contact_name, email) " +
                    " values (?, ?, ?);";
            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setInt(1, contact.getID());
            prepared.setString(2, contact.getName());
            prepared.setString(3, email);
            prepared.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts Time to UTC from local time zone
     * @return LocalDateTime object converted to local timezone from UTC
     * @param dateTime LocalDateTime
     */
    private LocalDateTime convertToUTC(LocalDateTime dateTime) {
        ZonedDateTime zoned = dateTime.atZone(getZone());
        return zoned.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
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
            String sql = "SELECT DISTINCT Type, MONTHNAME(Start), COUNT(*) FROM appointments GROUP BY Type, MONTH(Start) ORDER BY Type";
            ResultSet result = queryDatabase(sql, connection);
            while (result.next()) {
                report.add(new TypeReport(result.getString(1), result.getString(2), result.getInt(3)));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return report;
    }

    /**
     * Queries database and returns an ObservableList of Appointments objects
     * that correspond to each Contact's schedule for the month
     * @return ObservableList
     */
    public ObservableList<Appointments> getScheduleReport() {
        List<Appointments> rep = new ArrayList<>();
        ObservableList<Appointments> report = FXCollections.observableList(rep);
        AppointmentsDatabaseAccessor ada = AppointmentsDatabaseAccessor.getInstance();
        try {
            Connection connection = DriverManager.getConnection(path, username, pass);
            String sql = "SELECT * FROM appointments GROUP BY Contact_ID, start ORDER BY Contact_ID ASC, start DESC";
            ResultSet result = queryDatabase(sql, connection);
            while (result.next()) {
                LocalDateTime start = ada.convertFromUTC(result.getDate(6).toLocalDate().atTime(result.getTime(6).toLocalTime()));
                LocalDateTime end = ada.convertFromUTC(result.getDate(7).toLocalDate().atTime(result.getTime(7).toLocalTime()));
                Appointments a = new Appointments(result.getInt(1), result.getString(2), result.getString(3),
                        result.getString(4), result.getString(5), start.toString(), end.toString(),
                        result.getInt(12), result.getInt(13), result.getInt(14));
                report.add(a);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return report;
    }

    /**
     * Queries the Database and returns an Observable list of UserReport objects
     * @return ObservableList UserReport
     */
    public ObservableList<UserReport> getUserReport() {
        List<UserReport> rep = new ArrayList<>();
        ObservableList<UserReport> report = FXCollections.observableList(rep);
        try {
            Connection connection = DriverManager.getConnection(getPath(), getUsername(), getPass());
            String sql = "SELECT user_id, type, MONTHNAME(start), COUNT(*) FROM appointments GROUP BY user_id, type ORDER BY user_id";
            ResultSet result = queryDatabase(sql, connection);
            while (result.next()) {
                UserReport u = new UserReport(result.getInt(1), result.getString(2), result.getString(3), result.getInt(4));
                report.add(u);
            }
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
            Statement statement = connection.createStatement();
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
        Locale local = Locale.getDefault();
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
        setNewUserId();
    }
}
