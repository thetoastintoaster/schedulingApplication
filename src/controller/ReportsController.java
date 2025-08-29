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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/** Controller for the reports menu */
public class ReportsController implements Initializable {

//    @FXML private TableView<Customers> customerTable;
//    @FXML private TableView<Appointments> appointmentTable;

    /** The report based on the month and type */
    @FXML private TableView<MonthlyTypeReport> monthlyTypeReportTable;
    @FXML private TableColumn<MonthlyTypeReport, String> monthCol;
    @FXML private TableColumn<MonthlyTypeReport, String> typeCol;
    @FXML private TableColumn<MonthlyTypeReport, Integer> totalCol;

    /** The report based on the customers' contact */
    @FXML private TableView<ContactSchedule> contactScheduleTable;
    @FXML private TableColumn<ContactSchedule, Integer> apptIdCol;
    @FXML private TableColumn<ContactSchedule, String> apptTitleCol;
    @FXML private TableColumn<ContactSchedule, String> apptTypeCol;
    @FXML private TableColumn<ContactSchedule, String> apptDescCol;
    @FXML private TableColumn<ContactSchedule, String> apptStartCol;
    @FXML private TableColumn<ContactSchedule, String> apptEndCol;
    @FXML private TableColumn<ContactSchedule, Integer> apptCustIdCol;
    @FXML private TableColumn<ContactSchedule, String> apptContactCol;

    /** The table that shows the amount of appointments per customer */
    @FXML private TableView<AppointmentPerCustomer> appointmentsPerCustomerTable;
    @FXML private TableColumn<AppointmentPerCustomer, Integer> custIdCol;
    @FXML private TableColumn<AppointmentPerCustomer, String> custNameCol;
    @FXML private TableColumn<AppointmentPerCustomer, Integer> apptCountCol;

    /** Function used to set up the tables before they fully load */
    public void initialize(URL location, ResourceBundle resources) {
        /** Monthly report */
        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

        /** Contact schedule report */
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        apptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        apptCustIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        apptContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));

        try {
            populateReports();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    /** Method that populates the tables with their respective information (Report based on
     * month and type, contact, and appointments per customer) */
    public void populateReports() throws SQLException {
        ObservableList<MonthlyTypeReport> monthlyReport = FXCollections.observableArrayList();
        ObservableList<ContactSchedule> contactReport = FXCollections.observableArrayList();
        ObservableList<AppointmentPerCustomer> customerAppointmentReport = FXCollections.observableArrayList();

        String url = "jdbc:mysql://localhost/client_schedule?connectionTimeZone=SERVER";
        String username = "sqlUser";
        String password = "passw0rd!";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {

            // Appointments by Month and Type
            String query1 = "SELECT MONTHNAME(start) AS month, type, COUNT(*) AS total " +
                    "FROM appointments GROUP BY MONTHNAME(start), type";
            try (PreparedStatement ps = conn.prepareStatement(query1);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    monthlyReport.add(new MonthlyTypeReport(
                            rs.getString("month"),
                            rs.getString("type"),
                            rs.getInt("total")
                    ));
                }
            }

            /** Contact schedule */
            String query2 = "SELECT a.appointment_id, a.title, a.type, a.description, a.start, a.end, " +
                    "a.customer_id, c.contact_name FROM appointments a " +
                    "JOIN contacts c ON a.contact_id = c.contact_id";
            try (PreparedStatement ps = conn.prepareStatement(query2);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    contactReport.add(new ContactSchedule(
                            rs.getInt("appointment_id"),
                            rs.getString("title"),
                            rs.getString("type"),
                            rs.getString("description"),
                            rs.getTimestamp("start").toLocalDateTime().toString(),
                            rs.getTimestamp("end").toLocalDateTime().toString(),
                            rs.getInt("customer_id"),
                            rs.getString("contact_name")
                    ));
                }
            }

            /** Appointments per customer */
            String query3 = """
            SELECT c.Customer_ID, c.Customer_Name, COUNT(a.Appointment_ID) AS Total_Appointments
            FROM customers c
            LEFT JOIN appointments a ON c.Customer_ID = a.Customer_ID
            GROUP BY c.Customer_ID, c.Customer_Name
            """;

            try (PreparedStatement ps = conn.prepareStatement(query3);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("Customer_ID");
                    String name = rs.getString("Customer_Name");
                    int total = rs.getInt("Total_Appointments");

                    customerAppointmentReport.add(new AppointmentPerCustomer(id, name, total));
                }

                custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
                custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
                apptCountCol.setCellValueFactory(new PropertyValueFactory<>("totalAppointments"));


            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


        /** Sets the information to their respective tables */
        monthlyTypeReportTable.setItems(monthlyReport);
        contactScheduleTable.setItems(contactReport);
        appointmentsPerCustomerTable.setItems(customerAppointmentReport);
    }



    /** Returns to the previous menu */
    public void returnBtn(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }

}

