package model;

/** Class for the contact schedule*/
public class ContactSchedule {

    private final int appointmentId;
    private final String title, type, description, start, end, contact;
    private final int customerId;

    /** ContactSchedule constructor */
    public ContactSchedule(int appointmentId, String title, String type, String description,
                           String start, String end, int customerId, String contact) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.type = type;
        this.description = description;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.contact = contact;
    }

    /** ContactSchedule getters */
    public int getAppointmentId() { return appointmentId; }
    /** Getter that returns the title of the appointment */
    public String getTitle() { return title; }

    /** Getter that returns the type of appointment */
    public String getType() { return type; }

    /** Getter that returns the description of the appointment */
    public String getDescription() { return description; }

    /** Getter that returns the start time of the appointment */
    public String getStart() { return start; }

    /** Getter that returns the end time of the appointment */
    public String getEnd() { return end; }

    /** Getter that returns the customerId;  */
    public int getCustomerId() { return customerId; }

    /** Getter that returns the title of the appointment */
    public String getContact() { return contact; }
}
