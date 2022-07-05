
package sample;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
/**
 * Appointments is a class that is usedd to create new appointments. It populates the list that is used for the table
 * views in the GUI
 */
public class Appointments {
    private int id;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private int customerID;
    private int userID;

    /**
     * constructor for appointments
     * @param id
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param userID
     */
    public Appointments(int id, String title, String description, String location, String contact, String type,
                        Timestamp start, Timestamp end, int customerID, int userID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
    }

    /**
     * gets id
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * sets ID
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * gets title
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * gets description
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get location
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     * set location
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }
    /**
     * get contact
     * @return
     */
    public String getContact() {
        return contact;
    }

    /**
     * set contact
     * @param contact
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * get type
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * set type
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * get start
     * @return
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * set start
     * @param start
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     * get end
     * @return
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * set end
     * @param end
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * get customer id
     * @return
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * set customer id
     * @param customerID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * get user id
     * @return
     */
    public int getUserID() {
        return userID;
    }

    /**
     * set user id
     * @param userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
}


