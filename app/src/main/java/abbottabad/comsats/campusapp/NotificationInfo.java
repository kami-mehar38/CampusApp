package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/20/16.
 */
class NotificationInfo {

    private String notification;
    private String dateTime;
    private int mine;
    private String notificationType;
    private String notificationSender;

    public String getNotificationSender() {
        return notificationSender;
    }

    public void setNotificationSender(String notificationSender) {
        this.notificationSender = notificationSender;
    }

    public String getNotificationType() {
        return notificationType;
    }

    void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    void setNotification(String notification) {
        this.notification = notification;
    }

    String getNotification() {
        return notification;
    }

    String getDateTime() {
        return dateTime;
    }

    void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    int getMine() {
        return mine;
    }

    void setMine(int mine) {
        this.mine = mine;
    }
}
