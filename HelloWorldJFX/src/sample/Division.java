
package sample;
/**
 * there are 3 countries with seperate divisions-- states, regions and provinces. I created a class to streamline the
 * data from the sql server to populate the combo boxes with the correct information.
 */
public class Division {
    private int divisionID;
    private String division;
    private int countryID;

    public Division(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
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
}



