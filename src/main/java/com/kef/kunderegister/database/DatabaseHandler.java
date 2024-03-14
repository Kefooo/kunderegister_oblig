package com.kef.kunderegister.database;

import com.kef.kunderegister.model.Kunde;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;
import java.util.Scanner;

/*
* @author: Kef
* Klasse som skal håndtere databasespørringer
* */

public class DatabaseHandler{

    /**
     * Metode som henter alle kunder fra databasen
     * @return Array med kunde objekter
     */
    public Kunde[] selectAllKunder(){
        String sql = "SELECT * FROM Kunde";
        Kunde[] kundeListe = new Kunde[500];
        try{

            PreparedStatement pstm = getConnection().prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            int i = 0;
            while(rs.next()){
                    int kNr = rs.getInt(1);
                    String navn = rs.getString(2);
                    String adresse = rs.getString(3);
                    String epost = rs.getString(4);
                    String tlfNr = rs.getString(5);
                    kundeListe[i] = new Kunde(kNr, navn, adresse, epost, tlfNr);
                i++;
            }
        } catch (SQLException e) {
            System.out.println("Feil ved henting av kundene: " + e.getMessage());
        }
        return kundeListe;
    }

    /**
     * Metode for å lagre kunden i databasen.
     * @param kunde Får inn Kunde objekt som skal bli lagret i databasen.
     * @return Returnerer false om kunden ikke ble lagret, er true om den ble lagret.
     */
    public boolean insertKunde(Kunde kunde){
        String sql = "INSERT INTO Kunde VALUES(?,?,?,?,?)";

        try{
            PreparedStatement pstm = getConnection().prepareStatement(sql);
            pstm.setInt(1, kunde.getkNr());
            pstm.setString(2, kunde.getNavn());
            pstm.setString(3, kunde.getAdresse());
            pstm.setString(4, kunde.getEpost());
            pstm.setString(5, kunde.getTlfNr());
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Feil ved innsetting av Kunde: " + e.getMessage());
        }
        return false;
    }

    /**
     * Metode som skal slette kunden
     * @param kNr
     * @return true om kunden ble slettet. False om det var feil
     */
    public boolean deleteKundeByKNr(int kNr){
        String sql = "DELETE FROM Kunde WHERE kNr = ?";
        try{
            PreparedStatement pstm = getConnection().prepareStatement(sql);
            pstm.setInt(1, kNr);
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Feil ved sletting av Kunde: " + e.getMessage());
        }
        return false;
    }

    public boolean updateKunde(Kunde kunde){
        String sql = "UPDATE Kunde SET navn = ?, adresse = ?, epost = ?, tlfNr = ? WHERE kNr = ?";
        try{
            PreparedStatement pstm = getConnection().prepareStatement(sql);
            pstm.setString(1, kunde.getNavn());
            pstm.setString(2, kunde.getAdresse());
            pstm.setString(3, kunde.getEpost());
            pstm.setString(4, kunde.getTlfNr());
            pstm.setInt(5, kunde.getkNr());
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Feil ved oppdatering av Kunde: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lagrer kunde til fil. Oppretter mappe om den ikke finnes
     * @return True om alt gikk bra, ellers False
     */
    public boolean lagreBackup(){
        Kunde[] kundeListe = selectAllKunder();
        String fileName = "kunderegisterBackup.txt";
        try{
            File directory = new File("src/main/resources/backup");
            if(!directory.exists()){
                directory.mkdirs();
            }
            File file = new File( directory, fileName);
            FileWriter writer = new FileWriter(file, false);
            for(Kunde kunde: kundeListe){
                if(kunde != null){
                    String linje = kunde.getkNr()+ ";" + kunde.getNavn()+";"+kunde.getAdresse()+";"+kunde.getEpost()+";"+kunde.getTlfNr()+"\n";
                    writer.write(linje);
                }
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    /**
     * Henter data fra fil
     * @return True om alt kjørte riktig, else False
     */
    public boolean lastInnFraBackup(){
        String linje;
        try{
            Path pathname = Path.of("src/main/resources/backup/kunderegisterBackup.txt");
            Scanner leser = new Scanner(pathname);
            while(leser.hasNextLine()){
                linje = leser.nextLine();
                String[] kundeTabell = linje.split(";");

                int kNr = Integer.parseInt(kundeTabell[0]);
                String navn = kundeTabell[1];
                String adresse = kundeTabell[2];
                String epost = kundeTabell[3];
                String tlfNr  = kundeTabell[4];
                insertKunde(new Kunde(kNr, navn, adresse, epost, tlfNr));
            }
            leser.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Feil ved innlesing fra backupfil. Feilmeding: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;

    }
    public boolean close(){
        return false;
    }


    /**
     * Metode for å opprette kundetabellen
     * @return true if om tabbelen er opprettet
     */
    public boolean createKundeTabell(){

        String sql = " CREATE TABLE if not exists Kunde(" +
                "kNr integer primary key AUTOINCREMENT," +
                "navn varchar(70),"+
                "adresse varchar(50)," +
                "epost varchar(50)," +
                "tlfNr varchar(8)" +
                ")";
        try{
            PreparedStatement pstm = getConnection().prepareStatement(sql);
            pstm.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Fikk ikke opprettet kundetabellen: " + e.getMessage());
        }
        return false;
    }
    /**
     * Lager forbindelse til Sqlite
     * @return Connection
     */
    private Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
}