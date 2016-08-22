package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/21/16.
 */
public class NotificationsController {
    private static String notification;
    private static String dateTime;
    private static int mine;

    public static void setNotification(String notification) {
        NotificationsController.notification = notification;
    }

    public static String getNotification() {
        return notification;
    }

    public static String getDateTime() {
        return dateTime;
    }

    public static void setDateTime(String dateTime) {
        NotificationsController.dateTime = dateTime;
    }

    public static int getMine() {
        return mine;
    }

    public static void setMine(int mine) {
        NotificationsController.mine = mine;
    }
}
