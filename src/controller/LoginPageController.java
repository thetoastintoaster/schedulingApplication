package controller;

import javafx.application.Platform;
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
import java.util.*;

import model.User;
import model.UserDatabase;

import java.time.ZoneId;


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

//        Locale frenchLocale = new Locale("fr");
//        ResourceBundle messages = ResourceBundle.getBundle("messages", frenchLocale);


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

            if ((!Objects.equals(userName, firstUser.userName) && !Objects.equals(userName,"")) && (!Objects.equals(password, firstUser.password) && !Objects.equals(password,""))) {
                alertMessage(1);
            }

            if (Objects.equals(userName, "") && Objects.equals(password, "")) {
                alertMessage(5);
            } else if (Objects.equals(userName, "")) {
                alertMessage(2);
            } else if (Objects.equals(password, "")) {
                alertMessage(3);
            }


        } catch (Exception e) {
            alertMessage(4);

        }
    }

    public void exitBtn(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Closing...");
        alert.setContentText("Exit the application?");
        Optional<ButtonType> res = alert.showAndWait();

        if (res.isPresent() && res.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }

    }

    private void alertMessage(int alertType) {

        Locale currentLocale = Locale.getDefault();
        ResourceBundle messages = ResourceBundle.getBundle("messages", currentLocale);

//        Locale frenchLocale = new Locale("fr");
//        ResourceBundle messages = ResourceBundle.getBundle("messages", frenchLocale);

        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (alertType) {
            case 1:
                alert.setTitle(messages.getString("error"));
                alert.setHeaderText(messages.getString("incorrect_credentials"));
                alert.setContentText(messages.getString("user_or_pass_incorrect"));
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle(messages.getString("error"));
                alert.setHeaderText(messages.getString("no_input_username"));
                alert.setContentText(messages.getString("username_field_is_blank"));
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle(messages.getString("error"));
                alert.setHeaderText(messages.getString("no_input_password"));
                alert.setContentText(messages.getString("password_is_blank"));
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle(messages.getString("error"));
                alert.setHeaderText(messages.getString("login_error"));
                alert.setContentText(messages.getString("unable_to_login"));
                alert.showAndWait();
                break;

            case 5:
                alert.setTitle(messages.getString("error"));
                alert.setHeaderText(messages.getString("no_input"));
                alert.setContentText(messages.getString("both_fields_are_blank"));
                alert.showAndWait();
                break;
        }
    }

}
