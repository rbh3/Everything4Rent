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

public class editPet {
    ObservableList<String> q1= FXCollections.observableArrayList("סוג חיית מחמד:", "כלב","חתול","אחר");
    ObservableList<String> q2= FXCollections.observableArrayList("מין:", "זכר","נקבה");
    public ChoiceBox petKind;
    public ChoiceBox petSex;
    public TextField petName;
    public TextField petAge;
    public TextField petDescription;
    public Button editit;
    Connection conn;

    @FXML
    public void initialize() throws Exception
    {
        petKind.setItems(q1);
        petSex.setItems(q2);
        petAge.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    petAge.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });


        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement prep = conn.prepareStatement(
                "SELECT * FROM PetItem WHERE ID=?");
        prep.setString(1,Controller.selectedID);
        ResultSet rs=prep.executeQuery();
        petKind.setValue(rs.getString(3));
        petSex.setValue(rs.getString(4));
        petName.setText(rs.getString(2));
        petAge.setText(rs.getString(5));
        petDescription.setText(rs.getString(6));

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
                "UPDATE PetItem SET petName=? ,kind=?,gender=?,age=?,description=? WHERE ID=?" );

        switch (petKind.getSelectionModel().getSelectedItem().toString())
        {
            case "סוג חיית מחמד:": showAlertError("יש לבחור סג חיית מחמד"); return;
            default:  prep.setString(2, petKind.getSelectionModel().getSelectedItem().toString()); break;
        }
        switch (petSex.getSelectionModel().getSelectedItem().toString())
        {
            case "מין:": showAlertError("יש לבחור מין"); return;
            default:  prep.setString(3, petSex.getSelectionModel().getSelectedItem().toString()); break;
        }
        prep.setString(1, petName.getText());
        prep.setString(4, petAge.getText());
        prep.setString(5, petDescription.getText());
        prep.setString(6, Controller.selectedID);

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
