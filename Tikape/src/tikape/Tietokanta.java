/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date; 

public class Tietokanta {

    private Connection db;
    private Statement s;
    private PreparedStatement p;
    private ResultSet r = null;
    
    private ArrayList<String> asiakkaat;

    public Tietokanta() throws SQLException {
        this.db = DriverManager.getConnection("jdbc:sqlite:testi.db");
        this.asiakkaat = new ArrayList<>();

    }

    public void luoTietokanta() throws SQLException {
        s = db.createStatement();
        s.execute("CREATE TABLE Paikka(id INTEGER PRIMARY KEY,nimi TEXT UNIQUE)");
        s.execute("CREATE TABLE Asiakas(id INTEGER PRIMARY KEY,nimi TEXT UNIQUE)");
        s.execute("CREATE TABLE Paketti(id INTEGER PRIMARY KEY,asiakas_id INTEGER,seurantakoodi TEXT UNIQUE)");
        s.execute("CREATE TABLE Tapahtuma(id INTEGER PRIMARY KEY,date TEXT,paikkaId INTEGER ,seurantakoodiId INTEGER, kuvaus TEXT)");
        System.out.println("Tietokanta luotu!");
    }

    public void lisaaUusiPaikka(String paikka) throws SQLException {
        s = db.createStatement();
        p = db.prepareStatement("INSERT INTO Paikka (nimi) VALUES (?)");
        p.setString(1, paikka);
        p.executeUpdate();

       
        System.out.println("Paikka lisätty");

    }

    public void lisaaUusiAsiakas(String asiakas) throws SQLException {
        s = db.createStatement();
        //Contains liian hidas?
        if (!asiakkaat.contains(asiakas)) {
            p = db.prepareStatement("INSERT INTO Asiakas (nimi) VALUES (?)");
            p.setString(1, asiakas);
            p.executeUpdate();
            asiakkaat.add(asiakas);
            
            System.out.println("Asiakas Lisätty");
        }
        
        else{
            System.out.println("Asiakas on jo lisätty");
        }

        

    }

    public void lisaaUusiPaketti(String asiakas, String seurantakoodi) throws SQLException {
        s = db.createStatement();
        p = db.prepareStatement("SELECT id FROM Asiakas WHERE nimi = ?");
        p.setString(1, asiakas);
        r = p.executeQuery();
        int id = 0;
        if (r.next()) {
            System.out.println(id);
            id = r.getInt("id");
            
        } else {
            System.out.println("Asiakasta ei ollut tietokannassa");
        }

        if (id != 0) {
            p = db.prepareStatement("INSERT INTO Paketti (asiakas_id,seurantakoodi) VALUES (?,?)");
            p.setInt(1, id);
            p.setString(2, seurantakoodi);
            p.executeUpdate();
        }

        if (id == 0) {

            System.out.println("Pakettia ei lisätty, sillä asiakasta ei löytynyt");
        } else {

            System.out.println("Paketti Lisätty");
        }

    }

    public void lisaaUusiTapahtuma(String seurantakoodi, String paikka, String kuvaus) throws SQLException {
        //Haetaan seurantakoodi id
        
        Date dates = new Date();
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dates);
        System.out.println(date);

        s = db.createStatement();
        p = db.prepareStatement("SELECT id FROM Paketti WHERE seurantakoodi =?");
        p.setString(1, seurantakoodi);
        r = p.executeQuery();

        int seurantakoodiId = 0;

        if (r.next()) {
            seurantakoodiId = r.getInt("id");
        } else {
        }

        //Haetaan paikka id
        s = db.createStatement();
        p = db.prepareStatement("SELECT id FROM Paikka WHERE nimi =?");
        p.setString(1, paikka);
        r = p.executeQuery();

        int paikkaId = 0;
        if (r.next()) {
            paikkaId = r.getInt("id");
        } else {
            System.out.println("Paikkaa ei löytynyt!");
        }

