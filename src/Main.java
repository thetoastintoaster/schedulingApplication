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

import static javafx.application.Application.launch;
//public class App {
//    public static void main(String[] args) {
//        Main.main(args);
//    }
//}

public class Main extends Application {
    @Override
    public void init() {
    }

    @Override
    public void start(Stage stage) throws IOException {
//        Locale currentLocale = Locale.getDefault();
        Locale frenchLocale = new Locale("fr");
        ResourceBundle messages = ResourceBundle.getBundle("messages", frenchLocale);

        String languageTest = messages.getString("testWord");
        System.out.println(languageTest);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/LoginPage.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Software II WGU");
        stage.show();

//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/LoginPage.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
//        stage.setTitle("Software I Project");
//        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch();


        JDBC.openConnection();
        LoginPageController.addTestUser();
//        LoginPageController.setLocation();
/*
        int rowsAffected = FruitsQuery.insert("Cherries", 1);

        if(rowsAffected > 0) {
            System.out.println("Insert Successful");
        } else {
            System.out.println("Insert Failed");
        }
*/

//        int rowsAffected = FruitsQuery.update(7, "Red Peppers");
//        int rowsAffected = FruitsQuery.delete(7);
        // FruitsQuery.select();
        // FruitsQuery.select(3);


        JDBC.closeConnection();
        }

    @Override
    public void stop() {
    }
    }

