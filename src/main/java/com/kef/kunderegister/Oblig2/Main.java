package com.kef.kunderegister.Oblig2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Optional;
import java.util.Scanner;
import static java.lang.Integer.parseInt;
import static java.lang.System.out;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

/* Obligatorisk oppgave 2: Studentregister
 * Programmet kjører en Meny som utfører ulike funksjoner av studentregister.
 * Deretter blir informasjon hentet ut av en database og utfører metodene i menyen.
 * Laget av Kevin Tomasz Matarewicz
 */

public class Main {

    /*
     * Variabler for opphenting av dokumenter
     */

    public static final String STUDENTREGISTER_TXT = "studentregister.txt";
    public static final String STUDENTREGISTER_BACKUP_TXT = "studentregisterBackup.txt";
    public static final String SQLITE_STUDENT_DB = "jdbc:sqlite:student.db";

    static Connection con = null;
    private static File dbFil = new File("student.db");
    private static String SELECT_ALLE_STUDENT = "SELECT * FROM Student";

    /*
     * En switch metode for å deklarere oppgaven programmet skal utføre i menyen.
     */
    public static void main(String[] args) throws SQLException, IOException {
        opprettTabell();

        int valg;
        do {
            valg = visMeny();

            switch (valg) {
                case 1:
                    visAlleEtternavn();
                    break;
                case 2:
                    visAlleTelefonnr();
                    break;
                case 3:
                    visAlleStudenter();
                    break;
                case 4:
                    nyStudent();
                    break;
                case 5:
                    redigerStudent();
                    break;
                case 6:
                    slettStudent();
                    break;
                case 7:
                    taBackup();
                    break;
                case 8:
                    lastInnBackup();
                    break;
                case 0:
                    avsluttProgram();
                    break;
            }
        } while (valg != 0);
    }

    /*
     * Dialogvindu for brukeren.
     * Viser ulike funksjoner programmet kan utføre og hvordan aktivere de.
     */
    private static int visMeny() {
        // Utvid menyen med flere aktuelle valg
        String ut = "Velg oppgave" + "\n"
                + "[1]: Liste på etternavn" + "\n"
                + "[2]: Liste på telefonnummer" + "\n"
                + "[3]: Liste på Studenter" + "\n"
                + "[4]: Register ny student" + "\n"
                + "[5]: Rediger studentinformasjon" + "\n"
                + "[6]: Slett student" + "\n"
                + "[7]: Utfør backup av registeret" + "\n"
                + "[8]: Last opp backup av registeret" + "\n"
                + "[0]: Avslutt program" + "\n"
                + "Skrive kode for ditt valg:";

        String svar = showInputDialog(ut).toUpperCase();
        return parseInt(String.valueOf(svar.charAt(0)));
    }

    /*
     * Metoder for uthenting av meldinger og resultater i koden
     */
    private static void visAlleEtternavn() {
        visAlleNavn();
        showMessageDialog(null, "Viser alle studenter sortert på etternavn");
    }

    private static void visAlleNavn() {
        showMessageDialog(null, sorterEtternavn());
    }

    private static void visAlleTelefonnr() {
        visAlleTelefon();
        showMessageDialog(null, "Viser alle studentr sortert på tlfnr");
    }

    private static void visAlleTelefon() {
        showMessageDialog(null, sorterTelefonnr());
    }

    private static void visAlleVerv() {
        visAlleVerv();
        showMessageDialog(null, "Viser alle studenter sortert på studentnr");
    }

    private static void visAlleStudenter() {
        showMessageDialog(null, sorterVerv());
    }

    private static void nyStudent() {
        String resultat;
        if(!lagNyStudent()){
            resultat = "Feil!, studenten ble ikke opprettet";
        }else{
            resultat = "Student ble opprettet";
        }
        showMessageDialog(null, resultat);
    }

    private static void redigerStudent() {
        redigerStudentinformasjon();
        showMessageDialog(null, "Student informasjonen er redigert");
    }

