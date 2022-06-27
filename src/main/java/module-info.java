module com.example.c195 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.junit.jupiter.api;
    requires org.junit.platform.commons;


    opens com.example.c195 to javafx.fxml, org.junit.platform.commons, org.junit.platform.engine;
    exports com.example.c195;
}