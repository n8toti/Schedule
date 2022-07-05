
package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
/**
 * appointments controller is the hub for all appointments. The user has the option to navigate to different appointment
 * related GUIS, they also have the ability to delete Appointments.
 */
public class appointmentsController implements Initializable {

    @FXML
    private TableView<Appointments> appTable;

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<Appointments, Integer> appointmentIDCol;

    @FXML
    private Button backButton;

    @FXML
    private Button contactButton;

    @FXML
    private TableColumn<Appointments, String> contactCol;

    @FXML
    private TableColumn<Appointments, Integer> customerIDCol;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<Appointments, String> descriptCol;

    @FXML
    private TableColumn<Appointments, Timestamp> endCol;

    @FXML
    private TableColumn<Appointments, String> locationCol;

    @FXML
    private RadioButton monthRadio;

    @FXML
    private Button nextButton;

    @FXML
    private TableColumn<Appointments, Timestamp> startCol;

    @FXML
    private TableColumn<Appointments, String> titleCol;

    @FXML
    private Button totalButton;

    @FXML
    private TableColumn<Appointments, String> typeCol;

    @FXML
    private Button updateButton;

    @FXML
    private TableColumn<Appointments, Integer> userIDCol;

    @FXML
    private RadioButton weekRadio;

    /**
     * takes user to a GUI screen to add appointment
     * @param event
     * @throws IOException
     */
    @FXML
    public void addClicked(MouseEvent event) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addAppointment.fxml")));
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.setScene(new Scene(root, 940, 700));
        stage.show();
    }

    /**
     * returns user to customer table
     * @param event
     * @throws IOException
     */
    @FXML
    public void backClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customers.fxml")));
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root, 1040, 700));
        stage.show();
    }

    /**
     * shows the contact schedule in table view
     * @param event
     * @throws IOException
     */
    @FXML
    public void contactOnCLick(MouseEvent event) throws IOException{
        Schedule.contactSchedule();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("contactSchedule.fxml")));
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.setScene(new Scene(root, 1140, 700));
        stage.show();
    }

    /**
     * delete selected appointment
     * @param event
     */
    @FXML
    public void deleteClicked(MouseEvent event) {
        Appointments appToDelete = appTable.getSelectionModel().getSelectedItem();
        int id = appToDelete.getId();
        String type = appToDelete.getType();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Action");
        alert.setHeaderText("Delete Appointment?");
        alert.setContentText("Delete appointment ID: " + id + " of type: " + type + "?");


        String insert = "DELETE FROM appointments WHERE (Appointment_ID =?)";
        Connection con = JDBC.getConnection();

        try(PreparedStatement state = con.prepareStatement(insert)){
            state.setInt(1, id);
            state.executeUpdate();
        }
        catch(SQLException ex)
        {ex.printStackTrace();}

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            Schedule.deleteApp(appToDelete);
            deleteButton.requestFocus();
            appTable.getSelectionModel().clearSelection();

        }
    }

    @FXML
    void monthClicked(MouseEvent event) {

        if (monthRadio.isSelected())
        {
            Schedule.setAppMonthTable();
            weekRadio.setSelected(false);
            appTable.setItems(Schedule.appMonthTable);
            appTable.refresh();
        }
        if (!monthRadio.isSelected() && !weekRadio.isSelected())
        {
            appTable.setItems(Schedule.appTable);
            appTable.refresh();
        }

    }

    @FXML
    void nextOnClick(MouseEvent event) {
        Schedule.nextApp();
    }

    @FXML
    void totalOnClick(MouseEvent event) throws IOException{
        Schedule.typeTotal();
        Schedule.monthTotal();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("total.fxml")));
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.setScene(new Scene(root, 940, 500));
        stage.show();
    }

    @FXML
    void updateClicked(MouseEvent event) throws IOException {
        Appointments updateApp = appTable.getSelectionModel().getSelectedItem();

        if(updateApp != null)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateAppointment.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1040, 700));
            stage.show();

            updateAppointmentController control = loader.getController();
            int index = appTable.getSelectionModel().getSelectedIndex();
            control.setAppUpdate(updateApp, index);
        }

    }

    /**
     * show data in table for appointments this week
     * @param event
     */
    @FXML
    public void weekClicked(MouseEvent event) {
        if (weekRadio.isSelected())
        {
            Schedule.setAppWeekTable();
            monthRadio.setSelected(false);
            appTable.setItems(Schedule.appWeekTable);
            appTable.refresh();
        }
        if (!monthRadio.isSelected() && !weekRadio.isSelected())
        {
            appTable.setItems(Schedule.appTable);
            appTable.refresh();
        }
    }
    /**
     * specify the columns in table
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("id"));
        contactCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("contact"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("customerID"));
        descriptCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("location"));
        startCol.setCellValueFactory(new PropertyValueFactory<Appointments, Timestamp>("start"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("type"));
        endCol.setCellValueFactory(new PropertyValueFactory<Appointments, Timestamp>("end"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("userID"));

        appTable.refresh();
        appTable.setItems(Schedule.appTable);


    }

}
