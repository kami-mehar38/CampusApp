package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 25-Oct-16.
 */
class NotificationsListInfo {

    private String groupImageUri;
    private String groupName;
    private String userName;
    private String regId;
    private String timeStamp;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    String getGroupImageUri() {
        return groupImageUri;
    }

    void setGroupImageUri(String groupImageUri) {
        this.groupImageUri = groupImageUri;
    }

    String getGroupName() {
        return groupName;
    }

    void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
