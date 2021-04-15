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
public class Room {
    private Integer id;
    private int nbPersonnes;
    private String description;

    private String type;

    private int prix;
    private int id_hotel_id;

    public Room() {
    }

    @Override
    public String toString() {
        return "room{" + "id=" + id + ", nbPersonnes=" + nbPersonnes + ", description=" + description + ", type=" + type + ", prix=" + prix + ", id Hotel=" + id_hotel_id+ '}';
    }
    
    
    public Room(Integer id, int nbPersonnes, String description, String type, int prix, int id_hotel_id) {
        this.id = id;
        this.nbPersonnes = nbPersonnes;
        this.description = description;
        this.type = type;
        this.prix = prix;
        this.id_hotel_id = id_hotel_id;
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
    
        public int getId_hotel_id() {
        return id_hotel_id;
    }

    public void setId_hotel_id(int id_hotel_id) {
        this.id_hotel_id = id_hotel_id;
    }

    
}
