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
import entity.Hotel;

/**
 *
 * @author rami
 */
public class HotelService {
    // Connection avec la base de donnée.
    Connection c = connexionbd.getinstance().getConn();
    
    //Ajouter un hotel à la base de donnée.
    public void addHotel(Hotel h) {
        try {
            PreparedStatement req = c.prepareStatement("insert into hotel(name,stars,photo,description,adress)values(?,?,?,?,?)");
            
            req.setString(1, h.getName());
            req.setInt(2, h.getStars());
            req.setString(3, h.getPhoto());
            req.setString(4, h.getDescription());
            req.setString(5, h.getAdress());
            req.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    // Récupérer toute les données de la base de donnée.
    public ResultSet getAll() {
         
        try {
            PreparedStatement req = c.prepareStatement("SELECT * FROM hotel ");
            ResultSet rs = req.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
      return null;
    }
    
    //Retourne une un objet de type hotel selon l'id passer en argument.
    public Hotel getonehotel(int id) {
        Hotel h = new Hotel();
        try {
            PreparedStatement req = c.prepareStatement("select * from hotel where id=?  ");
            req.setInt(1, id);
            ResultSet rs = req.executeQuery();
            rs.next();
            h.setId(rs.getInt("id"));
            h.setName(rs.getString("name"));
            h.setStars(rs.getInt("stars"));
            h.setDescription(rs.getString("description"));
            h.setAdress(rs.getString("adress"));
            h.setPhoto(rs.getString("photo"));
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return h;
    }
    
    //Update name hotel.
       public boolean updateName(Hotel h, String newValue) {
      Boolean ok = false;
        try {
            PreparedStatement req = c.prepareStatement("update hotel set name=? where id = ? ");
            
       req.setString(1, newValue);
       req.setInt(2, h.getId());
       req.executeUpdate();
        ok = true;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
return ok;
    }
    
    public void updatePhoto(Hotel a,String a1){
   
     try {
            PreparedStatement req = c.prepareStatement("update  hotel set photo=? where id = ? ");
            
       req.setString(1, a1);
       req.setInt(2, a.getId());
       req.executeUpdate();
        } catch (SQLException ex) {
             System.out.println(ex);
            
        }
   }
    
         public boolean delete(Hotel h) {
         boolean ok=false;
        try {
            
            PreparedStatement req = c.prepareStatement("delete from hotel where id = ? ");
            req.setInt(1, h.getId());
            req.executeUpdate();
            ok=true;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return ok;
    }
    
    
}
