
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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
/**
 * The customer controller gives user access to the Customer table. They are able to delte customers as long as there are
 * no associated appointments with that customer. If there are associated appointments they will need to delete them before
 * removing the customer.
 */
public class customerController implements Initializable {


    @FXML
    private Button addButton;

    @FXML
    private Button appointmentsButton;

    @FXML
    private TableColumn<Customers, Integer> countryID;

    @FXML
    private TableColumn<Customers, String> customerAddress;

    @FXML
    private TableColumn<Customers, Integer> customerID;

    @FXML
    private TableColumn<Customers, String> customerName;

    @FXML
    private TableView<Customers> customerTable;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<Customers, String> division;

    @FXML
    private TableColumn<Customers, Integer> divisionID;

    @FXML
    private TableColumn<Customers, String> phoneCustomer;

    @FXML
    private Button updateButton;

    @FXML
    private TableColumn<Customers, String> zipCode;

    @FXML
    void addOnClick(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addCustomer.fxml")));
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.setScene(new Scene(root, 640, 700));
        stage.show();

    }

    @FXML
    void appointmentsOnClick(MouseEvent event) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("appointments.fxml")));
        Stage stage = (Stage) appointmentsButton.getScene().getWindow();
        stage.setScene(new Scene(root, 1180, 700));
        stage.show();

    }

    @FXML
    void deleteOnClick(MouseEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Action");
        alert.setHeaderText("Delete Customer?");
        alert.setContentText("Delete Customer? If there are appointments Associated with this Customer " +
                "you cannot delete.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Customers customerToDelete = customerTable.getSelectionModel().getSelectedItem();
            System.out.println(customerToDelete.getCustomerID());
            if (!Customers.checkAssociatedApp(customerToDelete))
            {
                Schedule.deleteCustomer(customerToDelete);
                deleteButton.requestFocus();
                customerTable.getSelectionModel().clearSelection();
                String name = customerToDelete.getCustomerName();
                alert.setTitle("Confirm Action");
                alert.setHeaderText(name);
                alert.setContentText("has been deleted.");
                Optional<ButtonType> resul = alert.showAndWait();
               /**
                Delete from Database below and from table above
                */
                int id = customerToDelete.getCustomerID();
                String insert = "DELETE FROM customers WHERE (Customer_ID ='" + id + "')";
                Connection con = JDBC.getConnection();

                try(PreparedStatement state = con.prepareStatement(insert)){



                    //state.setInt(1, id);

                    state.executeUpdate();
                }
                catch(SQLException ex)
                {ex.printStackTrace();}

                if (resul.get() == ButtonType.OK) {}

            }
            else
            {
                alert.setTitle("Confirm Action");
                alert.setHeaderText("Cannot Delete Customer");
                alert.setContentText("There are Associated Appointments-- Unable to delete");
                Optional<ButtonType> resul = alert.showAndWait();
                if (resul.get() == ButtonType.OK) {}
            }
        }

    }

    @FXML
    void updateOnClick(MouseEvent event) throws IOException{
        Customers updateCustomer = customerTable.getSelectionModel().getSelectedItem();

        if(updateCustomer != null)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateCustomer.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1040, 700));
            stage.show();

            updateCustomerController control = loader.getController();
            int index = customerTable.getSelectionModel().getSelectedIndex();
            control.setUpdate(updateCustomer, index);
        }


    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**
         * specify the columns in table
         */
        countryID.setCellValueFactory(new PropertyValueFactory<Customers, Integer>("countryID"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<Customers, String>("address"));
        customerID.setCellValueFactory(new PropertyValueFactory<Customers, Integer>("customerID"));
        customerName.setCellValueFactory(new PropertyValueFactory<Customers, String>("customerName"));
        division.setCellValueFactory(new PropertyValueFactory<Customers, String>("division"));
        divisionID.setCellValueFactory(new PropertyValueFactory<Customers, Integer>("divisionID"));
        phoneCustomer.setCellValueFactory(new PropertyValueFactory<Customers, String>("phone"));
        zipCode.setCellValueFactory(new PropertyValueFactory<Customers, String>("postal"));
        customerTable.refresh();
        customerTable.setItems(Schedule.customersTable);

    }

}