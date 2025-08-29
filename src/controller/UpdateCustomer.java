package controller;

import helper.Utility;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller class for the update customer menu */
public class UpdateCustomer implements Initializable {

    /** */
    @FXML
    private TextField customerNameField;
    @FXML private TextField customerAddressField;
    @FXML private TextField customerPostalCodeField;
    @FXML private TextField customerPhoneField;

    /** */
    @FXML private ComboBox<String> countryComboBox;
    @FXML private ComboBox<String> divisionComboBox;

    /** Variables to map the id's to their respective names */
    private HashMap<String, Integer> countryMap = new HashMap<>();
    private HashMap<String, Integer> divisionMap = new HashMap<>();

    /** Init function for the update controller class
     * A lambda expression is used in the setOnAction method for the countryComboBox
     * because it makes the code easier to read */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String url = "jdbc:mysql://localhost/client_schedule?connectionTimeZone = SERVER";
        String username = "sqlUser";
        String password = "passw0rd!";

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM countries");
            ResultSet rs = stmt.executeQuery();

            ObservableList<String> countryNames = FXCollections.observableArrayList();

            while(rs.next()) {
                String country = rs.getString("Country");
                int countryID = rs.getInt("country_id");
                countryMap.put(country, countryID);
                countryNames.add(country);
            }

            /** Sets the list of countries in the combobox */
            countryComboBox.setItems(countryNames);


            Customers selectedCustomer = HomeController.selectedCustomerToModify();

            /** If the selected customer is not null, update the selected customer */
            if (selectedCustomer != null) {
                customerNameField.setText(selectedCustomer.getName());
                customerAddressField.setText(selectedCustomer.getAddress());
                customerPostalCodeField.setText(selectedCustomer.getPostalCode());
                customerPhoneField.setText(selectedCustomer.getPhone());


                int divisionId = selectedCustomer.getDivisionID();

                String divisionName = null;
                int countryId = -1;

                String getDivisionAndCountry = "SELECT d.Division, c.Country_ID, c.Country " +
                        "FROM first_level_divisions d JOIN countries c ON d.Country_ID = c.Country_ID " +
                        "WHERE d.Division_ID = ?";

                PreparedStatement ps = connection.prepareStatement(getDivisionAndCountry);
                ps.setInt(1, divisionId);
                ResultSet rsDivision = ps.executeQuery();

                if (rsDivision.next()) {
                    divisionName = rsDivision.getString("Division");
                    countryId = rsDivision.getInt("Country_ID");
                    String countryName = rsDivision.getString("Country");

                    countryComboBox.setValue(countryName); // Set country
                    loadDivisions(countryId, url, username, password); // Load divisions for country
                    divisionComboBox.setValue(divisionName); // Set division
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        /** A lambda expression is used here because it makes the code easier to read */
        countryComboBox.setOnAction(event -> {
            String selectedCountry = countryComboBox.getValue();
            if (selectedCountry != null && countryMap.containsKey(selectedCountry)) {
                int countryID = countryMap.get(selectedCountry);
                loadDivisions(countryID, url, username, password);
            }
        });
    }
    public void addButton(){}

    /** Updates the selected customer */
    public void saveButton(ActionEvent event) throws IOException{

        /** Gets the texts and values*/
        String name = customerNameField.getText();
        String address = customerAddressField.getText();
        String postalCode = customerPostalCodeField.getText();
        String phone = customerPhoneField.getText();
        String selectedDivision = divisionComboBox.getValue();

        /** Checks if any of the test fields or values are empty */
        if (name.isEmpty() || address.isEmpty() || postalCode.isEmpty() || phone.isEmpty() || selectedDivision == null) {
            showAlert("All fields must be filled out.");
            return;
        }

        int divisionId = divisionMap.get(selectedDivision);
        int customerId = HomeController.selectedCustomerToModify().getCustomerId();

        String url = "jdbc:mysql://localhost/client_schedule?connectionTimeZone = SERVER";
        String username = "sqlUser";
        String password = "passw0rd!";

        /** Updates the selected customer with new values */
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "UPDATE customers SET " +
                    "Customer_Name = ?, " +
                    "Address = ?, " +
                    "Postal_Code = ?, " +
                    "Phone = ?, " +
                    "Division_ID = ?, " +
                    "Last_Update = NOW(), " +
                    "Last_Updated_By = 'admin' " +
                    "WHERE Customer_ID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setInt(5, divisionId);
            ps.setInt(6, customerId);

            ps.executeUpdate();

            showAlert("Customer updated successfully!");


            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Software II WGU");
            stage.show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
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
    public void removeButton(){}
    public void searchButton(){}

    /** Load division helper method. This is to load the divisions for the combobox element */
    private void loadDivisions(int countryID, String url, String username, String password) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT division, division_ID FROM first_level_divisions WHERE Country_ID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, countryID);
            ResultSet rs = ps.executeQuery();

            ObservableList<String> divisionNames = FXCollections.observableArrayList();
            divisionMap.clear();

            while (rs.next()) {
                String divisionName = rs.getString("division");
                int divisionId = rs.getInt("division_ID");
                divisionNames.add(divisionName);
                divisionMap.put(divisionName, divisionId);
            }

            divisionComboBox.setItems(divisionNames);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Show alert helper function */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
