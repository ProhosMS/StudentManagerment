package ui_admin.ui_approvestudent;/**
 * Created by Prohos on 11/29/2016.
 */

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import datamanager.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import ui_admin.DashboardController;

public class ApproveStudentController implements Initializable {

    @FXML
    private JFXTextField txtFirstName;

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtSubject;

    @FXML
    private JFXButton btnApprove;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //split string
        String[] name = DashboardController.rowData.getName().split("\\s");
        txtFirstName.setText(name[0]);
        txtLastName.setText(name[1]);
        txtEmail.setText(DashboardController.rowData.getEmail());
        txtSubject.setText(DashboardController.rowData.getSubject());

    }

    @FXML
    void clickApprove(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Confirmation");
              alert.setHeaderText("Are you sure to approve student '"
                                    +txtFirstName.getText()+" "+txtLastName.getText()+"'");
              alert.showAndWait()
                      .filter(response -> response == ButtonType.OK)
                      .ifPresent(response -> updateData());

        btnApprove.setDisable(true);
    }

    private void updateData(){
        try{
            Connection con = ConnectionManager.getConnection();
            String sql = "UPDATE tbl_student SET isPaid=True WHERE std_id=?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1,DashboardController.rowData.getId());
            if (statement.executeUpdate() > 0){
                Notifications.create()
                        .title("Success")
                        .text("Student is officially register")
                        .hideAfter(Duration.seconds(5))
                        .graphic(new ImageView(new Image("/icon/approve_check.png",100,100,false,false)))
                        .show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}