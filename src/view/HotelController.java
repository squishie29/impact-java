/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import entity.Hotel;
import java.awt.Desktop;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import projet.util.connexionbd;
import service.HotelService;

/**
 * FXML Controller class
 *
 * @author Rami Guesmi
 */
public class HotelController implements Initializable {

    @FXML
    private TableView<Hotel> hotel_tv;
    @FXML
    private TableColumn<Hotel, Integer> tv_id;
    @FXML
    private TableColumn<Hotel, String> tv_name;
    @FXML
    private TableColumn<Hotel, Integer> tv_stars;
    @FXML
    private TableColumn<Hotel, String> tv_description;
    @FXML
    private TableColumn<Hotel, String> tv_adress;
    @FXML
    private TableColumn<Hotel, ImageView> tv_photo;
    @FXML
    private TableColumn<Hotel, Button> delete;
    @FXML
    private TableColumn<Hotel, Button> update;
    @FXML
    private TextField search;
    @FXML
    private AnchorPane anchorpane;
    private FileChooser fileChooser;
    private File file;
    @FXML
    private TextField insert_stars;
    @FXML
    private TextField insert_name;
    @FXML
    private TextArea insert_description;
    @FXML
    private TextField insert_adress;
    @FXML
    private TextField upload_text;
    @FXML
    private Button upload;
    @FXML
    private ImageView view_image_hotel;
    @FXML
    private Button add;
    private Path to;
    private Path from;
    @FXML
    private Button adc1;
    public static Stage stage;
    @FXML
    private ImageView nameCheck;
    @FXML
    private ImageView starsCheck;
    @FXML
    private ImageView adressCheck;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tv_id.setVisible(false);
        // TODO
        showHotel();
    }

    public void showHotel() {
        HotelService ss = new HotelService();
        ResultSet resultset = ss.getAll();

        //List<Reclamation> lr = new ArrayList<Reclamation>();
        ObservableList<Hotel> listHotels = FXCollections.observableArrayList();
        try {
            tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            tv_name.setCellValueFactory(new PropertyValueFactory<>("name"));
            tv_stars.setCellValueFactory(new PropertyValueFactory<>("stars"));
            tv_description.setCellValueFactory(new PropertyValueFactory<>("description"));
            tv_adress.setCellValueFactory(new PropertyValueFactory<>("adress"));
            tv_photo.setCellValueFactory(new PropertyValueFactory<>("photo_view"));
            while (resultset.next()) {
                Hotel r1 = new Hotel();
                r1.setId(resultset.getInt("id"));
                r1.setName(resultset.getString("name"));
                r1.setStars(resultset.getInt("stars"));
                r1.setDescription(resultset.getString("description"));
                r1.setPhoto(resultset.getString("photo"));
                ImageView a = new ImageView();
                Image img = new Image("file:" + resultset.getString("photo"));
                a.setImage(img);
                a.setFitHeight(90);
                a.setFitWidth(90);
                r1.setPhoto_view(a);
                listHotels.add(r1);

            }

            //doubleClickShowPiece();
            update.setCellFactory((param) -> {
                return new TableCell() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        setGraphic(null);
                        if (!empty) {
                            Button b = new Button("change image");
                            b.setOnAction((event) -> {

                                FileChooser filechooser = new FileChooser();
                                Stage stage = (Stage) anchorpane.getScene().getWindow();
                                filechooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"));
                                file = filechooser.showOpenDialog(stage);
                                if (file != null) {
                                    System.out.println();
                                    ImageView a = new ImageView();
                                    Image image = new Image(file.toURI().toString(), 80, 80, true, true);
                                    a.setImage(image);

                                    ss.updatePhoto(hotel_tv.getItems().get(getIndex()), String.valueOf(file));

                                    hotel_tv.getItems().get(getIndex()).setPhoto_view(a);
                                    hotel_tv.refresh();

                                }
                            });
                            setGraphic(b);
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

                                if (ss.delete(hotel_tv.getItems().get(getIndex()))) {
                                    showHotel();
                                }
                            });
                            setGraphic(b);
                        }
                    }
                };
            });

           
            
            
            //partie recherche
            FilteredList<Hotel> filteredData = new FilteredList<>(listHotels, b -> true);
            search.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(hotel -> {
                    // If filter text is empty, display all persons.

                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare first name and last name of every person with filter text.
                    String lowerCaseFilter = newValue.toLowerCase();

                    if (hotel.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true; // Filter matches first name.
                    } //				else if (String.valueOf(reclamation.getSalary()).indexOf(lowerCaseFilter)!=-1)
                    //				     return true;
                    else {
                        return false; // Does not match.
                    }
                });
            });
            SortedList<Hotel> sortedData = new SortedList<>(filteredData);

            // 4. Bind the SortedList comparator to the TableView comparator.
            // 	  Otherwise, sorting the TableView would have no effect.
            sortedData.comparatorProperty().bind(hotel_tv.comparatorProperty());
            hotel_tv.setItems(filteredData);

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        //edit
        tv_name.setCellFactory(TextFieldTableCell.forTableColumn());
        tv_name.setOnEditCommit((e) -> {

            if (ss.updateName(hotel_tv.getItems().get(e.getTablePosition().getRow()), e.getNewValue())) {
                hotel_tv.getItems().get(e.getTablePosition().getRow()).setName(e.getNewValue());
            }
            hotel_tv.refresh();
        });
    }

    @FXML
    private void uploadfile(ActionEvent event) throws IOException {
        FileChooser filechooser = new FileChooser();
        Stage stage1 = (Stage) anchorpane.getScene().getWindow();
        filechooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        file = filechooser.showOpenDialog(stage1);
        if (file != null) {
            from = Paths.get(file.toURI());
            to = Paths.get("src/imageshotel/" + file.getName());
            upload_text.setText(to.toString());

            Image image = new Image(file.toURI().toString(), 200, 200, true, true);
            view_image_hotel.setImage(image);
            Files.copy(from, to);
        }
    }

    @FXML
    private void addHotel(ActionEvent event) {
        if( testSaisie()){
        HotelService hs = new HotelService();
        Hotel h = new Hotel();
        //insert_stars
        h.setName(insert_name.getText());
            try {
                h.setStars(Integer.parseInt(insert_stars.getText()));
            } catch (NumberFormatException numberFormatException) {
            }
        h.setAdress(insert_adress.getText());
        h.setDescription(insert_description.getText());
        h.setPhoto(upload_text.getText());

        hs.addHotel(h);}
        
        showHotel();

    }

    @FXML
    private void roomz(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("room.fxml"));
        Parent root = loader.load();
        hotel_tv.getScene().setRoot(root);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    @FXML
    private void export(ActionEvent event) throws DocumentException {
        try {
            String file_name = ("evennement.pdf");
            Document document = new Document();
            
            PdfWriter.getInstance(document, new FileOutputStream(file_name));
            
            document.open();
            
            document.addTitle("HOTELS ");
            
                 Paragraph paraHeader1 = new Paragraph("HOTELS");
            document.add(paraHeader1);
           //         ObservableList<Message> list =pcr.getMessage();
                  // while(list)

            
                 // ObservableList<Message> ProductList = FXCollections.observableArrayList();
       // String requete = "SELECT nom,destination,prix,nbr_places FROM event where id="+id+"";
         String requete = "SELECT  nom,destination,prix,nbr_places FROM event where id";
        try {
            Connection c = connexionbd.getinstance().getConn();
            
            PreparedStatement pst = c.prepareStatement("SELECT name,stars,description,adress FROM hotel where id");
            

            //Statement st;
            try {

                ResultSet rs = pst.executeQuery();
                
                Hotel e;

                while (rs.next()) {
                  
               Paragraph paraHeader11 = new Paragraph(((rs.getString("name").concat("    ")).concat(rs.getString("stars"))).concat("   ".concat(rs.getString("description"))).concat("   ".concat(rs.getString("adress"))));
            document.add(paraHeader11);
                }

            } catch (DocumentException | SQLException ex) {
                

            }
        } catch (SQLException ex) {
        }
                  
                  
                  
                  
                  
                  
                   
            
            
            
            document.close();
            Desktop.getDesktop().open(new File(file_name));
        } catch (IOException ex) {
            
        }
    }
    
    
    private Pattern patternName = Pattern.compile("\\b(\\w{4,})+\\b");
    private Pattern patternStars = Pattern.compile("^[1-7]$");
    

        public boolean testName() {
  
        if (patternName.matcher(insert_name.getText()).matches())
        {
           nameCheck.setImage(new Image("Image/checkmark.png")); 
        }
        else
        {
            nameCheck.setImage(new Image("Image/alertemark.png"));
        }
        
        return patternName.matcher(insert_name.getText()).matches();
    }
    
    public boolean testStars() {
  
        if (patternStars.matcher(insert_stars.getText()).matches())
        {
           starsCheck.setImage(new Image("Image/checkmark.png")); 
        }
        else
        {
            starsCheck.setImage(new Image("Image/alertemark.png"));
        }
        
        return (patternStars.matcher(insert_stars.getText()).matches());
    }

    private Boolean testSaisie() {
        System.out.println(testStars());
        String erreur = "";
        if (!testName()) {
            erreur = erreur + ("Veuillez verifier votre Nom: seulement des caractères et de nombre >= 3 \n");
        }
        if (testStars()==false) {
            erreur = erreur + ("stars numer only");
        }/*
        if (!testemail()) {
            erreur = erreur + ("Veuillez verifier que votre adresse email est de la forme : ***@***.** \n");
        }
        if (!testPassword()) {
            erreur = erreur + ("Veuillez verifier votre Mot de passe: seulement des caractères et de nombre >= 3");
        }*/
        

        if ( (!testName()) || (!testStars()) ) {
           
           
           
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
    private void nameCheck(KeyEvent event) {
        testName();
    }

    @FXML
    private void starsCheck(KeyEvent event) {
        testStars();
    }

    @FXML
    private void adressCheck(KeyEvent event) {
    }



}