        //Jos id != 0 eli löytyi tarvitut asiat
        if (seurantakoodiId != 0 && paikkaId !=0) {
            p = db.prepareStatement("INSERT INTO Tapahtuma (date,paikkaId,seurantakoodiId,kuvaus) VALUES (?,?,?,?)");
            p.setString(1, date);
            p.setInt(2, paikkaId);
            p.setInt(3, seurantakoodiId);
            p.setString(4, kuvaus);
            p.executeUpdate();

        }

        if (paikkaId!=0 && seurantakoodiId!=0) {
            System.out.println("");
            System.out.println("Tapahtuma lisätty");
        }

    }

    public void hakuSeurantakoodi(String seurantakoodi) throws SQLException {
        PreparedStatement p = db.prepareStatement("SELECT id FROM Paketti WHERE seurantakoodi =?");
        p.setString(1, seurantakoodi);
        ResultSet r = p.executeQuery();

        p = db.prepareStatement("SELECT * FROM Tapahtuma WHERE seurantakoodiId =?");
        p.setInt(1, r.getInt("id"));

        r = p.executeQuery();

        while (true) {
            if (r.next()) {
                PreparedStatement p2 = db.prepareStatement("SELECT nimi FROM Paikka WHERE id = ?");
                p2.setInt(1, r.getInt("paikkaId"));

                ResultSet r2 = p2.executeQuery();

                System.out.println(r.getString("date") + "," + r2.getString("nimi") + "," + r.getString("kuvaus"));

                continue;
            } else if (!r.next()) {

            }
            System.out.println("");
            break;
        }

    }
    
    public void hakuAsiakkaanPaketit(String nimi) throws SQLException {
        PreparedStatement h = db.prepareStatement("SELECT id FROM Asiakas WHERE nimi = ?");
        h.setString(1, nimi);
        ResultSet r = h.executeQuery();
        
        while(r.next()){
            h = db.prepareStatement("SELECT Paketti.seurantakoodi, COUNT(Paketti.seurantakoodi) FROM Paketti,Tapahtuma"
                    + " WHERE Paketti.asiakas_id =? AND Tapahtuma.seurantakoodiId =Paketti.id GROUP BY Paketti.seurantakoodi ");
            
            h.setInt(1, r.getInt("id"));
            
        }
        
        r = h.executeQuery();
        
        while(r.next()){
            System.out.println(r.getString("seurantakoodi")+","+r.getInt(2));
        }
        

    }
    
    public void haePaivamaara(String paikka, String pvm)throws SQLException {
        PreparedStatement h = db.prepareStatement("SELECT id FROM Paikka WHERE paikka = ?");
        h.setString(1, paikka);
        ResultSet r = h.executeQuery();
        
        h = db.prepareStatement("SELECT COUNT()");
        
    }
    
    public void test() throws SQLException {
        Statement s = db.createStatement();
        ResultSet r = s.executeQuery("SELECT * FROM Asiakas");
        System.out.println("Asiakkaat: ");
        while (r.next()) {
            System.out.println("id: " + r.getInt("id") + "\t| Nimi: " + r.getString("nimi"));
        }
        r = s.executeQuery("SELECT * FROM Paikka");
        System.out.println("Paikat: ");
        while (r.next()) {
            System.out.println("id: " + r.getInt("id") + "\t| Nimi: " + r.getString("nimi"));
        }
        System.out.println("Paketit:");
        r = s.executeQuery("SELECT * FROM Paketti");
        while (r.next()) {
            System.out.println("id: " + r.getInt("id") + "\t| as.id: " + r.getInt("asiakas_id") + "\t| Seurantakoodi: " + r.getString("seurantakoodi"));
        }
        System.out.println("Tapahtumat:");
        r = s.executeQuery("SELECT * FROM Tapahtuma");
        while (r.next()) {
            System.out.println("id: " + r.getInt("id") + "\t| paikkaId: " + r.getInt("paikkaId") + "\t| Kuvaus: " + r.getString("kuvaus") + "\t\t| Kello: " + r.getString("date") + "\t");
        }
    }

}

//'https://stackoverflow.com/questions/22506930/how-to-query-datetime-field-using-only-date-in-sql-server