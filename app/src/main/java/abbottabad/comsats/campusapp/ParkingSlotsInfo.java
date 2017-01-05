package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 06-Jan-17.
 */

class ParkingSlotsInfo {

    private boolean isFree;
    private String slotNo;

    public String getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(String slotNo) {
        this.slotNo = slotNo;
    }

    boolean isFree() {
        return isFree;
    }

    void setFree(boolean free) {
        isFree = free;
    }
}
