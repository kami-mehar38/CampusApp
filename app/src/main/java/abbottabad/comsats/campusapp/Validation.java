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
        if (REG.equals("")){
            return false;
        } else if (REG.length() > 30){
            return false;
        } else if (!REG.matches(".*\\d.*")){
            return false;
        }
        return true;
    }

    public   boolean validateEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
