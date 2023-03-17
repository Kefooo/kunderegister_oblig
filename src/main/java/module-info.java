module com.kef.kunderegister {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.kef.kunderegister to javafx.fxml;
    exports com.kef.kunderegister;
}