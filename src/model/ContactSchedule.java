package model;

public class ContactSchedule {

    private final int appointmentId;
    private final String title, type, description, start, end, contact;
    private final int customerId;

    // Contact schedule constructor
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

    // ContactSchedule getters
    public int getAppointmentId() { return appointmentId; }
    public String getTitle() { return title; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getStart() { return start; }
    public String getEnd() { return end; }
    public int getCustomerId() { return customerId; }
    public String getContact() { return contact; }
}
