/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.jfoenix.controls.JFXTextArea;
import entity.Hotel;
import entity.Room;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
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
import javafx.scene.control.Alert;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TouchEvent;
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
    private JFXTextArea description;
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
    @FXML
    private ImageView prixCheck;
    @FXML
    private ImageView typeCheck;
    @FXML
    private ImageView nbCheck;
    @FXML
    private TableColumn<?, ?> hotelR;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idR.setVisible(false);
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
            hotelR.setCellValueFactory(new PropertyValueFactory<>("name"));
            while (resultset.next()) {
                Room r1 = new Room();
                r1.setId(resultset.getInt("id"));
               
                r1.setDescription(resultset.getString("description"));
                r1.setType(resultset.getString("type"));
                r1.setPrix(resultset.getInt("prix"));
                r1.setNbPersonnes(resultset.getInt("nb_personnes"));
                r1.setId_hotel_id(resultset.getInt("id_hotel_id"));
                r1.setName(resultset.getString("name"));
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
        if( testSaisie()){
        RoomService hs = new RoomService();
        Room h = new Room();
        //insert_stars
        h.setNbPersonnes(Integer.parseInt(nb.getText())); 
        h.setDescription(description.getText());
        h.setType(type.getText());
        h.setPrix(Integer.parseInt(prix.getText()));
        h.setId_hotel_id((test.getValue()));

        hs.addRoom(h);}
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
    
    
    
    
     private Pattern patternType = Pattern.compile("[a-zA-Z]+[ .]*[a-zA-Z]+");
    private Pattern patternNb = Pattern.compile("^[1-4]$");
    private Pattern patternPrix = Pattern.compile("([0-9]+)");
    

        public boolean testType() {
  
        if (patternType.matcher(type.getText()).matches())
        {
           typeCheck.setImage(new Image("Image/checkmark.png")); 
        }
        else
        {
            typeCheck.setImage(new Image("Image/alertemark.png"));
        }
        
        return patternType.matcher(type.getText()).matches();
    }
    
    public boolean testNb() {
  
        if (patternNb.matcher(nb.getText()).matches())
        {
           nbCheck.setImage(new Image("Image/checkmark.png")); 
        }
        else
        {
            nbCheck.setImage(new Image("Image/alertemark.png"));
        }
        
        return (patternNb.matcher(nb.getText()).matches());
    }
    
    
     public boolean testPrix() {
  
        if (patternPrix.matcher(prix.getText()).matches())
        {
           prixCheck.setImage(new Image("Image/checkmark.png")); 
        }
        else
        {
            prixCheck.setImage(new Image("Image/alertemark.png"));
        }
        
        return (patternPrix.matcher(prix.getText()).matches());
    }

    private Boolean testSaisie() {
        //System.out.println(testStars());
        String erreur = "";
        if (!testType()) {
            erreur = erreur + ("Insert a valid Room type\n");
        }
        if (testNb()==false) {
            erreur = erreur + ("number of users must be between 1 and 4\n");
        }
        if (testPrix()==false) {
            erreur = erreur + ("price must be positive");
        }
        

        if ( (!testType()) || (!testNb()) || (!testPrix()) ) {
           
           
           
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Erreur de format");
        alert.setHeaderText("Vérifier les champs");
        alert.setContentText(erreur);
        alert.showAndWait();
        return false;
       
        }

        return true;
    }
    
    
    
    

    @FXML
    private void typeCehck(KeyEvent event) {
        testType();
    }

    @FXML
    private void nbCehck(KeyEvent event) {
        testNb();
    }

 

    @FXML
    private void prixCheck(KeyEvent event) {
        testPrix();
    }

    
}
