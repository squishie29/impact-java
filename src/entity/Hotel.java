/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javafx.scene.image.ImageView;

/**
 *
 * @author rami
 */
public class Hotel {
    private int id;
    private String name;
    private int stars;
    private String photo;
    private String description;
    private String adress;
    private ImageView photo_view;

    public Hotel() {
    }

    public Hotel(int id, String name, int stars, String photo, String description, String adress) {
        this.id = id;
        this.name = name;
        this.stars = stars;
        this.photo = photo;
        this.description = description;
        this.adress = adress;
    }

    @Override
    public String toString() {
        return "Hotel{" + "id=" + id + ", name=" + name + ", stars=" + stars + ", photo=" + photo + ", description=" + description + ", adress=" + adress + '}';
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

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public ImageView getPhoto_view() {
        return photo_view;
    }

    public void setPhoto_view(ImageView photo_view) {
        this.photo_view = photo_view;
    }
    
    
    
}
