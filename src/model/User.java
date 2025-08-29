package model;

/** This class was made initially to login, but I didn't notice there was already usernames and passwords in the database
 * Leaving this here because I might want to extrapolate on this after the assignment is submitted
 */
public class User {
    private int id;
    public String userName;
    public String password;

    /** User constructor */
    public User(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getID(int id) {
        return id;
    }

    public String getUserName(String userName) {
        return userName;
    }

    public String getPassword(String password) {
        return password;
    }

}