package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/20/16.
 */
public class EventNotificationInfo {

    private String notification;
    private boolean isMine;

    public EventNotificationInfo(String notification, boolean isMine) {
        this.notification = notification;
        this.isMine = isMine;
    }

    public String getNotification() {
        return notification;
    }

    public boolean isMine() {
        return isMine;
    }

}
