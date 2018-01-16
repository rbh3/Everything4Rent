package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.ArrayList;

public class Additem {
    public ChoiceBox policy;
    public TextField relaible;
    public Label cancellbl;
    public TextField cancelfld;
    public CheckBox cancelcheck;
    public Label packnum;
    public Button addhome;
    public Button add;
    public TextField pricefld;
    public Label depositlbl;
    public TextField depositefld;
    public CheckBox toTrade;
    public TextField pName;
    public TextField partielPrice;

    public static IntegerProperty items;
    public static ArrayList<PreparedStatement> query;
    public static Connection conn;
    ObservableList<String> q1= FXCollections.observableArrayList("מדיניות בחירת שוכר:", "גישת כל הקודם זוכה- עדיפות למשתמש הראשון","הגישה השמרנית- עדיפות לפי אמינות","הגישה הבטוחה-72 שעות לבחירת המשתמש");

    @FXML
    public void initialize() throws Exception
    {
        pricefld.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    pricefld.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });



        query=new ArrayList();
        items=new SimpleIntegerProperty(0);
        policy.setItems(q1);
        policy.setValue("מדיניות בחירת שוכר:");
        relaible.visibleProperty().bind( policy.getSelectionModel().selectedItemProperty().isEqualTo("הגישה השמרנית- עדיפות לפי אמינות"));
        cancelfld.visibleProperty().bind(cancelcheck.selectedProperty());
        cancellbl.visibleProperty().bind(cancelcheck.selectedProperty());
        packnum.textProperty().bind(items.asString());
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(pricefld.textProperty());
            }
            @Override
            protected boolean computeValue() {
                return (pricefld.getText().equals("0"));}
        };
        depositlbl.visibleProperty().bind(bb);
        depositefld.visibleProperty().bind(bb);
        partielPrice.visibleProperty().bind(toTrade.selectedProperty());


    }
    public void homewin() throws Exception
    {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        Stage s = new Stage();
        s.setTitle("Add Realestate");
        AnchorPane root = FXMLLoader.load(getClass().getResource("../sample/addHome.fxml"));
        Scene scene = new Scene(root, 600, 665);
        scene.getStylesheets().add(getClass().getResource("b.css").toExternalForm());
        //styiling
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
    }

    //adding car
    public void addCar() throws Exception
    {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        Stage s = new Stage();
        s.setTitle("Add Car");
        AnchorPane root = FXMLLoader.load(getClass().getResource("../sample/addCar.fxml"));
        Scene scene = new Scene(root, 600, 665);
        scene.getStylesheets().add(getClass().getResource("b.css").toExternalForm());
        //styiling
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
    }
    //adding new pet
    public void addPet() throws Exception
    {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        Stage s = new Stage();
        s.setTitle("Add Pet");
        AnchorPane root = FXMLLoader.load(getClass().getResource("../sample/addPet.fxml"));
        Scene scene = new Scene(root, 600, 665);
        scene.getStylesheets().add(getClass().getResource("b.css").toExternalForm());
        //styiling
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
    }
    //adding new secondHand
    public void addSecondHand() throws Exception
    {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        Stage s = new Stage();
        s.setTitle("Add SecondHand");
        AnchorPane root = FXMLLoader.load(getClass().getResource("../sample/AddSecondHand.fxml"));
        Scene scene = new Scene(root, 600, 665);
        scene.getStylesheets().add(getClass().getResource("b.css").toExternalForm());
        //styiling
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
    }

    public void addpckg() throws Exception
    {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        if(pricefld.getText().isEmpty()) {
            showAlertError("עליך לקבוע מחיר להשכרה או 0 להשאלה או החלפה!!");
            conn.close();
            return;
        }
        if(items.getValue().equals(0))
        {
            showAlertError("לא ניתן להוסיף חבילה ריקה");
            conn.close();
            return;
        }
        if(pName.getText().isEmpty())
        {
            showAlertError("שם חבילה הוא שדה חובה");
            conn.close();
            return;
        }
        Stage stage = (Stage) cancelfld.getScene().getWindow();
        PreparedStatement prep = conn.prepareStatement(
                "insert into Packets values (?,?,?,?,?,?,?,?,?,?,?,?);");
        prep.setString(1,""+Main.PacketsForRentInd);
        Main.PacketsForRentInd++;
        prep.setString(2, pName.getText());
        prep.setString(3, items.getValue().toString());
        prep.setString(4, pricefld.getText());
        if(cancelcheck.isSelected())
            prep.setString(5, "Yes");
        else
            prep.setString(5, "No");
        prep.setString(6, cancelfld.getText());
        switch (policy.getSelectionModel().getSelectedItem().toString())
        {
            case "מדיניות בחירת שוכר:": showAlertError("יש לבחור גישת בחירה למשתמשים"); conn.close(); return;
            case "גישת כל הקודם זוכה- עדיפות למשתמש הראשון":  prep.setString(7, "First"); break;
            case "הגישה השמרנית- עדיפות לפי אמינות":  prep.setString(7, "Rate"); break;
            case "הגישה הבטוחה-72 שעות לבחירת המשתמש":  prep.setString(7, "Safe"); break;

        }
        prep.setString(8, relaible.getText());
        prep.setString(9, Main.name.getValue().substring(6));
        prep.setString(10, depositefld.getText());
        if(toTrade.isSelected())
            prep.setString(11, "Yes");
        else
            prep.setString(11, "No");
        prep.setString(12,partielPrice.getText());
        conn.setAutoCommit(false);
        prep.execute();
        conn.commit();
        for(int i=0;i<query.size();i++)
        {
            query.get(i).executeBatch();
            conn.commit();
        }
        conn.setAutoCommit(true);
        ObjectOutputStream writer = null;
        try {
            writer = new ObjectOutputStream(new FileOutputStream("./src/num"));
            writer.writeObject(Main.PacketsForRentInd);
            writer.writeObject(Main.RealEstateID);
            writer.writeObject(Main.CarID);
            writer.writeObject(Main.PetID);
            writer.writeObject(Main.elec);
            writer.writeObject(Main.furn);
            writer.writeObject(Main.cell);
            writer.writeObject(Main.secHans);
            writer.writeObject(Main.Orders);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }

        conn.setAutoCommit(true);
        showAlertInfo("Package Added!");
        conn.close();
        stage.close();

    }
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

}
