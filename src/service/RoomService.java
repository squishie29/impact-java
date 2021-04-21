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
import entity.Room;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rami
 */
public class RoomService {
    // Connection avec la base de donnée.
    Connection c = connexionbd.getinstance().getConn();
    
    //Ajouter un hotel à la base de donnée.
    public void addRoom(Room h) {
        try {
            PreparedStatement req = c.prepareStatement("insert into room(nb_personnes,description,type,prix,id_hotel_id)values(?,?,?,?,?)");
            
            req.setInt(1, h.getNbPersonnes());
            req.setString(2, h.getDescription());
            req.setString(3, h.getType());
            req.setInt(4, h.getPrix());
            req.setInt(5, h.getId_hotel_id());
            req.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    // Récupérer toute les données de la base de donnée.
    public ResultSet getAll() {
         
        try {
            PreparedStatement req = c.prepareStatement("SELECT * FROM room ");
            ResultSet rs = req.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
      return null;
    }
    
    //Retourne une un objet de type hotel selon l'id passer en argument.
    public Room getoneroom(int id) {
        Room h = new Room();
        try {
            PreparedStatement req = c.prepareStatement("select * from room where id=?  ");
            req.setInt(1, id);
            ResultSet rs = req.executeQuery();
            rs.next();
            h.setId(rs.getInt("id"));
            h.setNbPersonnes(rs.getInt("nb_personnes"));
            h.setDescription(rs.getString("description"));
            h.setType(rs.getString("type"));
            h.setId_hotel_id(rs.getInt("id_hotel_id"));
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return h;
    }
    
    //Update name hotel.
       public boolean updateDescription(Room h, String newValue) {
      Boolean ok = false;
        try {
            PreparedStatement req = c.prepareStatement("update room set description=? where id = ? ");
            
       req.setString(1, newValue);
       req.setInt(2, h.getId());
       req.executeUpdate();
        ok = true;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
return ok;
    }
    

    
         public boolean delete(Room h) {
         boolean ok=false;
        try {
            
            PreparedStatement req = c.prepareStatement("delete from room where id = ? ");
            req.setInt(1, h.getId());
            req.executeUpdate();
            ok=true;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return ok;
    }
         
         
         
          public List<Integer> afficherALLID() {
        List<Integer> listID = new ArrayList<>();

        try {
            String requete = "SELECT id FROM room";
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {

                listID.add(rs.getInt(1));
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return listID;
    }  
    
}
