package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 27-Nov-16.
 */

class NotificationsRequestController {

    private static String name;
    private static String regId;
    private static String groupName;
    private static String timeStamp;

    static String getRegId() {
        return regId;
    }

    static void setRegId(String regId) {
        NotificationsRequestController.regId = regId;
    }

    static String getName() {
        return name;
    }

    static String getGroupName() {
        return groupName;
    }

    static void setGroupName(String groupName) {
        NotificationsRequestController.groupName = groupName;
    }

    public static void setName(String name) {
        NotificationsRequestController.name = name;
    }

    static String getTimeStamp() {
        return timeStamp;
    }

    static void setTimeStamp(String timeStamp) {
        NotificationsRequestController.timeStamp = timeStamp;
    }
}
