package sample;

import java.util.Date;

public class Tableau {
    Integer id;
    String date_creation;
    String produit,wpm,ci,methode,plannif,debut,fin;

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }
    public Tableau(){}
    public Tableau(String date_creation, String ci, String methode) {
        this.date_creation = date_creation;
        this.ci = ci;
        this.methode = methode;
    }

    public Tableau(String date_creation, String produit, String wpm, String ci, String methode, String plannif, String debut, String fin,Integer id ) {
        this.date_creation = date_creation;
        this.produit = produit;
        this.wpm = wpm;
        this.ci = ci;
        this.methode = methode;
        this.plannif = plannif;
        this.debut = debut;
        this.fin = fin;
        this.id=id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDebut() {
        return debut;
    }

    public void setDebut(String debut) {
        this.debut = debut;
    }

    public String getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public String getWpm() {
        return wpm;
    }

    public void setWpm(String wpm) {
        this.wpm = wpm;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }


    public String getPlannif() {
        return plannif;
    }

    public void setPlannif(String plannif) {
        this.plannif = plannif;
    }
}
