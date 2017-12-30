package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

/**
 * Created by Ravid on 27/12/2017.
 */
public class loginctr {

    @FXML
    public TextField usernamefld;
    public PasswordField passfld;
    public Button loginbtm;
    public Button reg;

    private void showAlertError(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
    }
    private void showAlertInfo(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }


    public void Login() throws Exception{
        if(usernamefld.getText().isEmpty()||passfld.getText().isEmpty()) {
            showAlertError("על כל השדות להיות מלאים");
            return;
        }
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement statement = conn.prepareStatement("SELECT password,fname FROM users WHERE userName =?");
        statement.setString(1, usernamefld.getText());
        ResultSet rs=statement.executeQuery();
        String name=null;
        if(!rs.next()) {
            showAlertError("שם משתמש לא קיים");
            usernamefld.clear();
            passfld.clear();
            statement.clearParameters();
            return;
        }
        else
        {
            name=rs.getString("fname");
            String orig=rs.getString("password");
            if(!orig.equals(passfld.getText()))
            {
                showAlertError("סיסמא שגויה");
                usernamefld.clear();
                passfld.clear();
                return;
            }
            statement.clearParameters();
        }
        statement.clearParameters();
        conn.close();

        Main.setUsername(usernamefld.getText());
        showAlertInfo(name+" is connected");

        Stage stage = (Stage) usernamefld.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    public void reg() throws IOException
    {
        Stage s = new Stage();
        s.setTitle("Register");
        AnchorPane root = FXMLLoader.load(getClass().getResource("../sample/register.fxml"));
        Scene scene = new Scene(root, 600, 665);
        scene.getStylesheets().add(getClass().getResource("b.css").toExternalForm());
        //styiling
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
        Stage stage = (Stage) usernamefld.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
