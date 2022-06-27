package com.example.c195;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentsTest {
    Appointments a;

    @BeforeEach
    void setup() {
        a = new Appointments(1, "task", "done", "lol", "this",
                "started", "ended", 1, 1, 1);
    }

    @Test
    void getAppointmentID() {
        assertEquals(1, a.getAppointmentID());
        assertNotEquals(0, a.getAppointmentID());
    }

    @Test
    void setAppointmentID() {
        a.setAppointmentID(5);
        assertNotEquals(1, a.getAppointmentID());
        assertEquals(5, a.getAppointmentID());
        assertNotEquals(0, a.getAppointmentID());
    }

    @Test
    void getTitle() {
        assertEquals("task", a.getTitle());
        assertNotEquals("something else", a.getTitle());
    }

    @Test
    void setTitle() {
        a.setTitle("if I only had a brain");
        assertNotEquals("task", a.getTitle());
        assertEquals("if I only had a brain", a.getTitle());
        assertNotEquals("something else", a.getTitle());
    }

    @Test
    void getDescription() {
        assertEquals("done", a.getDescription());
        assertNotEquals("something else", a.getDescription());
    }

    @Test
    void setDescription() {
        a.setDescription("ho ho ho, merry christmas");
        assertNotEquals("done", a.getDescription());
        assertEquals("ho ho ho, merry christmas", a.getDescription());
        assertNotEquals("something else", a.getDescription());
    }

    @Test
    void getLocation() {
        assertEquals("lol", a.getLocation());
        assertNotEquals("something else", a.getLocation());
    }

    @Test
    void setLocation() {
        a.setLocation("here and there");
        assertNotEquals("lol", a.getLocation());
        assertEquals("here and there", a.getLocation());
        assertNotEquals("something else", a.getLocation());
    }

    @Test
    void getStart() {
        assertEquals("started", a.getStart());
        assertNotEquals("something else", a.getStart());
    }

    @Test
    void setStart() {
        a.setStart("not yet started");
        assertNotEquals("started", a.getStart());
        assertEquals("not yet started", a.getStart());
        assertNotEquals("something else", a.getStart());
    }

    @Test
    void getEnd() {
        assertEquals("ended", a.getEnd());
        assertNotEquals("something else", a.getEnd());
    }

    @Test
    void setEnd() {
        a.setEnd("not yet ended");
        assertNotEquals("ended", a.getEnd());
        assertEquals("not yet ended", a.getEnd());
        assertNotEquals("something else", a.getEnd());
    }

    @Test
    void getCustomerID() {
        assertEquals(1, a.getCustomerID());
        assertNotEquals(-1, a.getCustomerID());
    }

    @Test
    void setCustomerID() {
        a.setCustomerID(15);
        assertNotEquals(1, a.getCustomerID());
        assertEquals(15, a.getCustomerID());
        assertNotEquals(-1, a.getCustomerID());
    }

    @Test
    void getUserID() {
        assertEquals(1, a.getUserID());
        assertNotEquals(-1, a.getUserID());
    }

    @Test
    void setUserID() {
        a.setUserID(15);
        assertNotEquals(1, a.getUserID());
        assertEquals(15, a.getUserID());
        assertNotEquals(-1, a.getUserID());
    }

    @Test
    void getContactID() {
        assertEquals(1, a.getContactID());
        assertNotEquals(-1, a.getContactID());
    }

    @Test
    void setContactID() {
        a.setContactID(15);
        assertNotEquals(1, a.getContactID());
        assertEquals(15, a.getContactID());
        assertNotEquals(-1, a.getContactID());
    }

    @Test
    void getType() {
        assertEquals("this", a.getType());
        assertNotEquals("something else", a.getType());
    }

    @Test
    void setType() {
        a.setType("that");
        assertNotEquals("this", a.getType());
        assertEquals("that", a.getType());
        assertNotEquals("something else", a.getType());
    }

    @Test
    void matches() {

        assertEquals(true, a.matches("1"));
        assertNotEquals(true, a.matches("2"));

        assertEquals(true, a.matches("Ta"));
        assertEquals(true, a.matches("Tart"));
        assertNotEquals(true, a.matches("Love"));
    }
}