    private static void redigerStudentinformasjon() {
        showMessageDialog(null, redigerInformasjon());
    }

    private static void slettStudent() {
        String svar = showInputDialog("Vil du slette studenten? [ja, nei]");

        if(svar.equalsIgnoreCase("JA")){
            int stnr = parseInt(showInputDialog("skriv StudentNr"));
            fjernStudent(stnr);
            showMessageDialog(null, "Studenten er slettet");
        }
        else {
            showMessageDialog(null, "Studenten ble ikke slettet");
        }

    }

//    private static void slettstudentinformasjon() {
//        showMessageDialog(null, slettStudent());
//    }

    private static void taBackup() {
        lagBackup();
    }

    private static void lagBackup() {
        try {
            showMessageDialog(null, lagreBackup());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void lastInnBackup() {
        hentBackup();
    }

    private static void hentBackup() {
        showMessageDialog(null, lastInnFraBackup());
    }

    private static void avsluttProgram() {
        lukkProgram();
        showMessageDialog(null, "Programmet er avslutet");
    }


    /************* DATABASEMETODER *************/
    //Oppretter tabel og database
    private static void opprettTabell() {
        try {
            if (!dbFil.exists()) {
                lagNyTabell(STUDENTREGISTER_TXT);
                visTabellrader();
                out.println("Student-tabellen er opprettet, ok!");
            }
        } catch (Exception e) {
            out.println("Databasefeil: " + e.toString());
        }
    }

    /*
     * Oppretter en ny tabel dersom det mangler.
     */
    private static void lagNyTabell(String fil) throws Exception {
        // Opprett forbindelsen til databasen
        Connection con = DriverManager.getConnection(SQLITE_STUDENT_DB);
        Statement stmt = con.createStatement();
        String sql = "create table Student(Stnr integer primary key, "
                + "Navn varchar(40), Epost varchar(25), Mob varchar(10), Verv varchar(40));";
        stmt.executeUpdate(sql);

        Scanner leser = new Scanner(Paths.get(STUDENTREGISTER_TXT));
        while (leser.hasNextLine()) {
            String linje = leser.nextLine();
            String[] dataTab = linje.split(";");
            int Stnr = parseInt(dataTab[0]);
            String navn = dataTab[1];
            String epost = dataTab[2];
            String mob = dataTab[3];
            String verv = dataTab[4];
            sql = "insert into Student values('" + Stnr + "','" + navn + "','" + epost + "','"
                    + mob + "','" + verv + "');";
            stmt.executeUpdate(sql);
        }
        leser.close();
        con.close();
        out.println("Opprettet tabellen!");
    }

    /*
     * Henter informasjonen fra databasen
     */
    private static void visTabellrader() throws Exception {
        String ut = "";
        // Opprett forbindelsen til databasen
        Connection con = DriverManager.getConnection(SQLITE_STUDENT_DB);
        Statement stmt = con.createStatement();
        String sql = "select * from Student;";
        // Utfør SQL-spørringen.
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) { // Behandle hver rad
            int Stnr = rs.getInt("Stnr");
            String navn = rs.getString("Navn");
            String epost = rs.getString("Epost");
            String mob = rs.getString("Mob");
            String verv = rs.getString("Verv");
            ut += Stnr + ": " + navn + "  " + epost + "  " + mob + "  " + verv + "\n";
        }
        con.close(); // Lukk forbindelsen til databasen
        showMessageDialog(null, ut);
    }

    /*
     * Metode for å sortere tabelen etter Etternavn
     */
    private static String sorterEtternavn() {
        String sql = SELECT_ALLE_STUDENT + " Order By navn ";
        String resultat = "Studenter sortert etter Etternavn: " + "\n";
        try {
            con = DriverManager.getConnection(SQLITE_STUDENT_DB);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int Stnr = rs.getInt(1);
                String navn = rs.getString(2);
                String epost = rs.getString(3);
                String mob = rs.getString(4);
                String verv = rs.getString(5);
                resultat += Stnr + ";" + navn + ";" + epost + ";" + mob + ";" + verv + "\n";
            }
            con.close();
        } catch (SQLException e) {
            resultat = "Feil ved henting av studenter: " + e.getMessage();
        }
        return resultat;
    }

