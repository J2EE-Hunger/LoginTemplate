/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.od.loginfx;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Dulal
 */
public class FXMLDocumentController implements Initializable {
    
     @FXML
    private AnchorPane main_form;

    @FXML
    private AnchorPane login_form;

    @FXML
    private TextField login_username;

    @FXML
    private PasswordField login_password;

    @FXML
    private TextField login_showpassword;

    @FXML
    private CheckBox login_show_password;

    @FXML
    private Button login_btn;

    @FXML
    private Button login_createAccount;

    @FXML
    private Hyperlink login_forgot_password;

    @FXML
    private AnchorPane signup_form;

    @FXML
    private TextField signup_username;

    @FXML
    private PasswordField signup_password;

    @FXML
    private Button signup_btn;

    @FXML
    private Button signup_loginAccount;

    @FXML
    private TextField signup_email;

    @FXML
    private PasswordField signup_cpassword;

    @FXML
    private ComboBox<?> signup_question;

    @FXML
    private TextField signup_answer;

    @FXML
    private AnchorPane forgot_form;

    @FXML
    private TextField forgot_username;

    @FXML
    private Button forgot_proceedBtn;

    @FXML
    private Button forgot_backBtn;

    @FXML
    private TextField forgot_answer;

    @FXML
    private ComboBox<?> forgot_question;

    @FXML
    private AnchorPane changePass_form;

    @FXML
    private Button changePass_proceedBtn;

    @FXML
    private Button changePass_backBtn;

    @FXML
    private PasswordField changePass_password;

    @FXML
    private PasswordField changePass_cpassword;
    
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;
    
    public Connection connectDB(){
         try {
             Class.forName("com.mysql.jdbc.Driver");
             Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/dsd","root","123");
             return connect;
         } catch (ClassNotFoundException ex) {
             Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
         } catch (SQLException ex) {
             Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
         }
         return null;
    }
    
    public void login(){
        AlertMessage alertMsg = new AlertMessage();
        if(login_username.getText().isEmpty()|| login_password.getText().isEmpty()){
            alertMsg.errorMessage("Username/Password fields are necessary to be filled");
        } else {
            String loginQuery = "SELECT username, password FROM users WHERE username = ?";
            connection = connectDB(); 
            
            if(login_show_password.isSelected()){
                login_password.setText(login_showpassword.getText());
            } else {
                login_showpassword.setText(login_password.getText());
            }
            
            try {
                preparedStatement = connection.prepareStatement(loginQuery);
                preparedStatement.setString(1, login_username.getText());
                resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    if(login_password.getText().equals(resultSet.getString("password"))){
                        alertMsg.successMessage("Login successfully");
                         Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        
                        Scene scene = new Scene(root);

                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
                        
                        login_btn.getScene().getWindow().hide();
                    } else {
                        alertMsg.errorMessage("Incorect password");
                    }
                } else {
                    alertMsg.errorMessage("Incorect username");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }            
        }
    }
    
    public void showPassword(){
        if(login_show_password.isSelected()){
            login_showpassword.setText(login_password.getText());
            login_showpassword.setVisible(true);
            login_password.setVisible(false);
        } else {
            login_password.setText(login_showpassword.getText());
            login_showpassword.setVisible(false);
            login_password.setVisible(true);
        }
    }
    
