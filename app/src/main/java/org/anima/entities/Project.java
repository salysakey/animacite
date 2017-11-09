package org.anima.entities;

/**
 * Created by thiam on 16/10/2015.
 */
public class Project {

    private String title;
    private String couleur;
    private String description;
    private String resume;
    private String picture;
    private long date_debut;
    private long date_fin;
    private int type;

    public Project(){
    }

    public Project(String title, String description, String resume, String picture){
        this.title = title;
        this.description = description;
        this.resume = resume;
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(long date_debut) {
        this.date_debut = date_debut;
    }

    public long getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(long date_fin) {
        this.date_fin = date_fin;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }
}
