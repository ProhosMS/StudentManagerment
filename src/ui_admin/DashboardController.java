/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_admin;

import com.jfoenix.controls.JFXButton;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import datamanager.CacheData;
import datamanager.ConnectionManager;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Prohos
 */
public class DashboardController implements Initializable {


    ObservableList<Student> list = FXCollections.observableArrayList();
    ObservableList<StudentApprove> listApprove = FXCollections.observableArrayList();
    public static HashMap name_id_subject = new HashMap<String, Integer>();
    public static HashMap id_name_subject = new HashMap<Integer, String>();
    public static Student rowData;

    @FXML
    private BorderPane rootPane;

    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<Student> tableView;
    @FXML
    private TableColumn<Student, Integer> colID;
    @FXML
    private TableColumn<Student, String> colName;
    @FXML
    private TableColumn<Student, String> colGender;
    @FXML
    private TableColumn<Student, String> colSubject;
    @FXML
    private TableColumn<Student, String> colEmail;
    @FXML
    private JFXButton btnAddMember;
    @FXML
    private JFXButton btnSubject;
    @FXML
    private JFXButton btnApprove;
    @FXML
    private JFXButton btnManage;
    @FXML
    private JFXButton btnReport;
    @FXML
    private JFXButton btnSetting;
    @FXML
    private JFXButton btnRefresh;
    @FXML
    private Label labelEmail;
    @FXML
    private TableView<StudentApprove> tableApprove;

    @FXML
    private TableColumn<StudentApprove, Integer> colIDAP;

    @FXML
    private TableColumn<StudentApprove, String> colNameAP;

    @FXML
    private TableColumn<StudentApprove, String> colGenderAP;

    @FXML
    private TableColumn<StudentApprove, String> colSubjectAP;

    @FXML
    private TableColumn<StudentApprove, String> colEmailAP;

    //Tab
    @FXML
    private Tab tabApprove;

    //MenuItem
    @FXML
    private MenuItem MenuItemAddMember;
    @FXML
    private MenuItem menuItemApprove;


