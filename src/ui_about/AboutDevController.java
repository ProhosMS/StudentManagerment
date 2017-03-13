package ui_about;/**
 * Created by Prohos on 12/28/2016.
 */

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;

public class AboutDevController implements Initializable {
    @FXML
    private Hyperlink facebookbtn;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }



    @FXML
    void clickFacebook(ActionEvent event) throws Exception {
        Desktop.getDesktop().browse(new URI("https://web.facebook.com/prohos.uchiha"));
    }

    @FXML
    void clickGitHub(ActionEvent event) throws Exception{
        Desktop.getDesktop().browse(new URI("https://github.com/ProhosMS/"));
    }


    @FXML
    void clickLinkedIn(ActionEvent event)throws Exception {
        Desktop.getDesktop().browse(new URI("https://www.linkedin.com/in/som-rithprohos-29410b131?trk=nav_responsive_tab_profile_pic"));
    }
}