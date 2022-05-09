package com.example.c195;

/**
 * A helper object to organize Division data
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class Divisions {
    private int ID;
    private String name;
    private int countryID;

    /**
     * Getter for ID
     * @return int ID
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
     * Getter for name
     * @return Division name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for country ID
     * @return countryID int
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * Setter for country ID
     * @param countryID int
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * Constructor
     * @param id division ID
     * @param name division name
     * @param cID country ID
     */
    public Divisions (int id, String name, int cID) {
        setID(id);
        setName(name);
        setCountryID(cID);
    }
}
