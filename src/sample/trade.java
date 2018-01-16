package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class trade {
    @FXML
    public ComboBox chooser;

    @FXML
    public void initialize() throws Exception {
        chooser.setItems(Showres.forTrade);
        chooser.getSelectionModel().selectFirst();

    }

    public void choose() throws Exception
    {
        Stage stage = (Stage) chooser.getScene().getWindow();
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement prep = conn.prepareStatement(
                "INSERT into TradeIn values (?,?,?,?,?,?) ;");
        prep.setString(2,Search.dateStart.toString());
        prep.setString(3,Search.dateEnd.toString());
        prep.setString(4,"WAITING");
        prep.setString(5,chooser.getSelectionModel().getSelectedItem().toString().split("-")[0]);
        prep.setString(6,Showres.selecpak.getID());
        prep.execute();
        //conn.commit();
        conn.close();
        showMy("ההזמנה נשלחה לאישור");
        stage.close();
        Showres.stage.close();
        Search.stage.close();
    }

    private void showMy(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Success");
        alert.setContentText(alertMessage);
        alert.show();
    }
}
