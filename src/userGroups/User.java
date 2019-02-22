package userGroups;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class User{
    private String userName, password, userType, firstName, lastName, email, cell, mailingAddress;
    private Integer id;//unique user ID based on which row it is on in the database

    public User(){}

    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }
    //mainly a contructor to create a User of type admin
    public User(Integer id, String userName, String password, String userType){
        this.userName = userName;
        this.password = password;
        this.id = id;
        this.userType = userType;
    }

    public User(String userName, String password, String firstName, String lastName, String email, String cell, String mailingAddress, String userType){
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cell = cell;
        this.mailingAddress = mailingAddress;
        this.userType = userType;
    }

    public User(Integer id, String userName, String password, String firstName, String lastName, String userType){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
    }

    public static void logOff(ActionEvent event, Parent login){
        Scene loginScene = new Scene(login);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(loginScene);
        app_stage.show();
    }

    public static void back(ActionEvent event, Parent parent){
        Scene scene = new Scene(parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }

    public static void showAlert(String alertMessage){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error!");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

    public static void exit(){
        System.exit(1);
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getPassword(){
        return password;
    }

    void setPassword(String password){
        this.password = password;
    }

    public void setId(int id){
        this.id = id;
    }

    public Integer getId(){
        return id;
    }

    public void setUserType(String userType) { this.userType = userType; }

    public String getUserType() { return userType; }

    public void setFullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFullName(){return firstName + " " + lastName;}

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){return email;}

    public void setCell(String cell){
        this.cell = cell;
    }

    public String getCell(){return  cell;}

    public void setMailingAddress(String mailingAddress){
        this.mailingAddress = mailingAddress;
    }

    public String getMailingAddress(){return mailingAddress;}

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}