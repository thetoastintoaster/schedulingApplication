import controller.LoginPageController;
import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import model.UserDatabase;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void init() {
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/LoginPage.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Software II WGU");
        stage.show();
    }

    public static void main(String[] args) {

        launch();


        JDBC.openConnection();


        }

    @Override
    public void stop() {
        JDBC.closeConnection();
    }
    }

