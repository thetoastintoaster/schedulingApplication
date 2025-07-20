package model;

public class User {
    private int id;
    public String userName;
    public String password;

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