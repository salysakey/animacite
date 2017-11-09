package org.anima.entities;

import java.util.List;

/**
 * Created by thiam on 03/07/15.
 */

public class Question {
    int id;
    String name;
    List<Proposition> propositions;
    int type;
    int gloablereponse;

    public Question(int id, String name, List<Proposition> propositions, int type,int gloablereponse) {
        super();
        this.gloablereponse=gloablereponse;
        this.propositions=propositions;
        this.id = id;
        this.name = name;
        this.type =type;
    }

    public Question() {
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



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public int getGloablereponse() {
        return gloablereponse;
    }

    public void setGloablereponse(int gloablereponse) {
        this.gloablereponse = gloablereponse;
    }

    public List<Proposition> getPropositions() {
        return propositions;
    }

    public void setPropositions(List<Proposition> propositions) {
        this.propositions = propositions;
    }
}

