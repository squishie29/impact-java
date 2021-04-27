/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import entity.Options;
import entity.Room;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
import javafx.util.Duration;
import projet.util.connexionbd;
import service.OptionsService;
import service.RoomService;

/**
 * FXML Controller class
 *
 * @author rami
 */
public class OptionsController implements Initializable {

    @FXML
    private TableView<Options> tOptions;
    @FXML
    private TableColumn<Options, Integer> idO;
    @FXML
    private TableColumn<Options, String> descO;
    @FXML
    private TableColumn<Options, Integer> idR;
    @FXML
    private TableColumn<Options, Button> delete;
    @FXML
    private TextField description;
    @FXML
    private ComboBox<Integer> roomId;
    @FXML
    private TextField search;
    ObservableList<Options> list = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        idO.setVisible(false);
        
        RoomService Nc = new RoomService();
        ObservableList<Integer> idc = FXCollections.observableArrayList(Nc.afficherALLID());
        roomId.setItems(idc);
        showOptions();
    } 
    
    
    
    public void showOptions() {
    
//****AFFICHAGE****
        OptionsService ss = new OptionsService();
        ResultSet resultset = ss.getAll();

        //List<Reclamation> lr = new ArrayList<Reclamation>();
        ObservableList<Options> listOptions = FXCollections.observableArrayList();
        try {
            idO.setCellValueFactory(new PropertyValueFactory<>("id"));
            descO.setCellValueFactory(new PropertyValueFactory<>("description"));
            idR.setCellValueFactory(new PropertyValueFactory<>("room_id_id"));
            while (resultset.next()) {
                Options r1 = new Options();
                r1.setId(resultset.getInt("id"));
               
                r1.setDescription(resultset.getString("description"));
                r1.setRoom_id_id(resultset.getInt("room_id_id"));
                
                listOptions.add(r1);

            }
            tOptions.setItems(listOptions);

//****DELETE*****        

            delete.setCellFactory((param) -> {
                return new TableCell() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        setGraphic(null);
                        if (!empty) {
                            Button b = new Button("delete");
                            b.setOnAction((event) -> {

                                if (ss.delete(tOptions.getItems().get(getIndex()))) {
                                    showOptions();
                                }
                            });
                            setGraphic(b);
                        }
                    }
                };
            });


            

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        
//****EDIT*****
        descO.setCellFactory(TextFieldTableCell.forTableColumn());       
        descO.setOnEditCommit((e) -> {

            if (ss.updateDescription(tOptions.getItems().get(e.getTablePosition().getRow()), e.getNewValue())) {
                tOptions.getItems().get(e.getTablePosition().getRow()).setDescription(e.getNewValue());
            }
            tOptions.refresh();
        });
    }

    @FXML
    private void addOptions(ActionEvent event) {
       OptionsService os = new OptionsService();
        Options o = new Options();
        //insert_stars
        o.setDescription(description.getText());

        o.setRoom_id_id((roomId.getValue()));

        os.addOptions(o);
        showOptions(); 
        
    }

    @FXML
    private void galleriez(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gallery.fxml"));
        Parent root = loader.load();
        tOptions.getScene().setRoot(root);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    @FXML
    private void rechercheOption(ActionEvent event) throws SQLException {
         tOptions.getItems().clear();
            Connection c = connexionbd.getinstance().getConn();
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM options WHERE description like'%"+search.getText()+"%'");
            while(rs.next()){
                list.add(new Options(rs.getInt(1), rs.getString(2), rs.getInt(3)));

            }


            idO.setCellValueFactory(new PropertyValueFactory<>("id"));
            descO.setCellValueFactory(new PropertyValueFactory<>("description"));
            idR.setCellValueFactory(new PropertyValueFactory<>("room_id_id"));
                tOptions.setItems(list);
                    
    }
    
}
