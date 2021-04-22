/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import projet.util.connexionbd;
import entity.Gallery;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rami
 */
public class GalleryService {
    // Connection avec la base de donnée.
    Connection c = connexionbd.getinstance().getConn();
    
    //Ajouter un hotel à la base de donnée.
    public void addGallery(Gallery g) {
        try {
            PreparedStatement req = c.prepareStatement("insert into gallery(imgpath,hotel_id_id)values(?,?)");
            

            req.setInt(2, g.getHotel_id_id());
            req.setString(1, g.getImgpath());

            req.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    // Récupérer toute les données de la base de donnée.
    public ResultSet getAll() {
         
        try {
            PreparedStatement req = c.prepareStatement("SELECT * FROM Gallery ");
            ResultSet rs = req.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
      return null;
    }
    
    //Retourne une un objet de type hotel selon l'id passer en argument.
    public Gallery getonegallery(int id) {
        Gallery g = new Gallery();
        try {
            PreparedStatement req = c.prepareStatement("select * from gallery where id=?  ");
            req.setInt(1, id);
            ResultSet rs = req.executeQuery();
            rs.next();
            g.setId(rs.getInt("id"));

            g.setHotel_id_id(rs.getInt("hotel_id_id"));

            g.setImgpath(rs.getString("imgpath"));
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return g;
    }
    
    
    public void updatePhoto(Gallery g,String a1){
   
     try {
            PreparedStatement req = c.prepareStatement("update  gallery set imgpath=? where id = ? ");
            
       req.setString(1, a1);
       req.setInt(2, g.getId());
       req.executeUpdate();
        } catch (SQLException ex) {
             System.out.println(ex);
            
        }
   }
    
         public boolean delete(Gallery g) {
         boolean ok=false;
        try {
            
            PreparedStatement req = c.prepareStatement("delete from gallery where id = ? ");
            req.setInt(1, g.getId());
            req.executeUpdate();
            ok=true;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return ok;
    }
    
  
}