    @Override
    public void initialize(URL url, ResourceBundle rb) {


        labelEmail.setText(CacheData.admin.getEmail());
        querySubjectToHashMap();
        initColPending();
        initColApprove();
        loadPendingTableData();
        loadApproveTableData();
        btnApprove.setDisable(true);

        //1280,818
        //Refresh button event
        btnRefresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadPendingTableData();
                loadApproveTableData();
                btnApprove.setDisable(true);
                menuItemApprove.setDisable(true);
            }
        });

        //Table Click event
        tableClick();

    }
    //action here
    @FXML
    void clickAddMember(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ui_admin/ui_addmember/ui_addmember.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/icon/Add user.png"));
        stage.setTitle("Add Member");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

    }

    @FXML
    void clickMenuAddMember(ActionEvent event) throws IOException{
        clickAddMember(event);
    }

    @FXML
    void clickAddSubject(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/ui_admin/ui_addsubject/ui_addsubject.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/icon/subject.png"));
        stage.setTitle("Manage Subject");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    void clickMenuAddSubject(ActionEvent event) throws IOException{
        clickAddSubject(event);
    }

    @FXML
    void clickApproveStudent(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/ui_admin/ui_approvestudent/ui_approvestudent.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/icon/subject.png"));
        stage.setTitle("Confirm Student");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    void clickMenuApprove(ActionEvent event) throws IOException{
        clickApproveStudent(event);
    }


    @FXML
    void clickManageStudent(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/ui_admin/ui_managestudent/ui_managestudent.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/icon/manage.png"));
        stage.setTitle("Manage Student");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    void clickMenuManage(ActionEvent event) throws  IOException{
        clickManageStudent(event);
    }

    @FXML
    void clickReport(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/ui_admin/ui_report/ui_report.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/icon/report.png"));
        stage.setTitle("Report");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    void clickMenuReport(ActionEvent event) throws IOException{
        clickReport(event);
    }

    @FXML
    void clickSetting(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ui_admin/ui_setting/ui_setting.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/icon/Settings.png"));
        stage.setTitle("Settings");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    void clickMenuSetting(ActionEvent event) throws IOException {
        clickSetting(event);
    }

    @FXML
    void typeOnTxtSearch(KeyEvent event) {
        if (tabApprove.isSelected()){
            searchData("std_full_name",listApprove,tableApprove,"True");
        }else {
            searchData("std_full_name",list,tableView,"False");
        }
    }

    @FXML
    void clickMenuAboutThisApp(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/ui_about/ui_aboutapp.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/icon/info.png"));
        stage.setTitle("About This APP");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    void clickMenuLogout(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/std_ms/FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/icon/login.png"));
        stage.setTitle("Log in");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        //close current stage
        rootPane.getScene().getWindow().hide();
    }


    @FXML
    void clickMenuDev(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ui_about/ui_aboutdev.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/icon/info.png"));
        stage.setTitle("About Developer");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    void clickMenuExit(ActionEvent event) {
        Platform.exit();
    }

    private void initColApprove() {
        colIDAP.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNameAP.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGenderAP.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colSubjectAP.setCellValueFactory(new PropertyValueFactory<>("subject"));
        colEmailAP.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void initColPending() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    //Table click even
    private void tableClick(){
        tableView.setRowFactory((tv) -> {
            TableRow<Student> row = new TableRow<>();
            row.setOnMouseClicked((event) -> {
                if(event.getClickCount()==1 && (!row.isEmpty())){
                    rowData = row.getItem();
                    btnApprove.setDisable(false);
                    menuItemApprove.setDisable(false);
                }else if(event.getClickCount()==2 && (!row.isEmpty())){
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/ui_admin/ui_approvestudent/ui_approvestudent.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.getIcons().add(new Image("/icon/subject.png"));
                        stage.setTitle("Confirm Student");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.show();
                    }catch (IOException e)
                    {

                    }
                }
            });
            return row;
        });
    }

    //Query data from database
    private void loadPendingTableData() {
        try {

            list.clear();
            
            Connection con = ConnectionManager.getConnection();
            String sql = "SELECT std_id,std_full_name,std_gender,std_sub_id,std_email FROM tbl_student WHERE isPaid=False";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {                
                list.add(new Student(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        getSubName(rs.getInt(4)),
                        rs.getString(5)
                ));
            }
            rs.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        
        tableView.getItems().setAll(list);
    }

    private void loadApproveTableData(){
        try {
            listApprove.clear();

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


                listApprove.add(new StudentApprove(id, name, gender, subject, email));

            }
            rs.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        tableApprove.getItems().setAll(listApprove);
    }

    //Get data from database and add to hashmap
    private void querySubjectToHashMap(){
        try{
            Connection con = ConnectionManager.getConnection();
            String querySubject = "SELECT * FROM tbl_subject";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(querySubject);
            while (rs.next()){
                name_id_subject.put(rs.getString("sub_name"),rs.getInt("sub_id"));
                id_name_subject.put(rs.getInt("sub_id"),rs.getString("sub_name"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Search data
    //@param colName for add column name in database
    private void searchData(String colName,ObservableList data,TableView table,String isPaidParam){
        data.clear();
        try {
            Connection con = ConnectionManager.getConnection();
            String sql = "SELECT * FROM tbl_student WHERE lower(" + colName + ") like lower(?) AND isPaid="+isPaidParam;

            PreparedStatement stm = con.prepareStatement(sql);

           stm.setString(1, "%"+ txtSearch.getText() + "%");

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                data.add(new Student(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        getSubName(rs.getInt(4)),
                        rs.getString(5)
                ));
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        table.getItems().setAll(data);
    }



    //Method Get data from hashmap
    private int getSubID(String key){
        return (int)name_id_subject.get(key);

    }

    private String getSubName(int id){
        return id_name_subject.get(id)
                .toString();
    }


    //class for get data from database
    public static class Student{
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty gender;
        private final SimpleStringProperty subject;
        private final SimpleStringProperty email;

        public Student(Integer id,String name,String gender,String subject,String email) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
            this.gender = new SimpleStringProperty(gender);
            this.subject = new SimpleStringProperty(subject);
            this.email = new SimpleStringProperty(email);
        
            
        }

        public Integer getId() {
            return id.get();
        }

        public String getName() {
            return name.get();
        }

        public String getGender() {
            return gender.get();
        }

        public String getSubject() {
            return subject.get();
        }

        public String getEmail() {
            return email.get();
        }
        
        
    }

    public static class StudentApprove{
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty gender;
        private final SimpleStringProperty subject;
        private final SimpleStringProperty email;

        public StudentApprove(Integer id,String name,String gender,String subject,String email) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
            this.gender = new SimpleStringProperty(gender);
            this.subject = new SimpleStringProperty(subject);
            this.email = new SimpleStringProperty(email);


        }

        public Integer getId() {
            return id.get();
        }

        public String getName() {
            return name.get();
        }

        public String getGender() {
            return gender.get();
        }

        public String getSubject() {
            return subject.get();
        }

        public String getEmail() {
            return email.get();
        }


    }
}
