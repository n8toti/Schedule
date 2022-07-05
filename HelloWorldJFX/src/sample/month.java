
package sample;
/**
 * the month class was created to view the total of appointments by month and populate a GUI table.
 */
public class month {
    private int total;
    private String month;

    public month(int total, String month) {
        this.total = total;
        this.month = month;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
