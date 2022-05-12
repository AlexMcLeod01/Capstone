package com.example.c195;

/**
 * A helper object for the User Appointments Changed/Set Report
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class UserReport {
    private int userID;
    private String type;
    private String month;
    private int number;

    /**
     * Getter for User_Id
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Setter for userID
     * @param userID int
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Getter for type
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for type
     * @param type String
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for month
     * @return month String
     */
    public String getMonth() {
        return month;
    }

    /**
     * Setter for month
     * @param month String
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * Getter for number
     * @return int number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Setter for number
     * @param number int
     */
    public void setNumber(int number) {
        this.number = number;
    }

    public UserReport(int id, String type, String month, int num) {
        setUserID(id);
        setType(type);
        setMonth(month);
        setNumber(num);
    }
}
