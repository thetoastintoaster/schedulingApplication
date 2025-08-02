package controller;

import dao.AppointmentDAO;
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

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import model.Appointments;
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
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;

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


    public static void addTestUser() {
        UserDatabase.addUser(firstUser);
    }
    @FXML
    private void loginButtonAction(ActionEvent event) {
        String usernameInput = usernameField.getText().trim();
        String passwordInput = passwordField.getText().trim();

        if (usernameInput.isEmpty() && passwordInput.isEmpty()) {
            alertMessage(5);
            return;
        } else if (usernameInput.isEmpty()) {
            alertMessage(2);
            return;
        } else if (passwordInput.isEmpty()) {
            alertMessage(3);
            return;
        }

        boolean login = false;
        String url = "jdbc:mysql://localhost/client_schedule?connectionTimeZone = SERVER";
        String username = "sqlUser";
        String password = "passw0rd!";
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, usernameInput);
                ps.setString(2, passwordInput);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    login = true;
                    logLogin(usernameInput, true);

                    checkUpcomingAppointments(usernameInput);

                    // Load home screen
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Main Menu");
                    stage.show();


                } else {
                    logLogin(usernameInput, false);
                    alertMessage(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
//            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    // Method that logs login activity to a text file
    private void logLogin(String username, boolean success) {
        try (FileWriter writer = new FileWriter("login_activity.txt", true)) {
            ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.systemDefault());
            String status = success ? "SUCCESS" : "FAILURE";
            writer.write(String.format("User: %s | Time: %s | Status: %s%n", username, timestamp, status));
        } catch (IOException e) {
            System.out.println("Error logging login attempt.");
        }
    }

    // Method that checks the upcoming appointments
    private void checkUpcomingAppointments(String username) {
        ZoneId userZone = ZoneId.systemDefault();
        ZonedDateTime now = ZonedDateTime.now(userZone);
        ZonedDateTime in15Min = now.plusMinutes(15);

        // Gets all of the appointments
        List<Appointments> appointments = AppointmentDAO.getAllAppointments();

        // Filters the list of appointments for an appointment that starts within the next 15 minutes
        Optional<Appointments> upcomingAppointment = appointments.stream()
                .filter(appointment -> {
                    ZonedDateTime startLocal = appointment.getStartDateTime()
                            .atZone(ZoneOffset.UTC)
                            .withZoneSameInstant(userZone);
                    return startLocal.isAfter(now) && startLocal.isBefore(in15Min);
                })
                .findFirst();

        // If an appointment is within/not within the next 15 minutes, display an alert message
        if (upcomingAppointment.isPresent()) {
            Appointments appointment = upcomingAppointment.get();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upcoming Appointment");
            alert.setHeaderText("You have an appointment within 15 minutes.");
            alert.setContentText("ID: " + appointment.getAppointmentId() +
                    "\nDate: " + appointment.getStartTime().toLocalDate() +
                    "\nTime: " + appointment.getStartTime().toLocalTime());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Upcoming Appointments");
            alert.setHeaderText(null);
            alert.setContentText("You have no appointments within the next 15 minutes.");
            alert.showAndWait();
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

    // Method for the alert messages
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
