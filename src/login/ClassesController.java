package login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    private GridPane grid;
    DBHandler dbHandler;

    public void initialize(){
        dbHandler = new DBHandler();
        List<Class> classes = dbHandler.getClasses();
        int currentCol = 0;
        int currentRow = 1;
        //assuming all columns are NOT NULL
        for(Class aClass : classes){
            Label label1 = new Label();
            label1.setText(aClass.getClassName());
            label1.setAccessibleText(aClass.getClassName());
            grid.add(label1, currentCol++, currentRow);

            Label label2 = new Label();
            label2.setText(aClass.getSize().toString());
            grid.add(label2, currentCol++, currentRow);

            Label label3 = new Label();
            label3.setText(aClass.getSpotsTaken().toString());
            grid.add(label3, currentCol++, currentRow);

            Label label4 = new Label();
            label4.setText(aClass.getInstructor());
            grid.add(label4, currentCol++, currentRow);

            Label label5 = new Label();
            label5.setText(aClass.getCRN());
            grid.add(label5, currentCol++, currentRow);

            Label label6 = new Label();
            label6.setText(aClass.getDays());
            grid.add(label6, currentCol++, currentRow);

            Label label7 = new Label();
            label7.setText(aClass.getStartTime().militaryToRegularTime() + "-"
                    + aClass.getEndTime().militaryToRegularTime());
            grid.add(label7, currentCol++, currentRow);

            Label label8 = new Label();
            label8.setText(aClass.getSubject());
            grid.add(label8, currentCol++, currentRow);

            Button addClass = new Button();
            addClass.setMaxWidth(Double.MAX_VALUE);
            addClass.setText("Add Class");
            grid.add(addClass, currentCol, currentRow);
            addClass.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e){
                    if(Class.addClassCheck(user, aClass, dbHandler))
                        label3.setText(aClass.getSpotsTaken().toString());

                    addClass.setDisable(true);
                }
            });
            currentCol = 0;
            currentRow++;
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