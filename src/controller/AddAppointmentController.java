package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyEvent;

import java.sql.Timestamp;
import java.time.*;

import java.io.IOException;
import java.sql.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import helper.Utility;
import static helper.Utility.showAlert;


public class AddAppointmentController {
    @FXML private TextField appointmentIdField;
    @FXML private TextField appointmentNameField;
    @FXML private TextField appointmentDescriptionField;
    @FXML private TextField appointmentLocationField;
    @FXML private TextField appointmentTypeField;
    @FXML private DatePicker appointmentStartDate;
    @FXML private DatePicker appointmentEndDate;
    @FXML private ComboBox<String> appointmentStartTime;
    @FXML private ComboBox<String> appointmentEndTime;
    @FXML private ComboBox<Integer> customerIdDropDownMenu;
    @FXML private ComboBox<Integer> userIdDropDownMenu;
    @FXML private ComboBox<Integer> contactIdDropDownMenu;

    private final String url = "jdbc:mysql://localhost/client_schedule?connectionTimeZone=SERVER";
    private final String username = "sqlUser";
    private final String password = "passw0rd!";

    @FXML
    public void initialize() {
        // Populate time combo boxes with 30-minute increments
        for (int hour = 0; hour < 24; hour++) {
            for (int min = 0; min < 60; min += 30) {
                String time = String.format("%02d:%02d", hour, min);
                appointmentStartTime.getItems().add(time);
                appointmentEndTime.getItems().add(time);
            }
        }

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            Statement stmt = conn.createStatement();

            // Populate customer ID drop down menu
            ResultSet rsCustomers = stmt.executeQuery("SELECT Customer_ID FROM customers");
            while (rsCustomers.next()) {
                customerIdDropDownMenu.getItems().add(rsCustomers.getInt("Customer_ID"));
            }

            // Populate user ID drop down menu
            ResultSet rsUsers = stmt.executeQuery("SELECT User_ID FROM users");
            while (rsUsers.next()) {
                userIdDropDownMenu.getItems().add(rsUsers.getInt("User_ID"));
            }

            // Populate contact ID drop down menu
            ResultSet rsContacts = stmt.executeQuery("SELECT Contact_ID FROM contacts");
            while (rsContacts.next()) {
                contactIdDropDownMenu.getItems().add(rsContacts.getInt("Contact_ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Adds the appointment to the database
    @FXML
    private void saveButton(ActionEvent event) throws SQLException {
        String name = appointmentNameField.getText();
        String description = appointmentDescriptionField.getText();
        String location = appointmentLocationField.getText();
        String type = appointmentTypeField.getText();
        LocalDate startDate = appointmentStartDate.getValue();
        LocalDate endDate = appointmentEndDate.getValue();
        LocalTime startTime = LocalTime.parse(appointmentStartTime.getValue());
        LocalTime endTime = LocalTime.parse(appointmentEndTime.getValue());
        try {
            if (name.isEmpty() ||
                    description.isEmpty() ||
                    location.isEmpty() ||
                    type.isEmpty() ||
                    startDate == null ||
                    endDate == null ||
                    startTime == null ||
                    endTime == null) {

                showAlert("Error", "All fields must be filled out.");
                return;
            }

            // Gets the start date/end date and start time/end time and stores it into localStart/localEnd respectively
            LocalDateTime localStart = LocalDateTime.of(startDate, startTime);
            LocalDateTime localEnd = LocalDateTime.of(endDate, endTime);

            // Gets localStart/localEnd and applies zone information (I don't fully understand this right now, but I think it
            // applies general zone information to prepare the variables for conversion later (zonedStart and zonedEnd
            // are used for Eastern Time conversion and UTC conversion)
            ZoneId localZone = ZoneId.systemDefault();
            ZonedDateTime zonedStart = ZonedDateTime.of(localStart, localZone);
            ZonedDateTime zonedEnd = ZonedDateTime.of(localEnd, localZone);

            // Convert to Eastern Time for business hour validation
            ZoneId easternZone = ZoneId.of("America/New_York");
            ZonedDateTime startET = zonedStart.withZoneSameInstant(easternZone);
            ZonedDateTime endET = zonedEnd.withZoneSameInstant(easternZone);

            // Checks if the selected time is within business hours
            LocalTime businessOpen = LocalTime.of(8, 0);
            LocalTime businessClose = LocalTime.of(22, 0);

            if (startET.toLocalTime().isBefore(businessOpen) || endET.toLocalTime().isAfter(businessClose)) {
                showAlert("Error", "Appointment must be between 8:00 AM and 10:00 PM");
                return;
            }

            // Convert to UTC
            ZonedDateTime startUTC = zonedStart.withZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime endUTC = zonedEnd.withZoneSameInstant(ZoneOffset.UTC);

            Timestamp startTimeStamp = Timestamp.valueOf(startUTC.toLocalDateTime());
            Timestamp endTimeStamp = Timestamp.valueOf(endUTC.toLocalDateTime());

            if (endTimeStamp.before(startTimeStamp)) {
                showAlert("Error", "End time must be after start time.");
                return;
            }

            String sql = "INSERT INTO appointments (title, description, location, type, start, end, customer_id, user_id, contact_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, name);
                ps.setString(2, description);
                ps.setString(3, location);
                ps.setString(4, type);
                ps.setTimestamp(5, startTimeStamp);
                ps.setTimestamp(6, endTimeStamp);
                ps.setInt(7, customerIdDropDownMenu.getValue());
                ps.setInt(8, userIdDropDownMenu.getValue());
                ps.setInt(9, contactIdDropDownMenu.getValue());

                ps.executeUpdate();

                showAlert("Success", "Appointment added successfully!");

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
            e.printStackTrace();
            showAlert("Error", "Could not save appointment: " + e.getMessage());
        }
    }

    // Does not anything to the database and then returns to home
    @FXML
    private void cancelButton(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Home.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Software II WGU");
        stage.show();
    }
}