    public void forgotPassword(){
        AlertMessage alertMsg = new AlertMessage();
        if(forgot_username.getText().isEmpty()||forgot_question.getSelectionModel().getSelectedItem()==null ||forgot_answer.getText().isEmpty()){
            alertMsg.errorMessage("Please fill all the blank fields");
        } else {
            String checkData = "SELECT username, question, answer FROM users "
                    + "WHERE username=? AND question=? AND  answer=? ";
            connection = connectDB();
            try {
                preparedStatement =  connection.prepareStatement(checkData);
                preparedStatement.setString(1, forgot_username.getText());
                preparedStatement.setString(2, (String) forgot_question.getSelectionModel().getSelectedItem());
                preparedStatement.setString(3, forgot_answer.getText());
                
                resultSet = preparedStatement.executeQuery();
                
                if(resultSet.next()){
                    signup_form.setVisible(false);
                    login_form.setVisible(false);
                    forgot_form.setVisible(false);
                    changePass_form.setVisible(true);
                    
                    login_username.setText("");
                    login_password.setVisible(true);
                    login_password.setText("");
                    login_showpassword.setVisible(false);                    
                    login_show_password.setSelected(false);
                    
                    changePass_password.setText("");
                    changePass_cpassword.setText("");
                } else {
                    alertMsg.errorMessage("Incorrect information");
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void forgotListQuestion(){
        List<String> listQ = new ArrayList<>();
        for (String data : questionList) {
            listQ.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listQ);
        forgot_question.setItems(listData);
    }
    
    public void register(){
        AlertMessage alertMsg = new AlertMessage();
        if(signup_email.getText().isEmpty()||signup_username.getText().isEmpty()||signup_password.getText().isEmpty()
                ||signup_cpassword.getText().isEmpty()||signup_question.getSelectionModel().getSelectedItem()==null ||signup_answer.getText().isEmpty()){
            alertMsg.errorMessage("All fields are necessary to be filled");
        } else if (!signup_password.getText().equals(signup_cpassword.getText())){
            alertMsg.errorMessage("Password dose not match");
        } else if (signup_password.getText().length() < 8){
            alertMsg.errorMessage("Invalid password. atlest 8 characters needed");
        } else {
            String checkQuery = "select * from users where username = '"+signup_username.getText()+"' ";
            connection = connectDB();
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery(checkQuery);
                if(resultSet.next()){
                    alertMsg.errorMessage(signup_username.getText() + " is already taken");
                } else {
                    String insertQuery ="INSERT INTO users (email,username, password, question, answer, date) values (?,?,?,?,?,?)";
                    preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, signup_email.getText());
                    preparedStatement.setString(2, signup_username.getText());
                    preparedStatement.setString(3, signup_password.getText());
                    preparedStatement.setString(4, (String) signup_question.getSelectionModel().getSelectedItem());
                    preparedStatement.setString(5, signup_answer.getText());
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    preparedStatement.setString(6, String.valueOf(sqlDate));
                    
                    preparedStatement.execute();
                    
                    alertMsg.successMessage("Registerd Successfully.");
                    
                    registerClearFields();
                    
                    signup_form.setVisible(false);
                    login_form.setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // to clear all fields after register.
    public void registerClearFields(){
        signup_email.setText("");
        signup_username.setText("");
        signup_password.setText("");
        signup_cpassword.setText("");
        signup_question.getSelectionModel().clearSelection();
        signup_answer.setText("");
    }
    
    public void switchForm(ActionEvent event){
        if(event.getSource() == signup_loginAccount){
            signup_form.setVisible(false);
            login_form.setVisible(true);
            forgot_backBtn.setVisible(false);
            
        } else if(event.getSource() == login_createAccount){
            signup_form.setVisible(false);
            login_form.setVisible(true);
            
        }
    }
    
    public void changePassword(){
        AlertMessage alertMsg = new AlertMessage();
        if(changePass_password.getText().isEmpty()||changePass_cpassword.getText().isEmpty()){
            alertMsg.errorMessage("Please fill all blank fields");
        } else if(!changePass_password.getText().equals(changePass_cpassword.getText())){
            alertMsg.errorMessage("Password dose not match");
        }  else if(changePass_password.getText().length()<8){
            alertMsg.errorMessage("Invalid password, at least 8 characters needed");
        } else {
            String updateData ="UPDATE users SET password=?, update_date=? WHERE username =? ";
            connection = connectDB();
            try {
                preparedStatement = connection.prepareStatement(updateData);
                preparedStatement.setString(1, changePass_password.getText());
                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                preparedStatement.setString(2, String.valueOf(sqlDate));
                preparedStatement.setString(3, forgot_username.getText());
                
                preparedStatement.execute();
                
                alertMsg.successMessage("Successfully changed password");
                
                signup_form.setVisible(false);
                login_form.setVisible(true);
                forgot_form.setVisible(false);
                changePass_form.setVisible(false);
                
                changePass_password.setText("");
                changePass_cpassword.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void swichForm(ActionEvent evt){
        if(evt.getSource()== signup_loginAccount || evt.getSource()== forgot_backBtn){
            signup_form.setVisible(false);
            login_form.setVisible(true);
            forgot_form.setVisible(false);
            changePass_form.setVisible(false);
        } else if(evt.getSource()== login_createAccount){
            signup_form.setVisible(true);
            login_form.setVisible(false);
            forgot_form.setVisible(false);
            changePass_form.setVisible(false);
        } else if(evt.getSource() == login_forgot_password){
            signup_form.setVisible(false);
            login_form.setVisible(false);
            forgot_form.setVisible(true);
            changePass_form.setVisible(false);
            forgotListQuestion();
        } else if(evt.getSource() == changePass_backBtn){
            signup_form.setVisible(false);
            login_form.setVisible(false);
            forgot_form.setVisible(true);
            changePass_form.setVisible(false);
        }
    }
    private String [] questionList = {"What is your favorite food?", "What is your favorite color?",
                    "What is the name of your pet?", "What is your most favorite sport?"};
    
    public void questions(){
        List<String> listQ = new ArrayList<>();
        for (String data : questionList) {
            listQ.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listQ);
        signup_question.setItems(listData);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        questions();
        forgotListQuestion();
        showPassword();
    }    
    
}
