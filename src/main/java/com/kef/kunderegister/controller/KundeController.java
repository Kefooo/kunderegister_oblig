package com.kef.kunderegister.controller;


/*
 * @author Kef
 *
 * Klasse som skal koble sammen GUI-et og databasespørringene
 *
 * */
import com.kef.kunderegister.database.DatabaseHandler;
import com.kef.kunderegister.model.Kunde;
import javafx.scene.control.TableView;


public class KundeController {

    private final DatabaseHandler databaseHandler;

    public KundeController(DatabaseHandler databaseHandler){
        this.databaseHandler = databaseHandler;
    }

    public Kunde[] getKunder(){
        return this.databaseHandler.selectAllKunder();
    }

    public boolean addKunde(Kunde kunde){
        return this.databaseHandler.insertKunde(kunde);
    }

    public boolean deleteKunde(int kNr){
        return this.databaseHandler.deleteKundeByKNr(kNr);
    }

    public boolean updateKunde(Kunde kunde){
        return this.databaseHandler.updateKunde(kunde);
    }

    public boolean saveToFile(){
        return this.databaseHandler.lagreBackup();
    }

    public boolean retrieveFromBackup(){
        return this.databaseHandler.lastInnFraBackup();
    }

    public boolean opprettKundeTabell(){
        return this.databaseHandler.createKundeTabell();
    }

    public void refresh(TableView<Kunde> kundeTabell){
        kundeTabell.refresh();
    }
}
