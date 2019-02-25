package login;

import userGroups.Student;
import userGroups.User;

public class Class{
    private String className, instructor, CRN, days, subject;
    private Time startTime, endTime;
    private Integer id, size, spotsTaken;

    public Class(){}

    public Class(Integer id, String className, Integer size, Integer spotsTaken, String instructor, String CRN, String days, Time startTime, Time endTime, String subject){
        this.id = id;
        this.className = className;
        this.size = size;
        this.spotsTaken = spotsTaken;
        this.instructor = instructor;
        this.CRN = CRN;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subject = subject;
    }

    public Class(Integer id, String className, String instructor, String CRN, String days, Time startTime, Time endTime){
        this.id = id;
        this.className = className;
        this.instructor = instructor;
        this.CRN = CRN;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Class(String className, int size, String instructor, String CRN, String days, String startTime, String endTime, String subject){
        this.className = className;
        this.size = size;
        this.instructor = instructor;
        this.CRN = CRN;
        this.days = days;
        this.startTime = new Time(startTime);
        this.endTime = new Time(endTime);
        this.subject = subject;
    }
    //returns number equivalent to a day char. Assuming parameter can only be :
    //M(Monday), T(Tuesday), W(Wednesday), R(Thursday), F(Friday) chars
    public int getNumEquivalent(char day){
        switch(day){
            case 'M':
                return 0;
            case 'T':
                return 1;
            case 'W':
                return 2;
            case 'R':
                return 3;
            case 'F':
                return 4;
            default://should never reach this point
                return -1;
        }
    }
    //Returns a boolean if aClass can be inserted into database and schedule for the specific student user
    public static boolean addClassCheck(Student user, Class aClass, DBHandler dbHandler){
        if(user.schedule().sameClassCollision(aClass.getClassName())){
            //finds if user already added the session
            //or if user already added a class with the same class name
            User.showAlert("You have already added " + aClass.getClassName() +
                    " with CRN: "+ aClass.getCRN() + " or " +
                    "you have already added the same course from another session! " +
                    "If you want this session to replace another session for the same course, " +
                    "please drop the previous session first.");

            return false;
        }
        else if(aClass.getSize() - aClass.getSpotsTaken() <= 0){
            User.showAlert(aClass.getClassName() + " with CRN: " + aClass.getCRN() + " is full!");
            return false;
        }
        else if(user.schedule().collision(aClass)) {
            User.showAlert("The class " + aClass.getClassName() + " with CRN: " + aClass.getCRN() + " your trying to add has a time conflict with one or more classes that are already on your schedule!");
            return false;
        }
        else{//inserts the new class at the correct position in user's schedule and updates everything
            dbHandler.addClassToUser(aClass.getId(), aClass.getClassName(), user.getId(), user.getUserName());
            user.schedule().updateSchedule(aClass);
            //updates SpotsTaken in database, aClass, and label text in real time
            dbHandler.updateSpotsTaken(true, aClass.getId());
            aClass.setSpotsTaken(aClass.getSpotsTaken() + 1);
            return true;
        }
    }
    //Getters
    public Integer getId(){return id;}

    public Integer getSize(){return size;}

    public Integer getSpotsTaken(){return spotsTaken;}

    public String getClassName(){return className;}

    public String getInstructor(){return instructor;}

    public String getCRN(){return CRN;}

    public String getDays(){return days;}

    public Time getStartTime(){return startTime;}

    public Time getEndTime(){return endTime;}

    public String getSubject(){return subject;}
    //Setters
    public void setId(Integer id){
        this.id = id;
    }

    public void setSize(Integer size){
        this.size = size;
    }

    public void setSpotsTaken(Integer spotsTaken){
        this.spotsTaken = spotsTaken;
    }

    public void setClassName(String className){
        this.className = className;
    }

    public void setInstructor(String instructor){
        this.instructor = instructor;
    }

    public void setCRN(String CRN){
        this.CRN = CRN;
    }

    public void setDays(String days){
        this.days = days;
    }

    public void setStartTime(Time startTime){
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime){
        this.endTime = endTime;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }
}