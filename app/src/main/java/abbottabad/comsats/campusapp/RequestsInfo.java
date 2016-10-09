package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 7/6/16.
 */
class RequestsInfo {
    private String name;
    private String registration;
    private String bloodType;
    private String contact;
    private int isDonated;

    public int getIsDonated() {
        return isDonated;
    }

    public void setIsDonated(int isDonated) {
        this.isDonated = isDonated;
    }
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

    String getBloodType() {
        return bloodType;
    }

    void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
