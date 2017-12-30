package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.*;
import javax.sql.DataSource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.chrono.HijrahChronology;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Created by Ravid on 27/12/2017.
 */
public class regCTR {
    static int count=0;
    @FXML
    ChoiceBox q1cb;
    @FXML
    ChoiceBox q2cb;
    public PasswordField pass1fld;
    public PasswordField pass2fld;
    @FXML
    Label passMsg=new Label();
    public Button regbtm;
    public TextField usernamefld;
    public TextField fnamefld;
    public TextField lnamefld;
    public DatePicker birthdayfld;
    public Button picfld;
    public TextField q1fld;
    public TextField q2fld;
    public CheckBox checkbox;
    public TextField mailfld;
    private String picname="";


    ObservableList<String> q1= FXCollections.observableArrayList("בחר שאלת אבטחה 1:", "מה חיית המחמד האהובה עליך?","מהו שם התיכון בו למדת?","מה שם המורה האהובה עלייך?");
    ObservableList<String> q2= FXCollections.observableArrayList("בחר שאלת אבטחה 2:", "מה חיית המחמד האהובה עליך?","מהו שם התיכון בו למדת?","מה שם המורה האהובה עלייך?");

    @FXML
    public void initialize() {
        q1cb.setItems(q1);
        q1cb.setValue("בחר שאלת אבטחה 1:");
        q2cb.setItems(q2);
        q2cb.setValue("בחר שאלת אבטחה 2:");

        Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isAfter(LocalDate.now().minusYears(18))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        birthdayfld.setDayCellFactory(dayCellFactory);
        birthdayfld.setValue(LocalDate.now().minusYears(18));

    /*    Statement stat = conn.createStatement();
        stat.executeUpdate("drop table if exists users;");
        stat.executeUpdate("create table users (userName NOT NULL PRIMARY KEY , password,email,fname, lname,bday,picture,petsans,highschoolans,teacherans);");*/

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

    public void register() throws Exception {
        if (q1cb.getSelectionModel().getSelectedItem().toString().equals("בחר שאלת אבטחה 1:") ||
                q2cb.getSelectionModel().getSelectedItem().toString().equals("בחר שאלת אבטחה 2:")) {
            showAlertError("בחר שאלת אבטחה אחת");
            return;
        }
        if (q1cb.getSelectionModel().getSelectedItem().toString().equals(q2cb.getSelectionModel().getSelectedItem().toString())) {
            showAlertError("אנא בחר שאלות אבטחה שונות");
            return;
        }
        if(!pass1fld.getText().equals(pass2fld.getText())) {
            showAlertError("הסיסמאות לא תואמות");
            return;
        }
        if(!checkbox.isSelected()) {
            showAlertError("עליך לאשר את תנאי האתר");
            return;
        }
        if(!isValidEmailAddress(mailfld.getText())) {
            showAlertError("אימייל לא חוקי");
            return;
        }
        if( pass1fld.getLength()<6) {
            showAlertError("סיסמתך קצרה מדי- לפחות 6 תווים");
            return;
        }
        if(usernamefld.getText().isEmpty()|| pass1fld.getText().isEmpty() ||mailfld.getText().isEmpty() || lnamefld.getText().isEmpty() ||
                fnamefld.getText().isEmpty()|| picname.equals("") || birthdayfld.getValue()==null||q2fld.getText().isEmpty()|| q1fld.getText().isEmpty()){
            showAlertError("עליך למלא את כל השדות!!");
            return;
        }
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement prep = conn.prepareStatement(
                "insert into users values (?,?,?,?,?,?,?,?,?,?);");
        try {
            prep.setString(1, usernamefld.getText());
            prep.setString(2, pass1fld.getText());
            prep.setString(3, mailfld.getText());
            prep.setString(4, fnamefld.getText());
            prep.setString(5, lnamefld.getText());
            String s = birthdayfld.getValue().toString();
            prep.setString(6, birthdayfld.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            if (q1cb.getSelectionModel().getSelectedItem().toString().equals("מה חיית המחמד האהובה עליך?"))
                prep.setString(8, q1fld.getText().toString());
            else if (q1cb.getSelectionModel().getSelectedItem().toString().equals("מהו שם התיכון בו למדת?"))
                prep.setString(9, q1fld.getText().toString());
            else if (q1cb.getSelectionModel().getSelectedItem().toString().equals("מה שם המורה האהובה עלייך?"))
                prep.setString(10, q1fld.getText().toString());
            if (q2cb.getSelectionModel().getSelectedItem().toString().equals("מה חיית המחמד האהובה עליך?"))
                prep.setString(8, q2fld.getText().toString());
            else if (q2cb.getSelectionModel().getSelectedItem().toString().equals("מהו שם התיכון בו למדת?"))
                prep.setString(9, q2fld.getText().toString());
            else if (q2cb.getSelectionModel().getSelectedItem().toString().equals("מה שם המורה האהובה עלייך?"))
                prep.setString(10, q2fld.getText().toString());
            prep.setString(7, picname);
            prep.addBatch();

            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
            conn.close();
        }catch (SQLException e)
        {
            showAlertError("שם משתמש זה קיים כבר במערכת");
            usernamefld.clear();
            conn.close();
            return;
        }

        Thread T=new Thread(()->sendmail(mailfld.getText(),fnamefld.getText()));
        T.start();
        showAlertInfo("Success");
        Stage stage = (Stage) fnamefld.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public void upldpic() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("JPEG", "*.jpg");
        FileChooser.ExtensionFilter fileExtensions2 = new FileChooser.ExtensionFilter("PNG", "*.png");
        fileChooser.getExtensionFilters().add(fileExtensions);
        fileChooser.getExtensionFilters().add(fileExtensions2);
        File file = fileChooser.showOpenDialog((Stage) usernamefld.getScene().getWindow());
        if (file != null) {
            picname= file.getAbsolutePath().toString();
            return;
        }
        picname="";
    }

    public void sendmail(String mail,String fname){
        final String from="EveryThing4RentTheOriginal@gmail.com";
        final String password="18hrsi_dht";

        Properties props=new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mail));

            message.setSubject("Your Details At Everything4Rent");
            MimeMultipart multipart=new MimeMultipart("related");
            message.setText("");
            BodyPart m1=new MimeBodyPart();
            String htmlText="<H1>Dear "+fname+" ,</H1><H5> Thank you for your registration \n\n now you can rent Everything!</H5>"
                    +" <Img src=\"cid:image\">";
            m1.setContent(htmlText,"text/html");
            multipart.addBodyPart(m1);

            m1=new MimeBodyPart();
            FileDataSource fds=new FileDataSource("./src/logo.PNG");
            m1.setDataHandler(new DataHandler(fds));
            m1.setHeader("Content-ID","<image>");
            multipart.addBodyPart(m1);
            message.setContent(multipart);
            Transport.send(message);
            System.out.println("Done");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
