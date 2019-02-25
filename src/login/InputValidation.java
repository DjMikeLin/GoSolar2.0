package login;

import java.util.regex.Pattern;

public class InputValidation{
    private StringBuffer allErrorMessages;

    public InputValidation(){
        allErrorMessages = new StringBuffer();
    }
    //Checks every parameter to see if they are valid. If not appends error to allErrorMessages
    public void validateClass(String className, String size, String instructor, String CRN, String days, String startTime, String endTime, String subject){
        validClassName(className);
        validSize(size);
        validInstructorName(instructor);
        validCRN(CRN);
        validDays(days);
        validTimes(startTime, endTime);
        validSubject(subject);
    }
    //Returns a boolean whether className is valid based on regex
    public boolean validClassName(String className){
        if(!Pattern.matches("^[A-Z]{3}\\d{4}", className)) {
            allErrorMessages.append("Class Name not in the form of 3 capital letters followed by 4 digits!\n");
            return false;
        }
        return true;
    }
    //Returns a boolean whether size and spotsTaken contain only digits and is (size < 300) and is (spotstaken > size)
    public boolean validSize(String size){
        if(!Pattern.matches("[0-9]+", size) || Integer.parseInt(size) > 300){
            allErrorMessages.append("Class size has invalid characters OR class size is greater then 300\n");
        }
        return true;
    }
    //Returns a boolean whether instructor name is valid. Breaks it up into first and last name.
    public boolean validInstructorName(String instructor){
        String[] arr = instructor.split(" ");
        if(arr.length < 2){
            allErrorMessages.append("Instructor has only one name! Instructor should have a first name and last name separated by a space character\n");
            return false;
        }

        if(validName(arr[0]) && validName(arr[1]))
            return true;

        return false;
    }
    //Returns a boolean whether CRN is a String that only contains 5 digits
    public boolean validCRN(String CRN){
        if(!Pattern.matches("[0-9]{5}", CRN)) {
            allErrorMessages.append("CRN should only be 5 digits!\n");
            return false;
        }
        return true;
    }
    //Returns a boolean whether days is a String that only contains 'M', 'T', 'W', 'R', and 'F' chars
    public boolean validDays(String days){
        if(!Pattern.matches("([M]?[T]?[W]?[R]?[F]?)+", days)){
            allErrorMessages.append("Days should only be 'M', 'T', 'W', 'R', and 'F'!\n");
            return false;
        }
        return true;
    }
    //Returns a boolean whether startTime and endTime are in military time format(HH:MM:SS)
    public boolean validTimes(String startTime, String endTime){
        if(!Pattern.matches("^(?:[01]\\d|2[0-3]){1}(?::?[0-5]\\d){1}(?::?[0-5]\\d){1}", startTime) ||
                !Pattern.matches("^(?:[01]\\d|2[0-3]){1}(?::?[0-5]\\d){1}(?::?[0-5]\\d){1}", endTime)){
            allErrorMessages.append("Start time and/or end time in wrong format!\n");
            return false;
        }
        return true;
    }
    //Returns a boolean wheter subject is in the right form. Letters and spaces only.
    public boolean validSubject(String subject){
        if(!Pattern.matches("[A-Za-z' ']+", subject)){
            allErrorMessages.append("Subject in wrong form! Letters and spaces only.\n");
            return false;
        }
        return true;
    }
    //Returns false if userName has a invalid char or it's length is not correct. True otherwise.
    //Lowercase letters and numbers only
    public boolean validUserName(String userName){
        if(userName.length() < 5 || userName.length() > 16){//minimum lengh of userName input is 5 chars
            allErrorMessages.append("User Name must be between 5 and 16 characters in length!\n");
            return false;
        }

        char[] temp = userName.toCharArray();
        for(char c : temp){
            if((c < 48 || c > 57) && (c < 97 || c > 122)){
                allErrorMessages.append("User Name contains an invalid character(s)! Lowercase letters and numbers only\n");
                return false;
            }
        }
        return true;
    }
    //Returns false if password has a invalid char or it's empty. True otherwise
    //Any letters and numbers only
    public boolean validPassword(String password){
        if(password.length() < 6){
            allErrorMessages.append("Password must be at east 6 characters in length\n");
            return false;
        }

        char[] temp = password.trim().toCharArray();
        for(char c : temp){
            if((c < 48 || c > 57) && (c < 97 || c > 122) && (c < 65 || c > 90)){
                allErrorMessages.append("User Name contains an invalid character(s)! Lowercase letters and numbers only\n");
                return false;
            }
        }
        return true;
    }
    //Returns false if first name or last name has a invalid char or it's empty. True otherwise
    //Any letter only
    public boolean validName(String name){
        if(isEmpty(name)){
            allErrorMessages.append("The field First Name or Last Name is required\n");
            return false;
        }

        char[] temp = name.trim().toCharArray();
        for(char c : temp){
            if((c < 97 || c > 122) && (c < 65 || c > 90)){
                allErrorMessages.append("First Name or Last Name contains an invalid character(s)! Letters only\n");
                return false;
            }
        }
        return true;
    }
    //Returns false if email has a invalid char or it's empty. True otherwise
    //Any letters, numbers, '@', '.' only
    public boolean validEmail(String email){
        if(isEmpty(email)){
            allErrorMessages.append("The field Email is required\n");
            return false;
        }

        char[] temp = email.trim().toCharArray();
        for(char c : temp){
            if((c < 48 || c > 57) && (c < 97 || c > 122) && (c < 65 || c > 90) && c != 64 && c!= 46) {
                allErrorMessages.append("Email contains an invalid character(s)! Letters, '.', '@', and numbers only\n");
                return false;
            }
        }
        return true;
    }
    //Returns false if number has a invalid char or it's empty. True otherwise
    //Any numbers and '-' only
    public boolean validNumber(String number){
        if(isEmpty(number)){
            allErrorMessages.append("The field Cellphone# is required\n");
            return false;
        }

        char[] temp = number.trim().toCharArray();
        for(char c : temp){
            if((c < 48 || c > 57) && c!= 45){
                allErrorMessages.append("Phone# contains an invalid character(s)! Numbers and '-' only\n");
                return false;
            }
        }
        return true;
    }
    //Returns false if address has a invalid char or it's empty. True otherwise
    //Any letters, numbers, ',' only
    public boolean validAddress(String address){
        if(isEmpty(address)){
            allErrorMessages.append("The field Address is required\n");
            return false;
        }

        char[] temp = address.trim().toCharArray();
        for(char c : temp){
            if((c < 48 || c > 57) && (c < 97 || c > 122) && (c < 65 || c > 90) && c != 44 && c != 32){
                allErrorMessages.append("Address contains an invalid character(s)! Letters, numbers, Space, and ',' only\n");
                return false;
            }
        }
        return true;
    }
    //returns true if string is empty and false if string has chars
    private boolean isEmpty(String string){
        if(string.equals(""))
            return true;
        return false;
    }
    //returns empty string if no errors
    public String getAllErrorMessages(){
        return allErrorMessages.toString();
    }
    //erases everything from allErrorMessages
    public void clear(){
        allErrorMessages.setLength(0);
    }
}