package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customers;
import helper.JDBC;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {
    @FXML private TextField customerNameField;
    @FXML private TextField customerAddressField;
    @FXML private TextField customerPostalCodeField;
    @FXML private TextField customerPhoneField;

    @FXML private ComboBox<String> countryComboBox;
    @FXML private ComboBox<String> divisionComboBox;


    private HashMap<String, Integer> countryMap = new HashMap<>();
    private HashMap<String, Integer> divisionMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String url = "jdbc:mysql://localhost/client_schedule?connectionTimeZone = SERVER";
        String username = "sqlUser";
        String password = "passw0rd!";

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM countries");
            ResultSet rs = stmt.executeQuery();

            // List of the country names
            ObservableList<String> countryNames = FXCollections.observableArrayList();

            // Maps the country ids to the country names and then add all of it to the countryNames list
            while(rs.next()) {
             String country = rs.getString("Country");
             int countryID = rs.getInt("country_id");
             countryMap.put(country, countryID);
             countryNames.add(country);
            }

            // Puts all of the country names in the combo box
            countryComboBox.setItems(countryNames);



        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Once a country is selected, load all the related divisions/states to the divisions combo box
        countryComboBox.setOnAction(event -> {
            String selectedCountry = countryComboBox.getValue();
            if (selectedCountry != null && countryMap.containsKey(selectedCountry)) {
                int countryID = countryMap.get(selectedCountry);
                loadDivisions(countryID, url, username, password);
            }
    });
}
    // This method loads the divisions/states for the selected country
    public void loadDivisions(int countryID, String url, String username, String password){
        divisionMap.clear();

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement ps = connection.prepareStatement("SELECT division, division_id FROM first_level_divisions WHERE country_id = ?");
            ps.setInt(1, countryID);
            ResultSet rs = ps.executeQuery();

            // List of the divisions/states
            ObservableList<String> divisions = FXCollections.observableArrayList();

            // Maps the division IDs to the division names
            while (rs.next()) {
                String division = rs.getString("division");
                int divisionId = rs.getInt("division_id");
//                divisions.add(rs.getString(division));
                divisionMap.put(division, divisionId);
                divisions.add(division);
            }

            // Puts all the division names of the selected country in the division combo box
            divisionComboBox.setItems(divisions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addButton(){

    }

    // Returns to main menu without saving anything
    public void cancelButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Home.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Software II WGU");
        stage.show();
    }

    // Adds a new customer to the database
    @FXML
    private void saveButton(ActionEvent event) {

        // Gets the text from the text fields
        String name = customerNameField.getText();
        String address = customerAddressField.getText();
        String postalCode = customerPostalCodeField.getText();
        String phone = customerPhoneField.getText();
        String selectedDivision = divisionComboBox.getValue();

        // Checks if the fields are empty
        if (name.isEmpty() || address.isEmpty() || postalCode.isEmpty() || phone.isEmpty() || selectedDivision == null) {
            showAlert("All fields must be filled out.");
            return;
        }

        // Gets the selected division from the combo box but only the ID, not the actual name
        // (because the foreign key and primary keys are integers)
        int divisionId = divisionMap.get(selectedDivision);

        String url = "jdbc:mysql://localhost/client_schedule?connectionTimeZone = SERVER";
        String username = "sqlUser";
        String password = "passw0rd!";

        // Attempts to execute the insert query
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO customers (customer_name, address, postal_code, phone, division_id, create_date, created_by, last_update, last_updated_by) " +
                    "VALUES (?, ?, ?, ?, ?, NOW(), 'admin', NOW(), 'admin')";
            PreparedStatement ps = connection.prepareStatement(sql);

            // Sets the values
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setInt(5, divisionId);

            ps.executeUpdate();

            showAlert("Customer added successfully!");

            // optionally redirect to main screen
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeButton(){

    }
    public void appointmentSearchButton(){

    }
    public void appointmentSearch(){

    }

    // An alert helper function that receives string input
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}