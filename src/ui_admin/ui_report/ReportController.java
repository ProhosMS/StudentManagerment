package ui_admin.ui_report;/**
 * Created by Prohos on 12/2/2016.
 */


import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import datamanager.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import ui_admin.DashboardController;

import javax.swing.*;

public class ReportController implements Initializable {

    private ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();

    private ObservableList<PieChart.Data> chartDataSubject = FXCollections.observableArrayList();

    @FXML
    private PieChart chartSubject;

    @FXML
    private PieChart chartGender;

    @FXML
    private Label lb1;

    @FXML
    private Label lb2;

    @FXML
    private JFXButton btnShowReport;

    @FXML
    private JFXComboBox<String> cbSubject;

    @FXML
    private JFXComboBox<String> cbReportType;

    @FXML
    private JFXComboBox<String> cbGender;

    ArrayList<String> subject = new ArrayList<>();

    HashMap<String,Object> param = new HashMap<>();

    //todo:
    String reportPath = new String();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getChartSubjectData();
        getChartGenderData();
        setDataToComboReportType();
        setDataToComboSubject();
        setDataToComboGender();
        pieChartSubjectMouseEven();
        pieChartGenderMouseEvent();
    }

    //On Action
    @FXML
    void clickCbReportType(ActionEvent event) {
        cbGender.setDisable(false);
        cbSubject.setDisable(false);
        btnShowReport.setDisable(false);
    }

    @FXML
    void clickShowReport(ActionEvent event) throws Exception {
        if (!checkComboValidation())
            return;
        reportParamCondition();
        Connection con = ConnectionManager.getConnection();
      //  String reportPath1 = "src/ui_admin/ui_report/studentReportParamSub.jasper";
       // param.put("std_gender",cbGender.getValue());
       // param.put("sub_name",cbSubject.getValue());
        JasperPrint print = JasperFillManager.fillReport(reportPath,param,con);
        JasperViewer.viewReport(print,false);



    }


    //Method block
    private void getChartSubjectData(){
        try {
            chartData.clear();
            Connection con = ConnectionManager.getConnection();
            String sql = "SELECT sub_id,sub_name FROM tbl_subject";
            String sql1 = "SELECT \"count\"(std_sub_id) FROM tbl_student WHERE std_sub_id=? AND isPaid=True";
            Statement statement = con.createStatement();
            PreparedStatement statement1 = con.prepareStatement(sql1);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
              //  System.out.println(resultSet.getString("sub_name"));
               // System.out.println(resultSet.getInt("sub_id"));
                statement1.setInt(1,resultSet.getInt("sub_id"));
                ResultSet resultSet1 = statement1.executeQuery();
                while (resultSet1.next()){
                   // System.out.println(resultSet1.getInt(1));
                    chartData.add(new PieChart.Data(resultSet.getString("sub_name"),resultSet1.getInt(1)));
                    subject.add(resultSet.getString("sub_name"));
                }

            chartSubject.setData(chartData);
            resultSet1.close();
            }

            resultSet.close();
        }catch (Exception e){

        }
    }

    private void getChartGenderData(){
        try {
            Connection con = ConnectionManager.getConnection();
            String sql = "SELECT \"count\"(std_gender) FROM tbl_student WHERE std_gender='Male' AND ispaid=TRUE";
            String sql1 = "SELECT \"count\"(std_gender) FROM tbl_student WHERE std_gender='Female' AND ispaid=TRUE";
            Statement statement = con.createStatement();
            Statement statement1 = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSet resultSet1 = statement1.executeQuery(sql1);
            while (resultSet.next() && resultSet1.next()){
                chartDataSubject.add(new PieChart.Data("Male",resultSet.getInt(1)));
                chartDataSubject.add(new PieChart.Data("Female",resultSet1.getInt(1)));
            }
            resultSet.close();
            resultSet1.close();
            chartGender.setData(chartDataSubject);
        }catch (Exception e){

        }
    }

    private void pieChartSubjectMouseEven(){
        chartSubject.getData()
                    .stream()
                    .forEach(data -> {
                        data.getNode().addEventHandler(MouseEvent.ANY, e -> {
                            lb1.setText("There are "+(int)data.getPieValue()+" students studied "+data.getName());
                        });
                    });
    }

    private void pieChartGenderMouseEvent(){
        chartGender.getData()
                .stream()
                .forEach(data -> {
                    data.getNode().addEventHandler(MouseEvent.ANY, e -> {
                        lb2.setText("Total "+data.getName()+" student is "+(int)data.getPieValue());
                    });
                });
    }

    //About Component method and validation
    private void setDataToComboReportType(){
        String[] reportType = new String[]{"Approve Student","Pending Student"};
        Arrays.stream(reportType)
                .forEach(e->cbReportType.getItems().add(e));
    }

    private void setDataToComboSubject(){
        cbSubject.getItems().add("All Subject");
        subject.forEach(e->cbSubject.getItems().add(e));
    }

    private void setDataToComboGender(){
        String[] gender = new String[]{"All Gender","Male","Female"};
        Arrays.stream(gender)
                .forEach(e->cbGender.getItems().add(e));
    }

    //About Component behavior
    private boolean checkComboValidation(){
        if (cbGender.getSelectionModel().getSelectedIndex()==-1){
            Alert alert= new Alert(Alert.AlertType.ERROR);
                  alert.setTitle("ERROR");
                  alert.setHeaderText("Please select GENDER");
                  alert.showAndWait();
            return false;
        }

        if (cbSubject.getSelectionModel().getSelectedIndex()==-1){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Please select SUBJECT");
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private void reportParamCondition() throws Exception{

        param.clear();
        if (cbReportType.getSelectionModel().getSelectedIndex()==0){
             reportPath = "src/ui_admin/ui_report/studentReportParamSub.jasper";
            if (cbGender.getSelectionModel().getSelectedIndex()==0){
                String object = "";
                param.put("std_gender",object);
            }else
                param.put("std_gender",cbGender.getValue());

            if (cbSubject.getSelectionModel().getSelectedIndex() == 0){
                String object = "";
                param.put("sub_name",object);
            }else
                param.put("sub_name",cbSubject.getValue());
        }else if(cbReportType.getSelectionModel().getSelectedIndex()==1){
            reportPath = "src/ui_admin/ui_report/studentReportParamSubPending.jasper";
            String object = "";
            if (cbGender.getSelectionModel().getSelectedIndex()==0){
                param.put("std_gender",object);
            }else
                param.put("std_gender",cbGender.getValue());
            if (cbSubject.getSelectionModel().getSelectedIndex() == 0){
                param.put("sub_name",object);
            }else
                param.put("sub_name",cbSubject.getValue());
        }
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