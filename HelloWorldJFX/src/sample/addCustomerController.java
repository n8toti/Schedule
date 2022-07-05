
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
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
/**
 * add customer controller creates a new customer and adds it to the table in the GUI. There are two combo boxes that
 * takes use the stream() command and lambdas to populate the data.
 */
public class addCustomerController{

    Customers addCustomer = new Customers(0,"","","",
                                            "", 0,"",0);
    int cID = Schedule.customersTable.size() + 1;
    String country = "";

    public void initialize(){
        idTextField.setText(Integer.toString(cID));
        countryCombo.getItems().add("Canada");
        countryCombo.getItems().add("United Kingdom");
        countryCombo.getItems().add("United States");
    }
    @FXML
    private Label address;

    @FXML
    private Button addButton;

    @FXML
    private TextField addressTextField;

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<String> countryCombo;

    @FXML
    private ComboBox<String> firstLevelCombo;

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
    /**
        gets division ID from a filter mapped colelction with a lambda function if no Division picked keep original
        streamlines the process to pick out a specific divison from 100+ to choose from
     * @param event
     * @throws IOException
     */
    @FXML
    public void addOnClick(MouseEvent event) throws IOException{

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
        int divisionID = 0;
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
        addCustomer.setCustomerID(cID);
        addCustomer.setCustomerName(nameTextField.getText());
        addCustomer.setAddress(addressTextField.getText());
        addCustomer.setPhone(phoneTextField.getText());
        addCustomer.setDivisionID(divisionID);
        addCustomer.setDivision(Division);
        addCustomer.setCountryID(countryID);
        addCustomer.setPostal(postalTextField.getText());

        /**
         * add new customer to the DB
         */
        Schedule.addCustomer(addCustomer);
        String insert = "INSERT INTO customers(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?, ?, ?, ?, ?, CURRENT_TIMESTAMP(), ?, CURRENT_TIMESTAMP(), ?, ?)";
        Connection con = JDBC.getConnection();

        try(PreparedStatement state = con.prepareStatement(insert)){

            int id = addCustomer.getCustomerID();
            String name = addCustomer.getCustomerName();
            String address = addCustomer.getAddress();
            String zip = addCustomer.getPostal();
            String phone = addCustomer.getPhone();
            //CURRENT_TIMESTAMP()
            String script = "Script";
            //CURRENT_TIMESTAMP()
            //script
            int dID = addCustomer.getDivisionID();

            //inset
            state.setInt(1, id);
            state.setString(2,name);
            state.setString(3,address);
            state.setString(4, zip);
            state.setString(5,phone);
            state.setString(6,script);
            state.setString(7,script);
            state.setInt(8,dID);

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
    void backOnClick(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customers.fxml")));
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root, 1040, 700));
        stage.show();

    }

    /**
     * Select a country and change tables and combo box to reflect the country chosen.
     * @param event
     */
    @FXML
    public void countryOnClick(ActionEvent event)
    {
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
     * if country is united states use filter collection and a lambda to set the items. The list of states and
     * first-level-divisions is large and the filter with lambda expressions stream lined the process
     *
     * if country is Canada use filter collection and a lambda to set the items. The list of
     * first-level-divisions iss large and the filter with lambda expressions stream lined the process
     *
     * if country is United Kingdom stream filter and map with lambda and collect a list. The list of
     * first-level-divisions iss large and the filter with lambda expressions stream lined the process
     * @param event
     */
    @FXML
    public void firstOnClick(MouseEvent event)
    {
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