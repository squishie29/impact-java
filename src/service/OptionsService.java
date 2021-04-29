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
import entity.Options;

/**
 *
 * @author rami
 */
public class OptionsService {
    // Connection avec la base de donnée.
    Connection c = connexionbd.getinstance().getConn();
    
    //Ajouter un hotel à la base de donnée.
    public void addOptions(Options o) {
        try {
            PreparedStatement req = c.prepareStatement("insert into options(description,room_id_id)values(?,?)");
            req.setString(1, o.getDescription());
            req.setInt(2, o.getRoom_id_id());
            
            req.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    // Récupérer toute les données de la base de donnée.
    public ResultSet getAll() {
         
        try {
            PreparedStatement req = c.prepareStatement("SELECT O.id,O.description,O.room_id_id,R.type,H.name FROM `options` O LEFT OUTER JOIN `room` R ON O.room_id_id = R.id LEFT OUTER JOIN `hotel` H ON R.id_hotel_id = H.id");
            ResultSet rs = req.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
      return null;
    }
    
    //Retourne une un objet de type hotel selon l'id passer en argument.
    public Options getoneoptions(int id) {
        Options o = new Options();
        try {
            PreparedStatement req = c.prepareStatement("select * from options where id=?  ");
            req.setInt(1, id);
            ResultSet rs = req.executeQuery();
            rs.next();
            o.setId(rs.getInt("id"));
            o.setDescription(rs.getString("description"));
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return o;
    }
    
    //Update name hotel.
       public boolean updateDescription(Options o, String newValue) {
      Boolean ok = false;
        try {
            PreparedStatement req = c.prepareStatement("update options set description=? where id = ? ");
            
       req.setString(1, newValue);
       req.setInt(2, o.getId());
       req.executeUpdate();
        ok = true;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
return ok;
    }
    

    
         public boolean delete(Options o) {
         boolean ok=false;
        try {
            
            PreparedStatement req = c.prepareStatement("delete from options where id = ? ");
            req.setInt(1, o.getId());
            req.executeUpdate();
            ok=true;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return ok;
    }
    
    
}
