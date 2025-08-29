package controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller class for the main menu */
public class HomeController implements Initializable {

    /** The customerToModify & appointmentToModify variables is for when the user
     * selects a customer/appointment in the table */
    private static Customers customerToModify;
    private static Appointments appointmentToModify;

    /** The table for the customers */
    @FXML private TableView<Customers> customerTable;

    /** The table for the appointments */
    @FXML private TableView<Appointments> appointmentTable;

    /** TableColumn variables for the customer table*/
    @FXML private TableColumn<Customers, Integer> customerIDCol;
    @FXML private TableColumn<Customers, String> customerNameCol;
    @FXML private TableColumn<Customers, String> customerAddressCol;
    @FXML private TableColumn<Customers, String> customerPostalCodeCol;
    @FXML private TableColumn<Customers, String> customerPhoneCol;

    /** TableColumn variables for the appointment table*/
    @FXML private TableColumn<Appointments, Integer> appointmentIDCol;
    @FXML private TableColumn<Appointments, String> appointmentNameCol;
    @FXML private TableColumn<Appointments, String> appointmentDescriptionCol;
    @FXML private TableColumn<Appointments, String> appointmentLocationCol;
    @FXML private TableColumn<Appointments, String> appointmentContactCol;
    @FXML private TableColumn<Appointments, String> appointmentTypeCol;
    @FXML private TableColumn<Appointments, String> appointmentStartCol;
    @FXML private TableColumn<Appointments, String> appointmentEndCol;
    @FXML private TableColumn<Appointments, Integer> appointmentCustomerIDCol;
    @FXML private TableColumn<Appointments, Integer> appointmentUserIDCol;

    /** Radio buttons for the month, week, and all  */
    @FXML private RadioButton monthRadioButton;
    @FXML private RadioButton weekRadioButton;
    @FXML private RadioButton allRadioButton;
    @FXML private ToggleGroup dateFilterGroup;


    /** Creates a list for the customers */
    private ObservableList<Customers> allCustomers = FXCollections.observableArrayList();

    /** Creates a list for the appointments */
    private ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();

