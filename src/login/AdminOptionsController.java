package login;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userGroups.User;

import java.io.IOException;

public class AdminOptionsController{
    public void pressAddRemoveUser(ActionEvent event) throws IOException{
        Parent userOption = FXMLLoader.load(getClass().getResource("ViewUsers.fxml"));
        Scene viewUsers = new Scene(userOption, 400, 800);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(viewUsers);
        app_stage.centerOnScreen();
        app_stage.show();
    }

    public void pressAddRemoveClass(ActionEvent event) throws IOException{
        Parent userOption = FXMLLoader.load(getClass().getResource("ViewClasses.fxml"));
        Scene viewClases = new Scene(userOption, 400, 800);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(viewClases);
        app_stage.centerOnScreen();
        app_stage.show();
    }

    public void pressLogOff(ActionEvent event) throws IOException{
        Parent login = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        User.logOff(event, login);
    }

    public void pressExit(ActionEvent event){
        User.exit();
    }
}