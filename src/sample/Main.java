package sample;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ibex.nestedvm.util.Seekable;

import java.io.*;
import java.nio.file.Files;
import java.sql.SQLException;

public class Main extends Application {

    public static Button login;
    public static Button logout;
    public Menu hello;
    public MenuItem li;
    public MenuItem lo;
    public Menu crud;

    public static Integer PacketsForRentInd=0;
    public static Integer RealEstateID=0;
    public static Integer PetID=0;
    public static Integer CarID=0;
    public static Integer elec=0;
    public static Integer furn=0;
    public static Integer cell=0;
    public static Integer secHans=0;
    public static Integer Orders=0;


    public static Stage primeryS;
    static BooleanProperty isloogedin=new SimpleBooleanProperty(false);
    static BooleanProperty isloogedout=new SimpleBooleanProperty(true);
    static StringProperty name=new SimpleStringProperty("Hello Guest");

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("EveryThing4Rent");
        primeryS = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("sample.fxml").openStream());
        //styleing
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> FullExit());
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream("./src/num"));
            PacketsForRentInd=(int) in.readObject();
            RealEstateID=(int) in.readObject();
            PetID=(int) in.readObject();
            CarID=(int) in.readObject();
            elec=(int) in.readObject();
            furn=(int) in.readObject();
            cell=(int) in.readObject();
            secHans=(int) in.readObject();
            Orders=(int) in.readObject();
        } catch (Exception e) {
            return;
        }
        in.close();
    }

    public void loginwin() throws IOException
    {
        hello.textProperty().bind(name);
        li.disableProperty().bind(isloogedin);
        lo.disableProperty().bind(isloogedout);
        crud.disableProperty().bind(isloogedout);
        Stage s = new Stage();
        s.setTitle("Login");
        AnchorPane root = FXMLLoader.load(getClass().getResource("../sample/login.fxml"));
        Scene scene = new Scene(root, 378, 345);
        scene.getStylesheets().add(getClass().getResource("bksmall.css").toExternalForm());
        //styiling
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
        s.setOnCloseRequest(event -> s.close());

    }

    public void addwin() throws IOException
    {
        Stage s = new Stage();
        s.setTitle("Add Package for Rent");
        AnchorPane root = FXMLLoader.load(getClass().getResource("../sample/Additem.fxml"));
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("b.css").toExternalForm());
        //styiling
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
        s.setOnCloseRequest(event ->s.close());
    }

    public void Searchwin() throws IOException
    {
        Stage s = new Stage();
        s.setTitle("Search");
        AnchorPane root = FXMLLoader.load(getClass().getResource("../sample/Search.fxml"));
        Scene scene = new Scene(root, 447, 358);
        scene.getStylesheets().add(getClass().getResource("b.css").toExternalForm());
        //styiling
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
        s.setOnCloseRequest(event ->s.close());
    }
    public void logout() throws IOException
    {
        showAlertInfo(name.getValue().substring(6)+" is disconnected");
        name.setValue("Hello Guest");
        isloogedin.setValue(false);
        isloogedout.setValue(true);

    }
    private void showAlertInfo(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void FullExit()
    {
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
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    public static void setUsername(String username) {
        name.setValue("Hello "+username);
        isloogedin.setValue(true);
        isloogedout.setValue(false);
    }

    public void myPack() throws IOException
    {
        Stage s = new Stage();
        s.setTitle("CRUD Product");
        AnchorPane root = FXMLLoader.load(getClass().getResource("../sample/MyPackages.fxml"));
        Scene scene = new Scene(root, 641, 500);
        scene.getStylesheets().add(getClass().getResource("b.css").toExternalForm());
        //styiling
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
        s.setOnCloseRequest(event -> s.close());

    }






}
