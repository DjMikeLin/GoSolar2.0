package login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import userGroups.Student;
import userGroups.User;

import java.io.IOException;
import java.util.*;

public class AddDropController{
    private Student user;
    @FXML
    private GridPane grid;
    @FXML
    private TextField CRN1, CRN2, CRN3;
    DBHandler dbHandler;

    public void initialize(Student user){
        dbHandler = new DBHandler();
        HashSet<Class> classes = user.schedule().getClassesInSchedule();
        int currentCol = 0;
        int currentRow = 1;
        //assuming all columns are NOT NULL
        for(Class aClass : classes){
            Label label1 = new Label();
            label1.setText(aClass.getClassName());
            label1.setAccessibleText(aClass.getClassName());
            grid.add(label1, currentCol++, currentRow);

            Label label2 = new Label();
            label2.setText(aClass.getInstructor());
            grid.add(label2, currentCol++, currentRow);

            Label label3 = new Label();
            label3.setText(aClass.getCRN());
            grid.add(label3, currentCol++, currentRow);

            Label label4 = new Label();
            label4.setText(aClass.getDays());
            grid.add(label4, currentCol++, currentRow);

            Label label5 = new Label();
            label5.setText(aClass.getStartTime().militaryToRegularTime() + "-"
                    + aClass.getEndTime().militaryToRegularTime());
            grid.add(label5, currentCol++, currentRow);

            Button removeClass = new Button();
            removeClass.setMaxWidth(Double.MAX_VALUE);
            removeClass.setText("Remove Class");
            grid.add(removeClass, currentCol, currentRow);
            removeClass.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e){
                    //update database
                    dbHandler.removeClassFromUser(aClass.getId(), user.getId());
                    //update schedule
                    user.schedule().removeClassFromSchedule(aClass);
                    //Updates spots taken for that class
                    dbHandler.updateSpotsTaken(false, aClass.getId());
                    removeClass.setDisable(true);
                }
            });
            currentCol = 0;
            currentRow++;
        }
    }
    //Onclick for addClasses button. Attempts to add all the courses from CRN inputs into user schedule and update database
    public void pressAddClasses(MouseEvent mouseEvent) throws IOException{
        List<Class> classList = dbHandler.getClasses(CRN1.getText(), CRN2.getText(), CRN2.getText());
        for(Class c : classList){
            Class.addClassCheck(user, c, dbHandler);
        }
        CRN1.setText("");
        CRN2.setText("");
        CRN3.setText("");
    }
    //Changes stage to the previous stage
    public void pressBack(ActionEvent event) throws IOException{
        dbHandler.closeConnection();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("studentOptions.fxml"));
        loader.load();

        StudentOptionsController display = loader.getController();
        display.setUser(user);

        Parent parent = loader.getRoot();
        User.back(event, parent);
    }

    public void setUser(Student user){
        this.user = user;
    }
}