package login;
/*
Makes a time object without any time zones. Assumes all time parameters is in 'HH:MM:SS' format.
*/
public class Time{
    private String[] timeArr;//time[0] = Hours, time[1] = Mins, time[2] = Secs; should only have 3 indexes
    private long mills;//millisecond representation of MilitaryTime
    private String time;//String time in "HH:MM:SS' format
    //Constructor which takes a String time in "HH:MM:SS' format
    public Time(String time){
        this.time = time;
        timeArr = time.split(":");
        mills = (60 * 1000)*((60 * Integer.parseInt(timeArr[0])) + Integer.parseInt(timeArr[1]));
    }

    public String getTime(){
        return time;
    }

    public long getMills(){
        return mills;
    }

    public String getHours(){
        return timeArr[0];
    }

    public String getMins(){
        return timeArr[1];
    }

    public String getSecs(){
        return timeArr[2];
    }
    //a value 0 if the argument time is equal to this time
    //a value -1 if this time is before the time argument
    //a value 1 if this time is after the time argument.
    public int compareTo(Time time){
        if(this.mills == time.getMills())
            return 0;
        else if(this.mills > time.getMills())
            return 1;

        return -1;
    }
    //converts the militaryTime in 'HH:MM:SS' format and returns a regular time in 'HH:MM:SS' format as String
    public String militaryToRegularTime(){
        int hour = Integer.parseInt(getHours());

        if(hour > 12){
            hour = hour - 12;
            return "0" + hour + ":" + getMins() + ":" + getSecs()+ "pm";
        }
        return this.time + "am";
    }
}