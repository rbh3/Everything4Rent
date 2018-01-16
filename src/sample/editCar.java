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

public class editCar {
    ObservableList<String> car= FXCollections.observableArrayList("סוג רכב:", "אירופאי","קוריאני","יפני","אמריקאי","אחר");
    public ChoiceBox kindofcar;
    public Button editit;
    public TextField model;
    public TextField color;
    public TextField year;
    public TextField name;
    public TextField desc;
  /*  public TextField street;
    public TextField stnum;
    public TextField city;
    public TextField maxppl;
    public TextField size;*/
    Connection conn;

    @FXML
    public void initialize() throws Exception
    {
        kindofcar.setItems(car);
        year.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    year.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });


        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement prep = conn.prepareStatement(
                "SELECT * FROM CarItem WHERE ID=?");
        prep.setString(1,Controller.selectedID);
        ResultSet rs=prep.executeQuery();
        model.setText(rs.getString(2));
        color.setText(rs.getString(3));
        year.setText(rs.getString(4));
        kindofcar.setValue(rs.getString(5));
        name.setText(rs.getString(6));
        desc.setText(rs.getString(7));

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
                "UPDATE CarItem SET Modle=? ,Color=?,Year=?,KindOfCar=?,Name=?,Description=? WHERE ID=? " );
        prep.setString(1, model.getText());
        prep.setString(2, color.getText());
        prep.setString(3, year.getText());
        prep.setString(4,kindofcar.getSelectionModel().getSelectedItem().toString());
        prep.setString(5, name.getText());
        prep.setString(6, desc.getText());

        prep.setString(7,Controller.selectedID);
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
