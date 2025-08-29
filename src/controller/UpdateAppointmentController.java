package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Appointments;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller for the update appointment menu */
public class UpdateAppointmentController {

    /** Initializing the variables to use for the respective fields in the update appointment window */
    @FXML private TextField appointmentIdField;
    @FXML private TextField appointmentNameField;
    @FXML private TextField appointmentDescriptionField;
    @FXML private TextField appointmentLocationField;
    @FXML private TextField appointmentTypeField;
    @FXML private DatePicker appointmentStartDate;
    @FXML private DatePicker appointmentEndDate;
    @FXML private ComboBox<String> appointmentStartTime;
    @FXML private ComboBox<String> appointmentEndTime;

    /** Drop down menus (ContactId is a string because later in the code, the Id
     * is replaced with the respective name of the contact */
    @FXML private ComboBox<Integer> customerIdDropDownMenu;
    @FXML private ComboBox<Integer> userIdDropDownMenu;
    @FXML private ComboBox<String> contactIdDropDownMenu;

    private final String url = "jdbc:mysql://localhost/client_schedule?connectionTimeZone=SERVER";
    private final String username = "sqlUser";
    private final String password = "passw0rd!";

    private Appointments selectedAppointment;
//    int appointmentId = HomeController.selectedAppointmentToModify().getAppointmentId();

    /** Init function used to set up the menu before it fully loads */
    @FXML
    public void initialize() {

        selectedAppointment = HomeController.selectedAppointmentToModify();

        /** Populate time combo boxes with 30-minute increments */
        for (int hour = 0; hour < 24; hour++) {
            for (int min = 0; min < 60; min += 30) {
                String time = String.format("%02d:%02d", hour, min);
                appointmentStartTime.getItems().add(time);
                appointmentEndTime.getItems().add(time);
            }
        }

        /** If the selected appointment is not null, update that appointment */
            Appointments selectedAppointment = HomeController.selectedAppointmentToModify();
            if (selectedAppointment != null) {
                appointmentIdField.setText(String.valueOf(selectedAppointment.getAppointmentId()));
                appointmentNameField.setText(selectedAppointment.getTitle());
                appointmentDescriptionField.setText(selectedAppointment.getDescription());
                appointmentLocationField.setText(selectedAppointment.getLocation());
                appointmentTypeField.setText(selectedAppointment.getType());

                appointmentStartDate.setValue(selectedAppointment.getStartDateTime().toLocalDate());
                appointmentEndDate.setValue(selectedAppointment.getEndDateTime().toLocalDate());
                appointmentStartTime.setValue(selectedAppointment.getStartDateTime().toLocalTime().toString());
                appointmentEndTime.setValue(selectedAppointment.getEndDateTime().toLocalTime().toString());

                customerIdDropDownMenu.setValue(selectedAppointment.getCustomerID());
                userIdDropDownMenu.setValue(selectedAppointment.getUserID());
                contactIdDropDownMenu.setValue(selectedAppointment.getContact());

        }
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            Statement stmt = conn.createStatement();

            /** Populate customer ID drop down menu */
            ResultSet rsCustomers = stmt.executeQuery("SELECT Customer_ID FROM customers");
            while (rsCustomers.next()) {
                customerIdDropDownMenu.getItems().add(rsCustomers.getInt("Customer_ID"));
            }

            /** Populate user ID drop down menu */
            ResultSet rsUsers = stmt.executeQuery("SELECT User_ID FROM users");
            while (rsUsers.next()) {
                userIdDropDownMenu.getItems().add(rsUsers.getInt("User_ID"));
            }

            /** Populate contact ID drop down menu */
            ResultSet rsContacts = stmt.executeQuery("SELECT Contact_Name FROM contacts");
            while (rsContacts.next()) {
                contactIdDropDownMenu.getItems().add(rsContacts.getString("Contact_Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /** Cancels the input and returns to /Home.fxml */
    public void cancelButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Do you want cancel changes and return to the main screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
//            Utility.returnToHome(event);
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

    /** Adds the changes to the database */
    public void saveButton(ActionEvent actionEvent) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {

            /** Look up contact_id based on the selected contact name */
            String contactName = contactIdDropDownMenu.getValue();
            int contactId;

            try (PreparedStatement psContact = conn.prepareStatement(
                    "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?")) {
                psContact.setString(1, contactName);
                try (ResultSet rs = psContact.executeQuery()) {
                    if (rs.next()) {
                        contactId = rs.getInt("Contact_ID");
                    } else {
                        throw new IllegalStateException(
                                "Selected contact name '" + contactName + "' not found in database.");
                    }
                }
            }

            /** Parse dates and times from the UI */
            LocalDate startDate = appointmentStartDate.getValue();
            LocalDate endDate = appointmentEndDate.getValue();
            LocalTime startTime = LocalTime.parse(appointmentStartTime.getValue());
            LocalTime endTime = LocalTime.parse(appointmentEndTime.getValue());

            ZoneId localZone = ZoneId.systemDefault();
            ZonedDateTime zonedStart = ZonedDateTime.of(LocalDateTime.of(startDate, startTime), localZone);
            ZonedDateTime zonedEnd = ZonedDateTime.of(LocalDateTime.of(endDate, endTime), localZone);

            /** Convert to UTC */
            ZonedDateTime startUTC = zonedStart.withZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime endUTC = zonedEnd.withZoneSameInstant(ZoneOffset.UTC);

            /** Convert to Timestamps */
            Timestamp startTimestamp = Timestamp.valueOf(startUTC.toLocalDateTime());
            Timestamp endTimestamp = Timestamp.valueOf(endUTC.toLocalDateTime());

            /** Check business hours */
             LocalTime businessOpen = LocalTime.of(8, 0);
             LocalTime businessClose = LocalTime.of(22, 0);
             ZoneId easternZone = ZoneId.of("America/New_York");
             ZonedDateTime easternStart = zonedStart.withZoneSameInstant(easternZone);
             ZonedDateTime easternEnd = zonedEnd.withZoneSameInstant(easternZone);


             if (easternStart.toLocalTime().isBefore(businessOpen) || easternEnd.toLocalTime().isAfter(businessClose)) {
                 Alert alert = new Alert(Alert.AlertType.ERROR, "Appointment must be between 8:00 AM and 10:00 PM EST.");
                 alert.showAndWait();
                 return;
             }

             /** Update query */
            String sql = "UPDATE appointments SET title=?, description=?, location=?, type=?, start=?, end=?, " +
                    "customer_id=?, user_id=?, contact_id=? WHERE appointment_id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, appointmentNameField.getText());
                ps.setString(2, appointmentDescriptionField.getText());
                ps.setString(3, appointmentLocationField.getText());
                ps.setString(4, appointmentTypeField.getText());
                ps.setTimestamp(5, startTimestamp);
                ps.setTimestamp(6, endTimestamp);
                ps.setInt(7, customerIdDropDownMenu.getValue());
                ps.setInt(8, userIdDropDownMenu.getValue());
                ps.setInt(9, contactId);
                ps.setInt(10, selectedAppointment.getAppointmentId());

                ps.executeUpdate();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Appointment updated successfully!");
            alert.showAndWait();

            Stage stage = (Stage) appointmentNameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error updating appointment: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
