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
    private String path = "jdbc:mysql://localhost:3306/client_schedule";
    private String username = "sqlUser";
    private String pass = "Passw0rd!";

    //Login screen variables
    private ZoneId zone;
    private Locale local;
    private ResourceBundle bundle;
    private SimpleDateFormat format;
    private File loginFile;
    private User currentUser;
    //Customer Screen variables
    private ObservableList<Customer> customers;
    private int customerID;
    private Customer selectedCustomer;
    //Appointment Screen variables
    private int appointmentID;
    private ObservableList<Appointments> appointments;
    private Appointments selectedAppointment;
    private ObservableList<Contact> contacts;
    private ObservableList<LocalTime> times;


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

    /*************************************************************************************************************
     *******************************************CUSTOMER**********************************************************
     *******************************************FUNCTIONS*********************************************************
     *************************************************************************************************************/

    private ObservableList<Customer> createExampleCustomerData() {
        List<Customer> cust = new ArrayList<>();
        ObservableList<Customer> customers = FXCollections.observableList(cust);
        Customer c1 = new Customer(this.getNewCustomerID(), "A", "123 Main St", "54536", "+1(850)678-5018", "Florida", "USA");
        Customer c2 = new Customer(this.getNewCustomerID(), "B", "124 Main St", "54536", "+1(850)678-5019", "Florida", "USA");
        Customer c3 = new Customer(this.getNewCustomerID(), "C", "125 Main St", "54536", "+1(850)678-5020", "Florida", "USA");
        Customer c4 = new Customer(this.getNewCustomerID(), "D", "126 Main St", "54536", "+1(850)678-5021", "Florida", "USA");
        Customer c5 = new Customer(this.getNewCustomerID(), "E", "127 Main St", "54536", "+1(850)678-5022", "Florida", "USA");
        customers.add(c1);
        customers.add(c2);
        customers.add(c3);
        customers.add(c4);
        customers.add(c5);
        return customers;
    }

    /**
     * This method returns an observable list of Customer objects
     * from database information
     * @return
     */
    public ObservableList<Customer> getAllCustomers() {
        return customers;
    }

    /**
     * This method deletes a Customer from the database
     * Should also delete any appointments for Customer
     * @param customer A Customer object
     */
    public void deleteCustomer(Customer customer) {
        ObservableList<Appointments> custAppoint = getAppointmentsByCustomerID(customer.getID());
        if (!custAppoint.isEmpty()) {
            for (Appointments appoint : custAppoint) {
                deleteAppointment(appoint);
            }
        }
        customers.remove(customer);
    }

    /**
     * This method adds a Customer to the database
     * @param customer a Customer object
     */
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    /**
     * Replaces the customer object with second customer object
     * @param customer1 to replace
     * @param customer2 object to replace above
     */
    public void updateCustomer(Customer customer1, Customer customer2) {
        customers.set(customers.indexOf(customer1), customer2);
    }

    /**
     * sets the selectedCustomer to given customer
     * @param customer the selected customer
     */
    public void setSelectedCustomer(Customer customer) {
        this.selectedCustomer = customer;
    }

    /**
     * gets the selectedCustomer
     * @return selectedCustomer
     */
    public Customer getSelectedCustomer() {
        return this.selectedCustomer;
    }

    /**
     * This method generates a new customer ID every time it is called
     * so that customer ID never gets reused. Not all generated IDs will
     * necessarily be used
     * @return customerID
     */
    public int getNewCustomerID() {
        int i = this.customerID;
        this.customerID++;
        return i;
    }

    /**
     * Gets Customer by ID number
     * @param ID number
     * @return Customer
     */
    public Customer getCustomerByID(int ID) {
        Customer customer = new Customer(-1, "", "", "", "", "", "");
        for (Customer cust : customers) {
            if (cust.getID() == ID) {
                customer = cust;
            }
        }
        return customer;
    }

    /*******************************************************************************************************
     ******************************************APPOINTMENT**************************************************
     ******************************************FUNCTIONS****************************************************
     *******************************************************************************************************
     *******************************************************************************************************/

    private ObservableList<Appointments> createExampleAppointmentData() {
        List<Appointments> appoint = new ArrayList<>();
        ObservableList<Appointments> appointments = FXCollections.observableList(appoint);
        Appointments a1 = new Appointments(this.getNewAppointmentID(), "A", "Sales", "54* by 63*", "Marketing", "2022-04-30T17:00", "2022-04-30T18:00", 2, 1, 3);
        Appointments a2= new Appointments(this.getNewAppointmentID(), "B", "Delivery", "Longhorns", "Delivery", "2022-05-01T10:40", "2022-05-01T11:00", 1, 2, 2);
        Appointments a3 = new Appointments(this.getNewAppointmentID(), "C", "Sales", "Dark Island Hotel", "Sales", "2022-06-12T09:00", "2022-06-12T10:00", 3, 2, 1);
        appointments.add(a1);
        appointments.add(a2);
        appointments.add(a3);
        return appointments;
    }

    /**
     * This method gets all the appointments from the database and turns them into an ObservableList
     * and returns that List
     * @return Observable List of all appointments
     */
    public ObservableList<Appointments> getAllAppointments() {
        appointments.clear();
        try {
            Connection connection = DriverManager.getConnection(path, username, pass);
            String sql = "SELECT * FROM appointments";//SELECT * FROM appointments";
            ResultSet result = queryDatabase(sql, connection);
            while (result.next()) {
                //Build an Appointments object with results
                int id = result.getInt(1);
                String title = result.getString(2);
                String desc = result.getString(3);
                String loc = result.getString(4);
                String type = result.getString(5);
                //Convert datetime to zoneddatetime
                LocalDateTime start = result.getDate(6).toLocalDate().atTime(result.getTime(6).toLocalTime());
                ZonedDateTime zStart = start.atZone(ZoneOffset.UTC);
                LocalDateTime localStart = zStart.withZoneSameInstant(getZone()).toLocalDateTime();
                LocalDateTime end = result.getDate(7).toLocalDate().atTime(result.getTime(7).toLocalTime());
                ZonedDateTime zEnd = end.atZone(ZoneOffset.UTC);
                LocalDateTime localEnd = zEnd.withZoneSameInstant(getZone()).toLocalDateTime();
                int cust = result.getInt(12);
                int user = result.getInt(13);
                int cont = result.getInt(14);
                Appointments a = new Appointments(id, title, desc, loc, type, localStart.toString(), localEnd.toString(),
                    cust, user, cont);
                appointments.add(a);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.appointments;
    }

    /**
     * Gets a list of all appointments this week
     * @return ObservableList of Appointment Objects
     */
    public ObservableList<Appointments> getWeekAppointments() {
        List<Appointments> list = new ArrayList<Appointments>();
        ObservableList<Appointments> weekAppoint = FXCollections.observableList(list);
        for (Appointments a : appointments) {
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
    public ObservableList<Appointments> getMonthAppointments() {
        List<Appointments> list = new ArrayList<Appointments>();
        ObservableList<Appointments> monthAppoint = FXCollections.observableList(list);
        for (Appointments a : appointments) {
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
            Connection connection = DriverManager.getConnection(path, username, pass);
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
        LocalDateTime start = LocalDateTime.parse(appointment.getStart());
        ZonedDateTime zStart = start.atZone(getZone());
        start = zStart.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime end = LocalDateTime.parse(appointment.getEnd());
        ZonedDateTime zEnd = end.atZone(getZone());
        end = zEnd.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        try {
            Connection connection = DriverManager.getConnection(path, username, pass);
            String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type," +
                    "Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, " +
                    "User_ID, Contact_ID) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setInt(1, appointment.getAppointmentID());
            prepared.setString(2, appointment.getType());
            prepared.setString(3, appointment.getDescription());
            prepared.setString(4, appointment.getLocation());
            prepared.setString(5, appointment.getType());
            prepared.setTimestamp(6, Timestamp.valueOf(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            prepared.setTimestamp(7, Timestamp.valueOf(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            prepared.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            prepared.setString(9, getCurrentUser().getName());
            prepared.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            prepared.setString(11, getCurrentUser().getName());
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
     * Replaces the data for the selected appointment with the new data
     * @param appointment an appointment object
     */
    public void replaceSelectedAppointment(Appointments appointment) {
        int id = appointment.getAppointmentID();
        this.appointments.set(this.appointments.indexOf(getAppointmentByID(id)), appointment);
        try {
            Connection connection = DriverManager.getConnection(path, username, pass);
            String sql = "UPDATE appointments SET ? = ? WHERE Appointment_ID = ?";
            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setInt(3, appointment.getAppointmentID());
            prepared.setString(1, "Title");
            prepared.setString(2, appointment.getTitle());
            prepared.executeUpdate();
            prepared.setString(1, "Description");
            prepared.setString(2, appointment.getDescription());
            prepared.executeUpdate();
            prepared.setString(1, "Location");
            prepared.setString(2, appointment.getLocation());

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
        return;
    }

    /**
     * Generates a new appointment ID and returns the old one
     * @return an unused appointment ID
     */
    public int getNewAppointmentID() {
        int i = this.appointmentID;
        this.appointmentID++;
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

    private void setExampleContactList() {
        List<Contact> contact = new ArrayList<>();
        contacts = FXCollections.observableList(contact);
        Contact c1 = new Contact(1, "Yue");
        Contact c2 = new Contact(2, "Spock");
        Contact c3 = new Contact(3, "Yoda");
        contacts.add(c1);
        contacts.add(c2);
        contacts.add(c3);
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
     */
    public ObservableList<LocalTime> getTimes() {
        if (times.isEmpty()) {
            setTimes();
        }
        return times;
    }

    /**
     * Getter for contact list
     * @return
     */
    public ObservableList<Contact> getContactList() {
        setExampleContactList();
        return contacts;
    }

    /**
     * Gets the Contact that has this ID number
     * @param Id contact's ID number
     * @return Contact
     */
    public Contact getContactByID(int Id) {
        Contact contact = new Contact(-1, "");
        for  (Contact con : contacts) {
            if (con.getID() == Id) {
                contact = con;
            }
        }
        return contact;
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

    private ResultSet queryDatabase(String sql, Connection connection) {
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
     * The constructor should eventually initialize the database connection
     * and any global data that needs to be passed around
     */
    public DBAccessor () {
        appointmentID=1;
        appointments = createExampleAppointmentData();
        customerID = 1;
        customers = createExampleCustomerData();
        zone = ZoneId.systemDefault();
        local = Locale.getDefault();
        setTimes();
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
