module com.kef.kunderegister {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.kef.kunderegister to javafx.fxml;
    opens com.kef.kunderegister.model;
    exports com.kef.kunderegister;
}