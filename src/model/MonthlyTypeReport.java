package model;

public class MonthlyTypeReport {
    private final String month;
    private final String type;
    private final int total;

    public MonthlyTypeReport(String month, String type, int total) {
        this.month = month;
        this.type = type;
        this.total = total;
    }

    public String getMonth() { return month; }
    public String getType() { return type; }
    public int getTotal() { return total; }
}
