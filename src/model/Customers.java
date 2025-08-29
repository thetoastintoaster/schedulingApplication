package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/** Class for the customers*/
public class Customers {

    /** Variables for the customer excluding */
    /** created date, created by [insert user], update date, and updated by [insert user] */
    private int customerId;
    private final String name;
    private final String address;
    private final String postalCode;
    private final String phone;
    private final int divisionID;

    /** The constructor */
    public Customers(int customerId, String name, String address, String postalCode, String phone, int divisionID) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionID = divisionID;
    }

    /** Returns the customerId */
    public int getCustomerId(){
        return customerId;
    }

    /** Returns the customers' name */
    public String getName(){
        return name;
    }
    /** Returns the customers' address */
    public String getAddress(){
        return address;
    }
    /** Returns the postal code */
    public String getPostalCode(){
        return postalCode;
    }
    /** Returns the customers phone number */
    public String getPhone(){
        return phone;
    }

    /** Returns the divisionID (country or division) */
    public int getDivisionID(){
        return divisionID;
    }

}
