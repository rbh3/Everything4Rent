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
                "SELECT * FROM realEstateItem JOIN Packets ON realEstateItem.PackageID = Packets.ID WHERE userName=? ORDER BY Packets.ID");
        prep.setString(1, Main.name.getValue().substring(6));
        ResultSet rs=prep.executeQuery();
        while(rs.next())
        {
            String s=rs.getString(8)+"-"+rs.getString(16);
            if(!q1.contains(s)) {
                q1.add(s);
            }
            q2.add("RealEstate-ID: "+rs.getString(1)+" Address- "+rs.getString(2)+" "+rs.getString(4));
        }
        prep = conn.prepareStatement(
                "SELECT * FROM CarItem JOIN Packets ON CarItem.PackageID = Packets.ID WHERE userName=? ORDER BY Packets.ID");
        prep.setString(1, Main.name.getValue().substring(6));
        rs=prep.executeQuery();
        while(rs.next())
        {
            String s=rs.getString(8)+"-"+rs.getString(10);
            if(!q1.contains(s)) {
                q1.add(s);
            }
            q2.add("Car-ID: "+rs.getString(1)+" Name- "+rs.getString(6));
        }

        prep = conn.prepareStatement(
                "SELECT * FROM PetItem JOIN Packets ON PetItem.PackageID = Packets.ID WHERE userName=? ORDER BY Packets.ID");
        prep.setString(1, Main.name.getValue().substring(6));
        rs=prep.executeQuery();
        while(rs.next())
        {
            String s=rs.getString(7)+"-"+rs.getString(9);
            if(!q1.contains(s)) {
                q1.add(s);
            }
            q2.add("Pet-ID: "+rs.getString(1)+" Name- "+rs.getString(2));
        }

        prep = conn.prepareStatement(
                "SELECT * FROM electricItem JOIN Packets ON electricItem.PackageID = Packets.ID WHERE userName=? ORDER BY Packets.ID");
        prep.setString(1, Main.name.getValue().substring(6));
        rs=prep.executeQuery();
        while(rs.next())
        {
            String s=rs.getString(6)+"-"+rs.getString(8);
            if(!q1.contains(s)) {
                q1.add(s);
            }
            q2.add("Electric-ID: "+rs.getString(1)+" Name- "+rs.getString(2));
        }

        prep = conn.prepareStatement(
                "SELECT * FROM furnitureItem JOIN Packets ON furnitureItem.PackageID = Packets.ID WHERE userName=? ORDER BY Packets.ID");
        prep.setString(1, Main.name.getValue().substring(6));
        rs=prep.executeQuery();
        while(rs.next())
        {
            String s=rs.getString(8)+"-"+rs.getString(10);
            if(!q1.contains(s)) {
                q1.add(s);
            }
            q2.add("furniture-ID: "+rs.getString(1)+" Name- "+rs.getString(2));
        }

        prep = conn.prepareStatement(
                "SELECT * FROM CellolarItem JOIN Packets ON CellolarItem.PackageID = Packets.ID WHERE userName=? ORDER BY Packets.ID");
        prep.setString(1, Main.name.getValue().substring(6));
        rs=prep.executeQuery();
        while(rs.next())
        {
            String s=rs.getString(8)+"-"+rs.getString(10);
            if(!q1.contains(s)) {
                q1.add(s);
            }
            q2.add("Cellular-ID: "+rs.getString(1)+" Name- "+rs.getString(2));
        }
        prep = conn.prepareStatement(
                "SELECT * FROM secondHandItem JOIN Packets ON secondHandItem.PackageID = Packets.ID WHERE userName=? ORDER BY Packets.ID");
        prep.setString(1, Main.name.getValue().substring(6));
        rs=prep.executeQuery();
        while(rs.next())
        {
            String s=rs.getString(5)+"-"+rs.getString(7);
            if(!q1.contains(s)) {
                q1.add(s);
            }
            q2.add("SecHand-ID: "+rs.getString(1)+" Name- "+rs.getString(2));
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
        String id = selectedID.substring(0,2);
        String tableName="";
        switch (id)
        {
            case "EL":tableName="editSecondHand"; break;
            case  "FU":tableName="editSecondHand";break;
            case "CE":tableName="editSecondHand";break;
            case "SH":tableName="editSecondHand";break;
            case "RE":tableName="editHome";break;
            case  "CA":tableName="editCar";break;
            case  "PE":tableName="editPet";break;
        }
        Stage s = new Stage();
        s.setTitle("Edit");
        AnchorPane root = FXMLLoader.load(getClass().getResource("../sample/"+tableName+".fxml"));
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
        String id = selectedID.substring(0,2);
        String tableName="";
        switch (id)
        {
            case "EL":tableName="electricItem"; break;
            case  "FU":tableName="furnitureItem";break;
            case "CE":tableName="cellolarItem";break;
            case "SH":tableName="secondHandItem";break;
            case "RE":tableName="realEstateItem";break;
            case  "CA":tableName="CarItem";break;
            case  "PE":tableName="PetItem";break;
        }
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement prep = conn.prepareStatement(
                "DELETE FROM "+tableName+ " WHERE ID=? ");
        prep.setString(1,selectedID);
        prep.executeUpdate();
        conn.close();
        my=(Stage) edit.getScene().getWindow();
        showAlertInfo("הפריט נמחק");
        my.close();
    }

    public void delPkg() throws Exception
    {
        String p=pkg.getSelectionModel().getSelectedItem().toString().split("-")[0];
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement prep = conn.prepareStatement(
                "DELETE FROM electricItem WHERE PackageID=? ");
        prep.setString(1,p);
        prep.executeUpdate();
        prep = conn.prepareStatement(
                "DELETE FROM furnitureItem WHERE PackageID=? ");
        prep.setString(1,p);
        prep.executeUpdate();
        prep = conn.prepareStatement(
                "DELETE FROM cellolarItem WHERE PackageID=? ");
        prep.setString(1,p);
        prep.executeUpdate();
        prep = conn.prepareStatement(
                "DELETE FROM secondHandItem WHERE PackageID=? ");
        prep.setString(1,p);
        prep.executeUpdate();
        prep = conn.prepareStatement(
                "DELETE FROM realEstateItem WHERE PackageID=? ");
        prep.setString(1,p);
        prep.executeUpdate();
        prep = conn.prepareStatement(
                "DELETE FROM CarItem WHERE PackageID=? ");
        prep.setString(1,p);
        prep.executeUpdate();
        prep = conn.prepareStatement(
                "DELETE FROM PetItem WHERE PackageID=? ");
        prep.setString(1,p);
        prep.executeUpdate();
        PreparedStatement prep2 = conn.prepareStatement(
                "DELETE FROM Packets WHERE ID=? ");
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
