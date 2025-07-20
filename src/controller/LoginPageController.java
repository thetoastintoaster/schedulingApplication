package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

import model.User;
import model.UserDatabase;

import java.time.ZoneId;
import java.util.ResourceBundle;


import java.util.Locale;
import java.util.ResourceBundle;


public class LoginPageController implements Initializable {

    @FXML private TextField userNameID;
    @FXML private PasswordField passwordID;

    @FXML private Button loginButton;

    @FXML private Label locationID;
//    if(userNameID = ) {
//
//    }
//

//    public LoginPageController(TextField userNameID, PasswordField passwordID) {
//    this.userNameID = userNameID;
//    this.passwordID = passwordID;
//    }

    static int userID = UserDatabase.incrementUserID();
    static User firstUser = new User(userID,"companyUser1","password123");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId zoneId = ZoneId.systemDefault();
        String location = zoneId.toString();
        locationID.setText(location);
    }

//    @FXML public static String loadLocation(){
//        ZoneId zoneId = ZoneId.systemDefault();
//        //        String location = locationID.setText(currentLocation);
//        return zoneId.toString();
//    }
//
//    public void setLocation() {
//        String location = loadLocation();
//        locationID.setText(location);
////        locationID.setText(location);
//    }

    public static void addTestUser() {
        UserDatabase.addUser(firstUser);
    }
    public void loginButtonAction(ActionEvent event){
        try{
            String userName = userNameID.getText();
            String password = passwordID.getText();

            if(Objects.equals(userName, firstUser.userName) && Objects.equals(password, firstUser.password)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/Home.fxml"));
                Parent root = loader.load();

                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Software II WGU");
                stage.show();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
