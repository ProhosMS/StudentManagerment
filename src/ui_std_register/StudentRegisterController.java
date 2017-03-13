/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_std_register;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import datamanager.ConnectionManager;
import datamanager.Version;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import stylesheet.StyleSheet;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import validate.PasswordValidate;

/**
 * FXML Controller class
 *
 * @author Prohos
 */
public class StudentRegisterController implements Initializable {

    private static HashMap name_id_subject = new HashMap<String,Integer>();

    @FXML
    private JFXComboBox<String> cbSubject;
    @FXML
    private AnchorPane rootpane;
    @FXML
    private JFXTextField txtFirstName;
    @FXML
    private JFXTextField txtLastName;
    @FXML
    private JFXComboBox<String> cbGender;
    @FXML
    private ImageView imgView;
    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private JFXPasswordField txtRePassword;
    @FXML
    private JFXButton btnReg;
    @FXML
    private Label labelVersion;
    @FXML
    private ImageView imgView1;
    @FXML
    private JFXTextField txtPhone;



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelVersion.setText(Version.VERSION_APP);
        setDataToCbGender();
        try{
            Connection con = ConnectionManager.getConnection();
            String sql = "SELECT * from tbl_subject";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            
            while(rs.next()){
                cbSubject.getItems()
                         .add(rs.getString("sub_name"));

                //add data from database to HashMap
                name_id_subject.put(rs.getString(2),
                                    rs.getInt(1));
            }
            rs.close();
            stm.close();
        }catch(Exception e){e.printStackTrace(); }
       // txtPhone.getText().matches("[0]"
    }
    @FXML
    void typeOfPhone(KeyEvent event) {
       //if (txtPhone.getText().equals("0")) {
        if (txtPhone.getText().matches("[0]")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You can't input number 0");
            alert.showAndWait();
            txtPhone.clear();
        }
    }
    @FXML
    private void makeSelect(ActionEvent event) {
        cbGender.setStyle(StyleSheet.NULL_BORDER);
        cbGender.setStyle(StyleSheet.BLUE_BACKGROUD);
        try {
            if (cbGender.getSelectionModel().getSelectedIndex() == 0) {
                Image boy = new Image("/icon/boy.png");
                imgView.setImage(boy);
            }else if (cbGender.getSelectionModel().getSelectedIndex() == -1){
                imgView.setImage(null);
            }
            else {
                Image girl = new Image("/icon/girl.png");
                imgView.setImage(girl);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        
    }

    //Click Register action
    @FXML
    private void makeRegister(ActionEvent event) {
        if(!checkAllValidateCondition())
            return;
        System.out.println("have data");
       // imgView.setImage(null);


        String fullName = txtFirstName.getText() + " " + txtLastName.getText();


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("data add");
        alert.showAndWait();

        try {
            Connection con = ConnectionManager.getConnection();
            String sql_insert = "INSERT INTO tbl_student("
                                +"std_full_name,"
                                +"std_gender,"
                                +"std_sub_id,"
                                +"std_email,"
                                +"std_phone,"
                                +"std_password) VALUES (?,?,?,?,?,?)";
            PreparedStatement statement = con.prepareStatement(sql_insert);
            statement.setString(1,fullName);
            statement.setString(2,cbGender.getValue());
            statement.setInt(3,getSubID(cbSubject.getValue()));
            statement.setString(4,txtEmail.getText());
            statement.setString(5,"+855"+txtPhone.getText());
            statement.setString(6,txtPassword.getText());

            if (statement.executeUpdate() > 0){
                Notifications.create()
                        .title("Success")
                        .text("Please confirmation in your email")
                        .hideAfter(Duration.seconds(10))
                        .graphic(new ImageView(new Image("/icon/approve.png",100,100,false,false)))
                        .show();

            }

            resetDefault();
            statement.close();

        }catch (Exception e){e.printStackTrace();}

    }
    @FXML
    void subjectSelected(ActionEvent event) {
        cbSubject.setStyle(StyleSheet.NULL_BORDER);
        cbSubject.setStyle(StyleSheet.BLUE_BACKGROUD);
        try{
            if (cbSubject.getSelectionModel().getSelectedIndex() >= 0) {
                String subject = cbSubject.getValue().toLowerCase();
                imgView1.setImage(new Image("/icon/subjecticon/" + subject + ".png"));
            }else if(cbSubject.getSelectionModel().getSelectedIndex() == -1){
                imgView1.setImage(null);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void typeInEmail(KeyEvent event) {
//        if(event.getCode()== KeyCode.TAB){
//            txtEmail.setText("@");
//        }
    }

    private void setDataToCbGender(){

        cbGender.getItems()
                .add("Male");
        cbGender.getItems()
                .add("Female");
    }
    
    private boolean checkAllValidateCondition(){
        return checkAllEmptyTextFieldAndCombo();
    }
    
    private boolean checkAllEmptyTextFieldAndCombo(){
        if(txtFirstName.getText().equals("") && 
                txtLastName.getText().equals("") &&
                    cbGender.getSelectionModel().getSelectedIndex()==-1 &&
                        cbSubject.getSelectionModel().getSelectedIndex()==-1 &&
                            txtEmail.getText().equals("") &&
                                txtPassword.getText().equals("") &&
                                    txtRePassword.getText().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
                  alert.setTitle("Error");
                  alert.setHeaderText("Please fill all information");
                  alert.setContentText("Your all information not yet fill \nplease fill all information before click register");
                  alert.showAndWait();
            return false;
        }
        return checkEmptyTextFieldAndCombo();
    }

    //todo:1111111
    private boolean checkEmptyTextFieldAndCombo(){
        if(txtFirstName.getText().equals("")){
            txtFirstName.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else
            txtFirstName.setStyle(StyleSheet.NULL_BORDER);

        //todo:2222222
        if(!txtFirstName.getText().matches("[a-zA-Z]+")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Can not contain number in name");
            alert.showAndWait();
            txtFirstName.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else
            txtFirstName.setStyle(StyleSheet.NULL_BORDER);
        
        if(txtLastName.getText().equals("")){
            txtLastName.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else
            txtLastName.setStyle(StyleSheet.NULL_BORDER);


        if(!txtLastName.getText().matches("[a-zA-Z]+")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Can not contain number in name");
            alert.showAndWait();
            txtLastName.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else
            txtLastName.setStyle(StyleSheet.NULL_BORDER);


        if(cbGender.getSelectionModel().getSelectedIndex()==-1){
            cbGender.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else{
            cbGender.setStyle(StyleSheet.NULL_BORDER);
            cbGender.setStyle(StyleSheet.BLUE_BACKGROUD);
        }
            
        
        if(cbSubject.getSelectionModel().getSelectedIndex()==-1){
            cbSubject.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else{
            cbSubject.setStyle(StyleSheet.NULL_BORDER);
            cbSubject.setStyle(StyleSheet.BLUE_BACKGROUD);
        }
        
        if(txtEmail.getText().equals("")){
            txtEmail.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else
            txtEmail.setStyle(StyleSheet.NULL_BORDER);

        String email = txtEmail.getText();
        String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        if(!email.matches(regex)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("your e-mail not correct");
            alert.setContentText("Example : acb@domain.com");
            alert.showAndWait();
            txtEmail.setText("");
            txtEmail.setStyle(StyleSheet.RED_BORDER);
            return false;
        }
   
        if(txtPassword.getText().equals("")){
            txtPassword.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else
            txtPassword.setStyle(StyleSheet.NULL_BORDER);
        
        if(txtRePassword.getText().equals("")){
            txtRePassword.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else
            txtRePassword.setStyle(StyleSheet.NULL_BORDER);

        return PasswordValidate.checkPasswordCorrect(txtPassword,txtRePassword);
    }


    private void resetDefault() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtRePassword.setText("");
        txtPhone.clear();
        cbGender.getSelectionModel().select(-1);
        cbSubject.getSelectionModel().select(-1);

    }

    //Get value from HashMap
    private int getSubID(String key){
        return (int)name_id_subject.get(key);

    }



}
