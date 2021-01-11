package sample;

import java.util.Date;

public class Tableau {
    String date_creation;
    String produit,wpm,ci,methode,FC,plannif;

    public Tableau(String date_creation,String produit, String wpm, String ci) {
        this.produit = produit;
        this.wpm = wpm;
        this.ci = ci;
        this.date_creation=date_creation;
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

    public String getFC() {
        return FC;
    }

    public void setFC(String FC) {
        this.FC = FC;
    }

    public String getPlannif() {
        return plannif;
    }

    public void setPlannif(String plannif) {
        this.plannif = plannif;
    }
}
