package model;
import java.time.LocalDateTime;

/** Appointment class */
public class Appointments {

    /** All of the variables needed for the appointment class except for
     * created date, created by [insert user], update date, and updated by [insert user] */
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


    /** Appointments constructor class */
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

    /** Appointment getters */
    public int getAppointmentId() { return appointmentId; }

    /** Getter that returns the title */
    public String getTitle() { return title; }

    /** Getter that returns the description */
    public String getDescription() { return description; }

    /** Getter that returns the location */
    public String getLocation() { return location; }

    /** Getter that returns the appointment type */
    public String getType() { return type; }

    /** Getter for the foreign keys */
    public int getCustomerID() { return customerID; }


    /** Getter that returns the user ID */
    public int getUserID() { return userID; }

    /** Getter that returns the contact */
    public String getContact() { return contact; }

    /** Getter for the start date time */
    public LocalDateTime getStartDateTime() { return startDateTime; }

    /** Getter for the end date time */
    public LocalDateTime getEndDateTime() { return endDateTime; }

    /** Getter for the start time */
    public LocalDateTime getStartTime() { return startDateTime;}

    /** Getter for the end date time date */
    public LocalDateTime getEndTime() { return endDateTime; }
//    public String getContactName() { return contactName; }
}
