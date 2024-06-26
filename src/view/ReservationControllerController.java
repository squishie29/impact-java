/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.twilio.Twilio;
import entity.ReservationHotel;
import entity.Room;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import static org.eclipse.persistence.expressions.ExpressionOperator.today;
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
    private TableColumn<ReservationHotel, java.sql.Date> debutR;
    @FXML
    private TableColumn<ReservationHotel, java.sql.Date> finR;
    @FXML
    private TableColumn<ReservationHotel, String> confirmationR;
    @FXML
    private TableColumn<ReservationHotel, Button> delete;
    @FXML
    private TableColumn<ReservationHotel, String> emailR;
    
    @FXML
    private ComboBox<String> user;
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
    
    public static Boolean recaptchaisvalid = false;
    int recaptchastate = 0;
    @FXML
    private CheckBox inputrecaptcha;
    
    public static final String ACCOUNT_SID = "AC6eb7ffebcd4e5112b2823056010a94c0";
    public static final String AUTH_TOKEN = "78fa4a0360f61107dcb69ebcc6bff907";
    @FXML
    private TextField admin;
    @FXML
    private ImageView numberCheck;
    @FXML
    private TableColumn<?, ?> typeR1;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userR.setVisible(false);
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
            emailR.setCellValueFactory(new PropertyValueFactory<>("email"));
            typeR1.setCellValueFactory(new PropertyValueFactory<>("type"));
            userR.setCellValueFactory(new PropertyValueFactory<>("user_id_id"));
            roomR.setCellValueFactory(new PropertyValueFactory<>("room_id_id"));
            debutR.setCellValueFactory(new PropertyValueFactory<>("debut"));
            finR.setCellValueFactory(new PropertyValueFactory<>("fin"));
            confirmationR.setCellValueFactory(new PropertyValueFactory<>("confirmation"));
            while (resultset.next()) {
                ReservationHotel r1 = new ReservationHotel();
                r1.setId(resultset.getInt("id"));
                
                
                //System.out.println(ss.getMail(resultset.getInt("user_id_id")));
               //String rep=ss.getMail(resultset.getInt("user_id_id"));
                //test.setCellValueFactory(x -> new SimpleObjectProperty<>(rep));
                
                //r1.setTest(ss.getMail(resultset.getInt("user_id_id")));
                 r1.setUser_id_id(resultset.getInt("user_id_id"));
                r1.setRoom_id_id(resultset.getInt("room_id_id"));
                r1.setDebut(resultset.getDate("debut"));
                r1.setFin(resultset.getDate("fin"));
                r1.setConfirmation(resultset.getString("confirmation"));
                
                r1.setEmail(resultset.getString("email"));
                r1.setType(resultset.getString("type"));
                
                System.out.println(resultset.getString("email"));
                
                listReservationHotel.add(r1);

            }

            //modifier 
            debutR.setCellFactory((param) -> {

                return new TableCell<ReservationHotel, java.sql.Date>() {
                    @Override
                    protected void updateItem(java.sql.Date item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(null);
                        if (item != null && !empty) {
                            LocalDate today = LocalDate.now();
                            DatePicker picker = new DatePicker();
                            //LocalDate date = LocalDate.parse("2020-01-08");
                            picker.setValue(item.toLocalDate());
                            setGraphic(picker);
                            picker.setOnAction(t -> {
                                if (picker.getValue().isAfter(today) || picker.getValue().isEqual(today)) {
                                commitEdit(item);
                                //System.out.println(item);
                                ss.updateDateDebut(tReservationHotel.getItems().get(getIndex()), java.sql.Date.valueOf(picker.getValue()));
                                tReservationHotel.getItems().get(getIndex()).setDebut(java.sql.Date.valueOf(picker.getValue()));
                                }
                                else
                                {
                                    System.out.println("hehehehe boy");
                                    //Alerts.warning("Invalid Date", "Start Date value cannot be before current date.");
                                    tReservationHotel.refresh();
                                }
                            });

                            // Addtv.refresh();
                            picker.setValue(tReservationHotel.getItems().get(getIndex()).getDebut().toLocalDate());
                            setGraphic(picker);
                            //System.out.println(picker.getValue());

                        }

                    }

                };
            });

           finR.setCellFactory((param) -> {

                return new TableCell<ReservationHotel, java.sql.Date>() {
                    @Override
                    protected void updateItem(java.sql.Date item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(null);
                        if (item != null && !empty) {
                            LocalDate today = LocalDate.now();
                            DatePicker picker = new DatePicker();
                            //LocalDate date = LocalDate.parse("2020-01-08");
                            picker.setValue(item.toLocalDate());
                            setGraphic(picker);
                            picker.setOnAction(t -> {
                                if (picker.getValue().isAfter(today)) {
                                commitEdit(item);
                                //System.out.println(item);
                                ss.updateDateFin(tReservationHotel.getItems().get(getIndex()), java.sql.Date.valueOf(picker.getValue()));
                                tReservationHotel.getItems().get(getIndex()).setFin(java.sql.Date.valueOf(picker.getValue()));
                                }
                                else
                                {
                                    System.out.println("o lala");
                                }
                            });
                            // Addtv.refresh();
                            picker.setValue(tReservationHotel.getItems().get(getIndex()).getFin().toLocalDate());
                            setGraphic(picker);
                            //System.out.println(picker.getValue());
                        }

                    }

                };
            });   
            
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
            
            
            userR.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

            
            
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
        
        userR.setOnEditCommit((e) -> {

            if (ss.updateUser(tReservationHotel.getItems().get(e.getTablePosition().getRow()), e.getNewValue())) {
                tReservationHotel.getItems().get(e.getTablePosition().getRow()).setUser_id_id(e.getNewValue());
            }
            tReservationHotel.refresh();
        });
        

        
        
        

    }
    
    
    

    @FXML
    private void addReservationHotel(ActionEvent event) {
        if( testSaisie()){
        
        ReservationHotelService hs = new ReservationHotelService();
        ReservationHotel h = new ReservationHotel();
        //insert_stars
        h.setUser_id_id(2);
        h.setRoom_id_id((room.getValue()));
        
        
        
        try {
            h.setDebut(java.sql.Date.valueOf(debut.getValue()));
            h.setFin(java.sql.Date.valueOf(fin.getValue()));
        } catch (Exception e) {
        }
        h.setConfirmation("confirmed");
        
        if(recaptchaisvalid == true){
            try {
                hs.addReservationHotel(h);
            } catch (Exception e) {
            }
        }
        else 
            System.out.println("no no no");
        
        showReservationHotel();
        envoyerSMS();}
    }
    

    @FXML
    private void listen(ActionEvent event) {
        Stage stageRecap = new Stage();
        WebView webView = new WebView();

        WebEngine webEngine = webView.getEngine();
        // Delete cache for navigate back
        webEngine.load("about:blank");
        // Delete cookies 
        java.net.CookieHandler.setDefault(new java.net.CookieManager());

        webEngine.load("http://ramy");

        VBox vBox = new VBox(webView);
        Scene scene = new Scene(vBox, 350, 500);
        stageRecap.setScene(scene);
        //stageRecap.initStyle(StageStyle.UNDECORATED);
        stageRecap.show();
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (Worker.State.SUCCEEDED.equals(newValue)) {
                String sss = webEngine.getLocation();
                if (sss.contains("success")) {
                    recaptchaisvalid = true;
                    stageRecap.close();
                }
                //System.out.println(sss);
            }
        });
    }
    
    public void envoyerSMS() {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            com.twilio.rest.api.v2010.account.Message message = com.twilio.rest.api.v2010.account.Message.creator( //preparation d'un nouveau msg
                    new com.twilio.type.PhoneNumber("+216" + admin.getText()), // num reception
                    new com.twilio.type.PhoneNumber("+12054489231"), // num d'envoie
                    "Reservation de " + user.getValue() + " sur chambre id" + room.getValue() + "de " + debut.getValue() + "jusqu'a" + fin.getValue() + "confirmé") // le msg
                    .create(); //creation
            System.out.println(userR.getText());
        } catch (Exception e) {
        }
        //System.out.println(message.getSid());
    }
    
    
     private Pattern patternPhone = Pattern.compile("^[0-9]{8}$");
    

        public boolean testPhone() {
  
        if (patternPhone.matcher(admin.getText()).matches())
        {
           numberCheck.setImage(new Image("Image/checkmark.png")); 
        }
        else
        {
            numberCheck.setImage(new Image("Image/alertemark.png"));
        }
        
        return patternPhone.matcher(admin.getText()).matches();
    }
    
        
        
            private Boolean testSaisie() {
                LocalDate today = LocalDate.now();
                
               
        //System.out.println(testStars());
        String erreur = "";
        if (!testPhone()) {
            erreur = erreur + ("Insert a valid phone number\n");
        }
        
         if (debut.getValue().isBefore(today))  {
             erreur = erreur + ("date debut !!\n");
         }
         if (fin.getValue().isBefore(debut.getValue()))  {
             erreur = erreur + ("date fin!!\n");
         }

        if  ((!testPhone()) || (debut.getValue().isBefore(today)) || (fin.getValue().isBefore(debut.getValue()))  )  {
           
           
           
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
    private void numberCheck(KeyEvent event) {
        testPhone();
    }
    
}
