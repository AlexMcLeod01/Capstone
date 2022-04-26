package com.example.c195;

/**
 * This class helps organize data from the Contacts table in the database
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class Contact {
    private int ID;
    private String name;

    /**
     * Getter for ID
     * @return ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Setter for ID
     * @param ID integer identifier
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Getter for name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name
     * @param name String contact's name
     */
    public void setName(String name) {
        this.name = name;
    }

    public Contact(int ID, String name) {
        setID(ID);
        setName(name);
    }
}
