package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class Search {
    static BooleanProperty isRealEstate = new SimpleBooleanProperty(false);
    public static ArrayList<Packet> ans;
    @FXML
    DatePicker datePickerStart;
    @FXML
    DatePicker datePickerEnd;
    public CheckBox karaokechk;
    public CheckBox poolchk;
    public CheckBox balconychk;
    public CheckBox smokechk;
    public CheckBox tvchk;
    public CheckBox speakerschk;
    public ComboBox kind;
    public ComboBox combo1;
    public TextField ftxt;
    public TextField price;

    public static Date dateStart;
    public static Date dateEnd;
    public static Stage stage;

    ObservableList<String> q1 = FXCollections.observableArrayList("קטגוריה:", "נדל\"ן", "רכב", "מוצר יד שנייה", "חיות מחמד");
    ObservableList<String> rel = FXCollections.observableArrayList("סוג מתחם:", "דירה", "וילה", "אולם אירועים", "משרד");
    ObservableList<String> anim = FXCollections.observableArrayList("סוג חיה:", "כלב", "חתול", "אחר");
    ObservableList<String> car = FXCollections.observableArrayList("סוג רכב:", "אירופאי", "קוריאני", "יפני", "אמריקאי", "אחר");
    ObservableList<String> yad2 = FXCollections.observableArrayList("סוג מוצר:", "מוצרי חשמל", "ריהוט", "סלולר", "אחר");

    @FXML
    public void initialize() {
        kind.setItems(q1);
        kind.getSelectionModel().selectFirst();
        //equipment
        karaokechk.visibleProperty().bind(kind.getSelectionModel().selectedItemProperty().isEqualTo("נדל\"ן"));
        poolchk.visibleProperty().bind(kind.getSelectionModel().selectedItemProperty().isEqualTo("נדל\"ן"));
        balconychk.visibleProperty().bind(kind.getSelectionModel().selectedItemProperty().isEqualTo("נדל\"ן"));
        smokechk.visibleProperty().bind(kind.getSelectionModel().selectedItemProperty().isEqualTo("נדל\"ן"));
        tvchk.visibleProperty().bind(kind.getSelectionModel().selectedItemProperty().isEqualTo("נדל\"ן"));
        speakerschk.visibleProperty().bind(kind.getSelectionModel().selectedItemProperty().isEqualTo("נדל\"ן"));
        ftxt.visibleProperty().bind(kind.getSelectionModel().selectedItemProperty().isNotEqualTo("מוצר יד שנייה"));


        Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        datePickerStart.setDayCellFactory(dayCellFactory);
        datePickerEnd.setDayCellFactory(dayCellFactory);
    }

    public void SearchKind() {
        if (kind.getSelectionModel().getSelectedItem().toString().equals("נדל\"ן")) {
            combo1.setItems(rel);
            combo1.getSelectionModel().selectFirst();
            ftxt.setPromptText("כמות אנשים מקס'");
        } else if (kind.getSelectionModel().getSelectedItem().toString().equals("רכב")) {
            combo1.setItems(car);
            combo1.getSelectionModel().selectFirst();
            ftxt.setPromptText("שנת ייצור מינימלית");
        } else if (kind.getSelectionModel().getSelectedItem().toString().equals("מוצר יד שנייה")) {
            combo1.setItems(yad2);
            combo1.getSelectionModel().selectFirst();
        } else if (kind.getSelectionModel().getSelectedItem().toString().equals("חיות מחמד")) {
            combo1.setItems(anim);
            combo1.getSelectionModel().selectFirst();
            ftxt.setPromptText("גיל מקסימלי לחיה");
        }
        stage = (Stage) ftxt.getScene().getWindow();
    }

    private void showAlertError(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void search() throws Exception {
        dateStart =Date.valueOf(datePickerStart.getValue());
        dateEnd =Date.valueOf(datePickerEnd.getValue());
        String OVER=" And Packets.ID NOT IN (SELECT PackageID from Orders WHERE Orders.endDate>=\'"+dateStart+"\' AND \'"+dateEnd+"\'>=Orders.startDate AND Status=\'APPROVED\')";
        if(datePickerEnd==null || datePickerStart==null)
        {
            showAlertError("יש למלא את התאריכים");
            return;
        }

        if(dateStart.after(dateEnd))
        {
            showAlertError("תאריך עזיבה צריך להיות אחרי תאריך הגעה");
            return;
        }
        ans = new ArrayList<>();
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        if (kind.getSelectionModel().getSelectedItem().toString().equals("נדל\"ן")) {
            String s = "SELECT DISTINCT Packets.ID,fname,lname,pName,price,Packets.userName,Deposite,toTrade,partielPrice,Packets.Policy FROM Packets JOIN realEstateItem ON realEstateItem.PackageID = Packets.ID JOIN users ON users.userName=Packets.userName WHERE " +
                    "realEstateItem.maxPeople>=? AND Packets.price<=?";
            if (karaokechk.isSelected())
                s += " AND realEstateItem.karoke=\'Yes\'";
            if (poolchk.isSelected())
                s += " AND realEstateItem.pool=\'Yes\'";
            if (balconychk.isSelected())
                s += " AND realEstateItem.balcony=\'Yes\'";
            if (smokechk.isSelected())
                s += " AND realEstateItem.smoke=\'Yes\'";
            if (tvchk.isSelected())
                s += " AND realEstateItem.tv=\'Yes\'";
            if (speakerschk.isSelected())
                s += " AND realEstateItem.speakers=\'Yes\'";
            if (!combo1.getSelectionModel().getSelectedItem().toString().equals("סוג מתחם:"))
                s += " AND realEstateItem.kind=?";
            s+=OVER;
            PreparedStatement prep = conn.prepareStatement(s);
            switch (combo1.getSelectionModel().getSelectedItem().toString()) {
                case "דירה":
                    prep.setString(3, "Apartment");
                    break;
                case "וילה":
                    prep.setString(3, "Villa");
                    break;
                case "אולם אירועים":
                    prep.setString(3, "Venue");
                    break;
                case "משרד":
                    prep.setString(3, "Office");
                    break;
            }
            if (ftxt.getText().isEmpty())
                prep.setString(1, "" + 0);
            else
                prep.setString(1, ftxt.getText());
            if (price.getText().isEmpty())
                prep.setString(2, "" + 0);
            else
                prep.setString(2, price.getText());
            // prep.addBatch();
            ResultSet rs = prep.executeQuery();
            InsertPacket(rs,conn);
        }

        if (kind.getSelectionModel().getSelectedItem().toString().equals("רכב")) {
            String s = "SELECT DISTINCT Packets.ID,fname,lname,pName,price,Packets.userName,Deposite,toTrade,partielPrice,Packets.Policy FROM Packets JOIN CarItem ON CarItem.PackageID = Packets.ID JOIN users ON users.userName=Packets.userName WHERE " +
                    "CarItem.Year>=? AND Packets.price<=?";
            if(!combo1.getSelectionModel().getSelectedItem().toString().equals("סוג רכב:"))
                s+= " AND CarItem.Modle=?";
            s+=OVER;
            PreparedStatement prep = conn.prepareStatement(s);
            if(!combo1.getSelectionModel().getSelectedItem().toString().equals("סוג רכב:"))
                    prep.setString(3, combo1.getSelectionModel().getSelectedItem().toString());

            if(ftxt.getText().isEmpty())
                prep.setString(1, ""+0);
            else
                prep.setString(1, ftxt.getText());
            if(price.getText().isEmpty())
                prep.setString(2, ""+0);
            else
                prep.setString(2, price.getText());
            // prep.addBatch();
            ResultSet rs=prep.executeQuery();
            InsertPacket(rs,conn);

        }

        if (kind.getSelectionModel().getSelectedItem().toString().equals("מוצר יד שנייה")) {
            String s="";
            switch (combo1.getSelectionModel().getSelectedItem().toString())
            {
                case "סוג מוצר:": showAlertError("יש לבחור סוג מוצר"); return;
                case "מוצרי חשמל": s = "SELECT DISTINCT Packets.ID,fname,lname,pName,price,Packets.userName,Deposite,toTrade,partielPrice,Packets.Policy FROM Packets JOIN electricItem ON electricItem.PackageID = Packets.ID JOIN users ON users.userName=Packets.userName WHERE " +
                        " Packets.price<=?"; break;
                case "ריהוט": s = "SELECT DISTINCT Packets.ID,fname,lname,pName,price,Packets.userName,Deposite,toTrade,partielPrice,Packets.Policy FROM Packets JOIN furnitureItem ON furnitureItem.PackageID = Packets.ID JOIN users ON users.userName=Packets.userName WHERE " +
                        " Packets.price<=?"; break;
                case "סלולר": s = "SELECT DISTINCT Packets.ID,fname,lname,pName,price,Packets.userName,Deposite,toTrade,partielPrice,Packets.Policy FROM Packets JOIN cellolarItem ON cellolarItem.PackageID = Packets.ID JOIN users ON users.userName=Packets.userName WHERE " +
                        " Packets.price<=?"; break;
                case "אחר": s = "SELECT DISTINCT Packets.ID,fname,lname,pName,price,Packets.userName,Deposite,toTrade,partielPrice,Packets.Policy FROM Packets JOIN secondHandItem ON secondHandItem.PackageID = Packets.ID JOIN users ON users.userName=Packets.userName WHERE " +
                        " Packets.price<=?"; break;
            }
            s+=OVER;
            PreparedStatement prep = conn.prepareStatement(s);
            if(price.getText().isEmpty())
                prep.setString(1, ""+0);
            else
                prep.setString(1, price.getText());
            // prep.addBatch();
            ResultSet rs=prep.executeQuery();
            InsertPacket(rs,conn);
        }

        if (kind.getSelectionModel().getSelectedItem().toString().equals("חיות מחמד")) {
            String s = "SELECT DISTINCT Packets.ID,fname,lname,pName,price,Packets.userName,Deposite,toTrade,partielPrice,Packets.Policy FROM Packets JOIN PetItem ON PetItem.PackageID = Packets.ID JOIN users ON users.userName=Packets.userName WHERE " +
                    "PetItem.age<=? AND Packets.price<=?";
            if(!combo1.getSelectionModel().getSelectedItem().toString().equals("סוג חיה:"))
                s+= " AND PetItem.kind=?";
            s+=OVER;
            PreparedStatement prep = conn.prepareStatement(s);
            if(!combo1.getSelectionModel().getSelectedItem().toString().equals("סוג חיה:"))
                prep.setString(3, combo1.getSelectionModel().getSelectedItem().toString());

            if(ftxt.getText().isEmpty())
                prep.setString(1, ""+0);
            else
                prep.setString(1, ftxt.getText());
            if(price.getText().isEmpty())
                prep.setString(2, ""+0);
            else
                prep.setString(2, price.getText());
            // prep.addBatch();
            ResultSet rs=prep.executeQuery();
            InsertPacket(rs,conn);

        }
        Stage s = new Stage();
        s.setTitle("Show Results");
        AnchorPane root = FXMLLoader.load(getClass().getResource("../sample/Showres.fxml"));
        Scene scene = new Scene(root,  654, 400);
        scene.getStylesheets().add(getClass().getResource("b.css").toExternalForm());
        //styiling
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
        s.setOnCloseRequest(event -> s.close());
    }

    public List<ResultSet> items(String PackId,Connection conn) throws Exception {
        ArrayList<ResultSet> ans=new ArrayList<>();
        PreparedStatement prep = conn.prepareStatement("SELECT * FROM realEstateItem WHERE PackageID="+PackId);
        ans.add(prep.executeQuery());
        prep = conn.prepareStatement("SELECT * FROM PetItem WHERE PackageID="+PackId);
        ans.add(prep.executeQuery());
        prep = conn.prepareStatement("SELECT * FROM CarItem WHERE PackageID="+PackId);
        ans.add(prep.executeQuery());
        prep = conn.prepareStatement("SELECT * FROM electricItem WHERE PackageID="+PackId);
        ans.add(prep.executeQuery());
        prep = conn.prepareStatement("SELECT * FROM furnitureItem WHERE PackageID="+PackId);
        ans.add(prep.executeQuery());
        prep = conn.prepareStatement("SELECT * FROM cellolarItem WHERE PackageID="+PackId);
        ans.add(prep.executeQuery());
        prep = conn.prepareStatement("SELECT * FROM secondHandItem WHERE PackageID="+PackId);
        ans.add(prep.executeQuery());
      return ans;
    }

    public void InsertPacket(ResultSet rs,Connection conn) throws Exception
    {
        while (rs.next()) {
            String id=rs.getString(1);
            String owner = rs.getString(2) +" "+ rs.getString(3);
            String name = rs.getString(4);
            int pri = rs.getInt(5);
            String userName=rs.getString(6);
            boolean t,l;
            String toTrade = rs.getString(8);
            if (toTrade.equals("No"))
                t = false;
            else
                t = true;
            int deposit = rs.getInt(7);
            if (pri==0 && deposit!=0)
                l = true;
            else if(pri==0 && deposit==0 && toTrade.equals("No")) {
                l = true;
            }
            else
                l=false;
            int partialPrice = rs.getInt(9);
            String pol=rs.getString(10);
            Packet p = new Packet(id,name,owner, userName, pri, deposit, t, l, partialPrice,pol);
            List<ResultSet> item=items(id,conn);
            p.setREItem(item.get(0));
            p.setPetItem(item.get(1));
            p.setCarItem(item.get(2));
            p.setYad2item(item.get(3),item.get(4),item.get(5),item.get(6));
            ans.add(p);
        }
    }

}
