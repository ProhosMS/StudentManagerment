/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validate;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Alert;
import stylesheet.StyleSheet;

/**
 *
 * @author Prohos
 */
public class EmailValidate {
    public static boolean checkEmail(JFXTextField textfield){
        String email = textfield.getText();
        if(!email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
                  alert.setTitle("Error");
                  alert.setHeaderText("your e-mail not correct");
                  alert.setContentText("Example : acb@domain.com");
                  alert.showAndWait();
            textfield.setText("");
            textfield.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else{
            textfield.setStyle(StyleSheet.NULL_BORDER);
        }
        return true;
    }
    

}
