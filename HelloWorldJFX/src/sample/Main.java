package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;
import java.util.Calendar;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("loginScreen.fxml"));
        primaryStage.setScene(new Scene(root, 520, 345));
        primaryStage.show();
    }


    public static void main(String[] args) {

        JDBC.makeConnection();
        /*
        *Retrieve Database information and add it to a list to populate tables
        */
        Schedule.addDB();
        Schedule.addAppData();
        Schedule.firstLevelData();

        launch(args);
    }
}
