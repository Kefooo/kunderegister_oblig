package com.kef.kunderegister.model;

import java.util.Objects;


public class Kunde {

    private int kNr;

    private String navn;

    private String adresse;

    private String epost;

    private String tlfNr;


    public int getkNr() {
        return kNr;
    }

    public void setkNr(int kNr) {
        this.kNr = kNr;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEpost() {
        return epost;
    }

    public void setEpost(String epost) {
        this.epost = epost;
    }

    public String getTlfNr() {
        return tlfNr;
    }

    public void setTlfNr(String tlfNr) {
        this.tlfNr = tlfNr;
    }

    @Override
    public String toString() {
        return "Kunde{" +
                "kNr=" + kNr +
                ", navn='" + navn + '\'' +
                ", adresse='" + adresse + '\'' +
                ", epost='" + epost + '\'' +
                ", tlfNr='" + tlfNr + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kunde kunde = (Kunde) o;
        return kNr == kunde.kNr && Objects.equals(navn, kunde.navn) && Objects.equals(adresse, kunde.adresse) && Objects.equals(epost, kunde.epost) && Objects.equals(tlfNr, kunde.tlfNr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kNr, navn, adresse, epost, tlfNr);
    }

    public Kunde(String navn, String adresse, String epost, String tlfNr) {
        this.adresse = adresse;
        this.tlfNr = tlfNr;
        this.navn = navn;
        this.epost = epost;
    }

    public Kunde(int kNr, String navn, String adresse, String epost, String tlfNr) {
        this.kNr = kNr;
        this.navn = navn;
        this.adresse = adresse;
        this.tlfNr = tlfNr;
        this.epost = epost;
    }


    public Kunde(){}



}
