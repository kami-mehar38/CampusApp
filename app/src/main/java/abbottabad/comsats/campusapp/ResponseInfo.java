package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 03-Oct-16.
 */

class ResponseInfo {
    private String name;
    private String registration;
    private String bloodType;
    private String contact;
    private int distance;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    String getName() {
        return name;
    }

    String getRegistration() {
        return registration;
    }

    void setRegistration(String registration) {
        this.registration = registration;
    }

    void setName(String name) {
        this.name = name;
    }

    String getBloodType() {
        return bloodType;
    }

    void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    String getContact() {
        return contact;
    }

    void setContact(String contact) {
        this.contact = contact;
    }


}
