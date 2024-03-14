package com.kef.kunderegister.database;

import com.kef.kunderegister.model.Kunde;

import static java.lang.System.*;
import static javax.swing.JOptionPane.*;
import static java.lang.Integer.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.sql.*;

/* Obligatorisk oppgave 2: Studentregister
 * Programmet kjører en Meny som utfører ulike funksjoner av studentregister.
 * Deretter blir informasjon hentet ut av en database og utfører metodene i menyen.
 * Laget av Kevin Tomasz Matarewicz
 */

public class Oblig2 {

    /*
     * Variabler for opphenting av dokumenter
     */

    public static final String KUNDEREGISTER_TXT = "kunderegister.txt";
    public static final String KUNDEREGISTER_BACKUP_TXT = "kunderegisterBackup.txt";
    static String url = "jdbc:sqlite:testkunde.db";
    static Connection con = null;
    private static File dbFil = new File("testkunde.db");
    private static String SELECT_ALLE_KUNDER = "SELECT * FROM Kunde";

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
                    visAlleKundenr();
                    break;
                case 4:
                    nyKunde();
                    break;
                case 5:
                    redigerKunde();
                    break;
                case 6:
                    slettKunde();
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
                + "[3]: Liste på kundenummer" + "\n"
                + "[4]: Register ny kunde" + "\n"
                + "[5]: Rediger kundeinformasjon" + "\n"
                + "[6]: Slett kunde" + "\n"
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
        showMessageDialog(null, "Viser alle kunder sortert på etternavn");
    }

    private static void visAlleNavn() {
        showMessageDialog(null, sorterEtternavn());
    }

    private static void visAlleTelefonnr() {
        visAlleTelefon();
        showMessageDialog(null, "Viser alle kunder sortert på tlfnr");
    }

    private static void visAlleTelefon() {
        showMessageDialog(null, sorterTelefonnr());
    }

    private static void visAlleKundenr() {
        visAlleKunder();
        showMessageDialog(null, "Viser alle kunder sortert på kundenr");
    }

    private static void visAlleKunder() {
        showMessageDialog(null, sorterKundernr());
    }

    private static void nyKunde() {
        String resultat;
        if(!lagNyKunde()){
            resultat = "Feil!, kunden ble ikke opprettet";
        }else{
            resultat = "Kunden ble opprettet";
        }
        showMessageDialog(null, resultat);
    }

    private static void redigerKunde() {
        redigerKundeinformasjon();
        showMessageDialog(null, "Kunde informasjonen er redigert");
    }

    private static void redigerKundeinformasjon() {
        showMessageDialog(null, redigerInformasjon());
    }

    private static void slettKunde() {
        String svar = showInputDialog("Vil du slette kunden? [ja, nei]");
        svar = svar.toUpperCase();

        if(svar.equals("JA")){
            int knr = parseInt(showInputDialog("skriv knr"));
            slettKundeinformasjon(knr);
            showMessageDialog(null, "Kunden er slettet");
        }
        else {
            showMessageDialog(null, "Kunden ble ikke slettet");
        }

    }

    private static void slettKundeinformasjon(int knr) {
            showMessageDialog(null, fjernKunde(knr));
    }

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

    //KODESKJELETT TIL ROY
    //Oppretter tabel og database
    private static void opprettTabell() {
        try {
            if (!dbFil.exists()) {
                lagNyTabell(KUNDEREGISTER_TXT);
                visTabellrader();
                out.println("Kunde-tabellen er opprettet, ok!");
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
        Connection con = DriverManager.getConnection(url);
        Statement stmt = con.createStatement();
        String sql = "create table Kunde(knr integer primary key, "
                + "Navn varchar(40), Adresse varchar(40), Epost varchar(25), Tlfnr varchar(10));";
        stmt.executeUpdate(sql);

        Scanner leser = new Scanner(Paths.get(KUNDEREGISTER_TXT));
        while (leser.hasNextLine()) {
            String linje = leser.nextLine();
            String[] dataTab = linje.split(";");
            int knr = parseInt(dataTab[0]);
            String navn = dataTab[1];
            String adresse = dataTab[2];
            String epost = dataTab[3];
            String tlfnr = dataTab[4];
            sql = "insert into Kunde values('" + knr + "','" + navn + "','" + adresse + "','"
                    + epost + "','" + tlfnr + "');";
            stmt.executeUpdate(sql);
        }
        leser.close();
        con.close();
        out.println("Opprettet tabellen Kunde");
    }

    /*
     * Henter informasjonen fra databasen
     */
    private static void visTabellrader() throws Exception {
        String ut = "";
        // Opprett forbindelsen til databasen
        Connection con = DriverManager.getConnection(url);
        Statement stmt = con.createStatement();
        String sql = "select * from Kunde;";
        // Utfør SQL-spørringen.
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) { // Behandle hver rad
            int knr = rs.getInt("Knr");
            String navn = rs.getString("Navn");
            String adresse = rs.getString("Adresse");
            String epost = rs.getString("Epost");
            String tlfnr = rs.getString("Tlfnr");
            ut += knr + ": " + navn + "  " + adresse + "  " + epost + "  " + tlfnr + "\n";
        }
        con.close(); // Lukk forbindelsen til databasen
        showMessageDialog(null, ut);
    }

    /*
     * Metode for å sortere tabelen etter Etternavn
     */
    private static String sorterEtternavn() {
        String sql = SELECT_ALLE_KUNDER + " Order By navn ";
        String resultat = "Kunder sortert etter Etternavn: " + "\n";
        try {
            con = DriverManager.getConnection(url);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int knr = rs.getInt(1);
                String navn = rs.getString(2);
                String adresse = rs.getString(3);
                String epost = rs.getString(4);
                String tlf = rs.getString(5);
                resultat += knr + ";" + navn + ";" + adresse + ";" + epost + ";" + tlf + "\n";
            }
            con.close();
        } catch (SQLException e) {
            resultat = "Feil ved henting av kunder: " + e.getMessage();
        }
        return resultat;
    }

    /*
     * Metode for å sortere tabelen etter Telefonnummer
     */
    private static String sorterTelefonnr() {
        String sql = SELECT_ALLE_KUNDER + " Order By tlfnr ";
        String resultat = "Kunder sortert etter Telefonnummer: " + "\n";
        try {
            con = DriverManager.getConnection(url);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int knr = rs.getInt(1);
                String navn = rs.getString(2);
                String adresse = rs.getString(3);
                String epost = rs.getString(4);
                String tlf = rs.getString(5);
                resultat += knr + ";" + navn + ";" + adresse + ";" + epost + ";" + tlf + "\n";
            }
            con.close();
        } catch (SQLException e) {
            resultat = "Feil ved henting av kunder: " + e.getMessage();
        }
        return resultat;
    }

    /*
     * Metode for å sortere tabelen etter Kundenummer
     */
    private static String sorterKundernr() {
        String sql = SELECT_ALLE_KUNDER + " Order By knr ";
        String resultat = "Kunder sortert etter Kundenummer: " + "\n";
        try {
            con = DriverManager.getConnection(url);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int knr = rs.getInt(1);
                String navn = rs.getString(2);
                String adresse = rs.getString(3);
                String epost = rs.getString(4);
                String tlf = rs.getString(5);
                resultat += knr + ";" + navn + ";" + adresse + ";" + epost + ";" + tlf + "\n";
            }
            con.close();
        } catch (SQLException e) {
            resultat = "Feil ved henting av kunder: " + e.getMessage();
        }
        return resultat;
    }

    /*
     * Metode for å opprette en ny Kunde og legge til informasjon i databasen.
     */
    private static boolean lagNyKunde() {
        url = "jdbc:sqlite:testkunde.db";
        String avslutt = "";
        boolean ok = false;

        do {
            int knr = parseInt(showInputDialog(null, "Skriv inn knr"));
            String navn = showInputDialog(null, "Oppgi navn (etternavn, fornavn)");
            String adresse = showInputDialog(null, "Oppgi adresse");
            String epost = showInputDialog(null, "Oppgi epost");
            String tlfnr = showInputDialog(null, "Oppgi tlfnr");
            avslutt = showInputDialog(null, "vil du avslutte?");
            String sql = "INSERT INTO Kunde(Knr, Navn, Adresse, Epost, Tlfnr) " +
                    "VALUES ( "+ knr +",";
            if(!navn.isEmpty()){
                sql += "'"+ navn + "', ";
            }
            if(!adresse.isEmpty()){
                sql += "'" + adresse + "', ";
            }
            if(!epost.isEmpty()){
                sql += "'" + epost + "', ";
            }else{
                sql += "'-', ";
            }
            if(!tlfnr.isEmpty()){
                sql += "'" + tlfnr + "')";
            }else{
                sql += "'-')";
            }
            sql += " on conflict(knr) do update set Navn = excluded.Navn, Adresse = excluded.Adresse, Epost = excluded.Epost, Tlfnr = excluded.Tlfnr";

            try(Connection con = DriverManager.getConnection(url)){
                Statement stmt = con.createStatement();
                stmt.executeUpdate(sql);
                ok = true;
            } catch (SQLException e) {
                System.out.println("Kunden ble ikke opprettet: " + e.getMessage());
            }
        } while (!avslutt.equals("ja"));
        return ok;
    }


