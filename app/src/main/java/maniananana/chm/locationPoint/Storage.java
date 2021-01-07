package maniananana.chm.locationPoint;

public class Storage {

    private static final LocationPointRepository locationPointRepository = new LocationPointRepository();

    public static LocationPointRepository getLocationPointRepository() {
        return locationPointRepository;
    }
}
