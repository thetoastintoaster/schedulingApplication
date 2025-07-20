package controller;

import helper.JDBC;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customers;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
//    private static Part customerToModify;

//    private static Product appointmentToModify;


    // The search field for the customers
    @FXML private TextField customerSearchTxt;

    // The table for the customers
    @FXML private TableView<Customers> customerTable;

    @FXML private TableColumn<Customers, Integer> customerIDCol;
    @FXML private TableColumn<Customers, String> customerNameCol;
    @FXML private TableColumn<Customers, String> customerAddressCol;
    @FXML private TableColumn<Customers, String> customerPostalCodeCol;
    @FXML private TableColumn<Customers, String> customerPhoneCol;

    private ObservableList<Customers> allCustomers = FXCollections.observableArrayList();

    public void initialize(URL location, ResourceBundle resources) {

//        TableView<Customers> customerTable = new TableView<>();

//        TableColumn<Customers, Integer> customerIDCol = new TableColumn<>("Customer ID");
//        TableColumn<Customers, String> customerNameCol = new TableColumn<>("Name");
//        TableColumn<Customers, String> customerAddressCol = new TableColumn<>("Address");
//        TableColumn<Customers, String> customerPostalCodeCol = new TableColumn<>("Postal Code");
//        TableColumn<Customers, String> customerPhoneCol = new TableColumn<>("Phone Number");
        //Populate customers table view
        customerTable.setItems(allCustomers);
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        //Populate appointments table view
//        appointmentTable.setItems();
//        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>(""));
//        appointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>(""));
//        appointmentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>(""));
//        appointmentLocationCol.setCellValueFactory(new PropertyValueFactory<>(""));
//        appointmentContactCol.setCellValueFactory(new PropertyValueFactory<>(""));
//        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>(""));
//        appointmentStartCol.setCellValueFactory(new PropertyValueFactory<>(""));
//        appointmentEndCol.setCellValueFactory(new PropertyValueFactory<>(""));
//        appointmentCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>(""));
//        appointmentUserIDCol.setCellValueFactory(new PropertyValueFactory<>(""));

        String url = "jdbc:mysql://localhost/client_schedule?connectionTimeZone = SERVER";
        String username = "sqlUser";
        String password = "passw0rd!";

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customers");

            while(rs.next()) {
                allCustomers.add(new Customers(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("address"),
                        rs.getString("postal_code"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void appointmentSearchText() {

    }

    public void deleteAppointment(){

    }

    public void displayModifyAppointment(){

    }

    public void displayAddAppointment(){

    }

    public void appointmentSearchButton(){

    }

    public void customerSearch(){

    }

    public void deleteCustomer(){

    }

    public void displayModifyCustomer(){

    }

    public void displayAddCustomer(){

    }

    public void customerSearchBtn(){

    }

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



}
