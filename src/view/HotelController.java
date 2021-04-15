/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

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
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Duration;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

            tv_name.setCellFactory(TextFieldTableCell.forTableColumn());
            
            
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
        HotelService hs = new HotelService();
        Hotel h = new Hotel();
        //insert_stars
        h.setName(insert_name.getText());
        h.setStars(Integer.parseInt(insert_stars.getText()));
        h.setAdress(insert_adress.getText());
        h.setDescription(insert_description.getText());
        h.setPhoto(upload_text.getText());

        hs.addHotel(h);
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

}
