/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package std_ms;

import datamanager.Version;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author Prohos
 */
public class SplashScreenController implements Initializable {

    @FXML
    private AnchorPane rootpane;
    @FXML
    private Label labelVersion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelVersion.setText(Version.VERSION_APP);
        new SplashScreen().start();
        
        
    }    
    
    class SplashScreen extends Thread{
        @Override
        public void run(){
            try {
                Thread.sleep(5000);
            
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                Parent root  = null;
                   try{
                        
                        root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        //FXMLDocument
                
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                
                stage.setTitle("Log in");
             
                stage.show();
                
                rootpane.getScene().getWindow().hide();
                }
            });
            } catch (InterruptedException ex) {
                Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
