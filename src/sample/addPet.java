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

public class addPet {
    ObservableList<String> q1= FXCollections.observableArrayList("סוג חיית מחמד:", "כלב","חתול","אחר");
    ObservableList<String> q2= FXCollections.observableArrayList("מין:", "זכר","נקבה");
    public ChoiceBox petKind;
    public ChoiceBox petSex;
    public TextField petName;
    public TextField petAge;
    public TextField petDescription;
    public Button addthis;

    @FXML
    public void initialize()
    {
        petKind.setItems(q1);
        petKind.setValue("סוג חיית מחמד:");
        petSex.setItems(q2);
        petSex.setValue("מין:");
        petAge.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    petAge.setText(newValue.replaceAll("[^\\d]", ""));
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
        if(petName.getText().isEmpty() || petAge.getText().isEmpty() || petDescription.getText().isEmpty()) {
            showAlertError("עליך למלא את כל השדות!!");
            return;
        }
        Stage stage = (Stage) addthis.getScene().getWindow();
        PreparedStatement prep = Additem.conn.prepareStatement(
                "INSERT into PetItem values (?,?,?,?,?,?,?) ;");
        prep.setString(1,"PE"+Main.PetID);
        Main.PetID++;
        prep.setString(2, petName.getText());
        prep.setString(5, petAge.getText());
        prep.setString(6, petDescription.getText());

        switch (petKind.getSelectionModel().getSelectedItem().toString())
        {
            case "סוג חיית מחמד:": showAlertError("יש לבחור סוג"); return;
           default: prep.setString(3,petKind.getSelectionModel().getSelectedItem().toString() ); break;

        }
        switch (petSex.getSelectionModel().getSelectedItem().toString())
        {
            case "מין:": showAlertError("יש לבחור מין"); return;
            default:prep.setString(4, petSex.getSelectionModel().getSelectedItem().toString());break;

        }


            prep.setString(7, ""+Main.PacketsForRentInd);
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
