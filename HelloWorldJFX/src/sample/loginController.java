
package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.*;
import java.util.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
/**
 * Here the user will attempt to login. Their attempt will be logged into a login_activity.txt report. Depending on the
 * users system settings the information in the login screen will appear in french or english. Including any alerts.
 */
public class loginController {

    // get the language being used
    Locale locale = Locale.getDefault();
    String language = locale.getDisplayLanguage();
    Boolean loggedin = false;

    private void englishMessage(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Incorrect username or password");
        alert.setHeaderText("Please Try Again");
        alert.setContentText("The Username and/or Password you entered is incorrect. Please try again.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
        }
    }
    private void frenchMessage(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("identifiant ou mot de passe incorrect");
        alert.setHeaderText("Veuillez réessayer");
        alert.setContentText("Le nom d'utilisateur et/ou le mot de passe que vous avez saisis sont incorrects. " +
                                "Veuillez réessayer.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
        }
    }


    @FXML
    private Label header;

    @FXML
    private Label name;

    @FXML
    private Label pass;

    @FXML
    private TextField PassID;

    @FXML
    private Label ZoneID;


    @FXML
    public void initialize(){

        if (!language.equals("English"))
        {
            pass.setText("LE MOT DE PASSE");
            name.setText("NOM D'UTILISATEUR");
            header.setText("Utilisateur en ligne");
            loginButton.setText("Connexion");
        }
        ZoneID.setText(TimeZone.getDefault().getID());

    }

    @FXML
    private Button loginButton;

    @FXML
    private TextField userID;

    @FXML
    void loginOnClick(MouseEvent event) throws IOException {
        Boolean logged = false;
        String eUser = userID.getText();
        String ePass = PassID.getText();

        try {
            String sql = "SELECT * FROM users WHERE User_Name=? AND Password=?";
            Connection con = JDBC.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1,eUser);
            state.setString(2,ePass);
            ResultSet result = state.executeQuery();
            if(result.next())
            {
                logged = true;
            }
        }
        catch(SQLException ex)
        {ex.printStackTrace();}

        if(logged)
        {
            loggedin = true;
            Schedule.logging(eUser, ePass, loggedin);
            Schedule.checkNow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customers.fxml")));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1040, 700));
            stage.show();

        }
        else if(language.equals("English"))
        {
            Schedule.logging(eUser, ePass, loggedin);
            englishMessage();
        }
        else
        {
            Schedule.logging(eUser, ePass, loggedin);
            frenchMessage();
        }

    }


}