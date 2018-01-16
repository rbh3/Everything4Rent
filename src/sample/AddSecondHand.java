package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class AddSecondHand {
    ObservableList<String> q1= FXCollections.observableArrayList("קטגוריה:", "מוצרי חשמל","רהיטים","סלולרי","אחר");
    public TextField name;
    public TextField date;
    public TextField desc;
    public ComboBox category;
    public Label label1;
    public TextField Txt1;
    public Label label2;
    public TextField Txt2;
    public Label label3;
    public TextField Txt3;
    public Button addthis;


    @FXML
    public void initialize()
    {
        category.setItems(q1);
        category.setValue("קטגוריה:");

        label1.setVisible(false);
        Txt1.setVisible(false);
        label2.setVisible(false);
        Txt2.setVisible(false);
        label3.setVisible(false);
        Txt3.setVisible(false);

    }

    public void SearchKind()
    {
        if(category.getSelectionModel().getSelectedItem().toString().equals("מוצרי חשמל")) {
           label1.setText("יצרן");
           label1.setVisible(true);
           Txt1.setVisible(true);
            label2.setVisible(false);
            Txt2.setVisible(false);
            label3.setVisible(false);
            Txt3.setVisible(false);

        }
        else if(category.getSelectionModel().getSelectedItem().toString().equals("רהיטים")) {
            label1.setText("צבע");
            label1.setVisible(true);
            Txt1.setVisible(true);
            label2.setText("אורך");
            label2.setVisible(true);
            Txt2.setVisible(true);
            label3.setText("רוחב");
            label3.setVisible(true);
            Txt3.setVisible(true);

        }
        else if(category.getSelectionModel().getSelectedItem().toString().equals("סלולרי")){
            label1.setText("יצרן");
            label1.setVisible(true);
            Txt1.setVisible(true);
            label2.setText("דגם");
            label2.setVisible(true);
            Txt2.setVisible(true);
            label3.setText("זיכרון");
            label3.setVisible(true);
            Txt3.setVisible(true);
        }
        else
        {
            label1.setVisible(false);
            Txt1.setVisible(false);
            label2.setVisible(false);
            Txt2.setVisible(false);
            label3.setVisible(false);
            Txt3.setVisible(false);
        }

    }

    private void showAlertError(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void addnow() throws Exception {
        if (category.getSelectionModel().getSelectedItem().toString().equals("קטגוריה:"))
            showAlertError("יש לבחור קטגוריה");
        if (category.getSelectionModel().getSelectedItem().toString().equals("מוצרי חשמל")) {
            if (Txt1.getText().isEmpty() || name.getText().isEmpty() || date.getText().isEmpty() || desc.getText().isEmpty()) {
                showAlertError("עליך למלא את כל השדות!!");
                return;
            }
            Stage stage = (Stage) addthis.getScene().getWindow();
            PreparedStatement prep = Additem.conn.prepareStatement(
                    "INSERT into electricItem values (?,?,?,?,?,?) ;");
            prep.setString(1,"EL"+Main.secHans);
            Main.secHans++;
            prep.setString(2,name.getText());
            prep.setString(3,date.getText());
            prep.setString(4,desc.getText());
            prep.setString(5, Txt1.getText());
            prep.setString(6, "" + Main.PacketsForRentInd);
            prep.addBatch();
            Additem.query.add(prep);
            int i = Additem.items.getValue();
            i++;
            Additem.items.setValue(i);
            showAlertInfo("Item Added!");

            stage.close();
        }

        if (category.getSelectionModel().getSelectedItem().toString().equals("רהיטים")) {
            if (Txt1.getText().isEmpty() || Txt2.getText().isEmpty() || Txt3.getText().isEmpty() || name.getText().isEmpty() || date.getText().isEmpty() || desc.getText().isEmpty()) {
                showAlertError("עליך למלא את כל השדות!!");
                return;
            }
            Stage stage = (Stage) addthis.getScene().getWindow();
            PreparedStatement prep = Additem.conn.prepareStatement(
                    "INSERT into furnitureItem values (?,?,?,?,?,?,?,?) ;");
            prep.setString(1,"FU"+Main.furn);
            Main.furn++;
            prep.setString(2,name.getText());
            prep.setString(3,date.getText());
            prep.setString(4,desc.getText());
            prep.setString(5, Txt1.getText());
            prep.setString(6, Txt2.getText());
            prep.setString(7, Txt3.getText());



            prep.setString(8, "" + Main.PacketsForRentInd);
            prep.addBatch();
            Additem.query.add(prep);
            int i = Additem.items.getValue();
            i++;
            Additem.items.setValue(i);
            showAlertInfo("Item Added!");

            stage.close();
        }
        if (category.getSelectionModel().getSelectedItem().toString().equals("סלולרי")) {
            if (Txt1.getText().isEmpty() || Txt2.getText().isEmpty() || Txt3.getText().isEmpty() || name.getText().isEmpty() || date.getText().isEmpty() || desc.getText().isEmpty()) {
                showAlertError("עליך למלא את כל השדות!!");
                return;
            }
            Stage stage = (Stage) addthis.getScene().getWindow();
            PreparedStatement prep = Additem.conn.prepareStatement(
                    "INSERT into cellolarItem values (?,?,?,?,?,?,?,?) ;");
            prep.setString(1,"CE"+Main.cell);
            Main.cell++;
            prep.setString(2,name.getText());
            prep.setString(3,date.getText());
            prep.setString(4,desc.getText());
            prep.setString(5, Txt1.getText());
            prep.setString(6, Txt2.getText());
            prep.setString(7, Txt3.getText());



            prep.setString(8, "" + Main.PacketsForRentInd);
            prep.addBatch();
            Additem.query.add(prep);
            int i = Additem.items.getValue();
            i++;
            Additem.items.setValue(i);
            showAlertInfo("Item Added!");

            stage.close();
        }

        if (category.getSelectionModel().getSelectedItem().toString().equals("אחר")) {
            if ( name.getText().isEmpty() || date.getText().isEmpty() || desc.getText().isEmpty()) {
                showAlertError("עליך למלא את כל השדות!!");
                return;
            }
            Stage stage = (Stage) addthis.getScene().getWindow();
            PreparedStatement prep = Additem.conn.prepareStatement(
                    "INSERT into secondHandItem values (?,?,?,?,?) ;");
            prep.setString(1,"SH"+Main.secHans);
            Main.secHans++;
            prep.setString(2,name.getText());
            prep.setString(3,date.getText());
            prep.setString(4,desc.getText());



            prep.setString(5, "" + Main.PacketsForRentInd);
            prep.addBatch();
            Additem.query.add(prep);
            int i = Additem.items.getValue();
            i++;
            Additem.items.setValue(i);
            showAlertInfo("Item Added!");

            stage.close();
        }
    }
    private void showAlertInfo(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }



}
