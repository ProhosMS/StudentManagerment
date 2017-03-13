/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validate;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Alert;

/**
 *
 * @author Prohos
 */
public class PasswordValidate {
    public static boolean checkPasswordCorrect(JFXPasswordField textFieldpassword,JFXPasswordField textFieldRePassword){
        if(!textFieldpassword.getText().equals(textFieldRePassword.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
                  alert.setTitle("Error");
                  alert.setHeaderText("Password not much");
                  alert.setContentText("You password must be the same. Please fill it again");
                  alert.showAndWait();
            textFieldpassword.setText("");
            textFieldRePassword.setText("");
            return false;
        }else
            return true;
    }
    
    public static boolean checkPasswordCorrect(JFXPasswordField textFieldpassword,JFXPasswordField textFieldRePassword,boolean trueFalse){
        if(!textFieldpassword.getText().equals(textFieldRePassword.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
                  alert.setTitle("Error");
                  alert.setHeaderText("Password not much");
                  alert.setContentText("You password must be the same. Please fill it again");
                  alert.showAndWait();
            textFieldpassword.setText("");
            textFieldRePassword.setText("");
            return false;
        }else
            return trueFalse;
    }
}
