package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/21/16.
 */
class NotificationsController {
    private static String notification;
    private static String dateTime;
    private static int mine;

    static void setNotification(String notification) {
        NotificationsController.notification = notification;
    }

    static String getNotification() {
        return notification;
    }

    static String getDateTime() {
        return dateTime;
    }

    static void setDateTime(String dateTime) {
        NotificationsController.dateTime = dateTime;
    }

    static int getMine() {
        return mine;
    }

    static void setMine(int mine) {
        NotificationsController.mine = mine;
    }
}
