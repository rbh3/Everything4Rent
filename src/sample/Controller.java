package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Controller {
    public Button edit;
    public Button delitem;
    public Button delpkg;
    public ChoiceBox pkg;
    public ChoiceBox prod;

    public static String selectedID;

    public static Stage my;
    @FXML
    public void initialize() throws Exception {
        ObservableList<String> q1= FXCollections.observableArrayList();
        ObservableList<String> q2= FXCollections.observableArrayList();

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement prep = conn.prepareStatement(
                "SELECT * FROM realEstateItem JOIN PacketsForRent ON realEstateItem.PackageID = PacketsForRent.ID WHERE userName=? ORDER BY PacketsForRent.ID");
        prep.setString(1, Main.name.getValue().substring(6));
        ResultSet rs=prep.executeQuery();
        while(rs.next())
        {
            if(!q1.contains(rs.getString(8))) {
                q1.add(rs.getString(8));
            }
            q2.add("RealEstate-ID: "+rs.getString(1)+" Address- "+rs.getString(2)+" "+rs.getString(4));
        }
        pkg.setItems(q1);
        pkg.getSelectionModel().selectFirst();
        prod.setItems(q2);
        prod.getSelectionModel().selectFirst();
        conn.close();
        if(prod.getSelectionModel().isEmpty()||pkg.getSelectionModel().isEmpty())
        {
            edit.setDisable(true);
            delitem.setDisable(true);
            delpkg.setDisable(true);
        }
    }

    public void editprod() throws IOException
    {
        String[] str=prod.getSelectionModel().getSelectedItem().toString().split(" ");
        selectedID= str[1];
        Stage s = new Stage();
        s.setTitle("Edit Home");
        AnchorPane root = FXMLLoader.load(getClass().getResource("../sample/edithome.fxml"));
        Scene scene = new Scene(root,  641, 500);
        scene.getStylesheets().add(getClass().getResource("b.css").toExternalForm());
        //styiling
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
        s.setOnCloseRequest(event -> s.close());
        my=(Stage) edit.getScene().getWindow();
    }

    public void delitem() throws Exception
    {
        String[] str=prod.getSelectionModel().getSelectedItem().toString().split(" ");
        selectedID= str[1];
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement prep = conn.prepareStatement(
                "DELETE FROM realEstateItem WHERE ID=? ");
        prep.setString(1,selectedID);
        prep.executeUpdate();
        conn.close();
        my=(Stage) edit.getScene().getWindow();
        showAlertInfo("הפריט נמחק");
        my.close();
    }

    public void delPkg() throws Exception
    {
        String p=pkg.getSelectionModel().getSelectedItem().toString();
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement prep = conn.prepareStatement(
                "DELETE FROM realEstateItem WHERE PackageID=? ");
        prep.setString(1,p);
        prep.executeUpdate();
        PreparedStatement prep2 = conn.prepareStatement(
                "DELETE FROM PacketsForRent WHERE ID=? ");
        prep2.setString(1,p);
        prep2.executeUpdate();
        conn.close();
        my=(Stage) edit.getScene().getWindow();
        showAlertInfo("החבילה נמחק");
        my.close();
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
