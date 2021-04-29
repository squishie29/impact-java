/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;


import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rami
 */
public class Options {
    private Integer id;
    private String description;
    private Integer room_id_id;
    private String type;
     private String name;

    
    
    

    public Options() {
    }

    @Override
    public String toString() {
        return "Options{" + "id=" + id + ", description=" + description + ", room_id_id=" + room_id_id + '}';
    }

    public Options(Integer id, String description,Integer room_id_id) {
        this.id = id;
        this.description = description;
        this.room_id_id = room_id_id; 
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRoom_id_id() {
        return room_id_id;
    }

    public void setRoom_id_id(Integer room_id_id) {
        this.room_id_id = room_id_id;
    }
    

}

