package model;

/*
This class was made initially to login, but I didn't notice there was already usernames and passwords in the database
Leaving this here because I might want to extrapolate on this after the assignment is submitted
 */

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
