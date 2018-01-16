package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class Packet {
    private String ownerName;
    private String ID;
    private String UserName;
    private int Price;
    private int Deposite;
    private boolean isTrade;
    private String policy;
    private boolean isLent;
    private int partielPrice;
    private String pName;
    public HashMap<String,String> Items;
    public ObservableList<String> RealEstate;
    public ObservableList<String> Cars;
    public ObservableList<String> Pets;
    public ObservableList<String> Yad2;

    public Packet(String ID, String pName, String ownerName, String userName, int price, int deposite, boolean isTrade, boolean isLent, int partielPrice, String policy){
        this.pName=pName;
        this.policy=policy;

        this.ID=ID;
        this.ownerName=ownerName;
        UserName = userName;
        Price = price;
        Deposite = deposite;
        this.isTrade = isTrade;
        this.isLent = isLent;
        this.partielPrice = partielPrice;
        Items=new HashMap<>();

    }


    public void setREItem(ResultSet REItem) throws Exception {
        RealEstate= FXCollections.observableArrayList();
        while (REItem.next()) {
            RealEstate.add("RealEstate-ID: " + REItem.getString(1) + " Address- " + REItem.getString(2) + " " + REItem.getString(4));
            String temp="RealEstate-ID: " + REItem.getString(1) + " Address- " + REItem.getString(2) + " "+ REItem.getString(3)+" "+ REItem.getString(4)+"\r\n"+
                    "Max People- "+REItem.getString(5)+" Size- "+REItem.getString(6)+ " Kind- "+REItem.getString(7)+"\r\n"+
                    "Equipment: \r\n Karaoke- "+REItem.getString(9) +" Pool- "+REItem.getString(10)+" Balcony- "+REItem.getString(11)+"\r\n"
                    +" Smoke- "+REItem.getString(12)+" TV Screen- "+REItem.getString(13)+" Speakers- "+REItem.getString(14);
            Items.put(REItem.getString(1),temp);
        }
    }

    public void setCarItem(ResultSet carItem) throws Exception {
        Cars= FXCollections.observableArrayList();
        while (carItem.next()) {
            Cars.add("CarItem-ID: " + carItem.getString(1) + " - " + carItem.getString(6));
            String temp="CarItem-ID: " + carItem.getString(1) + " Model- " + carItem.getString(2) + " Color-"+ carItem.getString(3)+"\r\n"+
                    "Year- "+carItem.getString(4)+" Kind Of Car- "+carItem.getString(5)+ " Name- "+carItem.getString(6)+"\r\n"+
                    "Description: \r\n"+ carItem.getString(7);
            Items.put(carItem.getString(1),temp);
        }
    }

    public void setPetItem(ResultSet petItem) throws Exception {
        Pets= FXCollections.observableArrayList();
        while (petItem.next()) {
            Pets.add("petItem-ID: " + petItem.getString(1) + " Name-" + petItem.getString(2));
            String temp="petItem-ID: " + petItem.getString(1) + " Name- " + petItem.getString(2) + " Kind-"+ petItem.getString(3)+"\r\n"+
                    "Gender- "+petItem.getString(4)+" Age- "+petItem.getString(5)+"\r\n"+
                    "Description: \r\n"+ petItem.getString(6);
            Items.put(petItem.getString(1),temp);
        }
    }

    public void setYad2item(ResultSet electric,ResultSet furn,ResultSet cell,ResultSet other) throws Exception {
        Yad2= FXCollections.observableArrayList();
        while (electric.next()) {
            Yad2.add("electricItem-ID: " + electric.getString(1) + " Name- " + electric.getString(2));
            String temp="electricItem-ID: " + electric.getString(1) + " Name- " + electric.getString(2) + " date-"+ electric.getString(3)+"\r\n"+
                    "manufacture- "+electric.getString(4)+"\r\n"+
                    "Description: \r\n"+ electric.getString(5);
            Items.put(electric.getString(1),temp);
        }
        while (furn.next()) {
            Yad2.add("Furniture-ID: " + furn.getString(1) + " Name- " + furn.getString(2));
            String temp="\"Furniture-ID: " + furn.getString(1) + " Name- " + furn.getString(2) + " date-"+ furn.getString(3)+"\r\n"+
                    "Color- "+furn.getString(5)+" Length- "+furn.getString(6)+" Weight-"+ furn.getString(7)+"\r\n"+
                    "Description: \r\n"+ furn.getString(4);
            Items.put(furn.getString(1),temp);
        }
        while (cell.next()) {
            Yad2.add("Cellular-ID: " + cell.getString(1) + " Name- " + cell.getString(2));
            String temp="Cellular-ID: " + cell.getString(1) + " Name- " + cell.getString(2) + " date-"+ cell.getString(3)+"\r\n"+
                    "Manufacture- "+cell.getString(5)+" Model- "+cell.getString(6)+" Memory-"+ cell.getString(7)+"\r\n"+
                    "Description: \r\n"+ cell.getString(4);
            Items.put(cell.getString(1),temp);
        }
        while (other.next()) {
            Yad2.add("SecHand-ID: " + other.getString(1) + " Name- " + other.getString(2));
            String temp="SecHand-ID: " + other.getString(1) + " Name- " + other.getString(2) + " date-"+ other.getString(3)+"\r\n"+
                    "Description: \r\n"+ other.getString(4);
            Items.put(other.getString(1),temp);
        }
    }

    public ObservableList<String> getRealEstate() {
        return RealEstate;
    }

    public String getpName() {
        return pName;
    }
    public String getItem(String id)
    {
        return Items.get(id);
    }

    public String getPolicy() {
        return policy;
    }

    public String getID() {
        return ID;
    }

    public String getUserName() {

        return UserName;
    }



    public String getOwnerName() {
        return ownerName;
    }

    public int getPrice() {
        return Price;
    }

    public int getDeposite() {
        return Deposite;
    }

    public boolean isTrade() {
        return isTrade;
    }

    public boolean isLent() {
        return isLent;
    }

    public int getPartielPrice() {
        return partielPrice;
    }

    public HashMap<String, String> getItems() {
        return Items;
    }

    public ObservableList<String> getCars() {
        return Cars;
    }

    public ObservableList<String> getPets() {
        return Pets;
    }

    public ObservableList<String> getYad2() {
        return Yad2;
    }
}
