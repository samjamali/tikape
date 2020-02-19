/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape;

import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author borna
 */
public class Tikape {

    public static void main(String[] args) throws SQLException {
        Scanner lukija = new Scanner(System.in);
        Tietokanta tietokanta = new Tietokanta();
        
        
        while(true){
            System.out.print("Valitse toiminto (1-9): ");
            int vaihto = Integer.valueOf(lukija.nextLine());
            
            if(vaihto == -1){
                break;
            }
            
            switch (vaihto) {
                case 1:
                    tietokanta.luoTietokanta();
                    System.out.println("");
                    break;
                case 2:
                    {
                        System.out.println("");
                        System.out.print("Anna paikan nimi: ");
                        String paikka = lukija.nextLine();
                        System.out.println("");
                        tietokanta.lisaaUusiPaikka(paikka);
                        System.out.println("");
                        break;
                    }
                case 3:
                    {
                        System.out.println("");
                        System.out.println("Anna asiakkaan nimi: ");
                        String asiakas = lukija.nextLine();
                        tietokanta.lisaaUusiAsiakas(asiakas);
                        break;
                    }
                case 4:
                    {
                        System.out.println("");
                        System.out.println("Anna paketin seurantakoodi: ");
                        String seurantakoodi = lukija.nextLine();
                        System.out.println("Anna asiakkaan nimi: ");
                        String asiakas = lukija.nextLine();
                        System.out.println("");
                        tietokanta.lisaaUusiPaketti(asiakas, seurantakoodi);
                        System.out.println("");
                        break;
                    }
                case 5:
                    {
                        System.out.println("");
                        System.out.println("Anna paketin seurantakoodi: ");
                        String seurantakoodi = lukija.nextLine();
                        System.out.println("Anna tapahtuman paikka: ");
                        String paikka = lukija.nextLine();
                        System.out.println("Anna tapahtuman kuvaus: ");
                        String kuvaus = lukija.nextLine();
                        System.out.println("");
                        tietokanta.lisaaUusiTapahtuma(seurantakoodi, paikka, kuvaus);
                        System.out.println("");
                        break;
                    }
                case 6:
                    {
                        System.out.println("");
                        System.out.println("Anna paketin seurantakoodi: ");
                        String seurantakoodi = lukija.nextLine();
                        System.out.println("");
                        tietokanta.hakuSeurantakoodi(seurantakoodi);
                        System.out.println("");
                        break;
                    }
                case 7:
                    System.out.println("");
                    System.out.println("Anna asiakkaan nimi: ");
                    String nimi = lukija.nextLine();
                    System.out.println("");
                    tietokanta.hakuAsiakkaanPaketit(nimi);
                    System.out.println("");
                    break;
                case -2:
                    tietokanta.test();
                    break;
                default:
                    break;
            }
            
            
            
        }
     
                
        
    }

}
