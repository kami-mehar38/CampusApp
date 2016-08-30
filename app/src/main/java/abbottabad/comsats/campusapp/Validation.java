package abbottabad.comsats.campusapp;

import java.util.regex.Pattern;

/**
 * This project CampusApp is created by Kamran Ramzan on 7/7/16.
 */
public class Validation {
    public boolean validateName(String name){
        String NAME = name.trim();
        if (NAME.equals("")){
            return false;
        } else if (NAME.length() > 30){
            return false;
        } else if (NAME.matches(".*\\d.*")){
            return false;
        }
        return true;
    }

    public boolean validatePhoneNumber(String phoneNumber){
        String PHONE_NUMBER = phoneNumber.trim();
        return Pattern.matches("[+]?[0-9]{10,13}", PHONE_NUMBER);
    }

    public boolean validateReg(String reg){
        String REG = reg.trim();
        if (REG.isEmpty()){
            return false;
        } else if (REG.length() > 30){
            return false;
        } else if (!REG.matches(".*\\d.*")){
            return false;
        } else if (!REG.contains("-")){
            return false;
        }
        return true;
    }

    public   boolean validateEmail(String email) {
        String EMAIL = email.trim();
        if (EMAIL.isEmpty()){
            return false;
        } else if (EMAIL.length() > 30){
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return false;
        }
        return true;
    }

    public boolean validatePassword(String password){
        String PASSWORD = password.trim();
        if (PASSWORD.isEmpty()){
            return false;
        } else if (PASSWORD.length() > 30){
            return false;
        }
        return true;
    }

    public boolean validateUsername(String username){
        String USERNAME = username.trim();
        if (USERNAME.isEmpty()){
            return false;
        } else if (USERNAME.length() > 30){
            return false;
        }else if (USERNAME.contains(".") || USERNAME.contains("#")) {
            return false;
        }
        return true;
    }
}
