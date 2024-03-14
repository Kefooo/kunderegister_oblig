//package com.kef.kunderegister.database;
//
///*
// * Oblig 2: Bestilling av Kunderegister
// *
// * Koden er laget av Kevin Tomasz Matarewicz
// *
// */
//
//        import static java.lang.System.*;
//        import static javax.swing.JOptionPane.*;
//        import static java.lang.Integer.*;
//        import java.util.*;
//        import java.io.*;
//        import java.sql.*;
//
//
//public class Kunderegister {
//
//    private static String url = "jdbc:sqlite:kunder.db";
//    private static Connection conn = null;
//
//    /*
//     * En switch metode for valg av funksjoner i menyen
//     *
//     */
//
//    public static void main(String[] args) {
//        int valg = 0;
//        do {
//            valg = meny();
//            switch (valg) {
//                case 1:
//                    listeEtternavn();
//                    break;
//                case 2:
//                    listeTelefon();
//                    break;
//                case 3:
//                    listeKunder();
//                    break;
//                case 4:
//                    nyKunde();
//                    break;
//                case 5:
//                    redigerKunde();
//                    break;
//                case 6:
//                    slettKunde();
//                    break;
//                case 7:
//                    taBackup();
//                    break;
//                case 8:
//                    lastInnBackup();
//                    break;
//                case 9:
//                    avsluttProgram();
//                    break;
//                default:
//                    break;
//            }
//        } while (valg != 0);
//    }
//
//    /*
//     * Dialogvindu for menyen
//     *
//     */
//
//    private static Integer meny() {
//        String meny = "Velg oppgave" + "\n"
//                + "[1]: Liste på etternavn" + "\n"
//                + "[2]: Liste på telefonnummer" + "\n"
//                + "[3]: Liste på kundenummer" + "\n"
//                + "[4]: Register ny kunde" + "\n"
//                + "[5]: Rediger kundeinformasjon" + "\n"
//                + "[6]: Slett kunde" + "\n"
//                + "[7]: Utfør backup av registeret" + "\n"
//                + "[8]: Last opp backup av registeret" + "\n"
//                + "[9]: Avslutt program" + "\n"
//                + "Skrive kode for ditt valg:";
//
//        String menysvar = showInputDialog(svar);
//    }
//
//    private static void listeEtternavn() {
//        sortertEtternavn();
//        showMessageDialog(null, "Liste sortert etter Etternavn");
//    }
//
//    private static void listeTelefon() {
//        sortertTelefonnummer();
//        showMessageDialog(null, "Liste sortert etter Telefonnummer");
//    }
//
//    private static void listeKunder() {
//        sortertKundenummer();
//        showMessageDialog(null, "Liste sortert etter Kundenummer");
//    }
//
//    private static void nyKunde() {
//        sortertKundenummer();
//        showMessageDialog(null, "Ny Kunde er registeret");
//    }
//
//    private static void redigerKunde() {
//        sortertKundenummer();
//        showMessageDialog(null, "Kunde informasjonen er redigert");
//    }
//
//    private static void slettKunde() {
//        sortertKundenummer();
//        showMessageDialog(null, "Kunden er slettet");
//    }
//
//    private static void taBackup() {
//        sortertKundenummer();
//        showMessageDialog(null, "Backup er opprettet");
//    }
//
//    private static void lastInnBackup() {
//        sortertKundenummer();
//        showMessageDialog(null, "Backup er lastet inn");
//    }
//
//    private static void avsluttProgram() {
//        sortertKundenummer();
//        showMessageDialog(null, "Programmet er avslutet");
//    }
//
//    public static void listeEtternavn() {
//
//        String sql = "SELECT * FROM Kunde ORDER BY navn";
//        try {
//            PreparedStatement pstm = getConnection().prepareStatement(sql);
//            ResultSet rs = pstm.executeQuery();
//            int i = 0;
//            while (rs.next()) {
//                int kNr = rs.getInt(1);
//                String navn = rs.getString(2);
//                String adresse = rs.getString(3);
//                String epost = rs.getString(4);
//                String tlfNr = rs.getString(5);
//                kundeListe[i] = new Kunde(kNr, navn, adresse, epost, tlfNr);
//                i++;
//            }
//        } catch (SQLException e) {
//            System.out.println("Feil ved henting av kundene: " + e.getMessage());
//        }
//        return kundeListe;
//    }
//
//    public static void listeTelefon() {
//
//        String sql = "SELECT * FROM Kunde ORDER BY tlfNr";
//        try {
//            PreparedStatement pstm = getConnection().prepareStatement(sql);
//            ResultSet rs = pstm.executeQuery();
//            int i = 0;
//            while (rs.next()) {
//                int kNr = rs.getInt(1);
//                String navn = rs.getString(2);
//                String adresse = rs.getString(3);
//                String epost = rs.getString(4);
//                String tlfNr = rs.getString(5);
//                kundeListe[i] = new Kunde(kNr, navn, adresse, epost, tlfNr);
//                i++;
//            }
//        } catch (SQLException e) {
//            System.out.println("Feil ved henting av kundene: " + e.getMessage());
//        }
//        return kundeListe;
//    }
//
//    public static void listeKunder() {
//
//        String sql = "SELECT * FROM Kunde ORDER BY kNr";
//        try {
//            PreparedStatement pstm = getConnection().prepareStatement(sql);
//            ResultSet rs = pstm.executeQuery();
//            int i = 0;
//            while (rs.next()) {
//                int kNr = rs.getInt(1);
//                String navn = rs.getString(2);
//                String adresse = rs.getString(3);
//                String epost = rs.getString(4);
//                String tlfNr = rs.getString(5);
//                kundeListe[i] = new Kunde(kNr, navn, adresse, epost, tlfNr);
//                i++;
//            }
//        } catch (SQLException e) {
//            System.out.println("Feil ved henting av kundene: " + e.getMessage());
//        }
//        return kundeListe;
//    }
//
//    public boolean nyKunde(Kunde kunde) {
//        String sql = "INSERT INTO Kunde VALUES(?,?,?,?,?)";
//
//        try {
//            PreparedStatement pstm = getConnection().prepareStatement(sql);
//            pstm.setInt(1, kunde.getkNr());
//            pstm.setString(2, kunde.getNavn());
//            pstm.setString(3, kunde.getAdresse());
//            pstm.setString(4, kunde.getEpost());
//            pstm.setString(5, kunde.getTlfNr());
//            pstm.executeUpdate();
//            return true;
//        } catch (SQLException e) {
//            System.out.println("Feil ved innsetting av Kunde: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public boolean redigerKunde(Kunde kunde) {
//        String sql = "UPDATE Kunde SET navn = ?, adresse = ?, epost = ?, tlfNr = ? WHERE kNr = ?";
//        try {
//            PreparedStatement pstm = getConnection().prepareStatement(sql);
//            pstm.setString(1, kunde.getNavn());
//            pstm.setString(2, kunde.getAdresse());
//            pstm.setString(3, kunde.getEpost());
//            pstm.setString(4, kunde.getTlfNr());
//            pstm.setInt(5, kunde.getkNr());
//            pstm.executeUpdate();
//            return true;
//        } catch (SQLException e) {
//            System.out.println("Feil ved oppdatering av Kunde: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public boolean slettKunde(int kNr) {
//        String sql = "DELETE FROM Kunde WHERE kNr = ?";
//        try {
//            PreparedStatement pstm = getConnection().prepareStatement(sql);
//            pstm.setInt(1, kNr);
//            pstm.executeUpdate();
//            return true;
//        } catch (SQLException e) {
//            System.out.println("Feil ved sletting av Kunde: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public boolean taBackup() {
//        Kunde[] kundeListe = selectAllKunder();
//        String fileName = "kunderegisterBackup.txt";
//        try {
//            File directory = new File("src/main/resources/backup");
//            if (!directory.exists()) {
//                directory.mkdirs();
//            }
//            File file = new File(directory, fileName);
//            FileWriter writer = new FileWriter(file, false);
//            for (Kunde kunde : kundeListe) {
//                if (kunde != null) {
//                    String linje = kunde.getkNr() + ";" + kunde.getNavn() + ";" + kunde.getAdresse() + ";" + kunde.getEpost() + ";" + kunde.getTlfNr() + "\n";
//                    writer.write(linje);
//                }
//            }
//            writer.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return false;
//    }
//
//    public boolean lastInnBackup() {
//        String linje;
//        try {
//            Path pathname = Path.of("src/main/resources/backup/kunderegisterBackup.txt");
//            Scanner leser = new Scanner(pathname);
//            while (leser.hasNextLine()) {
//                linje = leser.nextLine();
//                String[] kundeTabell = linje.split(";");
//
//                int kNr = Integer.parseInt(kundeTabell[0]);
//                String navn = kundeTabell[1];
//                String adresse = kundeTabell[2];
//                String epost = kundeTabell[3];
//                String tlfNr = kundeTabell[4];
//                insertKunde(new Kunde(kNr, navn, adresse, epost, tlfNr));
//            }
//            leser.close();
//            return true;
//        } catch (FileNotFoundException e) {
//            System.out.println("Feil ved innlesing fra backupfil. Feilmeding: " + e.getMessage());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return false;
//
//    }
//-----------------------------------------------------------------------------------------
//}       String kunde = showInputDialog("Legg til ny kunde: (knr; navn(Etternavn, Fornavn); Adresse; Epost; Tlfnr)" + "\n");
//
//        String[] nyKunde = kunde.split(";");
//
//        int knr = Integer.parseInt(nyKunde[0]);
//        String navn = nyKunde[1];
//        String adresse = nyKunde[2];
//        String epost = nyKunde[3];
//        String tlfnr = nyKunde[4];
//
//        String sql = "INSERT INTO Kunde (knr, navn, adresse, epost, tlfnr) VALUES ("+knr+","+navn+","+adresse+","+epost+","+tlfnr+")";
//        String resultat ="Kunde er regristert: " + "\n" + kunde;
//        try {
//            con = DriverManager.getConnection(url);
//            Statement stmt = con.createStatement();
//            stmt.executeQuery(sql);
//        } catch (SQLException e) {
//            resultat = "Feil ved regristering av kunde: " + e.getMessage();
//        }
//        return resultat;
//    }