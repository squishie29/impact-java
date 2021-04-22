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
public class Gallery {
    private Integer id;
    private String imgpath;
    private Integer hotel_id_id;
    private ImageView photo_view;

    public Gallery() {
    }



    public Gallery(Integer id, String imgpath, Integer hotel_id_id) {
        this.id = id;
        this.imgpath = imgpath;
        this.hotel_id_id = hotel_id_id;
    }

    @Override
    public String toString() {
        return "Gallery{" + "id=" + id + ", imgpath=" + imgpath + ", hotel_id_id=" + hotel_id_id + '}';
    }

    public Integer getHotel_id_id() {
        return hotel_id_id;
    }

    public void setHotel_id_id(Integer hotel_id_id) {
        this.hotel_id_id = hotel_id_id;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public ImageView getPhoto_view() {
        return photo_view;
    }

    public void setPhoto_view(ImageView photo_view) {
        this.photo_view = photo_view;
    }
    
    
    
    
    
    
}
