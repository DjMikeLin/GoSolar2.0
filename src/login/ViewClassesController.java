package login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import userGroups.User;

import java.io.IOException;
import java.util.List;

public class ViewClassesController{
    @FXML
    private GridPane grid;
    @FXML
    private TextArea className, size, instructor, CRN, days, startTime, endTime, subject;
    private DBHandler dbHandler;
    private int rowIndex;

    public void initialize(){
        dbHandler = new DBHandler();
        rowIndex = 0;
        List<Class> classes = dbHandler.getClasses();
        //assuming all columns are not null
        for(Class aClass : classes){
            Label label = new Label("Class Name: " + aClass.getClassName() +
                    " Instructor: " + aClass.getInstructor() +
                    " CRN: " + aClass.getCRN());
            final ContextMenu menu = new ContextMenu();
            MenuItem removeUser = new MenuItem("Remove Class");
            menu.getItems().addAll(removeUser);
            removeUser.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    //dbHandler.
                    label.setDisable(true);
                }
            });
            label.setContextMenu(menu);
            grid.add(label, 0, rowIndex++);
        }
    }

    @FXML
    public void pressAddClass(ActionEvent event) throws IOException{

    }
    //On button pressed, change stage to the previous stage and closes database connection
    public void pressBack(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("AdminOptions.fxml"));
        dbHandler.closeConnection();
        User.back(event, parent);
    }
}