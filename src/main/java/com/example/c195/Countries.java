package com.example.c195;

/**
 * Another helper class to pass Country data around
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class Countries {
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
     * @param ID int
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Getter for country name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for country name
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Simple constructor for Countries
     * @param id
     * @param name
     */
    public Countries(int id, String name) {
        setID(id);
        setName(name);
    }
}
