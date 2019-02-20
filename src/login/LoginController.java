package login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import userGroups.Student;
import userGroups.User;

import java.io.IOException;
import java.util.LinkedList;

public class LoginController{
    @FXML
    private TextField username_input;
    @FXML
    private PasswordField password_input;
    private String userType;
    private User user;
    DBHandler dbHandler;

    public void initialize(){
        dbHandler = new DBHandler();
    }

    @FXML
    public void pressLoginButton(ActionEvent event) throws IOException{
        if(username_input.getText().equals("") || password_input.getText().equals("")) {
            User.showAlert("Empty username and/or password.");
            return;
        }

        user = dbHandler.login(username_input.getText(), password_input.getText());
        userType = user.getUserType();

        if(user == null){
            User.showAlert("Wrong username or password. Please try again.");
            return;
        }
        //Figures out which .fxml file to load for what userType
        FXMLLoader loader = new FXMLLoader();
        if(userType.equals("student")){
            loader.setLocation(getClass().getResource("studentOptions.fxml"));
            loader.load();

            StudentOptionsController display = loader.getController();
            display.setUser((Student) user);
        }
        else if(userType.equals("admin")){
            loader.setLocation(getClass().getResource("AdminOptions.fxml"));
            loader.load();
        }
        //loads new scene and closes database for this page
        Parent userOption = loader.getRoot();
        Scene loginScene = new Scene(userOption);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(loginScene);
        app_stage.setResizable(false);
        app_stage.show();
        dbHandler.closeConnection();
    }

    @FXML
    public void pressNewUserButton(ActionEvent event) {

    }
}