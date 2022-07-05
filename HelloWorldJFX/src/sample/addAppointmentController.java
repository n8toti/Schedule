
package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
/**
 * addAppointment Controller creates new appointments and appends them to the appointment table in the GUI. It verifies
 * that the appointment time is within business hours of EST and displays the time in the users time zone.
 */
public class addAppointmentController {

    Appointments addApp = new Appointments(0,"","","",
            "", "",null,null,0,0);
    //app id if appointment table is empty
    int aID = 1;


    public void initialize() {
        //if appointment table not empty get last index id and add 1
        int aLen = Schedule.appTable.size();
        if(!Schedule.appTable.isEmpty()) {
            aID = Schedule.appTable.get(aLen - 1).getId() + 1;
        }
        appIDText.setText(Integer.toString(aID));
        contactCombo.getItems().add("Anika Costa");
        contactCombo.getItems().add("Daniel Garcia");
        contactCombo.getItems().add("Li Lee");
    }

    @FXML
    private Button addButton;

    @FXML
    private Label appID;

    @FXML
    private TextField appIDText;

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<String> contactCombo;

    @FXML
    private TextField customerIDText;

    @FXML
    private TextField descriptionText;

    @FXML
    private TextField endText;

    @FXML
    private TextField locationText;

    @FXML
    private TextField startText;

    @FXML
    private TextField titleText;

    @FXML
    private TextField typeText;

    @FXML
    private TextField userIDText;

    /**
     * when clicked data is checked before adding the appointment. If all is well the data is added and appears on the
     * appointment table.
     * @param event
     * @throws IOException
     */
    @FXML
    public void addOnClick(MouseEvent event) throws IOException{
        //EST time zoneID
        ZoneId EST = ZoneId.of("America/New_York");
        //get time from string
        try {
            //save the text as a Timestamp
            Timestamp start = Timestamp.valueOf(startText.getText());
            Timestamp end = Timestamp.valueOf(endText.getText());
            //convert to local timezone
            start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            //create EST time for reference
            ZonedDateTime checkStart = start.toInstant().atZone(EST);
            ZonedDateTime checkEnd = end.toInstant().atZone(EST);
            if (start.before(end)) {
                if (checkStart.getHour() >= 8 && checkEnd.getHour() <= 22) {
                    addApp.setId(aID);
                    addApp.setTitle(titleText.getText());
                    addApp.setDescription(descriptionText.getText());
                    addApp.setLocation(locationText.getText());
                    addApp.setContact(contactCombo.getValue());
                    addApp.setType(typeText.getText());
                    addApp.setStart(start);
                    addApp.setEnd(end);
                    try {
                        addApp.setCustomerID(Integer.parseInt(customerIDText.getText()));
                        addApp.setUserID(Integer.parseInt(userIDText.getText()));
                        //check if schedule overlaps with customers previous schedule
                        if (!Schedule.checkOverlappingApp(addApp)) {
                            Schedule.addApp(addApp);

                            /**
                             * add appointment to DB
                             */
                            String insert = "INSERT INTO appointments(Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP(), ?, CURRENT_TIMESTAMP(), ?, ?,?,?)";
                            Connection con = JDBC.getConnection();

                            try(PreparedStatement state = con.prepareStatement(insert)){
                                int id =addApp.getId();
                                String title = addApp.getTitle();
                                String descript = addApp.getDescription();
                                String loc = addApp.getLocation();
                                String contact = addApp.getContact();
                                String script = "script";
                                String type = addApp.getType();
                                int customerID = addApp.getCustomerID();
                                int userID = addApp.getUserID();
                                int contact_id = contactCombo.getSelectionModel().getSelectedIndex() + 1;
                                //inset
                                state.setInt(1, id);
                                state.setString(2,title);
                                state.setString(3,descript);
                                state.setString(4, loc);
                                state.setString(5, type);
                                state.setTimestamp(6,start);
                                state.setTimestamp(7,end);
                                state.setString(8,script);
                                state.setString(9,script);
                                state.setInt(10,customerID);
                                state.setInt(11,userID);
                                state.setInt(12,contact_id);

                                state.executeUpdate();
                            }
                            catch(SQLException ex)
                            {ex.printStackTrace();}

                            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("appointments.fxml")));
                            Stage stage = (Stage) backButton.getScene().getWindow();
                            stage.setScene(new Scene(root, 1180, 700));
                            stage.show();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Invalid Action");
                            alert.setHeaderText("Please choose a different time");
                            alert.setContentText("The time you chose is overlapping with another appointment");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK) {
                            }
                            ;
                        }
                    } catch (NumberFormatException x) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Invalid Action");
                        alert.setHeaderText("Please use numbers for Customer ID and User ID");
                        alert.setContentText("The data entered is Invalid");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                        }
                    }


                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Invalid Action");
                    alert.setHeaderText("Outside of business hours");
                    alert.setContentText("Please schedule time between 08:00-22:00 EST");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                    }
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Invalid Action");
                alert.setHeaderText("Invalid Date & Time");
                alert.setContentText("Start must be before End");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                }
            }
        }
        catch(IllegalArgumentException ex){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Invalid Action");
                alert.setHeaderText("Start and or End Date and time are not formatted correctly");
                alert.setContentText("yyyy-mm-dd hh:mm:ss please use 24 hour clock");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                }
            }

    }

    @FXML
    void backOnClick(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("appointments.fxml")));
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root, 1180, 700));
        stage.show();
    }

}
