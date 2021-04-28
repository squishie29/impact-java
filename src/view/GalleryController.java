/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import entity.Gallery;
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
import static java.lang.ProcessBuilder.Redirect.to;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.util.Duration;
import static jdk.nashorn.internal.objects.NativeJava.to;
import service.GalleryService;
import service.HotelService;

/**
 * FXML Controller class
 *
 * @author rami
 */
public class GalleryController implements Initializable {

    @FXML
    private TableView<Gallery> tGallery;
    @FXML
    private TableColumn<Gallery, Integer> idG;
    @FXML
    private TableColumn<Gallery, String> imgG;
    @FXML
    private TableColumn<Gallery, Integer> idHotel;
    @FXML
    private TableColumn<Gallery, Button> delete;
    @FXML
    private TextField upload_text;
    @FXML
    private Button upload;
    @FXML
    private Button add;
    @FXML
    private ComboBox<Integer> idH;
    @FXML
    private TableColumn<Gallery, Button> update;
    @FXML
    private AnchorPane anchorpane;
    private FileChooser fileChooser;
    private File file;
    private Path from;
    private Path to;
    @FXML
    private ImageView view_image_gallery;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO*
        idG.setVisible(false);
        showGallery();
        HotelService Nc = new HotelService();
        ObservableList<Integer> IdcP = FXCollections.observableArrayList(Nc.afficherALLID());
        idH.setItems(IdcP);
    }    
    
    
    
     public void showGallery() {
        GalleryService ss = new GalleryService();
        ResultSet resultset = ss.getAll();

        //List<Reclamation> lr = new ArrayList<Reclamation>();
        ObservableList<Gallery> listGalleries = FXCollections.observableArrayList();
        try {
            idG.setCellValueFactory(new PropertyValueFactory<>("id"));
            
            imgG.setCellValueFactory(new PropertyValueFactory<>("photo_view"));
            idHotel.setCellValueFactory(new PropertyValueFactory<>("hotel_id_id"));
            while (resultset.next()) {
                Gallery r1 = new Gallery();
                r1.setId(resultset.getInt("id"));
                r1.setImgpath(resultset.getString("imgpath"));
                r1.setHotel_id_id(resultset.getInt("hotel_id_id"));
                System.out.println(resultset.getInt("hotel_id_id"));
                
                ImageView a = new ImageView();
                Image img = new Image("file:" + resultset.getString("imgpath"));
                a.setImage(img);
                a.setFitHeight(90);
                a.setFitWidth(90);
                r1.setPhoto_view(a);
                listGalleries.add(r1);

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

                                    ss.updatePhoto(tGallery.getItems().get(getIndex()), String.valueOf(file));

                                    tGallery.getItems().get(getIndex()).setPhoto_view(a);
                                    tGallery.refresh();

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

                                if (ss.delete(tGallery.getItems().get(getIndex()))) {
                                    showGallery();
                                }
                            });
                            setGraphic(b);
                        }
                    }
                };
            });
            
            tGallery.setItems(listGalleries);

        } catch (SQLException ex) {
            System.out.println(ex);
        }
       
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
            view_image_gallery.setImage(image);
            Files.copy(from, to);
        }
    }

    @FXML
    private void addGallery(ActionEvent event) {
        GalleryService gs = new GalleryService();
        Gallery g = new Gallery();
        //insert_stars

        g.setHotel_id_id((idH.getValue()));



        g.setImgpath(upload_text.getText());

        gs.addGallery(g);
        showGallery();

    }

    @FXML
    private void addReservation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("reservationController.fxml"));
        Parent root = loader.load();
        tGallery.getScene().setRoot(root);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }



    

    
}
