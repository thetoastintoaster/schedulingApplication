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

    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label locationLabel;

    @FXML private TextField userNameID;
    @FXML private PasswordField passwordID;

    @FXML private Button loginButton;
    @FXML private Button exitButton;


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

        Locale currentLocale = Locale.getDefault();
        ResourceBundle messages = ResourceBundle.getBundle("messages", currentLocale);

        /*
        Locale frenchLocale = new Locale("fr");
        ResourceBundle messages = ResourceBundle.getBundle("messages", frenchLocale);
        */


        String usernameText = messages.getString("username");
        String passwordText = messages.getString("password");
        String locationText = messages.getString("location");
        String loginText = messages.getString("login");
        String exitText = messages.getString("exit");
        usernameLabel.setText(usernameText.substring(0,1).toUpperCase() + usernameText.substring(1));
        passwordLabel.setText(passwordText.substring(0,1).toUpperCase() + passwordText.substring(1));
        locationLabel.setText(locationText.substring(0,1).toUpperCase() + locationText.substring(1));
        loginButton.setText(loginText.substring(0,1).toUpperCase() + loginText.substring(1));
        exitButton.setText(exitText.substring(0,1).toUpperCase() + exitText.substring(1));

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
//        locationID.setText(location);
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
