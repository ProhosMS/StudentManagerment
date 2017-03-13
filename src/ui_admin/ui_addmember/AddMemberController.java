package ui_admin.ui_addmember;/**
 * Created by Prohos on 11/24/2016.
 */

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import datamanager.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import stylesheet.StyleSheet;
import validate.PasswordValidate;

import javax.management.Notification;

public class AddMemberController implements Initializable {

    @FXML
    private AnchorPane rootPaneAddMember;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXPasswordField txtRePassword;

    @FXML
    private JFXButton btnRegister;




    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO : Do Something what i want to init when form start up


    }

    @FXML
    void ClickRegister(ActionEvent event) {
        if (!checkAllValidateCondition())
            return;



        try {
            Connection con = ConnectionManager.getConnection();
            String sql = "INSERT INTO tbl_user(user_email,user_password) VALUES (?,?)";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1,txtEmail.getText());
            statement.setString(2,txtPassword.getText());

            if (statement.executeUpdate() > 0){
                Notifications.create()
                        .title("Success")
                        .text("Account is registered!!!")
                        .hideAfter(Duration.seconds(5))
                        .graphic(new ImageView(new Image("/icon/approve_check.png",100,100,false,false)))
                        .show();
            }

            clearAllTextBox();
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean checkAllValidateCondition(){
        return checkAllEmptyTextField();
    }

    private boolean checkAllEmptyTextField(){
        if (txtEmail.getText().equals("") &&
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
        return checkEmptyTextBoxAndEmailValidation();
    }

    private boolean checkEmptyTextBoxAndEmailValidation(){
        if (txtEmail.getText().equals("")){
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

        if (txtPassword.getText().equals("")){
            txtPassword.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else
            txtPassword.setStyle(StyleSheet.NULL_BORDER);

        if (txtRePassword.getText().equals("")){
            txtRePassword.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else
            txtRePassword.setStyle(StyleSheet.NULL_BORDER);

        return PasswordValidate.checkPasswordCorrect(txtPassword,txtRePassword);
    }

    private void clearAllTextBox(){
        txtEmail.setText("");
        txtPassword.setText("");
        txtRePassword.setText("");
    }

}