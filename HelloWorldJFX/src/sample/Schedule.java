package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.swing.plaf.nimbus.State;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Schedule is the hub of all Lists and Methods used to make this program work.
 */
public class Schedule {
    /**
     * list of all customers
     */
    public static ObservableList<Customers> customersTable = FXCollections.observableArrayList();

    /**
     * list of all appointments
     */
    public static ObservableList<Appointments> appTable = FXCollections.observableArrayList();

    /**
     * list of appointments this week
     */
    public static ObservableList<Appointments> appWeekTable = FXCollections.observableArrayList();
    /**
     * list of appointments this monoth
     */
    public static ObservableList<Appointments> appMonthTable = FXCollections.observableArrayList();

    /**
     * divison list
     */
    public static ObservableList<Division> divisionData = FXCollections.observableArrayList();

    /**
     * adds to list of division data
     * @param newDivision
     */
    public static void addDivision(Division newDivision) {divisionData.add(newDivision);}

    /**
     * anikas list
     */
    public static ObservableList<Appointments> anikaTable = FXCollections.observableArrayList();

    /**
     * adds to Anikas list
     * @param app
     */
    public static void addAnika(Appointments app) {anikaTable.add(app);}

    /**
     * daniels list
     */
    public static ObservableList<Appointments> danielTable = FXCollections.observableArrayList();

    /**
     * add apointment to Daniels list
     * @param app
     */
    public static void addDaniel(Appointments app) {danielTable.add(app);}

    /**
     * Li contacts list
     */
    public static ObservableList<Appointments> liTable = FXCollections.observableArrayList();

    /**
     * add appointment to Li contact list
     * @param app
     */
    public static void addLi(Appointments app) {liTable.add(app);}

    /**
     * total table to be used to populate total GUI
     */
    public static ObservableList<total> totalTable = FXCollections.observableArrayList();

    /**
     * adds total to total table
     * @param app
     */
    public static void addTotal(total app) {totalTable.add(app);}

    /**
     * total month table
     */
    public static ObservableList<month> totalMonthTable = FXCollections.observableArrayList();

    /**
     * adds month to total month table
     * @param app
     */
    public static void addMonthTotal(month app) {totalMonthTable.add(app);}

    /**
     * adds customer to customer table
     * @param newCustomer
     */
    public static void addCustomer(Customers newCustomer) {
        customersTable.add(newCustomer);
    }

    /**
     * adds appointment to appTable
     * @param newAppointment
     */
    public static void addApp(Appointments newAppointment) {appTable.add(newAppointment);}

    /**
     * adds appointment to week table
     * @param newAppointment
     */
    public static void addWeekApp(Appointments newAppointment) {appWeekTable.add(newAppointment);}

    /**
     * adds appointment to month table
     * @param newAppointment
     */
    public static void addMonthApp(Appointments newAppointment) {appMonthTable.add(newAppointment);}

    /**
     * pulls data from the Data base
     */
    public static void addDB() {
        int cID = 0;
        String cName = "";
        String address = "";
        String zip = "";
        String phone = "";
        int dID = 0;
        String division = "";
        int countryID = 0;

        try {
            String sql = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, C.Division_ID, d.Division, " +
                    "d.Country_ID FROM customers C JOIN first_level_divisions d ON C.Division_ID = d.Division_ID ";
            Connection con = JDBC.getConnection();
            Statement state = con.createStatement();
            ResultSet result = state.executeQuery(sql);
            int count = 0;
            while (result.next())
            {
                cID = result.getInt("Customer_ID");
                cName = result.getString("Customer_Name");
                address = result.getString("Address");
                zip = result.getString("Postal_Code");
                phone = result.getString("Phone");
                dID = result.getInt("Division_ID");
                division = result.getString("d.Division");
                countryID = result.getInt("d.Country_ID");

                Customers add = new Customers(cID,cName,address,zip,phone,dID,division,countryID);
                addCustomer(add);

            }


        }
        catch(SQLException ex)
        {ex.printStackTrace();}

    }

