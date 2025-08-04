package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customers {

    // Variables for the customer excluding
    // created date, created by [insert user], update date, and updated by [insert user]
    private int customerId;
    private final String name;
    private final String address;
    private final String postalCode;
    private final String phone;
    private final int divisionID;

    // The constructor
    public Customers(int customerId, String name, String address, String postalCode, String phone, int divisionID) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionID = divisionID;
    }

    // Customer getters
    public int getCustomerId(){
        return customerId;
    }
    public String getName(){
        return name;
    }
    public String getAddress(){
        return address;
    }
    public String getPostalCode(){
        return postalCode;
    }
    public String getPhone(){
        return phone;
    }

    public int getDivisionID(){
        return divisionID;
    }

}
