package abbottabad.comsats.campusapp;

/**
 * This project CampusApp is created by Kamran Ramzan on 03-Oct-16.
 */

class LocationController {
    private static double latitide;
    private static double longitude;

    public static double getLatitide() {
        return latitide;
    }

    static void setLatitide(double latitide) {
        LocationController.latitide = latitide;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        LocationController.longitude = longitude;
    }
}
