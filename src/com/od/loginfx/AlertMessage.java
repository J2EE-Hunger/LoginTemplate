/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.od.loginfx;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author Dulal
 */
public class AlertMessage {
    
    private Alert alert;
    
    public void errorMessage(String message){
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        
    }
    
    public void successMessage(String message){
        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informatiom Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        
    }
}
