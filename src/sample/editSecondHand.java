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

public class editSecondHand {
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
    Connection conn;
    public Button editit;
    @FXML
    public void initialize() throws Exception
    {
        category.setDisable(true);
        category.setItems(q1);
        category.setStyle("-fx-opacity: 1");
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:test.db");

        if (Controller.selectedID.startsWith("EL")) // electric
        {
            PreparedStatement prep = conn.prepareStatement(
                    "SELECT * FROM electricItem WHERE ID=?");
            prep.setString(1,Controller.selectedID);
            ResultSet rs=prep.executeQuery();
            name.setText(rs.getString(2));
            date.setText(rs.getString(3));
            desc.setText(rs.getString(4));
            category.setValue("מוצרי חשמל");
            Txt1.setVisible(true);
            label1.setVisible(true);
            Txt2.setVisible(false);
            label2.setVisible(false);
            Txt3.setVisible(false);
            label3.setVisible(false);
            label1.setText("יצרן");
            Txt1.setText(rs.getString(5));
        }
        else if (Controller.selectedID.startsWith("FU")) //furniture
        {
            PreparedStatement prep = conn.prepareStatement(
                    "SELECT * FROM furnitureItem WHERE ID=?");
            prep.setString(1,Controller.selectedID);
            ResultSet rs=prep.executeQuery();
            name.setText(rs.getString(2));
            date.setText(rs.getString(3));
            desc.setText(rs.getString(4));
            category.setValue("רהיטים");
            Txt1.setVisible(true);
            Txt2.setVisible(true);
            Txt3.setVisible(true);
            label1.setText("צבע");
            label1.setVisible(true);
            label2.setText("רוחב");
            label2.setVisible(true);
            label3.setText("משקל");
            label3.setVisible(true);
            Txt1.setText(rs.getString(5));
            Txt2.setText(rs.getString(6));
            Txt3.setText(rs.getString(7));
        }

        else if (Controller.selectedID.startsWith("CE")) //cellular
        {
            PreparedStatement prep = conn.prepareStatement(
                    "SELECT * FROM cellolarItem WHERE ID=?");
            prep.setString(1,Controller.selectedID);
            ResultSet rs=prep.executeQuery();
            name.setText(rs.getString(2));
            date.setText(rs.getString(3));
            desc.setText(rs.getString(4));
            category.setValue("סלולרי");
            Txt1.setVisible(true);
            Txt2.setVisible(true);
            Txt3.setVisible(true);
            label1.setText("יצרן");
            label1.setVisible(true);
            label2.setText("דגם");
            label2.setVisible(true);
            label3.setText("זיכרון");
            label3.setVisible(true);
            Txt1.setText(rs.getString(5));
            Txt2.setText(rs.getString(6));
            Txt3.setText(rs.getString(7));
        }
        else if (Controller.selectedID.startsWith("SH")) //secondHand
        {
            PreparedStatement prep = conn.prepareStatement(
                    "SELECT * FROM secondHandItem WHERE ID=?");
            prep.setString(1,Controller.selectedID);
            ResultSet rs=prep.executeQuery();
            name.setText(rs.getString(2));
            date.setText(rs.getString(3));
            desc.setText(rs.getString(4));
            category.setValue("אחר");
            Txt1.setVisible(false);
            Txt2.setVisible(false);
            Txt3.setVisible(false);
            label1.setVisible(false);
            label2.setVisible(false);
            label3.setVisible(false);

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

        if (Controller.selectedID.startsWith("EL")) // electric
        {
            PreparedStatement prep = conn.prepareStatement(
                    "UPDATE electricItem SET name=? ,date=? ,desc =? ,manufacture=? WHERE ID=?" );

            prep.setString(1, name.getText());
            prep.setString(2, date.getText());
            prep.setString(3, desc.getText());
            prep.setString(4, Txt1.getText());
            prep.setString(5, Controller.selectedID);
            prep.addBatch();
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
        }
        if (Controller.selectedID.startsWith("FU")) // furniture
        {
            PreparedStatement prep = conn.prepareStatement(
                    "UPDATE furnitureItem SET name=? ,date=? ,desc =? ,color=?,length=?,weight=? WHERE ID=?" );

            prep.setString(1, name.getText());
            prep.setString(2, date.getText());
            prep.setString(3, desc.getText());
            prep.setString(4, Txt1.getText());
            prep.setString(5, Txt2.getText());
            prep.setString(6, Txt3.getText());
            prep.setString(7, Controller.selectedID);
            prep.addBatch();
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
        }
        if (Controller.selectedID.startsWith("CE")) // cellular
        {
            PreparedStatement prep = conn.prepareStatement(
                    "UPDATE cellolarItem SET name=? ,date=? ,desc =? ,manufacture=?,model=?,memory=? WHERE ID=?" );

            prep.setString(1, name.getText());
            prep.setString(2, date.getText());
            prep.setString(3, desc.getText());
            prep.setString(4, Txt1.getText());
            prep.setString(5, Txt2.getText());
            prep.setString(6, Txt3.getText());
            prep.setString(7, Controller.selectedID);
            prep.addBatch();
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
        }
        if (Controller.selectedID.startsWith("SH")) // electric
        {
            PreparedStatement prep = conn.prepareStatement(
                    "UPDATE secondHandItem SET name=? ,date=? ,desc =? WHERE ID=?" );

            prep.setString(1, name.getText());
            prep.setString(2, date.getText());
            prep.setString(3, desc.getText());
            prep.setString(4, Controller.selectedID);
            prep.addBatch();
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
        }

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
