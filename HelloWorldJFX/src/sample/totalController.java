package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.ResourceBundle;
/**
 * total controller populates the two tables with totals for month and type
 */
public class totalController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<month, String> monthCol;

    @FXML
    private TableView<month> monthTable;

    @FXML
    private TableColumn<month, Integer> monthTotal;

    @FXML
    private TableColumn<total, String> typeCol;

    @FXML
    private TableView<total> typeTable;

    @FXML
    private TableColumn<total, Integer> typeTotal;

    @FXML
    void backClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("appointments.fxml")));
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root, 1180, 700));
        stage.show();
    }

    /**
     * initializes table with total data
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**
         * specify the columns in table
         */
        monthCol.setCellValueFactory(new PropertyValueFactory<month, String>("month"));
        monthTotal.setCellValueFactory(new PropertyValueFactory<month, Integer>("total"));
        typeTotal.setCellValueFactory(new PropertyValueFactory<total, Integer>("count"));
        typeCol.setCellValueFactory(new PropertyValueFactory<total, String>("type"));


        monthTable.refresh();
        typeTable.refresh();
        monthTable.setItems(Schedule.totalMonthTable);
        typeTable.setItems(Schedule.totalTable);
    }
}
