/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author rami
 */
public class room {
    private Integer id;
    private int nbPersonnes;
    private String description;

    private String type;

    private int prix;

    public room() {
    }

    @Override
    public String toString() {
        return "room{" + "id=" + id + ", nbPersonnes=" + nbPersonnes + ", description=" + description + ", type=" + type + ", prix=" + prix + '}';
    }
    
    
    public room(Integer id, int nbPersonnes, String description, String type, int prix) {
        this.id = id;
        this.nbPersonnes = nbPersonnes;
        this.description = description;
        this.type = type;
        this.prix = prix;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNbPersonnes() {
        return nbPersonnes;
    }

    public void setNbPersonnes(int nbPersonnes) {
        this.nbPersonnes = nbPersonnes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }
    
}
