module com.example.chat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;


    opens com.example.app to javafx.base, javafx.fxml;
    opens com.example.models to javafx.base;
    opens com.example.protocols to com.fasterxml.jackson.databind;
    exports com.example.app;

}