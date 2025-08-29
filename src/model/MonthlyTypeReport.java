package model;


/** CLass for the monthly type report (a report that shows the appointments based on type) */
public class MonthlyTypeReport {
    private final String month;
    private final String type;
    private final int total;


    /** The constructor */
    public MonthlyTypeReport(String month, String type, int total) {
        this.month = month;
        this.type = type;
        this.total = total;
    }

    /** Returns the month that the appointment is in */
    public String getMonth() { return month; }

    /** Returns the type of appointment */
    public String getType() { return type; }

    /** Returns the total number of appointments*/
    public int getTotal() { return total; }
}
