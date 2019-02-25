package login;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import userGroups.User;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class ViewUsersController{
    @FXML
    GridPane grid;
    @FXML
    TextArea userName, password, firstName, lastName, email, cell, mailing;
    @FXML
    ComboBox userTypeComboBox;
    @FXML
    Button backButton;

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
        //gets unique user types from database and adds them to userTypeComboBox's items
        userTypeComboBox.setItems(FXCollections.observableArrayList(dbHandler.getAllUserTypes()));
    }
    //On button pressed, add user to corresponding table in database
    @FXML
    public void pressAddUser(ActionEvent event) throws IOException{
        if(userTypeComboBox.getValue() == null){
            User.showAlert("No User Type is selected.");
            return;
        }
        String userType = userTypeComboBox.getValue().toString();//finds what userType user selected

        //All users need a username and password
        inputValidation.validUserName(userName.getText().trim());
        inputValidation.validPassword(password.getText().trim());

        if(userType.equals("student")){
            inputValidation.validName(firstName.getText().trim());
            inputValidation.validName(lastName.getText().trim());
            inputValidation.validEmail(email.getText().trim());
            inputValidation.validNumber(cell.getText().trim());
            inputValidation.validAddress(mailing.getText().trim());
        }

        if(inputValidation.getAllErrorMessages().equals("")){
            if(userType.equals("student") && inputValidation.getAllErrorMessages().equals("")){
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
            else if(userType.equals("admin") && inputValidation.getAllErrorMessages().equals("")){
                User user = new User(userName.getText(), password.getText());
                user.setUserType(userType);
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
        label.setTooltip(new Tooltip("Right click for more options."));
        label.getStyleClass().add("userInfoLabel");
        final ContextMenu menu = new ContextMenu();
        MenuItem removeUser = new MenuItem("Remove User");
        menu.getItems().addAll(removeUser);
        removeUser.setOnAction(event -> {
            if(user.getUserName().equals("admin")){
                User.showAlert("Cannot delete the main system administrator.");
                return;
            }

            dbHandler.deleteUser(user);
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