package login;

import java.util.HashSet;
import java.util.LinkedList;
/*
    index 0 = 'M' = Monday
    index 1 = 'T' = Tuesday
    index 2 = 'W' = Wednesday
    index 3 = 'R' = Thursday
    index 4 = 'F' = Friday
* */
public class Schedule{
    private LinkedList<Class>[] weeklySchedule;
    private HashSet<String> classNames;

    public Schedule(){
        classNames = new HashSet<>();
        weeklySchedule = new LinkedList[5];
    }

    public Schedule(LinkedList<Class>[] weeklySchedule){
        this.weeklySchedule = weeklySchedule;
        classNames = new HashSet<>();
        updateClassNames();
    }
    //adds every unique class name from the weekly schedule into the classNames HashSet
    private void updateClassNames(){
        for(int i = 0; i < weeklySchedule.length; i++) {
            if (weeklySchedule[i] == null)
                continue;

            for(Class c : weeklySchedule[i]){
                classNames.add(c.getClassName());
            }
        }
    }
    //adds the unique class name from aClass to the classNames HashSet
    private void updateClassNames(Class aClass){
        classNames.add(aClass.getClassName());
    }
    //removes class from classNames; assuming it exists.
    private void removeFromClassNames(Class aClass){
        classNames.remove(aClass.getClassName());
    }
    //returns a boolean whether weeklySchedule contains the class already or not
    public boolean sameClassCollision(String className){
        return classNames.contains(className) ? true : false;
    }
    //returns a boolean whether weeklySchedule has a time conflict with aClass
    public boolean collision(Class aClass){
        String days = aClass.getDays();
        for(int i = 0; i < days.length(); i++){
            if(weeklySchedule[aClass.getNumEquivalent(days.charAt(i))] == null)//if no linkedList at this index then collision is not possible
                continue;

            for(Class c : weeklySchedule[aClass.getNumEquivalent(days.charAt(i))]){
                if((aClass.getStartTime().compareTo(c.getStartTime()) >= 0 &&//if aClass.getStartTime() is between the current classes start and end time
                        aClass.getStartTime().compareTo(c.getEndTime()) <= 0) ||
                   (aClass.getEndTime().compareTo(c.getStartTime()) >= 0 &&//if aClass.getEndTime() is between the current classes start and end time
                        aClass.getEndTime().compareTo(c.getEndTime()) <= 0))
                    return true;
            }
        }
        return false;
    }

    public HashSet<String> getClassNames(){ return classNames; }

    public LinkedList<Class>[] getSchedule(){ return weeklySchedule; }

    public void setSchedule(LinkedList<Class>[] weeklySchedule){ this.weeklySchedule = weeklySchedule; }
    //finish this
    public LinkedList<Class>[] updateSchedule(Class aClass){
        updateClassNames(aClass);
        String days = aClass.getDays();

        for(int i = 0; i < days.length(); i++){
            LinkedList currList;
            if(weeklySchedule[aClass.getNumEquivalent(days.charAt(i))] == null ||
                    weeklySchedule[aClass.getNumEquivalent(days.charAt(i))].size() == 0){
                weeklySchedule[aClass.getNumEquivalent(days.charAt(i))] = new LinkedList<>();
                currList = weeklySchedule[aClass.getNumEquivalent(days.charAt(i))];
                currList.add(aClass);
                continue;
            }
            //currList should never be null at this point and at least size 1
            currList = weeklySchedule[aClass.getNumEquivalent(days.charAt(i))];
            Class lastClass = (Class) currList.getFirst();//keeps track of the last Node before the current iteration

            int index = 0;
            for(Class c : (LinkedList<Class>) currList){
                if(currList.size() == 1) {//if there is only one element in currList
                    if(((Class) currList.getFirst()).getEndTime().compareTo(aClass.getStartTime()) < 0)
                        currList.addLast(aClass);
                    else
                        currList.addFirst(aClass);

                    break;
                }
                //inserts aClass into a specific index
                if((lastClass.getEndTime().compareTo(aClass.getStartTime()) < 0) &&
                        (c.getStartTime().compareTo(aClass.getEndTime()) > 0)){
                    currList.add(index, aClass);
                    break;
                }
                index++;
            }
        }
        return weeklySchedule;
    }
    //
    public LinkedList<Class>[] removeClassFromSchedule(Class aClass){
        removeFromClassNames(aClass);
        String days = aClass.getDays();

        for(int i = 0; i < days.length(); i++){
            LinkedList currList = weeklySchedule[aClass.getNumEquivalent(days.charAt(i))];
            Class lastClass = (Class) currList.getFirst();//keeps track of the last Node before the current iteration

            int index = 0;
            for(Class c : (LinkedList<Class>) currList){
                if(currList.size() == 1) {//if there is only one element in currList
                    currList.removeFirst();
                    break;
                }
                //removes aClass from a specific index
                if(c.equals(aClass)){
                    currList.remove(index);
                    break;
                }
                index++;
            }
        }
        return weeklySchedule;
    }
    //
    public HashSet<Class> getClassesInSchedule(){
        HashSet<Class> set = new HashSet<>();

        for(int i = 0; i < weeklySchedule.length; i++){
            if(weeklySchedule[i] == null)
                continue;

            for(Class aClass : weeklySchedule[i]){
                set.add(aClass);
            }
        }
        return set;
    }
}