    /** Function used to set up the menu before it fully loads */
    public void initialize(URL location, ResourceBundle resources) {

        /** Populate customers table view (no values yet, it just sets it up) */
        customerTable.setItems(allCustomers);
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));


        /** Populate appointment table view (no values yet, it just sets it up) */
        appointmentTable.setItems(allAppointments);
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentNameCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        appointmentUserIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));

        /** Properly sets up the start and end times for the appointments */
        DateTimeFormatter newTimeFormat = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");

        /** A lambda expression is used here because it makes the code easier to read */
        appointmentStartCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDateTime().format(newTimeFormat)));
        appointmentEndCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndDateTime().format(newTimeFormat)));


        /** Radio buttons */
        ToggleGroup dateFilterGroup = new ToggleGroup();
        monthRadioButton.setToggleGroup(dateFilterGroup);
        weekRadioButton.setToggleGroup(dateFilterGroup);
        allRadioButton.setToggleGroup(dateFilterGroup);
        allRadioButton.setSelected(true);



        String url = "jdbc:mysql://localhost/client_schedule?connectionTimeZone = SERVER";
        String username = "sqlUser";
        String password = "passw0rd!";

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            Statement stmt = connection.createStatement();

            /** Selects the customer table */
            ResultSet rs = stmt.executeQuery("SELECT * FROM customers");

            /** Populates the customer table */
            while(rs.next()) {
                allCustomers.add(new Customers(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("address"),
                        rs.getString("postal_code"),
                        rs.getString("phone"),
                        rs.getInt("division_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



        try (Connection connection = DriverManager.getConnection(url, username, password)){
            Statement stmt = connection.createStatement();

            /** Selects the appointments table */
            ResultSet rs = stmt.executeQuery("SELECT * FROM appointments");

            /** Replaces the contact ID's with the corresponding contact name so that the user */
            /** sees the names instead of the ID's which could be confusing */
            while(rs.next()) {
                int contactId = rs.getInt("contact_id");

                String contactName = "";
                try (PreparedStatement ps = connection.prepareStatement("SELECT Contact_Name FROM contacts WHERE Contact_ID = ?")) {
                    ps.setInt(1, contactId);
                    ResultSet contactRs = ps.executeQuery();
                    if (contactRs.next()) {
                        contactName = contactRs.getString("Contact_Name");
                    }
                }
                /** Populates the appointment table */
                allAppointments.add(new Appointments(
                        rs.getInt("appointment_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getTimestamp("start").toLocalDateTime(),
                        rs.getTimestamp("end").toLocalDateTime(),
                        rs.getInt("customer_id"),
                        rs.getInt("user_id"), contactName
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadAppointments(){
        String url = "jdbc:mysql://localhost/client_schedule?connectionTimeZone=SERVER";
        String username = "sqlUser";
        String password = "passw0rd!";
    }

    /** Returns the selected customer */
    public static Customers selectedCustomerToModify() {
        return customerToModify;
    }

    /** Returns the selected appointment */
    public static Appointments selectedAppointmentToModify() {
        return appointmentToModify;
    }


    /** Method that displays the modify customer screen */
    public void displayModifyCustomer(ActionEvent event) throws IOException {

        /** Gets the selected customer */
        customerToModify = customerTable.getSelectionModel().getSelectedItem();

        if (customerToModify != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/UpdateCustomer.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Software II WGU");
            stage.show();
        } else {
            alertMessage(1);

        }
    }

    /** Alert message helper function */
    private void alertMessage(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch (alertType) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("No selection");
                alert.setContentText("A customer has not been selected");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("No selection");
                alert.setContentText("An appointment has not been selected");
                alert.showAndWait();
                break;
        }
    }

    /** Method that filters the appointments based on which radio button is selected */
    @FXML
    public void filterAppointments(ActionEvent event) {
        ObservableList<Appointments> filteredList = FXCollections.observableArrayList();
        LocalDate today = LocalDate.now();

        if (monthRadioButton.isSelected()) {
            for (Appointments appointment : allAppointments) {
                if (appointment.getStartDateTime().toLocalDate().getMonth() == today.getMonth() &&
                        appointment.getStartDateTime().getYear() == today.getYear()) {
                    filteredList.add(appointment);
                }
            }
        } else if (weekRadioButton.isSelected()) {
            LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
            LocalDate endOfWeek = startOfWeek.plusDays(6);

            for (Appointments appointment : allAppointments) {
                LocalDate appointmentDate = appointment.getStartDateTime().toLocalDate();
                if (!appointmentDate.isBefore(startOfWeek) && !appointmentDate.isAfter(endOfWeek)) {
                    filteredList.add(appointment);
                }
            }
        } else {
            filteredList.addAll(allAppointments); /** Show all */
        }

        appointmentTable.setItems(filteredList);
    }

    /** Method to delete customer from the database and refreshes the customer table */
    public void deleteCustomer(ActionEvent event) throws IOException, SQLException {

        /** Gets selected customer */
        Customers selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        /** Shows alert if selected customer is not null */
        if(selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Are you sure you want to delete?");
            Optional<ButtonType> result = alert.showAndWait();

            /** If customer is not null, delete it and the corresponding appointments from the database */
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int customerId = selectedCustomer.getCustomerId();

                String url = "jdbc:mysql://localhost/client_schedule?connectionTimeZone=SERVER";
                String username = "sqlUser";
                String password = "passw0rd!";

                /** Establishes a connection then runs the DELETE queries */
                try (Connection connection = DriverManager.getConnection(url, username, password)) {

                    String deleteAppointmentsQuery = "DELETE FROM appointments WHERE customer_id = ?";
                    try (PreparedStatement psAppointments = connection.prepareStatement(deleteAppointmentsQuery)) {
                        psAppointments.setInt(1, customerId);
                        psAppointments.executeUpdate();
                    }


                    String deleteCustomerQuery = "DELETE FROM customers WHERE customer_id = ?";
                    try (PreparedStatement psCustomer = connection.prepareStatement(deleteCustomerQuery)) {
                        psCustomer.setInt(1, customerId);
                        int rowsAffected = psCustomer.executeUpdate();

                        if (rowsAffected > 0) {
                            showAlert("Customer deleted successfully.");

                            customerTable.getItems().remove(selectedCustomer);
                        } else {
                            showAlert("Customer could not be deleted.");
                        }
                    }
                }

            }
        }
        else {
            alertMessage(1);

        }
    }

    /** Button that loads the report screen */
    public void reportsBtn(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Reports.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Software II WGU");
        stage.show();
    }

    /** Method to delete the selected appointment */
    public void deleteAppointment() throws SQLException {

        /** Gets selected appointment */
        Appointments selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();

        /** Shows alert if selected appointment is not null */
        if(selectedAppointment != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Are you sure you want to delete?");
            Optional<ButtonType> result = alert.showAndWait();

            /** If appointment is not null, delete it and the corresponding appointments from the database */
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int appointmentId = selectedAppointment.getAppointmentId();

                String url = "jdbc:mysql://localhost/client_schedule?connectionTimeZone=SERVER";
                String username = "sqlUser";
                String password = "passw0rd!";

                try (Connection connection = DriverManager.getConnection(url, username, password)) {

                    String deleteAppointmentsQuery = "DELETE FROM appointments WHERE appointment_id = ?";
                    try (PreparedStatement psAppointments = connection.prepareStatement(deleteAppointmentsQuery)) {
                        psAppointments.setInt(1, appointmentId);
                       int rowsAffected = psAppointments.executeUpdate();
                        if (rowsAffected > 0) {
                            showAlert("Appointment deleted successfully.");

                            appointmentTable.getItems().remove(selectedAppointment);
                        } else {
                            showAlert("Appointment could not be deleted.");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            alertMessage(2);
        }

    }

    /** Method that displays the modify appointment screen */
    public void displayModifyAppointment(ActionEvent event) throws IOException {
        appointmentToModify = appointmentTable.getSelectionModel().getSelectedItem();

        if (appointmentToModify != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/UpdateAppointment.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Software II WGU");
            stage.show();
        } else {
            alertMessage(2);

        }
    }

    /** Method that displays the add appointment screen */
    public void displayAddAppointment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/AddAppointment.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Software II WGU");
        stage.show();
    }

    /** Method that displays the add customer screen */
    public void displayAddCustomer(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/AddCustomer.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Software II WGU");
        stage.show();
    }

    /** Closes the application */
    public void exitBtn(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Closing...");
        alert.setContentText("Exit the application?");
        Optional<ButtonType> res = alert.showAndWait();

        if (res.isPresent() && res.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }


        private void showAlert(String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(message);
            alert.showAndWait();
        }

}
