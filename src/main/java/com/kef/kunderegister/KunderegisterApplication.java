package com.kef.kunderegister;

import com.kef.kunderegister.controller.KundeController;
import com.kef.kunderegister.database.DatabaseHandler;
import com.kef.kunderegister.model.Kunde;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.stream.Collectors;

public class KunderegisterApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        KundeController controller = new KundeController(new DatabaseHandler());
//        Button add = new Button("add");
//        add.setOnAction(e ->{
//            controller.addKunde(new Kunde());
//        });

        TableView<Kunde> kundeTableView = new TableView<>();
        TableColumn<Kunde, Integer> kNrColumn = new TableColumn<>("KundeNr");
        kNrColumn.setMinWidth(50);
        kNrColumn.setCellValueFactory(new PropertyValueFactory<>("kNr"));

        TableColumn<Kunde, String> navnColumn = new TableColumn<>("Navn");
        navnColumn.setMinWidth(50);
        navnColumn.setCellValueFactory(new PropertyValueFactory<>("navn"));

        TableColumn<Kunde, String> adresseColumn = new TableColumn<>("Adresse");
        adresseColumn.setMinWidth(50);
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        TableColumn<Kunde, String> epostColumn = new TableColumn<>("Epost");
        epostColumn.setMinWidth(50);
        epostColumn.setCellValueFactory(new PropertyValueFactory<>("epost"));

        TableColumn<Kunde, String> tlfNrColumn = new TableColumn<>("Telefon");
        tlfNrColumn.setMinWidth(50);
        tlfNrColumn.setCellValueFactory(new PropertyValueFactory<>("tlfNr"));

        ObservableList<Kunde> list = FXCollections.observableArrayList(controller.getKunder());

        kundeTableView.setItems(list);
        kundeTableView.getColumns().addAll(kNrColumn, navnColumn, adresseColumn, epostColumn, tlfNrColumn);



        GridPane gridPane = new GridPane();

        BorderPane mainPanel = new BorderPane();
        mainPanel.setCenter(kundeTableView);
        Scene scene = new Scene(mainPanel, 250, 250);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

