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

public class ViewUsersController{
    @FXML
    GridPane grid;
    @FXML
    TextArea userName, password, firstName, lastName, email, cell, mailing;
    DBHandler dbHandler;
    InputValidation inputValidation;
    private int rowIndex;

    public void initialize(){
        dbHandler = new DBHandler();
        rowIndex = 0;
        inputValidation = new InputValidation();
        List<User> users = dbHandler.getUsers();
        //assuming all columns are not null
        for(User user : users){
            addUserLabel(user);
        }
    }
    //On button pressed, add user to corresponding table in database
    @FXML
    public void pressAddUser(ActionEvent event) throws IOException{
        String userType;

        if(userName.getText().length() >= 5 && userName.getText().trim().substring(0, 5).equals("admin"))
            userType = "Admin";
        else
            userType = "Student";

        //All users need a username and password
        inputValidation.validUserName(userName.getText().trim());
        inputValidation.validPassword(password.getText().trim());

        if(userType.equals("Student")){
            inputValidation.validName(firstName.getText().trim());
            inputValidation.validName(lastName.getText().trim());
            inputValidation.validEmail(email.getText().trim());
            inputValidation.validNumber(cell.getText().trim());
            inputValidation.validAddress(mailing.getText().trim());
        }

        if(inputValidation.getAllErrorMessages().equals("")){
            if(userType.equals("Student") && inputValidation.getAllErrorMessages().equals("")){
                User user = new User(
                        userName.getText().trim(),
                        password.getText().trim(),
                        firstName.getText().trim(),
                        lastName.getText().trim(),
                        email.getText().trim(),
                        cell.getText().trim(),
                        mailing.getText().trim(),
                        userType);

                dbHandler.addUser(user);
                addUserLabel(user);
            }
            else if(userType.equals("Admin") && inputValidation.getAllErrorMessages().equals("")){
                User user = new User(userName.getText(), password.getText());
                dbHandler.addUser(user);
                addUserLabel(user);
            }
        }
        else{
            User.showAlert(inputValidation.getAllErrorMessages());//shows an Alert of all the different errors
            inputValidation.clear();//clears all errors
        }
    }
    //Adds a new label to grid showing the User Type, Username, and Full Name of the General User
    //Adds a context menu to the label that allows the user to remove a user
    private void addUserLabel(User user){
        Label label = new Label("User Type: " + user.getUserType() +
                " Username: " + user.getUserName() +
                " Name: " + user.getFullName());
        final ContextMenu menu = new ContextMenu();
        MenuItem removeUser = new MenuItem("Remove User");
        menu.getItems().addAll(removeUser);
        removeUser.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                if(user.getUserType().equals("Admin")){
                    User.showAlert("Cannot delete the main system administrator.");
                    return;
                }

                dbHandler.deleteUser(user);
                label.setDisable(true);
            }
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