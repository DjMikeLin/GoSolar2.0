package login;

import userGroups.Student;
import userGroups.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHandler{
    private Connection connection;
    //Handles reads, writes, connections. and closing connections with the sqlite database for the entire project.
    public DBHandler(){
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            connection.setAutoCommit(false);
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
    }
    //Returns a list of Class from classes table
    public List<Class> getClasses(){
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
    //Returns a list of classes based on the String CRNs given. CRNS can be empty Strings
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
                new Time(rs.getString("STARTTIME")),
                new Time(rs.getString("ENDTIME")),
                rs.getString("AREA")
            );
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
        return null;
    }

    public List<User> getUsers(){
        ArrayList<User> data = new ArrayList();
        getFromUserTable("Select ID, USERNAME, PASS, USERTYPE From Users, User_Types Where User_Types.USERID = Users.ID AND User_Types.USERTYPE = 'admin';",
                data);//get admin data from Users table
        return getFromUserTable("select ID, USERNAME, PASS, FIRSTNAME, LASTNAME, USERTYPE from Users, Students, User_Types where students.STUDENTID = users.ID AND User_Types.USERID = USERS.ID;",
                data);//get data from Students table and returns all the data from all users
    }

    private List<User> getFromUserTable(String query, ArrayList<User> data){
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                User user = createUser(rs);
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
    private User createUser(ResultSet rs){
        try{
            String userType = rs.getString("USERTYPE");
            if(userType.equals("admin")){
                return new User(
                        Integer.parseInt(rs.getString("ID")),
                        rs.getString("USERNAME"),
                        rs.getString("PASS"),
                        userType
                );
            }
            else if(userType.equals("student")){
                return new User(
                        Integer.parseInt(rs.getString("ID")),
                        rs.getString("USERNAME"),
                        rs.getString("PASS"),
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
    public boolean deleteUser(User user){
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
    //returns a List of all unique user types from the database
    public List<String> getAllUserTypes(){
        List<String> list = new ArrayList<>();
        try{
            String query = "Select Distinct USERTYPE from User_Types;";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                list.add(rs.getString("USERTYPE"));
            }
            rs.close();
            statement.close();
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
        return list;
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
    //Updates SPOTSTAKEN column for one class. increments by 1 if increase is true or vice versa
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
    //Returns a User Object with userName, pass, userID, and UserType; null if userName and pass combination is not found.
    public User findUser(String userName, String pass){
        User user = new User(userName, pass);
        try{
            String query = "SELECT ID, USERTYPE FROM Users, User_Types WHERE" +
                    " Users.USERNAME = '"+ userName + "' AND" +
                    " Users.PASS = '"+ pass +"' AND Users.ID = User_Types.USERID;";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            //Returns a null if the userName and pass combination is not found in the database
            if(!rs.isBeforeFirst())
                return null;

            while(rs.next()){
                user.setId(rs.getInt("ID"));
                user.setUserType(rs.getString("USERTYPE"));
            }
            rs.close();
            statement.close();
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
        return user;
    }
    //Returns a Student object based on information retrieved from database and what information was already in user
    public Student studentLogin(User user){
        Student student = new Student(
                user.getId(),
                user.getUserName(),
                user.getPassword(),
                user.getUserType()
        );

        try{
            String query = "SELECT * FROM Students WHERE STUDENTID= " + "'" + user.getId() + "'";
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                student.setFirstName(rs.getString("FIRSTNAME"));
                student.setLastName(rs.getString("LASTNAME"));
                student.setEmail("EMAIL");
                student.setCell("CELL");
                student.setMailingAddress("MAILING");
            }
            student.setSchedule(createTermSchedule(user.getUserName()));
        }
        catch(SQLException e){
            printStackOnSqlException(e);
        }
        return student;
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
                        new Time(rs.getString("STARTTIME")),
                        new Time(rs.getString("ENDTIME"))
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