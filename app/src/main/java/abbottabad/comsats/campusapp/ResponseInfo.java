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
    private int isAccepted;
    private int isRejected;

    public int getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(int isAccepted) {
        this.isAccepted = isAccepted;
    }

    public int getIsRejected() {
        return isRejected;
    }

    public void setIsRejected(int isRejected) {
        this.isRejected = isRejected;
    }

    int getDistance() {
        return distance;
    }

    void setDistance(int distance) {
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
