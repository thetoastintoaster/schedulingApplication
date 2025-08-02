package model;

public class AppointmentPerCustomer {
    private final int customerId;
    private final String customerName;
    private final int totalAppointments;

    public AppointmentPerCustomer(int customerId, String customerName, int totalAppointments) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.totalAppointments = totalAppointments;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }
}
