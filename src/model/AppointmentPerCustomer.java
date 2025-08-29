package model;

/** Appointments per customer class; this class is for one of the reports. My explaination for adding this is located
 * in the README file */
public class AppointmentPerCustomer {

    /** Appointment variables that are needed for the report */
    private final int customerId;
    private final String customerName;
    private final int totalAppointments;

    /** The constructor */
    public AppointmentPerCustomer(int customerId, String customerName, int totalAppointments) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.totalAppointments = totalAppointments;
    }

    /** Returns customerId */
    public int getCustomerId() {
        return customerId;
    }

    /** Returns the customers' name */
    public String getCustomerName() {
        return customerName;
    }

    /** Returns the total number of appointments */
    public int getTotalAppointments() {
        return totalAppointments;
    }
}
