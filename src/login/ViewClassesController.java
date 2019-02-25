package login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
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
            addCourseLabel(aClass);
        }
    }
    //Add Class handler. Takes all the info from text areas, validates them, and if they are validated add to database
    public void pressAddClass(ActionEvent event) throws IOException{
        InputValidation validator = new InputValidation();
        validator.validateClass(className.getText(), size.getText(), instructor.getText(), CRN.getText(),
                days.getText(), startTime.getText(), endTime.getText(), subject.getText());

        if(validator.getAllErrorMessages().equals("")){
            Class aClass = new Class(
                    className.getText(),
                    Integer.parseInt(size.getText()),
                    instructor.getText(),
                    CRN.getText(),
                    days.getText(),
                    startTime.getText(),
                    endTime.getText(),
                    subject.getText()
            );
            aClass.setSpotsTaken(0);
            dbHandler.addCourse(aClass);
            addCourseLabel(aClass);
        }
        else{
            User.showAlert(validator.getAllErrorMessages());//shows an Alert of all the different errors
            validator.clear();//clears all errors
        }
    }
    //Adds a new label to grid showing the User Type, Username, and Full Name of the General User
    //Adds a context menu to the label that allows the user to remove a user
    private void addCourseLabel(Class aClass){
        Label label = new Label("Class Name: " + aClass.getClassName() +
                " Instructor: " + aClass.getInstructor() +
                " CRN: " + aClass.getCRN());
        label.setTooltip(new Tooltip("Right click for more options."));
        label.getStyleClass().add("userInfoLabel");
        final ContextMenu menu = new ContextMenu();
        MenuItem removeUser = new MenuItem("Remove Class");
        menu.getItems().addAll(removeUser);
        removeUser.setOnAction(event -> {
            dbHandler.removeCourse(aClass);
            label.setDisable(true);
        });
        label.setContextMenu(menu);
        grid.add(label, 0, rowIndex++);
    }
    //On button pressed, change stage to the previous stage and closes database connection
    public void pressBack(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("AdminOptions.fxml"));
        dbHandler.closeConnection();
        User.back(event, parent);
    }
}