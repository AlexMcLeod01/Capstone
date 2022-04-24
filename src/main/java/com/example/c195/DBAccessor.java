package com.example.c195;

import javafx.collections.FXCollections;
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
    private ZoneId zone;
    private Locale local;
    private ResourceBundle bundle;
    private SimpleDateFormat format;
    private File loginFile;
    private ObservableList<Customer> customers;
    private int customerID;
    private Customer selectedCustomer;

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

    /*************************************************************************************************************
     * ************************************USER***************************************************************** *
     * ************************************FUNCTIONS************************************************************ *
     *************************************************************************************************************/


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
            return true;
        } else {
            String err = "\nFailed Login Attempt:\nUsername: " + user + "\nDate: " + format.format(date);
            Write(err);
            return false;
        }
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
     * @param customer
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
     * The constructor should eventually initialize the database connection
     * and any global data that needs to be passed around
     */
    public DBAccessor () {
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
