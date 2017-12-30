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

import java.sql.*;

public class addhome {
    ObservableList<String> q1= FXCollections.observableArrayList("סוג מתחם:", "דירה","וילה","אולם אירועים","משרד");
    public ChoiceBox kindofhome;
    public Button addthis;
    public TextField street;
    public TextField stnum;
    public TextField city;
    public TextField maxppl;
    public TextField size;

    @FXML
    public void initialize()
    {
        kindofhome.setItems(q1);
        kindofhome.setValue("סוג מתחם:");
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

    }

    private void showAlertError(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void addnow() throws Exception
    {
        Stage stage = (Stage) addthis.getScene().getWindow();
        PreparedStatement prep = Additem.conn.prepareStatement(
                "INSERT into realEstateItem values (?,?,?,?,?,?,?,?) ;");

            prep.setString(2, street.getText());
            prep.setString(3, stnum.getText());
            prep.setString(4, city.getText());
            prep.setString(5, maxppl.getText());
            prep.setString(6, size.getText());
            switch (kindofhome.getSelectionModel().getSelectedItem().toString())
            {
                case "סוג מתחם:": showAlertError("יש לבחור מתחם"); return;
                case "דירה":  prep.setString(7, "Apartment"); break;
                case "וילה":  prep.setString(7, "Villa"); break;
                case "אולם אירועים":  prep.setString(7, "Venue"); break;
                case "משרד":  prep.setString(7, "Office"); break;

            }

            prep.setString(8, ""+Main.PacketsForRentInd);
            prep.addBatch();/*

            conn.setAutoCommit(false);
            prep.executeBatch();


            conn.setAutoCommit(true);*/
            Additem.query.add(prep);
            int i=Additem.items.getValue();
            i++;
            Additem.items.setValue(i);
            showAlertInfo("Item Added!");

            stage.close();

    }
    private void showAlertInfo(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }



}
