/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package std_ms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;


import datamanager.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;
import stylesheet.StyleSheet;
import validate.EmailValidate;

/**
 *
 * @author Prohos
 */
public class FXMLDocumentController implements Initializable {

    ObservableList<String> email = FXCollections.observableArrayList();

    @FXML
    private AnchorPane rootpane;

    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXButton btnRegister;

      @FXML
    private JFXComboBox<String> cbAuth;
      
    @FXML
    void btnClicked(ActionEvent event) {
        if(!checkAllCondition())
            return;


        if (cbAuth.getValue().equals("Student")){
            loginAsStudent();
        }else
            loginAsAdmin();

        resetDefualt();
    }

    @FXML
    void btnRegClicked(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui_std_register/StudentRegister.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Register");
            stage.show();

            rootpane.getScene().getWindow().hide();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @FXML
    void cbAuthSelectedIndexChange(ActionEvent event) {
        cbAuth.setStyle(StyleSheet.NULL_BORDER);
        cbAuth.setStyle(StyleSheet.BLUE_BACKGROUD);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getAdminEmail();
        setItemToComboAuth();
        TextFields.bindAutoCompletion(txtUsername,email);
        
    }




    //database
    private void loginAsStudent() {
        try{
            Connection con = ConnectionManager.getConnection();
            String sql = "SELECT * FROM tbl_student WHERE std_email=? AND std_password=? AND isPaid=True";
            String sql_false = "SELECT * FROM tbl_student WHERE std_email=? AND std_password=? AND isPaid=False";
            PreparedStatement stm = con.prepareStatement(sql);
            PreparedStatement stm_false = con.prepareStatement(sql_false);
            stm.setString(1,txtUsername.getText());
            stm.setString(2,txtPassword.getText());
            stm_false.setString(1,txtUsername.getText());
            stm_false.setString(2,txtPassword.getText());
            ResultSet rs = stm.executeQuery();
            ResultSet rs_false = stm_false.executeQuery();
            if(rs.next()){


                CacheData.student = new Student(
                        rs.getInt(Db.STUDENT_ID),
                        rs.getString(Db.STUDENT_FULL_NAME),
                        rs.getString(Db.STUDENT_GENDER),
                        rs.getInt(Db.STUDENT_SUBJECT),
                        rs.getString(Db.STUDENT_EMAIL),
                        rs.getString(Db.STUDENT_PASSWORD)
                );

                Notifications.create()
                        .title("Success")
                        .text("Log in as student \nEmail :" + CacheData.student.getEmail())
                        .hideAfter(Duration.seconds(5))
                        .graphic(new ImageView(new Image("/icon/approve_check.png",100,100,false,false)))
                        .show();

                Parent root = FXMLLoader.load(getClass().getResource("/ui_student/ui_student.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.getIcons().add(new Image("/icon/boy.png"));
                stage.setTitle("Student Profile");
                stage.show();



                //Hide this panel
                rootpane.getScene().getWindow().hide();

            }else if(rs_false.next()){
                System.out.println("Account not yet approve");

                Alert alert = new Alert(Alert.AlertType.WARNING);
                      alert.setTitle("Error");
                      alert.setContentText("Your account is correct but not yet approved");
                      alert.showAndWait();
            }else{

                Alert alert = new Alert(Alert.AlertType.ERROR);
                      alert.setTitle("Error");
                      alert.setContentText("Wrong Email or Password");
                      alert.showAndWait();

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loginAsAdmin(){
        try{
            Connection con = ConnectionManager.getConnection();
            String sql = "SELECT * FROM tbl_user WHERE user_email=? AND user_password=?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1,txtUsername.getText());
            stm.setString(2,txtPassword.getText());
            ResultSet rs = stm.executeQuery();

            if (rs.next()){
                //Cache data from User log in store in Static Oject
                CacheData.admin = new Admin(
                        rs.getInt(Db.USER_ID),
                        rs.getString(Db.USER_EMAIL),
                        rs.getString(Db.USER_PASSWORD));

                Notifications.create()
                        .title("Success")
                        .text("Log in as admin \nEmail :" + CacheData.admin.getEmail())
                        .hideAfter(Duration.seconds(5))
                        .graphic(new ImageView(new Image("/icon/approve_check.png",100,100,false,false)))
                        .show();

                //Open Ui_admin form
                Parent root = FXMLLoader.load(getClass().getResource("/ui_admin/UI_admin.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Dashboard");
                //stage.setResizable(false);
                stage.setMaximized(true);
                stage.getIcons().add(new Image("/icon/dashboard.png"));
                stage.show();

                //Hide this panel
                rootpane.getScene().getWindow().hide();

            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                      alert.setTitle("Error");
                      alert.setHeaderText("Wrong password or Email");
                      alert.showAndWait();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getAdminEmail(){
        try{
            Connection con = ConnectionManager.getConnection();
            String sql = "SELECT user_email FROM tbl_user";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                email.add(rs.getString(Db.USER_EMAIL));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //behavior
    private void setItemToComboAuth(){
        cbAuth.getItems().add("Admin");
        cbAuth.getItems().add("Student");
    }

    private boolean checkAllCondition(){
        if(cbAuth.getSelectionModel().getSelectedIndex()==-1){
            cbAuth.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else{
            cbAuth.setStyle(StyleSheet.NULL_BORDER);
            cbAuth.setStyle(StyleSheet.BLUE_BACKGROUD);
        }


        if (txtUsername.getText().equals("")) {
            txtUsername.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else
            txtUsername.setStyle(StyleSheet.NULL_BORDER);

        if (txtPassword.getText().equals("")){
            txtPassword.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else
            txtPassword.setStyle(StyleSheet.NULL_BORDER);


        return EmailValidate.checkEmail(txtUsername);
    }

    private void resetDefualt(){
        txtPassword.setText("");
        txtUsername.setText("");
        cbAuth.getSelectionModel().select(-1);
    }


}
