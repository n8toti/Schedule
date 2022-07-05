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
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * gets the selected appoint from table and allows the user to update everything except for appointment ID.
 */
public class updateAppointmentController {

    Appointments appointments;
    int index = 0;

    /**
     * gets selected appointment from the table and populates the data to appointments variable to be used to modify
     * the data. The index of the appointment is also gathered
     * @param appointments
     * @param index
     */
    public void setAppUpdate(Appointments appointments, int index) {
        this.index = index;
        this.appointments = appointments;
        appIDText.setText(Integer.toString(appointments.getId()));
        customerIDText.setText(Integer.toString(appointments.getCustomerID()));
        descriptionText.setText(appointments.getDescription());
        endText.setText(appointments.getEnd().toString());
        startText.setText(appointments.getStart().toString());
        locationText.setText(appointments.getLocation());
        titleText.setText(appointments.getTitle());
        typeText.setText(appointments.getType());
        userIDText.setText(Integer.toString(appointments.getUserID()));
        contactCombo.getSelectionModel().select(appointments.getContact());
    }

    /**
     * adds items to contact combo box
     */
    public void initialize() {
        contactCombo.getItems().add("Anika Costa");
        contactCombo.getItems().add("Daniel Garcia");
        contactCombo.getItems().add("Li Lee");
    }
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
    private Button updateButton;

    @FXML
    private TextField userIDText;

    @FXML
    void backOnClick(MouseEvent event) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("appointments.fxml")));
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root, 1180, 700));
        stage.show();

    }

    /**
     * checks data thats being updated. if all is well updates data.
     * @param event
     * @throws IOException
     */
    @FXML
    public void updateOnClick(MouseEvent event) throws IOException{
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
                    appointments.setContact(contactCombo.getValue());
                    appointments.setTitle(titleText.getText());
                    appointments.setDescription(descriptionText.getText());
                    appointments.setType(typeText.getText());
                    appointments.setLocation(locationText.getText());
                    appointments.setStart(Timestamp.valueOf(startText.getText()));
                    appointments.setEnd(Timestamp.valueOf(endText.getText()));
                    appointments.setCustomerID(Integer.parseInt(customerIDText.getText()));
                    appointments.setUserID(Integer.parseInt(userIDText.getText()));


                    try {
                        appointments.setCustomerID(Integer.parseInt(customerIDText.getText()));
                        appointments.setUserID(Integer.parseInt(userIDText.getText()));
                        //check if schedule overlaps with customers previous schedule
                        if (!Schedule.checkOverlappingApp(appointments)) {
                            Schedule.updateAppointment(index, appointments);

                            String insert = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, " +
                                    "Start=?, End=?, Last_Update=CURRENT_TIMESTAMP(), Customer_ID=?, User_ID=?, " +
                                    "Contact_ID=? WHERE Appointment_ID=?";
                            Connection con = JDBC.getConnection();

                            try(PreparedStatement state = con.prepareStatement(insert)){
                                int id = appointments.getId();
                                String title = appointments.getTitle();
                                String descript = appointments.getDescription();
                                String loc = appointments.getLocation();
                                String contact = appointments.getContact();
                                String script = "script";
                                String type = appointments.getType();
                                int customerID = appointments.getCustomerID();
                                int userID = appointments.getUserID();
                                int contact_id = contactCombo.getSelectionModel().getSelectedIndex() + 1;
                                //inset
                                state.setString(1,title);
                                state.setString(2,descript);
                                state.setString(3, loc);
                                state.setString(4, type);
                                state.setTimestamp(5,start);
                                state.setTimestamp(6,end);
                                state.setInt(7,customerID);
                                state.setInt(8,userID);
                                state.setInt(9,contact_id);
                                state.setInt(10, id);

                                state.executeUpdate();
                            }
                            catch(SQLException ex) {ex.printStackTrace();}

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
            else
            {
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

}
