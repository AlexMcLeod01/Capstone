package com.example.c195;

/**
 * Creates a User object to pass around user data more effectively
 * Chose not to include a password property due to user data
 * Security concerns
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class User {
    private String name;
    private int ID;

    /**
     * Getter for name property
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name property
     * @param name username
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for user ID
     * @return ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Setter for user ID
     * @param ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Constructor for User object
     * @param name string username
     * @param ID user id number
     */
    public User(String name, int ID) {
        setName(name);
        setID(ID);
    }
}
