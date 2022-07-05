
package sample;

/**
 * the Customers class is used to create new customers to add to the list in the Schedule class which is then used
 * to populate the customers table
 */
public class Customers {


    private int customerID;
    private String customerName;
    private String address;
    private String postal;
    private String phone;
    //private DateTimeFormatter dateTime;
    //private String createdBy;
    //private DateTimeFormatter lastUpdate;
    //private String lastUpdateBy;
    private int divisionID;
    private String division;
    private int countryID;

    public Customers(int customerID, String customerName, String address, String postal, String phone,
                     int divisionID, String division, int countryID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postal = postal;
        this.phone = phone;

        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }


    public static Boolean checkAssociatedApp(Customers cus)
    {

        for (Appointments a : Schedule.appTable)
        {
            if (cus.getCustomerID() == a.getCustomerID())
            {
                System.out.println(a.getCustomerID());
                return true;
            }
        }
        return false;

    }
}

