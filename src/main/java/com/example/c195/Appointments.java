package com.example.c195;

/**
 * This class creates Appointment objects that can be more easily passed between
 * views and the database
 */
public class Appointments {
    //Declare object's properties
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private String start; //This property is a String but must be converted to DATETIME type when stored in database
    private String end; //See above
    private int customerID; //This should correspond to a Customer object's customer ID
    private int userID; //This should correspond to a User's ID from the Users table in the database
    private int contactID; //This should correspond to a Contact's ID from the Contacts table in the database

    /**
     * Getter for appointmentID
     * @return appointmentID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Setter for appointmentID
     * @param appointmentID the ID for this object
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * Getter for title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title
     * @param title of this appointment
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description
     * @param description of appointment
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for location
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for location
     * @param location of appointment
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter for start datetime
     * @return String start
     */
    public String getStart() {
        return start;
    }

    /**
     * Setter for start datetime
     * @param start a String representation of datetime
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     * Getter for end datetime
     * @return String end datetime
     */
    public String getEnd() {
        return end;
    }

    /**
     * Setter for end datetime
     * @param end String representation of datetime
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * Getter for customerID
     * @return customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Setter for customerID
     * @param customerID int
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Getter for userID
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Setter for userID
     * @param userID integer that represents user
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Getter for contactID
     * @return contactID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Setter for contactID
     * @param contactID integer that identifies contact
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * Getter for type
     * @return String value of type
     */
    public String getType() { return this.type; }

    /**
     * Setter for type
     * @param type String type of appointment
     */
    public void setType(String type) { this.type = type; }

    /**
     * A search helper function
     * @param query
     * @return true if any part of query is in Appointment object
     */
    public boolean matches(String query) {
        boolean match = false;
        try {
            int i = Integer.parseInt(query);
            if (i == appointmentID || i == customerID || i == userID || i == contactID) {
                match = true;
            }
        } catch (NumberFormatException e) {
            String q = query.toLowerCase();
            if (title.toLowerCase().contains(q) || description.toLowerCase().contains(q) ||
                    location.toLowerCase().contains(q) || type.toLowerCase().contains(q) ||
                    start.toLowerCase().contains(q) || end.toLowerCase().contains(q)) {
                match = true;
            }
        }
        return match;
    }

    /**
     * Constructor for Appointment object
     * @param appointID integer identifying Appointment object
     * @param title String name of Appointment
     * @param desc String explanation of Appointment purpose
     * @param loc String location of Appointment
     * @param type Type of appointment
     * @param start String representation of datetime start
     * @param end String representation of datetime end
     * @param custID int ID of related Customer object
     * @param userID int ID of user
     * @param contactID int ID of contact to meet customer
     */
    public Appointments(int appointID, String title, String desc, String loc, String type, String start, String end, int custID, int userID, int contactID) {
        setAppointmentID(appointID);
        setTitle(title);
        setDescription(desc);
        setLocation(loc);
        setType(type);
        setStart(start);
        setEnd(end);
        setCustomerID(custID);
        setUserID(userID);
        setContactID(contactID);
    }
}
