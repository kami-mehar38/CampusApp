package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/21/16.
 */
class NotificationsController {
    private static String notification;
    private static String notificationSender;
    private static String dateTime;
    private static int mine;
    private static String notificationType;

    public static String getNotificationSender() {
        return notificationSender;
    }

    public static void setNotificationSender(String notificationSender) {
        NotificationsController.notificationSender = notificationSender;
    }

    static String getNotificationType() {
        return notificationType;
    }

    static void setNotificationType(String notificationType) {
        NotificationsController.notificationType = notificationType;
    }

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
