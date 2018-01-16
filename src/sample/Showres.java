package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.mail.FetchProfile;
import java.sql.*;
import java.util.HashMap;


public class Showres {
    private HashMap<String,Packet> packages;
    @FXML
    public ListView resList;
    public Label ownerName;
    public TextField pricefld;
    public ComboBox ItemList;
    public Button viewProd;
    public Label depositLbl;
    public Label depositeNum;
    public Label tradelbl;
    public Label parcielPrice;
    public Button lown;
    public  Button trade;
    public static ObservableList<String> forTrade= FXCollections.observableArrayList();
    public static Packet selecpak;
    public static Stage stage;
    @FXML
    public void initialize() throws Exception {
        if(Search.ans.size()==0)
        {
            showAlertInfo("אין תוצאות");
            Stage s=(Stage)trade.getScene().getWindow();
            s.close();
            return;
        }
        packages=new HashMap<>();
        for (Packet pac : Search.ans) {
            String name = pac.getpName();
            resList.getItems().add(name);
            packages.put(name, pac);

        }
        tradelbl.setVisible(false);
        parcielPrice.setVisible(false);
        trade.setVisible(false);

    }

    public void choosePack(MouseEvent mouseEvent) {
        stage = (Stage) lown.getScene().getWindow();
        if(resList.getSelectionModel().getSelectedItem()==null)
            return;
        ObservableList<String> ans=FXCollections.observableArrayList();
        pricefld.setEditable(false);
        tradelbl.setVisible(false);
        parcielPrice.setVisible(false);
        depositeNum.setVisible(false);
        depositLbl.setVisible(false);
        trade.setVisible(false);
        if(resList.getSelectionModel()==null)
            return;
        Packet p=packages.get(resList.getSelectionModel().getSelectedItem().toString());
        selecpak=p;
        ownerName.setText(p.getOwnerName());
        pricefld.setText(""+p.getPrice());
        if(p.isLent())
        {
            depositLbl.setVisible(true);
            depositeNum.setText(""+p.getDeposite());
            depositeNum.setVisible(true);
        }
        else
            trade.setVisible(false);
        if(p.isTrade()) {
            tradelbl.setVisible(true);
            parcielPrice.setText(""+p.getPartielPrice());
            parcielPrice.setVisible(true);
            trade.setVisible(true);
        }
        else
            trade.setVisible(false);
        ans.addAll(p.getRealEstate());
        ans.addAll(p.getCars());
        ans.addAll(p.getYad2());
        ans.addAll(p.getPets());

        ItemList.setItems(ans);
    }

    public void viewProd() throws Exception
    {
        if(resList.getSelectionModel().getSelectedItem()==null)
            return;
        Packet p=packages.get(resList.getSelectionModel().getSelectedItem().toString());
        String s= ItemList.getSelectionModel().getSelectedItem().toString();
        String desc=p.getItem(s.split(" ")[1]);
        showAlertInfo(desc);
    }
    private void showAlertError(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
    }

    private void showAlertInfo(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product Details");
        alert.setHeaderText("Product Details");
        alert.setContentText(alertMessage);
        alert.show();
    }

    private void showMy(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Success");
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void order() throws Exception {
        if(Main.isloogedout.getValue()) {
            showAlertError("כדי לבצע פעולה זו, עליך להתחבר");
            return;
        }
        Packet p=packages.get(resList.getSelectionModel().getSelectedItem().toString());
        if(p.getUserName().equals(Main.name.getValue().substring(6)))
        {
            showAlertError("לא ניתן להשכיר חבילה שבבעלותך");
            return;
        }
        if(p.getPolicy().equals("First"))
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            PreparedStatement prep = conn.prepareStatement(
                    "INSERT into Orders values (?,?,?,?,?,?) ;");
            prep.setString(1,"Order"+Main.Orders);
            Main.Orders++;
            prep.setString(2,Search.dateStart.toString());
            prep.setString(3,Search.dateEnd.toString());
            prep.setString(4,"APPROVED");
            prep.setString(5,p.getUserName());
            prep.setString(6,p.getID());
            prep.execute();
            //conn.commit();
            conn.close();
            showMy("ההזמנה אושרה");
        }
        else
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            PreparedStatement prep = conn.prepareStatement(
                    "INSERT into Orders values (?,?,?,?,?,?) ;");
            prep.setString(1,"Order"+Main.Orders);
            Main.Orders++;
            prep.setString(2,Search.dateStart.toString());
            prep.setString(3,Search.dateEnd.toString());
            prep.setString(4,"WAITING");
            prep.setString(5,p.getUserName());
            prep.setString(6,p.getID());
            prep.execute();
            //conn.commit();
            conn.close();
            showMy("ההזמנה הועברה לאישור המשכיר");
        }


    }

    public void trade() throws Exception
    {
        forTrade.clear();
        if(Main.isloogedout.getValue()) {
            showAlertError("כדי לבצע פעולה זו, עליך להתחבר");
            return;
        }
        Packet p=packages.get(resList.getSelectionModel().getSelectedItem().toString());
        if(p.getUserName().equals(Main.name.getValue().substring(6)))
        {
            showAlertError("לא ניתן להחליף חבילה שבבעלותך");
            return;
        }
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement prep = conn.prepareStatement(
                "SELECT ID,pName FROM Packets WHERE userName=? AND toTrade='Yes' AND " +
                        "ID NOT IN (SELECT PackageID from Orders WHERE Orders.endDate>=? AND ?>=Orders.startDate AND Status='APPROVED') ORDER BY Packets.ID");
        prep.setString(1, Main.name.getValue().substring(6));
        prep.setString(2,Search.dateStart.toString());
        prep.setString(3,Search.dateEnd.toString());
        ResultSet rs=prep.executeQuery();
        while(rs.next())
        {
            String s=rs.getString(1)+"-"+rs.getString(2);
            if(!forTrade.contains(s)) {
                forTrade.add(s);
            }
        }
        if(forTrade.size()==0)
        {
            showAlertError("אין בבעלותך חבילות פנויות להחלפה בתאריכים אלו");
            return;
        }
        conn.close();
        Stage s = new Stage();
        s.setTitle("ChoosePack");
        AnchorPane root = FXMLLoader.load(getClass().getResource("../sample/trade.fxml"));
        Scene scene = new Scene(root, 400, 400);
        scene.getStylesheets().add(getClass().getResource("b.css").toExternalForm());
        //styiling
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
    }

}
