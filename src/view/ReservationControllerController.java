/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import entity.ReservationHotel;
import entity.Room;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import service.HotelService;
import service.ReservationHotelService;
import service.RoomService;

/**
 * FXML Controller class
 *
 * @author rami
 */
public class ReservationControllerController implements Initializable {

    @FXML
    private TableView<ReservationHotel> tReservationHotel;
    @FXML
    private TableColumn<ReservationHotel, Integer> idR;
    @FXML
    private TableColumn<ReservationHotel, Integer> userR;
    @FXML
    private TableColumn<ReservationHotel, Integer> roomR;
    @FXML
    private TableColumn<ReservationHotel, Date> debutR;
    @FXML
    private TableColumn<ReservationHotel, Date> finR;
    @FXML
    private TableColumn<ReservationHotel, String> confirmationR;
    @FXML
    private TableColumn<ReservationHotel, Button> delete;
    @FXML
    private ComboBox<Integer> user;
    @FXML
    private ComboBox<Integer> room;
    @FXML
    private DatePicker debut;
    @FXML
    private DatePicker fin;
    @FXML
    private TextField confirmation;
    @FXML
    private Button update;
    @FXML
    private TextField search;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showReservationHotel();
        //HotelService Nc = new HotelService();
        RoomService Nc = new RoomService();
        ObservableList<Integer> IdcP = FXCollections.observableArrayList(Nc.afficherALLID());
        room.setItems(IdcP);
    } 
    
    
    public void showReservationHotel() {
        ReservationHotelService ss = new ReservationHotelService();
        ResultSet resultset = ss.getAll();

        //List<Reclamation> lr = new ArrayList<Reclamation>();
        ObservableList<ReservationHotel> listReservationHotel = FXCollections.observableArrayList();
        try {
            idR.setCellValueFactory(new PropertyValueFactory<>("id"));
            userR.setCellValueFactory(new PropertyValueFactory<>("user_id_id"));
            roomR.setCellValueFactory(new PropertyValueFactory<>("room_id_id"));
            debutR.setCellValueFactory(new PropertyValueFactory<>("debut"));
            finR.setCellValueFactory(new PropertyValueFactory<>("fin"));
            confirmationR.setCellValueFactory(new PropertyValueFactory<>("confirmation"));
            while (resultset.next()) {
                ReservationHotel r1 = new ReservationHotel();
                r1.setId(resultset.getInt("id"));
               
                r1.setUser_id_id(resultset.getInt("user_id_id"));
                r1.setRoom_id_id(resultset.getInt("room_id_id"));
                r1.setDebut(resultset.getDate("debut"));
                r1.setFin(resultset.getDate("fin"));
                r1.setConfirmation(resultset.getString("confirmation"));
                listReservationHotel.add(r1);

            }

        

            delete.setCellFactory((param) -> {
                return new TableCell() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        setGraphic(null);
                        if (!empty) {
                            Button b = new Button("delete");
                            b.setOnAction((event) -> {

                                if (ss.delete(tReservationHotel.getItems().get(getIndex()))) {
                                    showReservationHotel();
                                }
                            });
                            setGraphic(b);
                        }
                    }
                };
            });

            confirmationR.setCellFactory(TextFieldTableCell.forTableColumn());
            
            
            
            tReservationHotel.setItems(listReservationHotel);

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        confirmationR.setOnEditCommit((e) -> {

            if (ss.updateConfirmation(tReservationHotel.getItems().get(e.getTablePosition().getRow()), e.getNewValue())) {
                tReservationHotel.getItems().get(e.getTablePosition().getRow()).setConfirmation(e.getNewValue());
            }
            tReservationHotel.refresh();
        });
    }
    

    @FXML
    private void addReservationHotel(ActionEvent event) {
        
        ReservationHotelService hs = new ReservationHotelService();
        ReservationHotel h = new ReservationHotel();
        //insert_stars
        h.setUser_id_id(2);
        h.setRoom_id_id((room.getValue()));
        
        
        
        h.setDebut(java.sql.Date.valueOf(debut.getValue()));
        h.setFin(java.sql.Date.valueOf(fin.getValue()));
        h.setConfirmation((confirmation.getText()));
        
        hs.addReservationHotel(h);
        showReservationHotel();
    }
    
}
