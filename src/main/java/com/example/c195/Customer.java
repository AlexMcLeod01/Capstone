package com.example.c195;

/**
 * This class is to create a Customer object for easily moving data around the forms
 */
public class Customer {
    /**
     * Declare the components of the object
     */
    private int ID;
    private String name;
    private String address;
    private String postCode;
    private String phone;
    private String division;
    private String country;

    /**
     * ID setter
     * @param ID Customer ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * ID getter
     * @return Customer ID
     */
    public int getID() {
        return this.ID;
    }

    /**
     * name setter
     * @param name Customer name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * name getter
     * @return Customer name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Address setter
     * @param add Customer Address
     */
    public void setAddress(String add) {
        this.address = add;
    }

    /**
     * Address getter
     * @return Customer Address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Postal Code setter
     * @param post Customer Postal Code
     */
    public void setPostCode(String post) {
        this.postCode = post;
    }

    /**
     * Postal Code getter
     * @return Customer Postal Code
     */
    public String getPostCode() {
        return this.postCode;
    }

    /**
     * Phone setter
     * @param phone Customer Phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Phone getter
     * @return Customer Phone Number
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * Division setter
     * @param div Customer Division
     */
    public void setDivision(String div) {
        this.division = div;
    }

    /**
     * Division getter
     * @return Customer Division
     */
    public String getDivision() {
        return this.division;
    }

    /**
     * Country setter
     * @param country Customer country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Country getter
     * @return Customer country
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * Constructor for Customer object
     * @param ID ID
     * @param name Name
     * @param add Address
     * @param post Postal Code
     * @param phone Phone number
     * @param div Division name
     * @param country Country name
     */
    public Customer (int ID, String name,  String add, String post, String phone, String div, String country) {
        setID(ID);
        setName(name);
        setAddress(add);
        setPostCode(post);
        setPhone(phone);
        setDivision(div);
        setCountry(country);
    }
}