    /**
     * pulls appointmetns from the database
     */
    public static void addAppData()
    {
        int id;
        String title;
        String description;
        String location;
        String contact;
        String type;
        Timestamp start;
        Timestamp end;
        int customerID;
        int userID;

        try {
            String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, " +
                            "c.Contact_Name FROM appointments a JOIN contacts c ON a.Contact_ID = c.Contact_ID";
            Connection con = JDBC.getConnection();
            Statement state = con.createStatement();
            ResultSet result = state.executeQuery(sql);
            int count = 0;
            while (result.next()) {
                id = result.getInt("Appointment_ID");
                title = result.getString("Title");
                description = result.getString("Description");
                location = result.getString("Location");
                contact = result.getString("c.Contact_Name");
                type = result.getString("Type");
                start = result.getTimestamp("Start");
                end = result.getTimestamp("End");
                customerID = result.getInt("Customer_ID");
                userID = result.getInt("User_ID");
                //set time from db to locale time
                start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                Appointments add = new Appointments(id, title, description, location, contact, type, start, end,
                                                        customerID, userID);
                addApp(add);

            }
        }
        catch(SQLException ex)
        {ex.printStackTrace();}
    }

    /**
     * pulls first level data from the database
     */
    public static void firstLevelData()
    {
        int dID = 0;
        String division = "";
        int countryID = 0;

        try {
            String sql = "SELECT Division_ID, Division, Country_ID FROM first_level_divisions";
            Connection con = JDBC.getConnection();
            Statement state = con.createStatement();
            ResultSet result = state.executeQuery(sql);
            int count = 0;
            while (result.next()) {
                dID = result.getInt("Division_ID");
                division = result.getString("Division");
                countryID = result.getInt("Country_ID");

                Division add = new Division(dID, division, countryID);
                addDivision(add);


            }
        }
        catch(SQLException ex)
        {ex.printStackTrace();}
    }

    /**
     * deletes customer
     * @param selectedCustomer
     * @return
     */
    public static boolean deleteCustomer(Customers selectedCustomer)
    {
        return customersTable.remove(selectedCustomer);
    }

    /**
     * deletes appointment
      * @param selectedApp
     * @return
     */
    public static boolean deleteApp(Appointments selectedApp)
    {
        return appTable.remove(selectedApp);
    }

    /**
     * updates customer
     * @param index
     * @param selectedCustomer
     */
    public static void updateCustomer(int index, Customers selectedCustomer) {
        customersTable.set(index, selectedCustomer);
    }

    /**
     * updates appointment
     * @param index
     * @param selectedApp
     */
    public static void updateAppointment(int index, Appointments selectedApp)
    {
        appTable.set(index, selectedApp);
    }

    /**
     * checks if any appointments from the same customer overlap
     * @param newApp
     * @return
     */
    public static Boolean checkOverlappingApp(Appointments newApp)
    {
        Boolean check = false;
        Calendar c = Calendar.getInstance();
        int sYear = newApp.getStart().getYear();
        int smonth = newApp.getStart().getMonth();
        int sday = newApp.getStart().getDay();
        int sHour = newApp.getStart().getHours();
        int sMin = newApp.getStart().getMinutes();

        int eYear = newApp.getEnd().getYear();
        int emonth = newApp.getEnd().getMonth();
        int eday = newApp.getEnd().getDay();
        int eHour = newApp.getEnd().getHours();
        int eMin = newApp.getEnd().getMinutes();

        int id = newApp.getCustomerID();


        for (Appointments e : appTable)
        {
            int Year = e.getStart().getYear();
            int month = e.getStart().getMonth();
            int day = e.getStart().getDay();
            int Hour = e.getStart().getHours();
            int Min = e.getStart().getMinutes();

            int endYear = e.getEnd().getYear();
            int endmonth = e.getEnd().getMonth();
            int endday = e.getEnd().getDay();
            int endHour = e.getEnd().getHours();
            int endMin = e.getEnd().getMinutes();

            if (id == e.getCustomerID() && newApp.getId() != e.getId())
            {
                System.out.println(id +" "+ e.getCustomerID());
                if (sYear == Year && eYear == endYear && smonth == month && emonth == endmonth && sday == day && eday == endday)
                {
                    System.out.println(sYear +" "+ Year +" "+ eYear +" "+ endYear +" "+  smonth +" "+ month +" "+ emonth +" "+endmonth +" "+ sday +" "+ day +" "+ eday +" "+endday );
                    if (sHour == Hour || sHour == endHour || eHour == Hour) {
                        if ((sMin >= Min && sMin <= endMin) || eMin >= Min && eMin <= endMin) {
                            check = true;
                        }
                    }
                }
            }

        }
        return check;
    }

