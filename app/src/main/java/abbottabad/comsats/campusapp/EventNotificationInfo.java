package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/20/16.
 */
public class EventNotificationInfo {

    private String notification;
    private String dateTime;
    private int mine;

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public  String getNotification() {
        return notification;
    }

    public  String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public  int getMine() {
        return mine;
    }

    public void setMine(int mine) {
        this.mine = mine;
    }
}
