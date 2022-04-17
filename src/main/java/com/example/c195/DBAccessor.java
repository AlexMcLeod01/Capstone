package com.example.c195;

import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class is used to abstract the Database functions from the controller.
 * I wanted to build everything separately, test that it is working first, and
 * then change the code here to actually pull data from the database.
 * @author Harold Alex McLeod
 * @version 1.0
 */
public final class DBAccessor {
    private ZoneId zone;
    private Locale local;
    private ResourceBundle bundle;

    /**
     * initializes INSTANCE as the singleton DBAccessor
     */
    private final static DBAccessor INSTANCE = new DBAccessor();

    /**
     * Gets an instance of the singleton DBAccessor
     * @return an instance
     */
    public static DBAccessor getInstance() {
        return INSTANCE;
    }

    /**
     * gets the timezone
     * @return
     */
    public ZoneId getZone() {
        return zone;
    }

    /**
     * gets the locale
     * @return
     */
    public Locale getLocal() {
        return local;
    }

    /**
     * gets the displayed text in user's language
     * @return
     */
    public ResourceBundle getMsg() { return bundle; }

    /**
     * Checks to see if username is in database
     * @param user
     * @return true if username is in database
     */
    public boolean userExists(String user) {
        if (user.equals("Admin")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks to see if username and password pair are in database
     * @param user
     * @param pass
     * @return true if username and password match pair in database
     */
    public boolean userPass(String user, String pass) {
        if (user.equals("Admin") && pass.equals("12345")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * The constructor should eventually initialize the database connection
     * and any global data that needs to be passed around
     */
    public DBAccessor () {
        zone = ZoneId.systemDefault();
        local = Locale.getDefault();
        bundle = ResourceBundle.getBundle("com.example.c195/MessagesBundle", local);
    }
}
