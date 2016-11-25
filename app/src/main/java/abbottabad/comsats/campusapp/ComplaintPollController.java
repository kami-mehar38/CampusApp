package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/31/16.
 */
class ComplaintPollController {

    private static String name;
    private static String registration;
    private static String contact;
    private static String description;
    private static String imageUrl;

    static String getImageUrl() {
        return imageUrl;
    }

    static void setImageUrl(String imageUrl) {
        ComplaintPollController.imageUrl = imageUrl;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        ComplaintPollController.name = name;
    }

    public static String getRegistration() {
        return registration;
    }

    public static void setRegistration(String registration) {
        ComplaintPollController.registration = registration;
    }

    public static String getContact() {
        return contact;
    }

    public static void setContact(String contact) {
        ComplaintPollController.contact = contact;
    }

    public static String getDescription() {
        return description;
    }

    public static void setDescription(String description) {
        ComplaintPollController.description = description;
    }
}
