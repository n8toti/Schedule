package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
/**
 * Update customer controller is used to select a customer from the customer table and append the information
 */
public class updateCustomerController {

    Customers customer;
    int index = 0;
    String country = "";

    /**
     * gets information from selected customer to be used to update the information as well as index
     * @param customer
     * @param index
     */
    public void setUpdate(Customers customer, int index) {
        this.index = index;
        this.customer = customer;
        addressTextField.setText(customer.getAddress());
        idTextField.setText(Integer.toString(customer.getCustomerID()));
        nameTextField.setText(customer.getCustomerName());
        phoneTextField.setText(customer.getPhone());
        postalTextField.setText(customer.getPostal());
        if (customer.getCountryID() == 1) {
            countryCombo.getSelectionModel().selectFirst();
        }
        else if (customer.getCountryID() == 2)
        {
            countryCombo.getSelectionModel().select("United Kingdom");
        }
        else{countryCombo.getSelectionModel().selectLast();}
        firstLevelCombo.getSelectionModel().select(customer.getDivision());
    }

    /**
     * initialize country combo box with data
     */
    public void initialize() {
        countryCombo.getItems().add("United States");
        countryCombo.getItems().add("United Kingdom");
        countryCombo.getItems().add("Canada");
    }

    @FXML
    private TextField addressTextField;

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<String> countryCombo;

    @FXML
    private ComboBox<String> firstLevelCombo;

    @FXML
    private Label address;

    @FXML
    private Label firstLevelLabel;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField postalTextField;

    @FXML
    private Button updateButton;

    @FXML
    void backOnClick(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customers.fxml")));
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root, 1040, 700));
        stage.show();

    }

    /**
     *
     *  Gets division ID by streaming the data wand filtering mapping and collecting the data with lambdas-- which
     *  help stream line the process by writing less code and not having to define a method to do the task. if no Division
     *  picked keep original.
     * @param event
     * @throws IOException
     */
    @FXML
    public void updateOnClick(MouseEvent event) throws IOException {
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String zip = postalTextField.getText();
        String phone = phoneTextField.getText();
        int countryID = 0;
        String Division = firstLevelCombo.getValue();
        if (country.equals("United States"))
        {
            countryID = 1;
        }
        else if (country.equals("United Kingdom"))
        {
            countryID = 2;
        }
        else if (country.equals("Canada"))
        {
            countryID = 3;
        }
        else{ countryID = customer.getCountryID();}
        int divisionID = customer.getDivisionID();
        if (Division != null) {
            String finalDivision = Division;
            List<Integer> dList = Schedule.divisionData.stream()
                    .filter(c -> c.getDivision() == finalDivision)
                    .map(ct -> ct.getDivisionID())
                    .collect(Collectors.toList());
            for (int e : dList)
            {
                divisionID = e;
            }
        }
        else
        {
            Division = customer.getDivision();
            divisionID = customer.getDivisionID();
        }



        customer.setCustomerName(name);
        customer.setAddress(address);
        customer.setPostal(zip);
        customer.setPhone(phone);
        customer.setDivision(Division);
        customer.setDivisionID(divisionID);
        customer.setCountryID(countryID);

        Schedule.updateCustomer(index, customer);
        int id = customer.getCustomerID();
        String insert = "UPDATE customers SET Customer_Name =?, Address =?, Postal_Code =?, Phone=?, " +
                 "Created_By=?, Last_Updated_By=?, Division_ID =? WHERE Customer_ID =?";
        Connection con = JDBC.getConnection();

        try(PreparedStatement state = con.prepareStatement(insert)){

            //int id = customer.getCustomerID();
            //CURRENT_TIMESTAMP()
            String script = "Script";
            //CURRENT_TIMESTAMP()
            //script

            //inset
            state.setString(1,name);
            state.setString(2,address);
            state.setString(3, zip);
            state.setString(4,phone);
            state.setString(5,script);
            state.setString(6,script);
            state.setInt(7,divisionID);
            state.setInt(8,id);

            state.executeUpdate();
        }
        catch(SQLException ex)
        {ex.printStackTrace();}

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customers.fxml")));
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root, 1040, 700));
        stage.show();


    }



    @FXML
    void countryOnClick(ActionEvent event) {
        firstLevelCombo.getItems().clear();
        country = countryCombo.getValue();
        if (country != null) {
            if (country.equals("United States")) {
                address.setText("U.S. address:");
                firstLevelLabel.setText("States");
                firstLevelCombo.setPromptText("Select State");

            } else if (country.equals("Canada")) {
                address.setText("Canadian address:");
                firstLevelLabel.setText("Province");
                firstLevelCombo.setPromptText("Select Province");
            } else {
                address.setText("UK address:");
                firstLevelLabel.setText("Region");
                firstLevelCombo.setPromptText("Select Region");
            }
        }

    }
    /**
     *if country is united states use filter, map, and collect with lambdas to set the items. Lambdas are used
     * to streamline the process. a method isn't needed to be defined and used to complete the task.
     *
     * if country is Canada use filter, map and, collect with lambdas to set the items in the combo box.
     * Lambdas are used to streamline the process. a method isn't needed to be defined and used to complete
     * the task.
     *
     *  if country is United Kingdoms use filter collection and a lambda to set the items in the combo box
     *  as opposed to writing loops to find the data I am able to stream the process with stream map and
     *  collect with lambdas as opposed to writing a method to allow me the same capability.
     * @param event
     */
    @FXML
    public void firstOnClick(MouseEvent event) {
        country = countryCombo.getValue();
        //check to see if country has been selected
        if (country != null) {

            if (country.equals("United States")) {
                List<String> dList = Schedule.divisionData.stream()
                        .filter(c->c.getCountryID() == 1)
                        .map(ct->ct.getDivision())
                        .collect(Collectors.toList());
                for(String e : dList)
                {
                    firstLevelCombo.getItems().add(e);
                }

            }
            else if (country.equals("Canada"))
            {

                    List<String> dList = Schedule.divisionData.stream()
                            .filter(c->c.getCountryID() == 3)
                            .map(ct->ct.getDivision())
                            .collect(Collectors.toList());
                    for(String e : dList)
                    {
                        firstLevelCombo.getItems().add(e);
                    }

            }
            else {

                List<String> dList = Schedule.divisionData.stream()
                        .filter(c->c.getCountryID() == 2)
                        .map(ct->ct.getDivision())
                        .collect(Collectors.toList());
                for(String e : dList)
                {
                    firstLevelCombo.getItems().add(e);
                }
            }
        }

    }
}
