package com.example.c195;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DBAccessor was getting smelly, so broke out individual classes
 * with different responsibilities
 * @author Harold Alex McLeod
 * @version 1.0
 */
public final class CustomerDatabaseAccessor {
    //Customer Screen variables
    private DBAccessor dba;
    private AppointmentsDatabaseAccessor ada;
    private ObservableList<Customer> customers;
    private int customerID;
    private Customer selectedCustomer;
    private ObservableList<Divisions> divisions;
    private ObservableList<Countries> countries;

    /**
     * initializes INSTANCE as the singleton CustomerDatabaseAccessor
     */
    private final static CustomerDatabaseAccessor INSTANCE = new CustomerDatabaseAccessor();

    /**
     * Gets an instance of the singleton CustomerDatabaseAccessor
     * @return an instance
     */
    public static CustomerDatabaseAccessor getInstance() {
        return INSTANCE;
    }

    /**
     * Queries the database and returns all the countries
     * @return ObservableList of Countries
     */
    public ObservableList<Countries> getCountries() {
        if(countries == null) {
            List<Countries> c = new ArrayList<>();
            countries = FXCollections.observableList(c);
        }
        if (!countries.isEmpty()) {
            countries.clear();
        }
        try {
            Connection connection = DriverManager.getConnection(dba.getPath(), dba.getUsername(), dba.getPass());
            String sql = "SELECT * FROM countries";
            ResultSet result = dba.queryDatabase(sql, connection);
            while (result.next()) {
                Countries c = new Countries(result.getInt(1), result.getString(2));
                countries.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countries;
    }

    /**
     * Queries the database and returns all the first-level divisions
     * @return ObservableList of Divisions objects
     */
    public ObservableList<Divisions> getDivisions() {
        if (divisions == null) {
            List<Divisions> l = new ArrayList<>();
            divisions = FXCollections.observableList(l);
        }
        if (!divisions.isEmpty()) {
            divisions.clear();
        }
        try {
            Connection connection = DriverManager.getConnection(dba.getPath(), dba.getUsername(), dba.getPass());
            String sql = "SELECT * FROM first_level_divisions";
            ResultSet result = dba.queryDatabase(sql, connection);
            while (result.next()) {
                Divisions d = new Divisions(Integer.parseInt(result.getString(1)), result.getString(2), result.getInt(7));
                divisions.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return divisions;
    }


    /**
     * This method returns an observable list of Customer objects
     * from database information
     * @return ObservableList of Customer objects
     */
    public ObservableList<Customer> getAllCustomers() {
        if (customers == null) {
            List<Customer> c = new ArrayList<>();
            customers = FXCollections.observableList(c);
        }
        if (!customers.isEmpty()) {
            customers.clear();
        }
        try {
            Connection connection = DriverManager.getConnection(dba.getPath(), dba.getUsername(), dba.getPass());
            String sql = "SELECT * FROM customers";
            ResultSet result = dba.queryDatabase(sql, connection);
            while (result.next()) {
                String div = null;
                String country = null;
                for (Divisions d : divisions) {
                    if (d.getID() == result.getInt(10)) {
                        div = d.getName();
                        for (Countries c : countries) {
                            if(d.getCountryID() == c.getID()) {
                                country = c.getName();
                            }
                        }
                    }
                }
                Customer c = new Customer(result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getString(5),
                        div,
                        country);
                customers.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    /**
     * This method deletes a Customer from the database
     * Should also delete any appointments for Customer
     * @param customer A Customer object
     */
    public void deleteCustomer(Customer customer) {
        /**ObservableList<Appointments> custAppoint = ada.getAppointmentsByCustomerID(customer.getID());
        if (!custAppoint.isEmpty()) {
            for (Appointments appoint : custAppoint) {
                ada.deleteAppointment(appoint);
            }
        }
        customers.remove(customer);*/
        try {
            Connection connection = DriverManager.getConnection(dba.getPath(), dba.getUsername(), dba.getPass());
            String sql = "DELETE FROM appointments WHERE Customer_ID = ?";
            String sql2 = "DELETE FROM customers WHERE Customer_ID = ?";
            PreparedStatement prepared = connection.prepareStatement(sql);
            PreparedStatement prepared2 = connection.prepareStatement(sql2);
            prepared.setInt(1, customer.getID());
            prepared2.setInt(1, customer.getID());
            prepared.executeUpdate();
            prepared2.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method adds a Customer to the database
     * @param customer a Customer object
     */
    public void addCustomer(Customer customer) {
        customers.add(customer);
        int div = 0;
        for (Divisions d : divisions) {
            if (d.getName().equalsIgnoreCase(customer.getDivision())) {
                div = d.getID();
            }
        }
        try {
            Connection connection = DriverManager.getConnection(dba.getPath(), dba.getUsername(), dba.getPass());
            String sql = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone," +
                    "Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                    " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setInt(1, customer.getID());
            prepared.setString(2, customer.getName());
            prepared.setString(3, customer.getAddress());
            prepared.setString(4, customer.getPostCode());
            prepared.setString(5, customer.getPhone());
            prepared.setTimestamp(6, Timestamp.valueOf(ada.convertToUTC(LocalDateTime.now())));
            prepared.setString(7, dba.getCurrentUser().getName());
            prepared.setTimestamp(8, Timestamp.valueOf(ada.convertToUTC(LocalDateTime.now())));
            prepared.setString(9, dba.getCurrentUser().getName());
            prepared.setInt(10, div);
            prepared.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Replaces the customer object with second customer object
     * @param customer1 to replace
     * @param customer2 object to replace above
     */
    public void updateCustomer(Customer customer1, Customer customer2) {
        customers.set(customers.indexOf(customer1), customer2);
        int div = 0;
        for (Divisions d : divisions) {
            if (d.getName().equalsIgnoreCase(customer2.getDivision())) {
                div = d.getID();
            }
        }
        try {
            Connection connection = DriverManager.getConnection(dba.getPath(), dba.getUsername(), dba.getPass());
            String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?," +
                    "Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?;";
            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setInt(8, customer2.getID());
            prepared.setString(1, customer2.getName());
            prepared.setString(2, customer2.getAddress());
            prepared.setString(3, customer2.getPostCode());
            prepared.setString(4, customer2.getPhone());
            prepared.setTimestamp(5, Timestamp.valueOf(ada.convertToUTC(LocalDateTime.now())));
            prepared.setString(6, dba.getCurrentUser().getName());
            prepared.setInt(7, div);
            prepared.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        int i = 0;
        try {
            Connection connection = DriverManager.getConnection(dba.getPath(), dba.getUsername(), dba.getPass());
            String sql = "SELECT MAX(Customer_ID) FROM customers";
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
     * Simple constructor
     */
    public CustomerDatabaseAccessor () {
        dba = DBAccessor.getInstance();
        ada = AppointmentsDatabaseAccessor.getInstance();
        divisions = getDivisions();
        countries = getCountries();
        customerID = 0;
    }
}
