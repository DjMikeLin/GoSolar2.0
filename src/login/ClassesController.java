package login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import userGroups.Student;
import userGroups.User;

import java.io.IOException;
import java.util.List;

public class ClassesController{
    private Student user;
    @FXML
    private GridPane grid, displayGrid;
    DBHandler dbHandler;

    public void initialize(){
        dbHandler = new DBHandler();
        List<Class> classes = dbHandler.getClasses();
        int currentCol = 0;
        int gridRow = 1;
        //assuming all columns are NOT NULL
        for(Class aClass : classes){
            GridPane classInfoPane = new GridPane();
            classInfoPane.getStyleClass().add("userInfoGrid");
            classInfoPane.getColumnConstraints().addAll(displayGrid.getColumnConstraints());

            Label label1 = new Label();
            label1.setText(aClass.getClassName());
            label1.setAccessibleText(aClass.getClassName());
            classInfoPane.add(label1, currentCol++, 0);

            Label label2 = new Label();
            label2.setText(aClass.getSize().toString());
            classInfoPane.add(label2, currentCol++, 0);

            Label label3 = new Label();
            label3.setText(aClass.getSpotsTaken().toString());
            classInfoPane.add(label3, currentCol++, 0);

            Label label4 = new Label();
            label4.setText(aClass.getInstructor());
            classInfoPane.add(label4, currentCol++, 0);

            Label label5 = new Label();
            label5.setText(aClass.getCRN());
            classInfoPane.add(label5, currentCol++, 0);

            Label label6 = new Label();
            label6.setText(aClass.getDays());
            classInfoPane.add(label6, currentCol++, 0);

            Label label7 = new Label();
            label7.setText(aClass.getStartTime().militaryToRegularTime() + " - "
                    + aClass.getEndTime().militaryToRegularTime());
            classInfoPane.add(label7, currentCol++, 0);

            Label label8 = new Label();
            label8.setText(aClass.getSubject());
            classInfoPane.add(label8, currentCol++, 0);

            Button addClass = new Button();
            addClass.setText("Add Class");
            classInfoPane.add(addClass, currentCol, 0);
            addClass.setOnAction(event -> {
                if(Class.addClassCheck(user, aClass, dbHandler))
                    label3.setText(aClass.getSpotsTaken().toString());

                addClass.setDisable(true);
            });

            grid.add(classInfoPane, 0, gridRow++);
            currentCol = 0;
        }
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