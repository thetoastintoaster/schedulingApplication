package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserDatabase {
    private static int userID = 0;
    private static ObservableList<User> allUsers = FXCollections.observableArrayList();

    public static void addUser(User newUser) {allUsers.add(newUser);}

    public static int incrementUserID() {
        return ++userID;
    }

}
