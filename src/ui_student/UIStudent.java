package ui_student;
/**
 * Created by Prohos on 11/22/2016.
 */


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import stylesheet.StyleSheet;
import validate.PasswordValidate;

public class UIStudent implements Initializable {

    //Var
    private String password = CacheData.student.getPassword();


    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtFirstName;

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXButton btnChangePassword;

    @FXML
    private AnchorPane passwordPane;

    @FXML
    private JFXPasswordField txtCurrent;

    @FXML
    private JFXPasswordField txtNew;

    @FXML
    private JFXPasswordField txtReNew;

    @FXML
    private JFXButton btnSaveChanged;

    @FXML
    private Hyperlink btnEditFN;

    @FXML
    private Hyperlink btnEditLN;

    @FXML
    private Hyperlink btnEditEmail;

    @FXML
    private Hyperlink btnSaveFN;

    @FXML
    private Hyperlink btnSaveLN;

    @FXML
    private Hyperlink btnSaveEmail;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showFirstData();
    }

    //Action

    @FXML
    void clickEditFN(ActionEvent event) {
        txtFirstName.setEditable(true);
        txtFirstName.requestFocus();
        btnSaveFN.setDisable(false);
        btnEditFN.setDisable(true);
    }

    @FXML
    void clickEditLN(ActionEvent event) {
        txtLastName.setEditable(true);
        txtLastName.requestFocus();
        btnSaveLN.setDisable(false);
        btnEditLN.setDisable(true);
    }

    @FXML
    void clickEditEmail(ActionEvent event) {
        txtEmail.setEditable(true);
        txtEmail.requestFocus();
        btnSaveEmail.setDisable(false);
        btnEditEmail.setDisable(true);
    }

    @FXML
    void clickChangePassword(ActionEvent event) {
        passwordPane.setVisible(true);
        txtCurrent.requestFocus();
    }

    @FXML
    void clickCancel(ActionEvent event) {
        clearTextFieldPasswordPane();
        passwordPane.setVisible(false);
    }


    @FXML
    void clickSaveFirstName(ActionEvent event) {
        updateData("std_full_name",txtFirstName,"firstname");
        btnSaveFN.setDisable(true);
        btnEditFN.setDisable(false);
        txtFirstName.setEditable(false);
    }

    @FXML
    void clickSaveLastName(ActionEvent event) {
        updateData("std_full_name",txtLastName,"lastname");
        btnSaveLN.setDisable(true);
        btnEditLN.setDisable(false);
        txtLastName.setEditable(false);
    }

    @FXML
    void clickSaveEmail(ActionEvent event) {
        updateData("std_email",txtEmail,"Email");
        btnSaveEmail.setDisable(true);
        btnEditEmail.setDisable(false);
        txtEmail.setEditable(false);
    }

    @FXML
    void clickSaveChanges(ActionEvent event) {
        password = CacheData.student.getPassword();
        if (!checkAllCondition())
            return;
        updatePassword();
        CacheData.student.setPassword(txtNew.getText());
        clickCancel(event);
    }

    //Method
    //Behavior
    private void clearTextFieldPasswordPane(){
        txtCurrent.clear();
        txtNew.clear();
        txtReNew.clear();
    }

    private void showFirstData(){
        txtID.setText(String.valueOf(CacheData.student.getId()));
        String[] full_name = CacheData.student.getFullName().split("\\s");
        txtFirstName.setText(full_name[0]);
        txtLastName.setText(full_name[1]);
        txtEmail.setText(CacheData.student.getEmail());
    }

    private boolean checkAllCondition(){
        return checkEmptyPasswordField();
    }

    private boolean checkEmptyPasswordField(){
        if (txtCurrent.getText().isEmpty()){
            txtCurrent.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else{
            txtCurrent.setStyle(StyleSheet.NULL_BORDER);
        }

        if (txtNew.getText().isEmpty()){
            txtNew.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else{
            txtNew.setStyle(StyleSheet.NULL_BORDER);
        }

        if (txtReNew.getText().isEmpty()){
            txtReNew.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else{
            txtReNew.setStyle(StyleSheet.NULL_BORDER);
        }
        return checkOldPassword();
    }

    private boolean checkOldPassword(){
        if (!txtCurrent.getText()
                .equals(password)){
            txtCurrent.setStyle(StyleSheet.RED_BORDER);
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Current Password");
                alert.setContentText("Your current password is not correct\nPlease check it again");
                alert.showAndWait();
            return false;
        }
        else {
            txtCurrent.setStyle(StyleSheet.NULL_BORDER);
        }
        return PasswordValidate.checkPasswordCorrect(txtNew,txtReNew);
    }

    //Database
    //Update data Except password
    private void updateData(String columnName,JFXTextField textField,String updateType){
        try {
            Connection con = ConnectionManager.getConnection();
            String sql = "UPDATE tbl_student SET "+columnName+"=? WHERE std_id=?";
            PreparedStatement statement = con.prepareStatement(sql);
            if(updateType.equalsIgnoreCase("firstname") || updateType.equalsIgnoreCase("lastname")){
                String nameCombine = txtFirstName.getText()+" "+txtLastName.getText();
                statement.setString(1,nameCombine);
            }else
                statement.setString(1,textField.getText());
            statement.setInt(2, CacheData.student.getId());
            if (statement.executeUpdate()>0){
                    Notifications.create()
                            .title("Success")
                            .text(updateType + " has successfully changed to "+textField.getText()+".")
                            .hideAfter(Duration.seconds(5))
                            .graphic(new ImageView(new Image("/icon/approve_check.png",100,100,false,false)))
                            .show();
            }
        }catch (Exception e){

        }

    }

    private void updatePassword(){
        try {
            Connection con = ConnectionManager.getConnection();
            String sql = "UPDATE tbl_student SET std_password=? WHERE std_id=?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1,txtNew.getText());
            statement.setInt(2,CacheData.student.getId());
            if (statement.executeUpdate()>0){
                Notifications.create()
                        .title("Success")
                        .text("Password has successfully changed.")
                        .hideAfter(Duration.seconds(5))
                        .graphic(new ImageView(new Image("/icon/approve_check.png",100,100,false,false)))
                        .show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}