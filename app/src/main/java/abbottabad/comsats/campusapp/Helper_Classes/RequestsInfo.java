package abbottabad.comsats.campusapp.Helper_Classes;

/**
 * Created by Kamran Ramzan on 7/6/16.
 */
public class RequestsInfo {
    private String name;
    private String registration;
    private String bloodType;
    private String contact;

    public String getName() {
        return name;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
