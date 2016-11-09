package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 08-Nov-16.
 */

class NotificationsUtills {

    private static String imageUri;
    private static String groupName;

    static String getGroupName() {
        return groupName;
    }

    static void setGroupName(String groupName) {
        NotificationsUtills.groupName = groupName;
    }

    static String getImageUri() {
        return imageUri;
    }

    static void setImageUri(String imageUri) {
        NotificationsUtills.imageUri = imageUri;
    }
}
