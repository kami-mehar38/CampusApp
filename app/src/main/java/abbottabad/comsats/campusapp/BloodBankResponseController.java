package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 04-Oct-16.
 */

class BloodBankResponseController {

    private static String name;
    private static String registration;
    private static String bloodGroup;
    private static String contact;
    private static int distance;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        BloodBankResponseController.name = name;
    }

    public static String getRegistration() {
        return registration;
    }

    public static void setRegistration(String registration) {
        BloodBankResponseController.registration = registration;
    }

    public static String getBloodGroup() {
        return bloodGroup;
    }

    public static void setBloodGroup(String bloodGroup) {
        BloodBankResponseController.bloodGroup = bloodGroup;
    }

    public static String getContact() {
        return contact;
    }

    public static void setContact(String contact) {
        BloodBankResponseController.contact = contact;
    }

    public static int getDistance() {
        return distance;
    }

    public static void setDistance(int distance) {
        BloodBankResponseController.distance = distance;
    }
}
