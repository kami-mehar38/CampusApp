package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 03-Jan-17.
 */

class EventsInfoController {
    private static String name;
    private static String notification;
    private static String timeDate;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        EventsInfoController.name = name;
    }

    public static String getNotification() {
        return notification;
    }

    public static void setNotification(String notification) {
        EventsInfoController.notification = notification;
    }

    public static String getTimeDate() {
        return timeDate;
    }

    public static void setTimeDate(String timeDate) {
        EventsInfoController.timeDate = timeDate;
    }
}
