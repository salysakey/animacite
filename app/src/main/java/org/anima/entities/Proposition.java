package org.anima.entities;

/**
 * Created by thiam on 09/02/2016.
 */
public class Proposition {

    private int id;
    private String text;
    private double resultat;


    public Proposition() {
    }

    public Proposition(int id, String text) {
        this.id = id;
        this.text = text;

    }

    public String getRank() {
        return text;
    }

    public void setRank(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getResultat() {
        return resultat;
    }

    public void setResultat(double resultat) {
        this.resultat = resultat;
    }
}
