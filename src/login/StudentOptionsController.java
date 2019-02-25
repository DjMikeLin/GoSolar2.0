package login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import userGroups.Student;
import userGroups.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StudentOptionsController{
    @FXML
    private TextArea text;
    @FXML
    private GridPane grid;//gridpane from ClassesToAdd.fxml
    @FXML
    private GridPane mainGrid;//scrollpane from AddDropClasses.fxml
    private Student user;

    public void pressLookUp(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ClassesToAdd.fxml"));
        loader.load();

        ClassesController display = loader.getController();
        display.setUser(user);

        Parent userOption = loader.getRoot();
        grid = loader.getRoot();
        Scene classesToAddScene = new Scene(userOption);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(classesToAddScene);
        app_stage.centerOnScreen();
        app_stage.show();
    }

    public void pressAddOrDropClasses(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddDropClasses.fxml"));
        loader.load();

        AddDropController display = loader.getController();
        display.setUser(user);
        display.initialize(user);

        Parent userOption = loader.getRoot();
        mainGrid = loader.getRoot();
        Scene AddDropScene = new Scene(userOption);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(AddDropScene);
        app_stage.show();
    }

    public void pressAgreement(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("registrationAgreement.fxml"));
        loader.setController(this); // Make this instance the controller
        Parent root = loader.load();
        Stage textFileStage = new Stage();
        textFileStage.setResizable(false);
        textFileStage.setTitle("Registration Agreement");
        textFileStage.setScene(new Scene(root, 400, 400));
        textFileStage.show();

        FileReader fr = new FileReader("C:\\Users\\linmi\\Desktop\\GoSolar 2.0\\GoSolar 2.0\\src\\login\\agreement.txt");
        BufferedReader br = new BufferedReader(fr);
        String line;
        while((line = br.readLine()) != null) {
            text.appendText("\n" + line);
        }
        br.close();
        fr.close();
    }

    public void pressLogOff(ActionEvent event) throws IOException{
        Parent login = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        User.logOff(event, login);
    }

    public void pressExit(ActionEvent event){
        User.exit();
    }

    public void setUser(Student user){
        this.user = user;
    }
}