package abbottabad.comsats.campusapp.helper_classes;

import java.util.regex.Pattern;

/**
 * Created by Kamran Ramzan on 7/7/16.
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
}
