package login;

import userGroups.Student;
import userGroups.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DBHandler
{
    private Connection connection;

    public DBHandler()
    {
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            connection.setAutoCommit(false);
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
    }
    //Returns a list of Class from classes table
    public List<Class> getClasses()
    {
        String query = "SELECT * FROM Classes";
        List<Class> classesData = new ArrayList();
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                classesData.add(makeClass(rs));
            }
            rs.close();
            statement.close();
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
        return classesData;
    }
    //Returns a list of classes based on the String CRNs given
    public List<Class> getClasses(String CRN1, String CRN2, String CRN3){
        List<Class> classList = new ArrayList();
        try{
            String sqlQuery = "SELECT * FROM Classes WHERE CRN = '" + CRN1 + "' OR CRN = '"
                    + CRN2 + "' OR CRN = '" + CRN3 +"';";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sqlQuery);

            while(rs.next()){
                classList.add(makeClass(rs));
            }

            rs.close();
            statement.close();
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
        return classList;
    }
    //Creates and returns a Class based on results from the Result Set
    private Class makeClass(ResultSet rs){
        try{
            return new Class(
                rs.getInt("IDCLASSES"),
                rs.getString("CLASSNAME"),
                rs.getInt("SIZE"),
                rs.getInt("SPOTSTAKEN"),
                rs.getString("INSTRUCTOR"),
                rs.getString("CRN"),
                rs.getString("DAYS"),
                getTime(rs.getString("STARTTIME")),
                getTime(rs.getString("ENDTIME")),
                rs.getString("AREA")
            );
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
        return null;
    }

    public Time getTime(String time){
        String[] arr = time.split(":");
        return new Time(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
    }

    public List<User> getUsers(){
        //select * from users INNER JOIN students ON students.STUDENTID = users.ID;
        ArrayList<User> data = new ArrayList();
        getFromUserTable("SELECT * FROM Users", "Admin", data);//get data from Users table
        return getFromUserTable("SELECT * FROM Students", "Student", data);//get data from Students table and returns all the data from all users
    }

    private List<User> getFromUserTable(String query, String userType, ArrayList<User> data){
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                User user = createUser(rs, userType);
                data.add(user);
            }
            rs.close();
            statement.close();
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
        return data;
    }
    //Returns a User of a specific type
    private User createUser(ResultSet rs, String userType){
        try{
            if(userType.equals("Admin")){
                return new User(
                        Integer.parseInt(rs.getString("ID")),
                        rs.getString("USERNAME"),
                        rs.getString("PASS"),
                        userType
                );
            }
            else if(userType.equals("Student")){
                return new User(
                        Integer.parseInt(rs.getString("STUDENTID")),
                        rs.getString("SUSERNAME"),
                        rs.getString("FIRSTNAME"),
                        rs.getString("LASTNAME"),
                        userType
                );
            }
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
        return new User();//Should never reach this
    }
    //deletes a user from the corresponding table
    public boolean deleteUser(User user)
    {
        String sqlQuery;

        if(user.getUserName().equals("admin"))
            sqlQuery = "DELETE FROM Users WHERE USERNAME = ?";
        else
            sqlQuery = "DELETE FROM Students WHERE SUSERNAME = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, user.getUserName());
            statement.executeUpdate();
            connection.commit();
            statement.close();
            return true;
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
        return false;
    }
    //adds a user of a specific userType to its corresponding table
    public boolean addUser(User user)
    {
        String sqlQuery;

        try{
            if(user.getUserType().equals("admin")){
                sqlQuery = "INSERT INTO Users(USERNAME, PASS) VALUES (?, ?);";
                PreparedStatement statement = connection.prepareStatement(sqlQuery);
                statement.setString(1, user.getUserName());
                statement.setString(2, user.getPassword());
                statement.executeUpdate();
                connection.commit();
                statement.close();
            }
            else{
                sqlQuery = "INSERT INTO Students(SUSERNAME, STUDENTPASS, FIRSTNAME, LASTNAME, EMAIL, CELL, MAILING)" +
                           " VALUES (?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement statement = connection.prepareStatement(sqlQuery);
                statement.setString(1, user.getUserName());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getFirstName());
                statement.setString(4, user.getLastName());
                statement.setString(5, user.getEmail());
                statement.setString(6, user.getCell());
                statement.setString(7, user.getMailingAddress());
                statement.executeUpdate();
                connection.commit();
                statement.close();
            }
            return true;
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
        return false;
    }
    //adds class at classIndex to student user in database in table CLASSES_HAVE_STUDENTS
    public void addClassToUser(int classIndex, String className, int userID, String userName){
        try{
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO CLASSES_HAVE_STUDENTS VALUES(?,?,?,?);");
            statement.setInt(1, classIndex);
            statement.setString(2, className);
            statement.setInt(3, userID);
            statement.setString(4, userName);
            statement.executeUpdate();
            connection.commit();
            statement.close();
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
    }
    //removes class from CLASSES_HAVE_STUDENTS for the specific student
    public void removeClassFromUser(int classIndex, int userID){
        try{
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM CLASSES_HAVE_STUDENTS WHERE CLASS_ID = ? and STUDENT_ID = ?;");
            statement.setInt(1, classIndex);
            statement.setInt(2, userID);
            statement.executeUpdate();
            connection.commit();
            statement.close();
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
    }

    public void updateSpotsTaken(boolean increase, int IDCLASSES){
        try{
            PreparedStatement statement;
            //if spotsTaken is increasing update the class SPOTSTAKEN increasing 1 if not vice versa
            if(increase){
                statement = connection.prepareStatement(
                        "UPDATE CLASSES SET SPOTSTAKEN = SPOTSTAKEN + 1 WHERE IDCLASSES = ?;");
            }
            else{
                statement = connection.prepareStatement(
                        "UPDATE CLASSES SET SPOTSTAKEN = SPOTSTAKEN - 1 WHERE IDCLASSES = ?;");
            }

            statement.setInt(1, IDCLASSES);
            statement.executeUpdate();
            connection.commit();
            statement.close();
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
    }
    //Returns a User if it exists in the database; returns a null if it doesn't exist.
    public User login(String userName, String password){
        try{
            Statement statement = connection.createStatement();
            //finds the unique userID for the user
            ResultSet rs = statement.executeQuery(userIDQuery(userName));
            int userID = Integer.parseInt(rs.getString("ID"));
            //finds the userType for the user
            rs = statement.executeQuery(userTypeQuery(userID));
            String userType = rs.getString("USERTYPE");

            if(userType.equals("student")){
                rs = statement.executeQuery(userQuery(userType, userName, password, userID));
                while(rs.next()){
                    return new Student(
                            userID,
                            userName,
                            password,
                            userType,
                            rs.getString("FIRSTNAME"),
                            rs.getString("LASTNAME"),
                            rs.getString("EMAIL"),
                            rs.getString("CELL"),
                            rs.getString("MAILING"),
                            createTermSchedule(userName)
                    );
                }
            }
            else if(userType.equals("admin")){
                while(rs.next()){
                    return new User(
                            userID,
                            userName,
                            password,
                            userType
                    );
                }
            }
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
        return null;
    }
    //Returns the current schedule the user has from the database
    public Schedule createTermSchedule(String userName){
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT IDCLASSES, CLASSNAME, INSTRUCTOR, CRN, DAYS, STARTTIME, ENDTIME" +
                    " FROM Classes c, Classes_Have_Students cs " +
                    "WHERE c.IDCLASSES = cs.CLASS_ID AND S_USERNAME = '" + userName + "';");

            Class aClass;
            Schedule schedule = new Schedule();

            while(rs.next()){
                aClass = new Class(
                        rs.getInt("IDCLASSES"),
                        rs.getString("CLASSNAME"),
                        rs.getString("INSTRUCTOR"),
                        rs.getString("CRN"),
                        rs.getString("DAYS"),
                        getTime(rs.getString("STARTTIME")),
                        getTime(rs.getString("ENDTIME"))
                );

                schedule.updateSchedule(aClass);
            }

            rs.close();
            statement.close();
            return schedule;
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }

        return new Schedule();
    }
    //Returns a String query for a specific type of user based on userName and password
    private String userQuery(String userType, String userName, String password, int userID){
        if(userType.equals("admin")){
            return  "SELECT * FROM Users WHERE USERNAME= " + "'" + userName + "'"
                    + "AND PASS= " + "'" + password + "'";
        }
        else if(userType.equals("student")){
            return  "SELECT * FROM Students WHERE STUDENTID= " + "'" + userID + "'";
        }

        return null;
    }
    //Returns a String Query to find the USERID from the Users table for the current user login
    private String userIDQuery(String userName){
        return "SELECT * FROM Users WHERE USERNAME= " + "'" + userName + "'";
    }
    //Returns a String Query to find the USERTYPE from the User_Types table for the current user
    private String userTypeQuery(int userID){
        return "SELECT USERTYPE FROM User_Types WHERE USERID= " + "'" + userID + "'";
    }
    //assumes militaryTime is in '00:00:00' format. returns regular time in '00:00' format
    public String militaryToRegularTime(String militaryTime)
    {
        int hour = Integer.parseInt(militaryTime.substring(0, 2));

        if(hour > 12){
            hour = hour - 12;
            return "0" + hour + militaryTime.substring(2, 5) + " pm";
        }
        return militaryTime.substring(0, 5) + " am";
    }
    //Closes the connection to the database
    public void closeConnection(){
        try{
            connection.close();
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
    }
    //prints the SQLException stacktrace. Shows an alert to the current user and exits the application
    public void printStackOnSqlException(SQLException e){
        e.printStackTrace();
        User.showAlert("Please contact an admin.");
        System.exit(0);
    }
}