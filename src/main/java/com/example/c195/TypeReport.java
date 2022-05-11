package com.example.c195;


import java.time.LocalDate;

/**
 * This is a helper class for formating data for the totalTypeReportTable view
 * @author Harold Alex McLeod
 * @version 1.0
 */
public class TypeReport {
    private String type;
    private String month;
    private int number;

    /**
     * Getter for type
     * @return String type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for type
     * @param type a String - the type of appointment
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for month
     * @return the LocalDate month
     */
    public String getMonth() {
        return month;
    }

    /**
     * Setter for month
     * @param month as a LocalDate
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * Getter for number
     * @return number - the number of appointments
     */
    public int getNumber() {
        return number;
    }

    /**
     * Setter for number
     * @param number - the number of appointments
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Constructor for object TypeReport
     * @param type
     * @param month
     * @param number
     */
    public TypeReport(String type, String month, int number) {
        setType(type);
        setMonth(month);
        setNumber(number);
    }
}
