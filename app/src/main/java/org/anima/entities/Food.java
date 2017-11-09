package org.anima.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thiam on 03/07/15.
 */

public class Food {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("pictureUrl")
    String pictureUrl;
    @SerializedName("price")
    String price;
    @SerializedName("descriptif")
    String descriptif;
    @SerializedName("type")
    int type;
    @SerializedName("ident")
    int ident;
    @SerializedName("date")
    long date;
    @SerializedName("couleur")
    String couleur;

    public Food(int id, String name, String pictureUrl, String price,String descriptif, int type, int ident,long date) {
        super();
        this.descriptif=descriptif;
        this.date=date;
        this.id = id;
        this.ident = ident;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.price = price;
        this.type =type;
    }


    public Food(int id, String name, String pictureUrl, String price,String descriptif, int type, int ident,long date, String couleur) {
        super();
        this.descriptif=descriptif;
        this.date=date;
        this.id = id;
        this.ident = ident;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.price = price;
        this.type =type;
        this.couleur = couleur;
    }

    public Food() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getDescriptif() {
        return descriptif;
    }

    public void setDescriptif(String descriptif) {
        this.descriptif = descriptif;
    }

    public String getCouleur() {
        return couleur;
    }

    @Override
    public String toString() {
        return "Food [id=" + id + ", name=" + name + ", pictureUrl=" + pictureUrl
                + ", price=" + price + "]";
    }

}