/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author rami
 */
public class ShowHotelsController implements Initializable {

    @FXML
    private AnchorPane anchorpane;
    @FXML
    private ImageView view_image_hotel;
    @FXML
    private ImageView nameCheck;
    @FXML
    private ImageView starsCheck;
    @FXML
    private ImageView image;
    @FXML
    private Label name;
    @FXML
    private Label stars;
    @FXML
    private Label adress;
    @FXML
    private Label description;
    @FXML
    private Label count;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showhotel();
        // TODO
    }    

    
    public void showhotel(){
         //image
    Image img = new Image("file:" + FrontHotelController.hotelShow.getPhoto());
                image.setImage(img);
                
                FrontHotelController.hotelShow.setPhoto_view(image);
     name.setText(FrontHotelController.hotelShow.getName());
    
     stars.setText(""+FrontHotelController.hotelShow.getStars());
     
     count.setText(""+FrontHotelController.countroom);
   
     adress.setText(FrontHotelController.hotelShow.getAdress());
    
     description.setText(FrontHotelController.hotelShow.getDescription());
    }
    
    @FXML
    private void HotelDep(ActionEvent event) throws IOException {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("FrontHotel.fxml"));
        Parent root = loader.load();
        anchorpane.getScene().setRoot(root);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    
}
