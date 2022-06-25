package com.example.c195;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Pulled out the Appointment Database functions
 * to clean up DBAccessor
 * @author Harold Alex McLeod
 * @version 1.0
 */
public final class AppointmentsDatabaseAccessor {
    //Appointment Screen variables
    private DBAccessor dba;
    private ObservableList<Appointments> appointments;
    private Appointments selectedAppointment;
    private ObservableList<LocalTime> times;

    /**
     * initializes INSTANCE as the singleton AppointmentsDatabaseAccessor
     */
    private final static AppointmentsDatabaseAccessor INSTANCE = new AppointmentsDatabaseAccessor();

    /**
     * Gets an instance of the singleton AppointmentsDatabaseAccessor
     * @return an instance
     */
    public static AppointmentsDatabaseAccessor getInstance() {
        return INSTANCE;
    }

    /**
     * Converting datetimes from UTC to local time zone
     * @param dateTime A LocalDateTime object in UTC
     * @return time at local machine
     */
    public LocalDateTime convertFromUTC(LocalDateTime dateTime) {
        ZonedDateTime zoned = dateTime.atZone(ZoneOffset.UTC);
        return zoned.withZoneSameInstant(dba.getZone()).toLocalDateTime();
    }

    /**
     * This method gets all the appointments from the database and turns them into an ObservableList
     * and returns that List
     * @return Observable List of all appointments
     */
    public ObservableList<Appointments> getAllAppointments() {
        if (appointments == null) {
            List<Appointments> l = new ArrayList<>();
            appointments = FXCollections.observableList(l);
        }
        if (!appointments.isEmpty()) {
            appointments.clear();
        }
        try {
            Connection connection = DriverManager.getConnection(dba.getPath(), dba.getUsername(), dba.getPass());
            String sql = "SELECT * FROM appointments";
            ResultSet result = dba.queryDatabase(sql, connection);
            while (result.next()) {
                //Build an Appointments object with results
                int id = result.getInt(1);
                String title = result.getString(2);
                String desc = result.getString(3);
                String loc = result.getString(4);
                String type = result.getString(5);
                //Convert datetime to zoneddatetime
                LocalDateTime start = convertFromUTC(result.getDate(6).toLocalDate().atTime(result.getTime(6).toLocalTime()));
                LocalDateTime end = convertFromUTC(result.getDate(7).toLocalDate().atTime(result.getTime(7).toLocalTime()));
                int cust = result.getInt(12);
                int user = result.getInt(13);
                int cont = result.getInt(14);
                Appointments a = new Appointments(id, title, desc, loc, type, start.toString(), end.toString(),
                        cust, user, cont);
                appointments.add(a);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.appointments;
    }

    public ObservableList<Appointments> lookupAppointments(String query) {
        List<Appointments> results = new ArrayList<>();
        ObservableList<Appointments> searchResults = FXCollections.observableList(results);
        if (this.appointments.isEmpty()) { getAllAppointments(); }
        for (Appointments a : this.appointments) {
            if (a.matches(query)) {
                searchResults.add(a);
            }
        }
        return searchResults;
    }

    /**
     * Gets a list of all appointments this week
     * @return ObservableList of Appointment Objects
     */
    public ObservableList<Appointments> getWeekAppointments(User user) {
        List<Appointments> appointList = new ArrayList<>();
        ObservableList<Appointments> userAppoint = FXCollections.observableList(appointList);
        if (user instanceof Admin) {
            userAppoint = this.appointments;
        }
        if (user instanceof Rep) {
            userAppoint = getAppointmentsByContactID(((Rep) user).getContactID());
        }
        List<Appointments> list = new ArrayList<>();
        ObservableList<Appointments> weekAppoint = FXCollections.observableList(list);
        for (Appointments a : userAppoint) {
            LocalDate aDate = LocalDate.parse(a.getStart().substring(0, 10));
            if (aDate.getYear() == LocalDate.now().getYear() &&
                    aDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR) == LocalDate.now().get(ChronoField.ALIGNED_WEEK_OF_YEAR)) {
                weekAppoint.add(a);
            }
        }
        return weekAppoint;
    }

    /**
     * Gets a list of all appointments this month
     * @return ObservableList of Appointment objects
     */
    public ObservableList<Appointments> getMonthAppointments(User user) {
        List<Appointments> appointList = new ArrayList<>();
        ObservableList<Appointments> userAppoint = FXCollections.observableList(appointList);
        if (user instanceof Admin) {
            userAppoint = this.appointments;
        }
        if (user instanceof Rep) {
            userAppoint = getAppointmentsByContactID(((Rep) user).getContactID());
        }
        List<Appointments> list = new ArrayList<>();
        ObservableList<Appointments> monthAppoint = FXCollections.observableList(list);
        for (Appointments a : userAppoint) {
            LocalDate aDate = LocalDate.parse(a.getStart().substring(0, 10));
            if (aDate.getYear() == LocalDate.now().getYear() &&
                    aDate.getMonth() == LocalDate.now().getMonth()) {
                monthAppoint.add(a);
            }
        }
        return monthAppoint;
    }

    /**
     * This method removes the selected appointment from the database
     * @param appointment an Appointments object
     */
    public void deleteAppointment(Appointments appointment) {
        this.appointments.remove(appointment);
        try {
            Connection connection = DriverManager.getConnection(dba.getPath(), dba.getUsername(), dba.getPass());
            String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setInt(1, appointment.getAppointmentID());
            prepared.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Setter for selectedAppointment property
     * @param appointment current selection
     */
    public void setSelectedAppointment(Appointments appointment) {
        this.selectedAppointment = appointment;
    }

    /**
     * Adds appointment to database
     * @param appointment an Appointment object
     */
    public void setNewAppointment(Appointments appointment) {
        this.appointments.add(appointment);
        //Convert time to UTC
        LocalDateTime start = convertToUTC(LocalDateTime.parse(appointment.getStart()));
        LocalDateTime end = convertToUTC(LocalDateTime.parse(appointment.getEnd()));

        try {
            Connection connection = DriverManager.getConnection(dba.getPath(), dba.getUsername(), dba.getPass());
            String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type," +
                    "Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, " +
                    "User_ID, Contact_ID) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setInt(1, appointment.getAppointmentID());
            prepared.setString(2, appointment.getTitle());
            prepared.setString(3, appointment.getDescription());
            prepared.setString(4, appointment.getLocation());
            prepared.setString(5, appointment.getType());
            prepared.setTimestamp(6, Timestamp.valueOf(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            prepared.setTimestamp(7, Timestamp.valueOf(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            prepared.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            prepared.setString(9, dba.getCurrentUser().getName());
            prepared.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            prepared.setString(11, dba.getCurrentUser().getName());
            prepared.setInt(12, appointment.getCustomerID());
            prepared.setInt(13, appointment.getUserID());
            prepared.setInt(14, appointment.getContactID());
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
    public LocalDateTime convertToUTC(LocalDateTime dateTime) {
        ZonedDateTime zoned = dateTime.atZone(dba.getZone());
        return zoned.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    /**
     * Replaces the data for the selected appointment with the new data
     * @param appointment an appointment object
     */
    public void replaceSelectedAppointment(Appointments appointment) {
        //Convert time to UTC
        LocalDateTime start = convertToUTC(LocalDateTime.parse(appointment.getStart()));
        LocalDateTime end = convertToUTC(LocalDateTime.parse(appointment.getEnd()));

        //Convert from Appointments object to database format
        int id = appointment.getAppointmentID();
        this.appointments.set(this.appointments.indexOf(getAppointmentByID(id)), appointment);
        try {
            Connection connection = DriverManager.getConnection(dba.getPath(), dba.getUsername(), dba.getPass());
            String sql = "UPDATE appointments SET title = ?, description = ?, location = ?, type = ?," +
                    "start = ?, end = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID =  ?, " +
                    "Contact_ID = ? WHERE appointment_id = ?";
            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setInt(12, appointment.getAppointmentID());
            prepared.setString(1, appointment.getTitle());
            prepared.setString(2, appointment.getDescription());
            prepared.setString(3, appointment.getLocation());
            prepared.setString(4, appointment.getType());
            prepared.setTimestamp(5, Timestamp.valueOf(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            prepared.setTimestamp(6, Timestamp.valueOf(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            prepared.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            prepared.setString(8, dba.getCurrentUser().getName());
            prepared.setInt(9, appointment.getCustomerID());
            prepared.setInt(10, appointment.getUserID());
            prepared.setInt(11, appointment.getContactID());
            prepared.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        clearSelectedAppointment();
    }

    /**
     * Getter for selectedAppointment property
     * @return selectedAppointment
     */
    public Appointments getSelectedAppointment() {
        return this.selectedAppointment;
    }

    /**
     * Clears the selectedAppointment property
     */
    public void clearSelectedAppointment() {
        this.selectedAppointment = null;
    }

    /**
     * Generates a new appointment ID and returns the old one
     * @return an unused appointment ID
     */
    public int getNewAppointmentID() {
        int i = 0;
        try {
            Connection connection = DriverManager.getConnection(dba.getPath(), dba.getUsername(), dba.getPass());
            String sql = "SELECT MAX(Appointment_ID) FROM appointments";
            ResultSet resultSet = dba.queryDatabase(sql, connection);
            while (resultSet.next()) {
                i = resultSet.getInt(1) + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * Gets the Appointment that has this ID number
     * @param Id Appointment's ID number
     * @return Appointment
     */
    public Appointments getAppointmentByID(int Id) {
        Appointments appointment = new Appointments(-1, "", "", "", "", "", "", -1, -1, -1);
        for  (Appointments appoint : appointments) {
            if (appoint.getAppointmentID() == Id) {
                appointment = appoint;
            }
        }
        return appointment;
    }

    /**
     * This method returns an observable list of appointments that are scheduled with a single customer
     * @param Id Customer ID
     * @return ObservableList of Appointment objects
     */
    public ObservableList<Appointments> getAppointmentsByCustomerID(int Id) {
        List<Appointments> list = new ArrayList<>();
        ObservableList<Appointments> customerAppointments = FXCollections.observableList(list);
        for (Appointments appoint : appointments) {
            if (appoint.getCustomerID() == Id) {
                customerAppointments.add(appoint);
            }
        }
        return customerAppointments;
    }

    /**
     * This method returns an observable list of appointments that are scheduled with a single contact
     * @param Id Contact ID
     * @return ObservableList of Appointment objects
     */
    public ObservableList<Appointments> getAppointmentsByContactID(int Id) {
        List<Appointments> list = new ArrayList<>();
        ObservableList<Appointments> contactAppointments = FXCollections.observableList(list);
        for (Appointments appoint : appointments) {
            if (appoint.getContactID() == Id) {
                contactAppointments.add(appoint);
            }
        }
        return contactAppointments;
    }

    /**
     * Creates a list of times for use elsewhere
     */
    private void setTimes() {
        //List of all the available times in EST 2400 hr format
        List<LocalTime> time = Arrays.asList(LocalTime.parse("00:00"), LocalTime.parse("01:00"), LocalTime.parse("02:00"),
                LocalTime.parse("03:00"), LocalTime.parse("04:00"), LocalTime.parse("05:00"), LocalTime.parse("06:00"),
                LocalTime.parse("07:00"), LocalTime.parse("08:00"), LocalTime.parse("09:00"), LocalTime.parse("10:00"),
                LocalTime.parse("11:00"), LocalTime.parse("12:00"), LocalTime.parse("13:00"), LocalTime.parse("14:00"),
                LocalTime.parse("15:00"), LocalTime.parse("16:00"), LocalTime.parse("17:00"), LocalTime.parse("18:00"),
                LocalTime.parse("19:00"), LocalTime.parse("20:00"), LocalTime.parse("21:00"), LocalTime.parse("22:00"),
                LocalTime.parse("23:00"));
        times = FXCollections.observableList(time);
    }

    /**
     * Returns the ObservableList times for use in Appointment time data
     * @return ObservableList of LocalTime objects
     */
    public ObservableList<LocalTime> getTimes() {
        if (times.isEmpty()) {
            setTimes();
        }
        return times;
    }

    /**
     * Access database and return an ObservableList of Contact objects
     * @return ObservableList Contact
     */
    public ObservableList<Contact> getContactList() {
        List<Contact> contact = new ArrayList<>();
        ObservableList<Contact> contacts = FXCollections.observableList(contact);
        try {
            Connection connection = DriverManager.getConnection(dba.getPath(), dba.getUsername(), dba.getPass());
            String sql = "SELECT * FROM contacts";
            ResultSet resultSet = dba.queryDatabase(sql, connection);
            while (resultSet.next()) {
                Contact c = new Contact(resultSet.getInt(1), resultSet.getString(2));
                contacts.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contacts;
    }

    /**
     * Constructor to initialize
     */
    public AppointmentsDatabaseAccessor() {
        dba = DBAccessor.getInstance();
        setTimes();
        appointments = getAllAppointments();
    }
}
