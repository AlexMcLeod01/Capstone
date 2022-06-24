package com.example.c195;

/**
 * A Sales Rep class that inherits from the User class
 */
public class Rep extends User {
    private int contactID;


    /**
     * Getters and Setters
     */
    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * Constructor for the Rep class
     * @param name String
     * @param ID int
     * @param contactID int - Contact object id
     */
    public Rep (String name, int ID, int contactID) {
        super(name, ID);
        setContactID(contactID);
    }
}