/*
 * Metode for å redigere informasjonen til en kunde i databasen
 */
    private static String redigerInformasjon() {
        String url = "jdbc:sqlite:testkunde.db";
        String resultat = "";
        try(Connection con = DriverManager.getConnection(url)){

            String redigerKunde = showInputDialog("Oppgi kundernr du vil oppdatere");
            int knr = Integer.parseInt(redigerKunde);
            String navn = showInputDialog("Legg til et navn");
            String adresse = showInputDialog("Legg til en adresse");
            String epost = showInputDialog("Legg til en epost");
            String tlfnr = showInputDialog("Legg til et telefonnummer");


            String sql = "UPDATE kunde SET navn = ?, adresse = ?, epost = ?, tlfNr = ? WHERE knr = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, navn);
            pstm.setString(2, adresse);
            pstm.setString(3, epost);
            pstm.setString(4, tlfnr);
            pstm.setInt(5, knr);
            pstm.executeUpdate();

            resultat = showInputDialog("Kundeinformasjonen er oppdatert");

        } catch (SQLException e) {
            System.out.println("Feil ved oppdatering av Kunde: " + e.getMessage());
        }
    return resultat;

    }

/*
 * Metode for å slette en kunde fra databasen
 */
    private static String fjernKunde(int knr){
        String url = "jdbc:sqlite:testkunde.db";
        String resultat = "";
        String sql = "DELETE FROM Kunde WHERE knr = ?";

        try(Connection con = DriverManager.getConnection(url)){

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setInt(1, knr);
            pstm.executeUpdate();
            resultat = "Kunden har blitt slettet";

        } catch (SQLException e) {
            resultat = "Feil ved sletting av Kunde: " + e.getMessage();
        }
    return resultat;
    }

    /*
     * Henter opp nåværende informasjon fra databasen og
     * oppretter en backupfil.
     */
    private static String lagreBackup() throws Exception {
        String sql = "SELECT * FROM kunde";
        String fileName = KUNDEREGISTER_BACKUP_TXT;
        String resultat = " ";
        FileWriter writer = new FileWriter(fileName, false);
        try{
            con = DriverManager.getConnection(url);
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
        String sql = "INSERT INTO kunde(knr, Navn, Adresse, Epost, Tlfnr) VALUES (?,?,?,?,?) on conflict (knr) do update set Navn= excluded.Navn, Adresse = excluded.Adresse, Epost = excluded.Epost, Tlfnr = excluded.Tlfnr";
                //"update Kunde set Knr = ?, Navn = ?, Adresse =?, Epost =?, Tlfnr = ? where Knr = ?";
        try {
            con = DriverManager.getConnection(url);
            Scanner leser = new Scanner(new File(KUNDEREGISTER_BACKUP_TXT));
            PreparedStatement pstm = con.prepareStatement(sql);
            while (leser.hasNextLine()) {
                linje = leser.nextLine();
                String[] kundeTabell = linje.split(";");

                int kNr = Integer.parseInt(kundeTabell[0]);
                String navn = kundeTabell[1];
                String adresse = kundeTabell[2];
                String epost = kundeTabell[3];
                String tlfNr = kundeTabell[4];

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