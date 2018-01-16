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

public class addCar {
    ObservableList<String> car= FXCollections.observableArrayList("סוג רכב:", "אירופאי","קוריאני","יפני","אמריקאי","אחר");
    public ChoiceBox kindofcar;
    public Button addthis;
    public TextField model;
    public TextField color;
    public TextField year;
    public TextField name;
    public TextField desc;

    @FXML
    public void initialize()
    {
        kindofcar.setItems(car);
        kindofcar.setValue("סוג רכב:");

        year.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    year.setText(newValue.replaceAll("[^\\d]", ""));
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
        if(color.getText().isEmpty()|| year.getText().isEmpty() ||model.getText().isEmpty() || name.getText().isEmpty()
                || desc.getText().isEmpty()) {
            showAlertError("עליך למלא את כל השדות!!");
            return;
        }
        Stage stage = (Stage) addthis.getScene().getWindow();
        PreparedStatement prep = Additem.conn.prepareStatement(
                "INSERT into CarItem values (?,?,?,?,?,?,?,?) ;");
        prep.setString(1, "CAR"+Main.CarID);
        Main.CarID++;
        prep.setString(2, model.getText());
        prep.setString(3, color.getText());
        prep.setString(4, year.getText());
        prep.setString(6, name.getText());
        prep.setString(7, desc.getText());
        switch (kindofcar.getSelectionModel().getSelectedItem().toString())
        {
            case "סוג רכב:": showAlertError("יש לבחור סוג רכב"); return;
            default:prep.setString(5,kindofcar.getSelectionModel().getSelectedItem().toString());
        }

        prep.setString(8, ""+Main.PacketsForRentInd);
        prep.addBatch();

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
