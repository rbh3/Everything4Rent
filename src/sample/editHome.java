package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class editHome {
    ObservableList<String> q1= FXCollections.observableArrayList("סוג מתחם:", "דירה","וילה","אולם אירועים","משרד");
    public ChoiceBox kindofhome;
    public Button editit;
    public TextField street;
    public TextField stnum;
    public TextField city;
    public TextField maxppl;
    public TextField size;
    Connection conn;

    @FXML
    public void initialize() throws Exception
    {
        kindofhome.setItems(q1);
        maxppl.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    maxppl.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        size.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    size.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement prep = conn.prepareStatement(
                "SELECT * FROM realEstateItem WHERE ID=?");
        prep.setString(1,Controller.selectedID);
        ResultSet rs=prep.executeQuery();
        street.setText(rs.getString(2));
        stnum.setText(rs.getString(3));
        city.setText(rs.getString(4));
        maxppl.setText(rs.getString(5));
        size.setText(rs.getString(6));
        switch (rs.getString(7))
        {
            case "Apartment": kindofhome.setValue("דירה"); break;
            case "Villa": kindofhome.setValue("וילה");break;
            case "Venue":kindofhome.setValue("אולם אירועים"); break;
            case "Office":kindofhome.setValue("משרד"); break;
        }

    }

    private void showAlertError(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void editNow() throws Exception
    {
        Stage stage = (Stage) editit.getScene().getWindow();
        PreparedStatement prep = conn.prepareStatement(
                "UPDATE realEstateItem SET street=? ,streetNum=?,city=?,maxPeople=?,size=?,kind=?" );

        prep.setString(1, street.getText());
        prep.setString(2, stnum.getText());
        prep.setString(3, city.getText());
        prep.setString(4, maxppl.getText());
        prep.setString(5, size.getText());
        switch (kindofhome.getSelectionModel().getSelectedItem().toString())
        {
            case "סוג מתחם:": showAlertError("יש לבחור מתחם"); return;
            case "דירה":  prep.setString(6, "Apartment"); break;
            case "וילה":  prep.setString(6, "Villa"); break;
            case "אולם אירועים":  prep.setString(6, "Venue"); break;
            case "משרד":  prep.setString(6, "Office"); break;

        }
        prep.addBatch();
        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);

        showAlertInfo("Item Updated!");
        conn.close();
        stage.close();
        Controller.my.close();
    }
    private void showAlertInfo(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }
}
