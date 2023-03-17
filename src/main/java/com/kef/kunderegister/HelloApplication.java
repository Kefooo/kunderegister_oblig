package com.kef.kunderegister;

import com.kef.kunderegister.controller.KundeController;
import com.kef.kunderegister.database.DatabaseHandler;
import com.kef.kunderegister.model.Kunde;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.sql.*;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        KundeController controller = new KundeController(new DatabaseHandler());
        Button add = new Button("add");
        add.setOnAction(e ->{
            controller.addKunde(new Kunde(56,"Kim A",  "4314 Oppedal","pompel@pil.com", "12345678"));
        });
        BorderPane mainPanel = new BorderPane();
        mainPanel.setCenter(add);
        Scene scene = new Scene(mainPanel, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}