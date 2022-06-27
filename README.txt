# C195 & Capstone
Application Title: Appointment Management System
Purpose: The Appointment Management System allows users to set and modify appointments
Author: Harold Alex McLeod
Email: hmcleo5@wgu.edu
Version: 1.1
Date: Jun 27, 2022
IDE Version Number: IntelliJ IDEA 2021.2.4 (Community Edition)
JDK Version: 11.0.12
JavaFX Version: 18.0.1

How to run: Build and run program. Assuming all dependencies are in place, it will bring up the login screen.
There are two available users in the default database users table: test and admin. Password is the same as the username for both.
These two users are the only admin level users by default.

Successful login will bring you to the schedule view, which shows the months/weeks appointments depending upon which tab is
selected. The buttons at the bottom can bring you to the Customer Records view, the Appointment Records view and the Reports view.

The additional report for section A3f is a list of all appointments that a user has set or was the most recent user to modify.
Modifying an appointment set by another user will change the user id of that appointment to the most recent user to do such
modification.

Non-Admin users cannot modify appointments set by other users, or set appointments for other users.

MySQL Driver Version: mysql-connector-java-8.0.25.jar
