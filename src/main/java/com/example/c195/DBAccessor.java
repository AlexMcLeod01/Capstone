package com.example.c195;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

/**
 * This class is used to abstract the Database functions from the controller.
 * I wanted to build everything separately, test that it is working first, and
 * then change the code here to actually pull data from the database.
 * @author Harold Alex McLeod
 * @version 1.0
 */
public final class DBAccessor {
    //Login screen variables
    private ZoneId zone;
    private Locale local;
    private ResourceBundle bundle;
    private SimpleDateFormat format;
    private File loginFile;
    //Customer Screen variables
    private ObservableList<Customer> customers;
    private int customerID;
    private Customer selectedCustomer;
    private String currentUser;
    //Appointment Screen variables
    private int appointmentID;
    private ObservableList<Appointments> appointments;
    private Appointments selectedAppointment;
    private ObservableList<Contact> contacts;


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
            currentUser = user;
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
    public String getCurrentUser() {
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
        Appointments a1 = new Appointments(this.getNewAppointmentID(), "A", "Sales", "54* by 63*", "Marketing", "04-05-2023T09:30:00", "04-05-2023T10:00:00", 2, 1, 3);
        Appointments a2= new Appointments(this.getNewAppointmentID(), "B", "Delivery", "Longhorns", "Delivery", "07-05-2022T10:30:00", "07-05-2022T11:30:00", 1, 2, 4);
        Appointments a3 = new Appointments(this.getNewAppointmentID(), "C", "Sales", "Dark Island Hotel", "Sales", "06-12-2022T09:00:00", "06-12-2022T10:00:00", 3, 2, 1);
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
        return appointments;
    }

    /**
     * This method removes the selected appointment from the database
     * @param appointment an Appointments object
     */
    public void deleteAppointment(Appointments appointment) {
        appointments.remove(appointment);
    }

    /**
     * Setter for selectedAppointment property
     * @param appointment current selection
     */
    public void setSelectedAppointment(Appointments appointment) {
        this.selectedAppointment = appointment;
    }

    /**
     * Getter for selectedAppointment property
     * @return selectedAppointment
     */
    public Appointments getSelectedAppointment() {
        return this.selectedAppointment;
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


    /*******************************************************************************************************
     ******************************************DBACCESSOR***************************************************
     ******************************************CONSTRUCTOR**************************************************
     *******************************************************************************************************/

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
