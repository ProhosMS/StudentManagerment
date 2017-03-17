package ui_admin.ui_addsubject;

/**
 * Created by Prohos on 11/28/2016.
 * A Controller for AddSubject update upject
 */

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import datamanager.ConnectionManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import ui_admin.DashboardController;

import javax.management.Notification;


public class AddSubjectController implements Initializable {


    private ObservableList<Subject> list = FXCollections.observableArrayList();


    @FXML
    private JFXTextField txtSubName;

    @FXML
    private TableView<Subject> tableSubject;

    @FXML
    private TableColumn<Subject, Integer> colID;

    @FXML
    private TableColumn<Subject, Integer> colSubject;

    @FXML
    private JFXButton btnSave;

    @FXML
    private Hyperlink btnEdit;

    @FXML
    private JFXTextField txtID;

    //MenuItem
    @FXML
    private MenuItem menuAdd;

    @FXML
    private MenuItem menuRefresh;

    @FXML
    private MenuItem menuDelete;


    @Override
    public void initialize(URL url, ResourceBundle rb) {


        initColumn();
        loadTable();

        //table click event
        tableSubject.setRowFactory((e) ->{
            TableRow<Subject> row = new TableRow<>();
            row.setOnMouseClicked((event) -> {
                if (event.getClickCount()==1 && (!row.isEmpty())){
                    Subject rowData = row.getItem();
                    String id = String.valueOf(rowData.getId());
                    txtID.setText(id);
                    txtSubName.setText(rowData.getSubject_name());
                    txtSubName.setDisable(true);
                    menuDelete.setDisable(false);

                }
            });

            return row;
        });

    }

    private void loadTable() {
        try {
            list.clear();
            Connection con = ConnectionManager.getConnection();
            String sql = "SELECT * FROM tbl_subject";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                list.add(new Subject(
                        resultSet.getInt(1),
                        resultSet.getString(2)
                ));
            }
            resultSet.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        tableSubject.getItems().setAll(list);
    }

    private void initColumn() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("subject_name"));
    }

    //action

    @FXML
    void clickEdit(ActionEvent event) {
        txtSubName.setDisable(false);
    }
    @FXML
    void clickSave(ActionEvent event) {
        if (txtID.getText().equals("")) {
            insertData();
        }else if (txtID.getLength() > 1){
            updateData();
        }
    }


    @FXML
    void clickDeleteMenu(ActionEvent event) {
        deleteData();
    }



    @FXML
    void clickAddMenu(ActionEvent event) {
        txtID.setText("");
        txtSubName.setText("");
        txtSubName.setDisable(false);
        txtSubName.requestFocus();
    }

    @FXML
    void clickRefreshMenu(ActionEvent event) {
        loadTable();
    }


    //code update data and insert data and delete data
    private void updateData() {
        try {
            Connection con = ConnectionManager.getConnection();
            String sql = "UPDATE tbl_suject SET sub_name=? WHERE sub_id=?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1,txtSubName.getText().toLowerCase());
            statement.setInt(2,Integer.parseInt(txtID.getText()));
            if (statement.executeUpdate() > 0){
                Notifications.create()
                        .title("Success")
                        .text("Subject is updated")
                        .hideAfter(Duration.seconds(5))
                        .graphic(new ImageView(new Image("/icon/subject.png",100,100,false,false)))
                        .show();
            }
            statement.close();
            txtSubName.setText("");
            txtSubName.setDisable(true);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Data is duplicate");
            alert.setContentText("Subject "+txtSubName.getText() + " is already exist in database");
            alert.showAndWait();
            txtSubName.requestFocus();
        }
    }

    private void insertData(){
        try {
            Connection con = ConnectionManager.getConnection();
            String sql = "INSERT INTO tbl_subject(sub_name) VALUES (?)";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1,txtSubName.getText().toLowerCase());

            if (statement.executeUpdate() > 0){
                Notifications.create()
                        .title("Success")
                        .text("Subject is added")
                        .hideAfter(Duration.seconds(5))
                        .graphic(new ImageView(new Image("/icon/subject.png",100,100,false,false)))
                        .show();
            }
            statement.close();
            txtSubName.setText("");
            txtSubName.setDisable(true);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Data is duplicate");
                    alert.setContentText("Subject "+txtSubName.getText() + " is already exist in database");
                    alert.showAndWait();
            txtSubName.clear();
            txtSubName.requestFocus();
        }
    }

    private void deleteData(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Confirmation");
              alert.setHeaderText("Are you sure to delete subject '"+txtSubName.getText()+"' ?");
              alert.showAndWait().ifPresent((ButtonType response) -> {
                  if (response == ButtonType.OK) {
                      try {
                          Connection con = ConnectionManager.getConnection();
                          String sql = "DELETE FROM tbl_subject WHERE sub_id=?";
                          PreparedStatement statement = con.prepareStatement(sql);
                          statement.setInt(1, Integer.parseInt(txtID.getText()));
                          if (statement.executeUpdate() > 0) {
                              Notifications.create()
                                      .title("Success")
                                      .text("Subject is deleted")
                                      .hideAfter(Duration.seconds(5))
                                      .graphic(new ImageView(new Image("/icon/delete.png",100,100,false,false)))
                                      .show();
                          }
                          statement.close();
                          txtID.setText("");
                          txtSubName.setText("");
                          loadTable();
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                  }
              });

    }

    public static class Subject{
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty subject_name;

        public Subject(Integer id,String subject_name){
            this.id = new SimpleIntegerProperty(id);
            this.subject_name = new SimpleStringProperty(subject_name);
        }

        public int getId() {
            return id.get();
        }

        public String getSubject_name() {
            return subject_name.get();
        }


    }
}