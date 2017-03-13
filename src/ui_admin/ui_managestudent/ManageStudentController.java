package ui_admin.ui_managestudent;/**
 * Created by Prohos on 11/30/2016.
 */

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import datamanager.ConnectionManager;
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
import stylesheet.StyleSheet;
import ui_admin.DashboardController;

public class ManageStudentController implements Initializable {

    ObservableList<DashboardController.Student> list = FXCollections.observableArrayList();

    @FXML
    private TableView<DashboardController.Student> tableView;

    @FXML
    private TableColumn<DashboardController.Student, Integer> colID;

    @FXML
    private TableColumn<DashboardController.Student, String> colName;

    @FXML
    private TableColumn<DashboardController.Student, String> colGender;

    @FXML
    private TableColumn<DashboardController.Student, String> colSubject;

    @FXML
    private TableColumn<DashboardController.Student, String> colEmail;

    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtFirstName;

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXComboBox<String> cbSubject;

    @FXML
    private JFXComboBox<String> cbGender;

    @FXML
    private Hyperlink btnCancel;

    @FXML
    private MenuItem menuDelete;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initColumn();
        loadTable();

        //Click row table even
        tableView.setRowFactory((tv) -> {
            TableRow<DashboardController.Student> row = new TableRow<>();
            row.setOnMouseClicked((event) -> {
                if(event.getClickCount()==1 && (!row.isEmpty())){
                    setEditableState(false);
                    clearAllComboBoxData();
                    DashboardController.rowData = row.getItem();
                    txtID.setText(String.valueOf(DashboardController.rowData.getId()));
                    String[] name = DashboardController.rowData.getName().split("\\s");
                    txtFirstName.setText(name[0]);
                    txtLastName.setText(name[1]);
                    cbGender.setPromptText(DashboardController.rowData.getGender());
                    txtEmail.setText(DashboardController.rowData.getEmail());
                    cbSubject.setPromptText(DashboardController.rowData.getSubject());
                    menuDelete.setDisable(false);
                }
            });
            return row;
        });


    }

    //Action
    @FXML
    void clickMenuAddStudent(ActionEvent event) {
        clearAllTextField();
        loadTable();
        setEditableState(true);
        cbGender.setPromptText("Gender");
        cbSubject.setPromptText("Subject");
        selectFromTableSubject();
        cbGender.getItems().add("Male");
        cbGender.getItems().add("Female");
        txtFirstName.requestFocus();
        btnSave.setDisable(false);
        btnCancel.setDisable(false);
        menuDelete.setDisable(true);
    }

    @FXML
    void clickSave(ActionEvent event) {
        if (!checkAllValidateCondition())
            return;

        insertData();
        loadTable();
        clearAllTextField();
        setEditableState(false);
        clearAllComboBoxData();
        btnSave.setDisable(true);
    }

    @FXML
    void clickCancel(ActionEvent event) {
        clearAllTextField();
        setEditableState(false);
        clearAllComboBoxData();
        btnSave.setDisable(true);
        btnCancel.setDisable(true);
        menuDelete.setDisable(true);

    }

    @FXML
    void clickMenuRefresh(ActionEvent event) {
        loadTable();
        clickCancel(event);
    }

    @FXML
    void clickMenuDelete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Confirmation");
              alert.setHeaderText("Are you sure to delete student id '"+txtID.getText()+"'");
              alert.showAndWait().ifPresent((ButtonType response) ->{
                  if (response == ButtonType.OK){
                      deleteData();
                      loadTable();
                      menuDelete.setDisable(true);
                      clearAllTextField();
                      clearAllComboBoxData();
                  }
              });
    }


    //Table and Database method
    private void loadTable() {
        try {
            list.clear();

            Connection con = ConnectionManager.getConnection();
            String sql = "SELECT std_id,std_full_name,std_gender,std_sub_id,std_email FROM tbl_student WHERE isPaid=True";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                Integer id = rs.getInt(1);
                String name = rs.getString(2);
                String gender = rs.getString(3);
                String subject = getSubName(rs.getInt(4));
                String email = rs.getString(5);


                list.add(new DashboardController.Student(id, name, gender, subject, email));

            }
            rs.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        tableView.getItems().setAll(list);
    }

    private void initColumn() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void selectFromTableSubject(){
        try {
            Connection con = ConnectionManager.getConnection();
            String sql = "SELECT * FROM tbl_subject";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            DashboardController.name_id_subject.clear();
            DashboardController.id_name_subject.clear();
            while (resultSet.next()){
                cbSubject.getItems().add(resultSet.getString("sub_name"));
                DashboardController.name_id_subject.put(resultSet.getString("sub_name")
                                ,resultSet.getInt("sub_id"));
                DashboardController.id_name_subject.put(resultSet.getInt("sub_id")
                                ,resultSet.getString("sub_name"));
            }
            resultSet.close();
        }catch (Exception e){

        }
    }

    private void insertData(){
        try {
            Connection con = ConnectionManager.getConnection();
            String sql = "INSERT INTO tbl_student(std_full_name,std_gender,std_sub_id,std_email,isPaid) "
                            +"VALUES(?,?,?,?,?)";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1,txtFirstName.getText() + " " + txtLastName.getText());
            statement.setString(2,cbGender.getValue());
            statement.setInt(3,getSubID(cbSubject.getValue()));
            statement.setString(4,txtEmail.getText());
            statement.setBoolean(5,true);
            if (statement.executeUpdate() > 0){
                Notifications.create()
                        .title("Success")
                        .text("Student is registered\nDefault Password: 12345")
                        .hideAfter(Duration.seconds(5))
                        .graphic(new ImageView(new Image("/icon/approve_check.png",100,100,false,false)))
                        .show();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                      alert.setTitle("Error");
                      alert.setHeaderText("Something occur please check again");
                      alert.showAndWait();
            }
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteData(){
        try {
            Connection con = ConnectionManager.getConnection();
            String sql = "DELETE FROM tbl_student WHERE std_id=?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1,Integer.parseInt(txtID.getText()));
            if (statement.executeUpdate() > 0){
                Notifications.create()
                        .title("Success")
                        .text("Student is deleted\nID= '"+txtID.getText()+"'")
                        .hideAfter(Duration.seconds(5))
                        .graphic(new ImageView(new Image("/icon/approve_check.png",100,100,false,false)))
                        .show();
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Something occur please check again");
                alert.showAndWait();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateData(){
        //TODO: update data - Edit - Save changed
        try {
            Connection con = ConnectionManager.getConnection();
           // String sql = "UPDATE tbl_student SET "
        }catch (Exception e){

        }
    }

    //Component method
    private void clearAllTextField(){
        txtID.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
    }

    private void setEditableState(boolean bool){
        txtFirstName.setEditable(bool);
        txtLastName.setEditable(bool);
        txtEmail.setEditable(bool);
    }

    private void clearAllComboBoxData(){
        cbGender.setPromptText("Gender");
        cbSubject.setPromptText("Subject");
        cbSubject.getItems().clear();
        cbGender.getItems().clear();
    }

    //validate method
    private boolean checkAllValidateCondition(){
        return checkAllEmptyComponent();
    }

    private boolean checkAllEmptyComponent(){
        if (txtFirstName.getText().equals("")
                && txtLastName.getText().equals("")
                    && txtEmail.getText().equals("")
                        && cbGender.getSelectionModel().getSelectedIndex()==-1
                            && cbSubject.getSelectionModel().getSelectedIndex()==-1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
                  alert.setTitle("");
                  alert.setHeaderText("Please fill information");
                  alert.showAndWait();
            return false;

        }

        return checkEmptyTextFieldAndEmailValidate();
    }

    private boolean checkEmptyTextFieldAndEmailValidate(){
        if (txtFirstName.getText().equals("")){
            txtFirstName.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else
            txtFirstName.setStyle(StyleSheet.NULL_BORDER);

        if (txtLastName.getText().equals("")){
            txtLastName.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else
            txtLastName.setStyle(StyleSheet.NULL_BORDER);

        if (cbGender.getSelectionModel().getSelectedIndex() == -1){
            cbGender.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else
            cbGender.setStyle(StyleSheet.NULL_BORDER);

        if (cbSubject.getSelectionModel().getSelectedIndex() == -1){
            cbSubject.setStyle(StyleSheet.RED_BORDER);
            return false;
        }else
            cbSubject.setStyle(StyleSheet.NULL_BORDER);

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

        return true;
    }

    //Method Get data from hashmap
    private String getSubName(int id){
        return DashboardController.id_name_subject.get(id)
                .toString();
    }

    private int getSubID(String key){
        return (int)DashboardController.name_id_subject.get(key);

    }
}