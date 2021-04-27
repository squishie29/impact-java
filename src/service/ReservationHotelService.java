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
import entity.ReservationHotel;
import java.sql.Date;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rami
 */
public class ReservationHotelService {
    // Connection avec la base de donnée.
    Connection c = connexionbd.getinstance().getConn();
    
    //Ajouter un hotel à la base de donnée.
    public void addReservationHotel(ReservationHotel h) {
        try {
            PreparedStatement req = c.prepareStatement("insert into reservation_hotel(user_id_id,room_id_id,debut,fin,confirmation)values(?,?,?,?,?)");
            
            req.setInt(1, h.getUser_id_id());
            req.setInt(2, h.getRoom_id_id());
            req.setDate(3, h.getDebut());
            req.setDate(4, h.getFin());
            req.setString(5, h.getConfirmation());
            req.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    // Récupérer toute les données de la base de donnée.
    public ResultSet getAll() {
         
        try {
            PreparedStatement req = c.prepareStatement("SELECT * FROM reservation_hotel ");
            ResultSet rs = req.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
      return null;
    }
    
    //Retourne une un objet de type hotel selon l'id passer en argument.
    public ReservationHotel getoneReservationHotel(int id) {
        ReservationHotel h = new ReservationHotel();
        try {
            PreparedStatement req = c.prepareStatement("select * from reservation_hotel where id=?  ");
            req.setInt(1, id);
            ResultSet rs = req.executeQuery();
            rs.next();
            h.setId(rs.getInt("id"));
            h.setUser_id_id(rs.getInt("user_id_id"));
            h.setRoom_id_id(rs.getInt("room_id_id"));
            h.setDebut(rs.getDate("debut"));
            h.setFin(rs.getDate("fin"));
            h.setConfirmation(rs.getString("confirmation"));
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return h;
    }
    
    //Update name hotel.
       public boolean updateConfirmation(ReservationHotel h, String newValue) {
      Boolean ok = false;
        try {
            PreparedStatement req = c.prepareStatement("update reservation_hotel set confirmation=? where id = ? ");
            
       req.setString(1, newValue);
       req.setInt(2, h.getId());
       req.executeUpdate();
        ok = true;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
return ok;
    }
       
       //Update name hotel.
       public boolean updateUser(ReservationHotel h, Integer newValue) {
      Boolean ok = false;
        try {
            PreparedStatement req = c.prepareStatement("update reservation_hotel set user_id_id=? where id = ? ");
            
       req.setInt(1, newValue);
       req.setInt(2, h.getId());
       req.executeUpdate();
        ok = true;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
return ok;
    }
    

    
         public boolean delete(ReservationHotel h) {
         boolean ok=false;
        try {
            
            PreparedStatement req = c.prepareStatement("delete from reservation_hotel where id = ? ");
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
            String requete = "SELECT id FROM reservation_hotel";
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
          public boolean updateDateDebut(ReservationHotel s, Date newValue) {
      Boolean ok = false;
        try {
            PreparedStatement req = c.prepareStatement("update reservation_hotel set debut=? where id = ? ");
            
       req.setDate(1, newValue);
       req.setInt(2, s.getId());
       req.executeUpdate();
        ok = true;
        } catch (SQLException ex) {
            System.out.println(ex);
            
        }
return ok;
    }
    
}
