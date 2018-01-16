package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    public CheckBox karaokechk;
    public CheckBox poolchk;
    public CheckBox balconychk;
    public CheckBox smokechk;
    public CheckBox tvchk;
    public CheckBox speakerschk;
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
        if(rs.getString(9).equals("true"))
            karaokechk.selectedProperty().setValue(true);
        else
            karaokechk.selectedProperty().setValue(false);
        if(rs.getString(10).equals("true"))
            poolchk.selectedProperty().setValue(true);
        else
            poolchk.selectedProperty().setValue(false);
        if(rs.getString(11).equals("true"))
            balconychk.selectedProperty().setValue(true);
        else
            balconychk.selectedProperty().setValue(false);
        if(rs.getString(12).equals("true"))
            smokechk.selectedProperty().setValue(true);
        else
            smokechk.selectedProperty().setValue(false);
        if(rs.getString(13).equals("true"))
            tvchk.selectedProperty().setValue(true);
        else
            tvchk.selectedProperty().setValue(false);
        if(rs.getString(14).equals("true"))
            speakerschk.selectedProperty().setValue(true);
        else
            speakerschk.selectedProperty().setValue(false);


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
                "UPDATE realEstateItem SET street=? ,streetNum=?,city=?,maxPeople=?,size=?,kind=?,karoke=?,pool=?,balcony=?," +
                        "smoke=?,tv=?,speakers=? WHERE ID=?" );

        prep.setString(1, street.getText());
        prep.setString(2, stnum.getText());
        prep.setString(3, city.getText());
        prep.setString(4, maxppl.getText());
        prep.setString(5, size.getText());
        prep.setString(7,karaokechk.selectedProperty().getValue().toString());
        prep.setString(8,poolchk.selectedProperty().getValue().toString());
        prep.setString(9,balconychk.selectedProperty().getValue().toString());
        prep.setString(10,smokechk.selectedProperty().getValue().toString());
        prep.setString(11,tvchk.selectedProperty().getValue().toString());
        prep.setString(12,speakerschk.selectedProperty().getValue().toString());
        switch (kindofhome.getSelectionModel().getSelectedItem().toString())
        {
            case "סוג מתחם:": showAlertError("יש לבחור מתחם"); return;
            case "דירה":  prep.setString(6, "Apartment"); break;
            case "וילה":  prep.setString(6, "Villa"); break;
            case "אולם אירועים":  prep.setString(6, "Venue"); break;
            case "משרד":  prep.setString(6, "Office"); break;

        }
        prep.setString(13,Controller.selectedID);
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
