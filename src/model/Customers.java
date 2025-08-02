package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customers {
    private int customerId;
    private final String name;
    private final String address;
    private final String postalCode;
    private final String phone;
    private final int divisionID;

    public Customers(int customerId, String name, String address, String postalCode, String phone, int divisionID) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionID = divisionID;
    }

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
