package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 27-Nov-16.
 */

class NotificationsRequestController {

    private static String name;
    private static String groupName;
    private static String timeStamp;

    public static String getName() {
        return name;
    }

    public static String getGroupName() {
        return groupName;
    }

    public static void setGroupName(String groupName) {
        NotificationsRequestController.groupName = groupName;
    }

    public static void setName(String name) {
        NotificationsRequestController.name = name;
    }

    public static String getTimeStamp() {
        return timeStamp;
    }

    public static void setTimeStamp(String timeStamp) {
        NotificationsRequestController.timeStamp = timeStamp;
    }
}
