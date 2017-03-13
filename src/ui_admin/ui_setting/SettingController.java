package ui_admin.ui_setting;/**
 * Created by Prohos on 12/9/2016.
 */

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import datamanager.CacheData;
import datamanager.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import stylesheet.StyleSheet;
import validate.PasswordValidate;

public class SettingController implements Initializable {

    //Var
    private String password = CacheData.admin.getPassword();

    @FXML
    private ListView<String> listSetting;

    @FXML
    private Pane paneUserProfile;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private Hyperlink btnEdit;

    @FXML
    private Hyperlink btnSave;

    @FXML
    private JFXButton btnChangePassword;

    @FXML
    private Pane paneChangePassword;

    @FXML
    private JFXPasswordField txtCurrentPwPane;

    @FXML
    private JFXPasswordField txtNewPwPane;

    @FXML
    private JFXPasswordField txtReNewPwPane;

    @FXML
    private JFXButton btnSavePwPane;

    @FXML
    private Hyperlink btnCancelPwPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listSetting.getItems().add("User Profile");
        listSetting.getSelectionModel().select(0);


        txtEmail.setText(CacheData.admin.getEmail());
    }

    //Action


    @FXML
    void clickChangePassword(ActionEvent event) {
        paneChangePassword.setVisible(true);
    }

    @FXML
    void clickEdit(ActionEvent event) {
        txtEmail.setEditable(true);
        btnEdit.setDisable(true);
        btnSave.setDisable(false);
        txtEmail.requestFocus();
    }

    @FXML
    void clickSave(ActionEvent event) {
        if(!updateEmail())
            return;
        btnSave.setDisable(true);
        txtEmail.setEditable(false);
        btnEdit.setDisable(false);
    }

    @FXML
    void clickSaveChangesPwPane(ActionEvent event) {
        password = CacheData.admin.getPassword();
        if (!checkAllConditionPwPane())
            return;
        updatePassword();
        CacheData.admin.setPassword(txtNewPwPane.getText());
        clickCancelPwPane(event);
    }

    @FXML
    void clickCancelPwPane(ActionEvent event) {
        paneChangePassword.setVisible(false);
        clearAllTextFieldPwPane();
    }




    //Method
    //Behavior
    private boolean checkAllConditionPwPane(){
        return checkEmptyTextFieldPwPane();
    }

    private boolean checkEmptyTextFieldPwPane(){
        if (txtCurrentPwPane.getText().isEmpty()){
            txtCurrentPwPane.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else{
            txtCurrentPwPane.setStyle(StyleSheet.NULL_BORDER);
        }

        if (txtNewPwPane.getText().isEmpty()){
            txtNewPwPane.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else{
            txtNewPwPane.setStyle(StyleSheet.NULL_BORDER);
        }

        if (txtReNewPwPane.getText().isEmpty()){
            txtReNewPwPane.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else{
            txtReNewPwPane.setStyle(StyleSheet.NULL_BORDER);
        }
        return checkOldPassword();
    }

    private boolean checkOldPassword(){
        if (!txtCurrentPwPane.getText()
                             .equals(password)){
            txtCurrentPwPane.setStyle(StyleSheet.RED_BORDER);
            Alert alert = new Alert(Alert.AlertType.ERROR);
                  alert.setTitle("Error");
                  alert.setHeaderText("Current Password");
                  alert.setContentText("Your current password is not correct\nPlease check it again");
                  alert.showAndWait();
            return false;
        }
        else {
            txtCurrentPwPane.setStyle(StyleSheet.NULL_BORDER);
        }
        return PasswordValidate.checkPasswordCorrect(txtNewPwPane,txtReNewPwPane);
    }

    void clearAllTextFieldPwPane(){
        txtCurrentPwPane.clear();
        txtNewPwPane.clear();
        txtReNewPwPane.clear();
    }

    //Database
    private boolean updateEmail(){
        try {
            Connection con = ConnectionManager.getConnection();
            String sql = "UPDATE tbl_user SET user_email=? WHERE user_id=?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1,txtEmail.getText().trim());
            statement.setInt(2,CacheData.admin.getId());
            if (statement.executeUpdate() > 0){
                Notifications.create()
                        .title("Success")
                        .text("Email has successfully changed to "+txtEmail.getText())
                        .hideAfter(Duration.seconds(5))
                        .graphic(new ImageView(new Image("/icon/approve_check.png",100,100,false,false)))
                        .show();
            }
            statement.close();

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
                  alert.setTitle("Error");
                  alert.setHeaderText("Email "+txtEmail.getText()+" already had , please try again");
                  alert.showAndWait();

            txtEmail.requestFocus();
            return false;
        }

        return true;
    }

    private void updatePassword(){
        try {
            Connection con = ConnectionManager.getConnection();
            String sql = "UPDATE tbl_user SET user_password=? WHERE user_id=?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1,txtNewPwPane.getText());
            statement.setInt(2,CacheData.admin.getId());
            if (statement.executeUpdate() > 0){
                Notifications.create()
                        .title("Success")
                        .text("Password has successfully changed")
                        .hideAfter(Duration.seconds(5))
                        .graphic(new ImageView(new Image("/icon/approve_check.png",100,100,false,false)))
                        .show();
            }
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //    private String getOldPassword(){
//        String password = "";
//        try {
//            Connection con = ConnectionManager.getConnection();
//            String sql = "SELECT * FROM tbl_user WHERE user_id=?";
//            PreparedStatement statement = con.prepareStatement(sql);
//            statement.setInt(1,CacheData.admin.getId());
//            ResultSet rs = statement.executeQuery();
//            if (rs.next()){
//                password = rs.getString("user_password");
//            }
//        }catch (Exception e){
//
//        }
//
//        return password;
//    }


}