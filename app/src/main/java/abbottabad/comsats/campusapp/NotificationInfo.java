package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/20/16.
 */
class NotificationInfo {

    private String notification;
    private String dateTime;
    private int mine;

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
