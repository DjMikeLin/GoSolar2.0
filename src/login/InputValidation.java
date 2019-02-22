package login;

public class InputValidation{
    private StringBuffer allErrorMessages;

    public InputValidation(){
        allErrorMessages = new StringBuffer();
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