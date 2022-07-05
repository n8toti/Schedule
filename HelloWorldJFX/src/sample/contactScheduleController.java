
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
import java.sql.Timestamp;
import java.util.Objects;
import java.util.ResourceBundle;
/**
 * the contact schedule controller gives the user access to the contacts schedule. They can choose between the three
 * contacts with radio buttons to view their schedule.
 */
public class contactScheduleController implements Initializable {

    @FXML
    private TableView<Appointments> schedulTable;


    @FXML
    private TableColumn<Appointments, Integer> appointmentIDCol;

    @FXML
    private Button backButton;

    @FXML
    private Label contactSchedule;

    @FXML
    private TableColumn<Appointments, Integer> customerIDCol;

    @FXML
    private TableColumn<Appointments, String> descriptCol;

    @FXML
    private TableColumn<Appointments, Timestamp> endCol;

    @FXML
    private TableColumn<Appointments, Timestamp> startCol;

    @FXML
    private TableColumn<Appointments, String> titleCol;

    @FXML
    private TableColumn<Appointments, String> typeCol;

    @FXML
    private RadioButton liRadio;

    @FXML
    private RadioButton danielRadio;

    @FXML
    private RadioButton anikaRadio;

    @FXML
    void anikaOnClick(MouseEvent event) {
        if (anikaRadio.isSelected())
        {
            danielRadio.setSelected(false);
            liRadio.setSelected(false);

            schedulTable.setItems(Schedule.anikaTable);
            schedulTable.refresh();
        }
    }

    @FXML
    void backClicked(MouseEvent event) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("appointments.fxml")));
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root, 1180, 700));
        stage.show();
    }

    @FXML
    void danielOnClick(MouseEvent event) {
        if (danielRadio.isSelected())
        {
            anikaRadio.setSelected(false);
            liRadio.setSelected(false);

            schedulTable.setItems(Schedule.danielTable);
            schedulTable.refresh();
        }
    }

    @FXML
    void liOnclick(MouseEvent event) {
        if (liRadio.isSelected())
        {
            danielRadio.setSelected(false);
            anikaRadio.setSelected(false);

            schedulTable.setItems(Schedule.liTable);
            schedulTable.refresh();
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**
         * specify the columns in table
         */
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("id"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("customerID"));
        descriptCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("description"));
        startCol.setCellValueFactory(new PropertyValueFactory<Appointments, Timestamp>("start"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("type"));
        endCol.setCellValueFactory(new PropertyValueFactory<Appointments, Timestamp>("end"));


        schedulTable.refresh();
        schedulTable.setItems(Schedule.anikaTable);
    }
}
