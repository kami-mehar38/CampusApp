package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/16/16.
 */
class StatusInfo {
    private String teacherName;
    private String teacherRegistration;
    private String status;
    private String mode;

    String getTeacherRegistration() {
        return teacherRegistration;
    }

    void setTeacherRegistration(String teacherRegistration) {
        this.teacherRegistration = teacherRegistration;
    }

    String getMode() {
        return mode;
    }

    void setMode(String mode) {
        this.mode = mode;
    }

    String getTeacherName() {
        return teacherName;
    }

    void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    String getStatus() {
        return status;
    }

    void setStatus(String status) {
        this.status = status;
    }
}
