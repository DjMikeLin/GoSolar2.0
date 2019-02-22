package userGroups;

import login.Schedule;

public class Student extends User{
    private Schedule schedule;

    public Student(){}

    public Student(Integer id, String userName, String password, String userType, String firstName, String lastName, String email, String cell, String mailingAddress, Schedule schedule){
        setId(id);
        setUserName(userName);
        setPassword(password);
        setUserType(userType);
        setFullName(firstName, lastName);
        setEmail(email);
        setCell(cell);
        setMailingAddress(mailingAddress);
        this.schedule = schedule;
    }

    public Student(Integer id, String userName, String firstName, String lastName){
        setId(id);
        setUserName(userName);
        setFullName(firstName, lastName);
    }

    public Schedule schedule(){
        return schedule;
    }

    public void setSchedule(Schedule schedule){
        this.schedule = schedule;
    }
}