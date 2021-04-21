/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import entity.Hotel;
import entity.Room;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.HotelService;
import service.RoomService;

/**
 * FXML Controller class
 *
 * @author rami
 */
public class RoomController implements Initializable {

    @FXML
    private TableView<Room> tRoom;
    @FXML
    private TableColumn<Room, Integer> idR;
    @FXML
    private TableColumn<Room, String> typeR;
    @FXML
    private TableColumn<Room, String> descR;
    @FXML
    private TableColumn<Room, Integer> nbR;
    @FXML
    private TableColumn<Room, Integer> prixR;
    @FXML
    private TableColumn<Room, Integer> idH;
    @FXML
    private TextField search;
    @FXML
    private TextField type;
    @FXML
    private TextField description;
    @FXML
    private TextField nb;
    @FXML
    private TextField prix;
    @FXML
    private Button update;
    @FXML
    private TableColumn<Room, Button> delete;
    @FXML
    private ComboBox<Integer> test;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        showRoom();
        HotelService Nc = new HotelService();
        ObservableList<Integer> IdcP = FXCollections.observableArrayList(Nc.afficherALLID());
        test.setItems(IdcP);
    }  
    
    public void showRoom() {
        RoomService ss = new RoomService();
        ResultSet resultset = ss.getAll();

        //List<Reclamation> lr = new ArrayList<Reclamation>();
        ObservableList<Room> listRooms = FXCollections.observableArrayList();
        try {
            idR.setCellValueFactory(new PropertyValueFactory<>("id"));
            typeR.setCellValueFactory(new PropertyValueFactory<>("type"));
            descR.setCellValueFactory(new PropertyValueFactory<>("description"));
            nbR.setCellValueFactory(new PropertyValueFactory<>("nbPersonnes"));
            prixR.setCellValueFactory(new PropertyValueFactory<>("prix"));
            idH.setCellValueFactory(new PropertyValueFactory<>("id_hotel_id"));
            while (resultset.next()) {
                Room r1 = new Room();
                r1.setId(resultset.getInt("id"));
               
                r1.setDescription(resultset.getString("description"));
                r1.setType(resultset.getString("type"));
                r1.setPrix(resultset.getInt("prix"));
                r1.setNbPersonnes(resultset.getInt("nb_personnes"));
                r1.setId_hotel_id(resultset.getInt("id_hotel_id"));
                listRooms.add(r1);

            }

        

            delete.setCellFactory((param) -> {
                return new TableCell() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        setGraphic(null);
                        if (!empty) {
                            Button b = new Button("delete");
                            b.setOnAction((event) -> {

                                if (ss.delete(tRoom.getItems().get(getIndex()))) {
                                    showRoom();
                                }
                            });
                            setGraphic(b);
                        }
                    }
                };
            });

            descR.setCellFactory(TextFieldTableCell.forTableColumn());
            
            
            //partie recherche
            FilteredList<Room> filteredData = new FilteredList<>(listRooms, b -> true);
            search.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(room -> {
                    // If filter text is empty, display all persons.

                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare first name and last name of every person with filter text.
                    String lowerCaseFilter = newValue.toLowerCase();

                    if (room.getType().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true; // Filter matches first name.
                    } //				else if (String.valueOf(reclamation.getSalary()).indexOf(lowerCaseFilter)!=-1)
                    //				     return true;
                    else {
                        return false; // Does not match.
                    }
                });
            });
            SortedList<Room> sortedData = new SortedList<>(filteredData);

            // 4. Bind the SortedList comparator to the TableView comparator.
            // 	  Otherwise, sorting the TableView would have no effect.
            sortedData.comparatorProperty().bind(tRoom.comparatorProperty());
            tRoom.setItems(filteredData);

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        descR.setOnEditCommit((e) -> {

            if (ss.updateDescription(tRoom.getItems().get(e.getTablePosition().getRow()), e.getNewValue())) {
                tRoom.getItems().get(e.getTablePosition().getRow()).setDescription(e.getNewValue());
            }
            tRoom.refresh();
        });
    }
    
    @FXML
    private void addRoom(ActionEvent event) {
        RoomService hs = new RoomService();
        Room h = new Room();
        //insert_stars
        h.setNbPersonnes(Integer.parseInt(nb.getText())); 
        h.setDescription(description.getText());
        h.setType(type.getText());
        h.setPrix(Integer.parseInt(prix.getText()));
        h.setId_hotel_id((test.getValue()));

        hs.addRoom(h);
        showRoom();

    }

    @FXML
    private void optionz(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("options.fxml"));
        Parent root = loader.load();
        tRoom.getScene().setRoot(root);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    
}