    /**
     * check if theres an appointment within 15mins of logging in
     */
    public static void checkNow()
    {
        Boolean check = false;
        Calendar cal = Calendar.getInstance();
        cal.getTimeInMillis();
        System.out.println(cal.get(Calendar.YEAR)+ " " + cal.get(Calendar.MONTH) + " " + cal.get(Calendar.DATE) + " " + cal.get(Calendar.HOUR) + " " + cal.get(Calendar.MINUTE));

        int curHoursAndMin = (cal.get(Calendar.HOUR) * 60) + cal.get(Calendar.MINUTE);

        for (Appointments e : appTable)
        {
            Calendar cal2 = Calendar.getInstance();
            Timestamp start = e.getStart();
            start.toInstant().atZone(ZoneId.systemDefault());
            cal2.setTime(start);

            int appHourAndMin = (cal2.get(Calendar.HOUR) * 60) + cal2.get(Calendar.MINUTE);
            if (cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal.get(Calendar.DATE) == cal2.get(Calendar.DATE))
            {
                if (appHourAndMin <= (curHoursAndMin + 15) && appHourAndMin >= curHoursAndMin)
                {
                    check = true;
                    int min2 = cal2.get(Calendar.MINUTE);
                    String min = String.format("%02d",min2);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Upcoming Appointment");
                    alert.setHeaderText("Upcoming Appointment");
                    alert.setContentText("Appointmetn ID:" + e.getId() + "\n" + "Date: " + cal2.get(Calendar.YEAR) + "-"
                            + cal2.get(Calendar.MONTH) + "-" + cal2.get(Calendar.DATE) + "\n" + "Time: "
                                + cal2.get(Calendar.HOUR) + ":"+ min);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }
            }
            System.out.println(cal2.get(Calendar.YEAR) + " " + cal2.get(Calendar.MONTH) + " " + cal2.get(Calendar.DATE) + " " + cal2.get(Calendar.HOUR) + " " + (cal2.get(Calendar.MINUTE) + 15));

        }
        if (!check)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Schedule");
            alert.setHeaderText("Upcoming Appointment");
            alert.setContentText("There are no upcoming appointments in the next 15minutes");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {}
        }
        //return check;
    }

    /**
     * gets the earliest appointment
     */
    public static void nextApp()
    {
        Appointments app = null;

        for (Appointments e : appTable)
        {
            if (app == null)
            {
                app = e;
            }
            else
            {
                if (e.getStart().before(app.getStart()))
                {
                    app = e;
                }
            }
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Earliest Appointmet");
        alert.setHeaderText("Earliest Appointment");
        alert.setContentText("Appointment ID: " + app.getId() + "\n" + "Title: " + app.getTitle() + "\n" +
                "Description: " + app.getDescription() + "\n" + "Location: " + app.getLocation() + "\n" + "Contact: "
                + app.getContact() + "\n" + "Type: " + app.getType() + "\n" + "Start: " + app.getStart() + "\n" +
                "End: " +app.getEnd() + "\n" + "Customer ID: " + app.getCustomerID() + "\n" + "User ID: " + app.getUserID());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {}
    }

    /**
     * sets data to a list to be used to set GUI table
     */
    public static void setAppMonthTable()
    {
        Calendar cal = Calendar.getInstance();
        cal.getTimeInMillis();
        appMonthTable.clear();
        if (appMonthTable.isEmpty()) {
            for (Appointments e : appTable) {
                Calendar cal2 = Calendar.getInstance();
                Timestamp start = e.getStart();
                start.toInstant().atZone(ZoneId.systemDefault());
                cal2.setTime(start);
                //System.out.print(cal2.get(Calendar.YEAR) + " " + cal.get(Calendar.YEAR) +" " + cal.get(Calendar.MONTH) + " " + cal.get(Calendar.MONTH));
                if (cal2.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && cal2.get(Calendar.MONTH) == cal.get(Calendar.MONTH)) {
                    System.out.println(cal2.get(Calendar.WEEK_OF_MONTH) + " " + cal.get(Calendar.WEEK_OF_MONTH));
                    addMonthApp(e);
                }
            }
        }

    }

    /**
     * sets week table data to a FX list to be used to populate data into GUI table
     */
    public static void setAppWeekTable()
    {
        Calendar cal = Calendar.getInstance();
        cal.getTimeInMillis();

        appWeekTable.clear();
        if(appWeekTable.isEmpty()) {
            for (Appointments e : appTable) {
                Calendar cal2 = Calendar.getInstance();
                Timestamp start = e.getStart();
                start.toInstant().atZone(ZoneId.systemDefault());
                cal2.setTime(start);
                //System.out.print(cal2.get(Calendar.YEAR) + " " + cal.get(Calendar.YEAR) +" " + cal.get(Calendar.MONTH) + " " + cal.get(Calendar.MONTH));
                if (cal2.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && cal2.get(Calendar.MONTH) == cal.get(Calendar.MONTH) && cal.get(Calendar.WEEK_OF_MONTH) == cal2.get(Calendar.WEEK_OF_MONTH)) {
                    System.out.println(cal2.get(Calendar.WEEK_OF_MONTH) + " " + cal.get(Calendar.WEEK_OF_MONTH));
                    addWeekApp(e);
                }
            }
        }

    }

    /**
     * gets data for contacts to display in GUI
     */
    public static void contactSchedule()
    {
        String anika = "Anika Costa";
        String daniel = "Daniel Garcia";
        String li = "Li Lee";
        anikaTable.clear();
        danielTable.clear();
        liTable.clear();

        for (Appointments e : appTable)
        {
            System.out.println(e.getContact());
            if (e.getContact().equals(anika))
            {
                addAnika(e);
            }
            else if (e.getContact().equals(daniel))
            {
                addDaniel(e);
            }
            else if (e. getContact().equals(li))
            {
                addLi(e);
            }
        }

    }

    /**
     *
     * get Distinct types from app table using stream distinct and collecting with. Lambdas are used to save tiem
     *  and code by not having to write methods to do the same task.
     *
     */
    public static void typeTotal(){
        List<String> dList = appTable.stream()
                .map(ct->ct.getType())
                .distinct()
                .collect(Collectors.toList());
        /*
        clear table compare to app table and count
         */
        if(!totalTable.isEmpty())
        {
            totalTable.clear();
        }
        for (String e : dList)
        {
            int count = 0;
            System.out.println(e);
            for (Appointments i : appTable)
            {
                if (i.getType().equals(e))
                {
                    ++count;
                }
            }
            total add = new total(count, e);
            addTotal(add);
        }
    }

    /**
     * streaming the data from appTable I use map with a lamdas to get the month and find the distinct months. Lamdas
     * are used in place of methods to help speed up the process and write less code that is easier to read.
     */
    public static void monthTotal(){

        List<Integer> dList = appTable.stream()
                .map(ct->ct.getStart().getMonth())
                .distinct()
                .collect(Collectors.toList());
        Calendar cal = Calendar.getInstance();
        if(!totalMonthTable.isEmpty()) {
            totalMonthTable.clear();
        }
        for (int i : dList) {
            String month = "";
            int count = 0;
            for (Appointments e : appTable)
            {
                Timestamp start = e.getStart();
                start.toInstant().atZone(ZoneId.systemDefault());
                cal.setTime(start);
                if (i == cal.get(Calendar.MONTH))
                {
                    ++count;
                    switch (i)
                    {
                        case 0:
                            month = "January";
                            break;
                        case 1:
                            month = "February";
                            break;
                        case 2:
                            month = "March";
                            break;
                        case 3:
                            month = "April";
                            break;
                        case 4:
                            month = "May";
                            break;
                        case 5:
                            month = "June";
                            break;
                        case 6:
                            month = "July";
                            break;
                        case 7:
                            month = "August";
                            break;
                        case 8:
                            month = "September";
                            break;
                        case 9:
                            month = "October";
                            break;
                        case 10:
                            month = "November";
                            break;
                        case 11:
                            month = "December";
                            break;

                    }
                }
            }
            month add = new month(count, month);
            addMonthTotal(add);
        }

    }

    /**
     * gets attempted and successful logins and saves to txt
     * @param usr
     * @param pw
     * @param loggedin
     */
    public static void logging(String usr, String pw, boolean loggedin){
        String time = Instant.now().toString();

        try(FileWriter fw = new FileWriter("login_activity.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw))
        {
            if (loggedin)
            {
                out.println(usr + " " + pw + " Logged In " + time);
            }
            else{
                out.println(usr + " " + pw + " Failed login attempt " + time);
            }
        }
        catch(IOException ex)
        {

        }
    }

}
