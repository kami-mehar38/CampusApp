package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 27-Nov-16.
 */

class PendingGroupRequestsInfo {

    private String name;
    private String regId;
    private String timeStamp;
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    String getRegId() {
        return regId;
    }

    void setRegId(String regId) {
        this.regId = regId;
    }

    void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
