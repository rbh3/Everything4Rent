package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Additem {
    public ChoiceBox policy;
    public TextField relaible;
    public Label cancellbl;
    public TextField cancelfld;
    public CheckBox cancelcheck;
    public Label packnum;
    public Button addhome;
    public Button add;
    public TextField pricefld;

    public static IntegerProperty items;
    public static ArrayList<PreparedStatement> query;
    public static Connection conn;
    ObservableList<String> q1= FXCollections.observableArrayList("מדיניות בחירת שוכר:", "גישת כל הקודם זוכה- עדיפות למשתמש הראשון","הגישה השמרנית- עדיפות לפי אמינות","הגישה הבטוחה-72 שעות לבחירת המשתמש");

    @FXML
    public void initialize() throws Exception
    {
        pricefld.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    pricefld.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        query=new ArrayList();
        items=new SimpleIntegerProperty(0);
        policy.setItems(q1);
        policy.setValue("מדיניות בחירת שוכר:");
        relaible.visibleProperty().bind( policy.getSelectionModel().selectedItemProperty().isEqualTo("הגישה השמרנית- עדיפות לפי אמינות"));
        cancelfld.visibleProperty().bind(cancelcheck.selectedProperty());
        cancellbl.visibleProperty().bind(cancelcheck.selectedProperty());
        packnum.textProperty().bind(items.asString());
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:test.db");

    }
    public void homewin() throws IOException
    {
        Stage s = new Stage();
        s.setTitle("Add Realestate");
        AnchorPane root = FXMLLoader.load(getClass().getResource("../sample/addHome.fxml"));
        Scene scene = new Scene(root, 600, 665);
        scene.getStylesheets().add(getClass().getResource("b.css").toExternalForm());
        //styiling
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
    }
    public void addpckg() throws Exception
    {
        Stage stage = (Stage) cancelfld.getScene().getWindow();
        PreparedStatement prep = conn.prepareStatement(
                "insert into PacketsForRent values (?,?,?,?,?,?,?,?);");
        prep.setString(1,""+Main.PacketsForRentInd);
        prep.setString(2, items.getValue().toString());
        prep.setString(3, pricefld.getText());
        if(cancelcheck.isSelected())
            prep.setString(4, "Yes");
        else
            prep.setString(4, "No");
        prep.setString(5, cancelfld.getText());
        switch (policy.getSelectionModel().getSelectedItem().toString())
        {
            case "מדיניות בחירת שוכר::": showAlertError("יש לבחור גישת בחירה למשתמשים"); conn.close(); return;
            case "גישת כל הקודם זוכה- עדיפות למשתמש הראשון":  prep.setString(6, "First"); break;
            case "הגישה השמרנית- עדיפות לפי אמינות":  prep.setString(6, "Rate"); break;
            case "הגישה הבטוחה-72 שעות לבחירת המשתמש":  prep.setString(6, "Safe"); break;

        }
        prep.setString(7, relaible.getText());
        prep.setString(8, Main.name.getValue().substring(6));
        prep.addBatch();

        conn.setAutoCommit(false);
        prep.executeBatch();
        for(int i=0;i<query.size();i++)
        {
            query.get(i).executeBatch();
        }
        Main.PacketsForRentInd=Main.PacketsForRentInd+1;
        conn.setAutoCommit(true);

        showAlertInfo("Package Added!");
        conn.close();
        stage.close();

    }

    private void showAlertError(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
    }
    private void showAlertInfo(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

}
