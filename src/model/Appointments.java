package model;
import java.time.LocalDateTime;

public class Appointments {

    // All of the variables needed for the appointment class except for
    // created date, created by [insert user], update date, and updated by [insert user]
    private int appointmentId;
    private final String title;
    private final String description;
    private final String location;
    private final String type;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final int customerID;
    private final int userID;
    private final String contact;
//    private final String contactName;


    // Appointments constructor class
    public Appointments(int appointmentId, String title, String description, String location, String type,
                        LocalDateTime startDateTime, LocalDateTime endDateTime,
                        int customerID, int userID, String contact) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.customerID = customerID;
        this.userID = userID;
        this.contact = contact;
//        this.contactName = contactName;
    }

    // Appointment getters
    public int getAppointmentId() { return appointmentId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getType() { return type; }

    // Getter for the foreign keys
    public int getCustomerID() { return customerID; }
    public int getUserID() { return userID; }
    public String getContact() { return contact; }

    // Getter for the start date/end date and start/end times
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public LocalDateTime getEndDateTime() { return endDateTime; }
    public LocalDateTime getStartTime() { return startDateTime;}
    public LocalDateTime getEndTime() { return endDateTime; }
//    public String getContactName() { return contactName; }
}