    /*
     * Metode for å sortere tabelen etter Telefonnummer
     */
    private static String sorterTelefonnr() {
        String sql = SELECT_ALLE_STUDENT + " Order By Mobil ";
        String resultat = "Studenter sortert etter Telefonnummer: " + "\n";
        try {
            con = DriverManager.getConnection(SQLITE_STUDENT_DB);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int Stnr = rs.getInt(1);
                String navn = rs.getString(2);
                String epost = rs.getString(3);
                String mob = rs.getString(4);
                String verv = rs.getString(5);
                resultat += Stnr + ";" + navn + ";" + epost + ";" + mob + ";" + verv + "\n";
            }
            con.close();
        } catch (SQLException e) {
            resultat = "Feil ved henting av studenter: " + e.getMessage();
        }
        return resultat;
    }

    /*
     * Metode for å sortere tabelen etter studentnummer
     */
    private static String sorterVerv() {
        String sql = SELECT_ALLE_STUDENT + " Order By verv ";
        String resultat = "Studenter sortert etter vervr: " + "\n";
        try {
            con = DriverManager.getConnection(SQLITE_STUDENT_DB);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int Stnr = rs.getInt(1);
                String navn = rs.getString(2);
                String epost = rs.getString(3);
                String mob = rs.getString(4);
                String verv = rs.getString(5);
                resultat += Stnr + ";" + navn + ";" + epost + ";" + mob + ";" + verv + "\n";
            }
            con.close();
        } catch (SQLException e) {
            resultat = "Feil ved henting av studenter: " + e.getMessage();
        }
        return resultat;
    }

    /*
     * Metode for å opprette en ny student og legge til informasjon i databasen.
     */
    private static boolean lagNyStudent() {
        String avslutt = "";
        boolean ok = false;
        try(Connection con = DriverManager.getConnection(SQLITE_STUDENT_DB)){
            do {
                int Stnr = parseInt(showInputDialog(null, "Skriv inn StudentNr"));
                String navn = showInputDialog(null, "Oppgi navn (etternavn, fornavn)");
                String epost = showInputDialog(null, "Oppgi epost");
                String mobil = showInputDialog(null, "Oppgi mobil");
                String verv = showInputDialog(null, "Oppgi verv");
                avslutt = showInputDialog(null, "vil du avslutte?");
                String sql = "INSERT INTO Student(Stnr, Navn, Epost, Mobil, verv) " +
                        "VALUES ( "+ Stnr +",";
                if(!navn.isEmpty()){
                    sql += "'"+ navn + "', ";
                }
                if(!epost.isEmpty()){
                    sql += "'" + epost + "', ";
                }
                if(!mobil.isEmpty()){
                    sql += "'" + mobil + "', ";
                }else{
                    sql += "'-', ";
                }
                if(!verv.isEmpty()){
                    sql += "'" + verv + "')";
                }else{
                    sql += "'-')";
                }
                sql += " on conflict(Stnr) do update set Navn = excluded.Navn, Epost = excluded.Epost, Mobil = excluded.Mobil, Verv = excluded.Verv";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(sql);
                ok = true;
            } while (!avslutt.equalsIgnoreCase("ja"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ok;
    }


    /*
     * Metode for å redigere informasjonen til en student i databasen
     */
    private static String redigerInformasjon() {

        String resultat = "";
        try(Connection con = DriverManager.getConnection(SQLITE_STUDENT_DB)){

            String redigerStudent = showInputDialog("Oppgi studentnr du vil oppdatere");
            int stnr = Integer.parseInt(redigerStudent);
            String navn = showInputDialog("Legg til et navn");
            String epost = showInputDialog("Legg til en epost");
            String mobil = showInputDialog("Legg til en mobil");
            String verv = showInputDialog("Legg til et verv");


            String sql = "UPDATE student SET navn = ?, epost = ?, mobil = ?, verv = ? WHERE stnr = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, navn);
            pstm.setString(2, epost);
            pstm.setString(3, mobil);
            pstm.setString(4, verv);
            pstm.setInt(5, stnr);
            pstm.executeUpdate();

            resultat = showInputDialog("Studentinformasjonen er oppdatert");

        } catch (SQLException e) {
            System.out.println("Feil ved oppdatering av Studenten: " + e.getMessage());
        }
        return resultat;

    }

    /*
     * Metode for å slette en student fra databasen
     */
    private static String fjernStudent(int stnr){
        String url = "jdbc:sqlite:student.db";
        String resultat = "";
        String sql = "DELETE FROM Student WHERE stnr = ?";

        try(Connection con = DriverManager.getConnection(url)){

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setInt(1, stnr);
            pstm.executeUpdate();
            resultat = "Studenten har blitt slettet";

        } catch (SQLException e) {
            resultat = "Feil ved sletting av Student: " + e.getMessage();
        }
        return resultat;
    }

    /*
     * Henter opp nåværende informasjon fra databasen og
     * oppretter en backupfil.
     */
    private static String lagreBackup() throws Exception {
        String sql = "SELECT * FROM student";
        String fileName = STUDENTREGISTER_BACKUP_TXT;
        String resultat = " ";
        FileWriter writer = new FileWriter(fileName, false);
        try{
            con = DriverManager.getConnection(SQLITE_STUDENT_DB);
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                int knr = rs.getInt(1);
                String navn = rs.getString(2);
                String adresse = rs.getString(3);
                String epost = rs.getString(4);
                String tlfnr = rs.getString(5);

                String linje = knr+ ";" + navn +";"+adresse+";"+epost+";"+tlfnr+"\n";
                writer.write(linje);
            }

            writer.close();
            resultat = "Backup er lagret!";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return resultat;
    }

    /*
     * Laster inn backupfilen
     */
    private static String lastInnFraBackup() {
        String linje;
        String resultat = " ";
        String sql = "INSERT INTO student(stnr, Navn, Adresse, Epost, Tlfnr) VALUES (?,?,?,?,?) on conflict (knr) do update set Navn= excluded.Navn, Adresse = excluded.Adresse, Epost = excluded.Epost, Tlfnr = excluded.Tlfnr";
        //"update student set Knr = ?, Navn = ?, Adresse =?, Epost =?, Tlfnr = ? where Knr = ?";
        try {
            con = DriverManager.getConnection(SQLITE_STUDENT_DB);
            Scanner leser = new Scanner(new File(STUDENTREGISTER_BACKUP_TXT));
            PreparedStatement pstm = con.prepareStatement(sql);
            while (leser.hasNextLine()) {
                linje = leser.nextLine();
                String[] studentTabell = linje.split(";");

                int kNr = Integer.parseInt(studentTabell[0]);
                String navn = studentTabell[1];
                String adresse = studentTabell[2];
                String epost = studentTabell[3];
                String tlfNr = studentTabell[4];

                pstm.setInt(1, kNr);
                pstm.setString(2, navn);
                pstm.setString(3, adresse);
                pstm.setString(4, epost);
                pstm.setString(5, tlfNr);

                pstm.executeUpdate();
            }
            leser.close();
            resultat = "Suksess: Lastet data fra backup";
        } catch (FileNotFoundException e) {
            resultat = "Feil ved innlesing fra backupfil. Feilmeding: " + e.getMessage();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultat;
    }

    /*
     * Lukker menyen
     */
    private static boolean lukkProgram(){
        return false;
    }

}