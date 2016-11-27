package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 25-Oct-16.
 */
class NotificationsListInfo {

    private String groupImageUri;
    private String groupName;
    private String groupPrivacy;
    private String userName;
    private String regId;
    private String timeStamp;

    public String getGroupPrivacy() {
        return groupPrivacy;
    }

    public void setGroupPrivacy(String groupPrivacy) {
        this.groupPrivacy = groupPrivacy;
    }

    String getUserName() {
        return userName;
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    String getRegId() {
        return regId;
    }

    void setRegId(String regId) {
        this.regId = regId;
    }

    String getTimeStamp() {
        return timeStamp;
    }

    void setTimeStamp(String timeStamp) {
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
