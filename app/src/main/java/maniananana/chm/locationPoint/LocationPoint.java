package maniananana.chm.locationPoint;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.UUID;

import lombok.Data;

@Data
public class LocationPoint {

    private UUID pointId;
    private double latitude;
    private double longitude;
    private DateTime createDate;

    public LocationPoint(double latitude, double longitude) {
        this.pointId = UUID.randomUUID();
        this.latitude = latitude;
        this.longitude = longitude;
        this.createDate = DateTime.now();
    }

    public boolean isOutdated() {
        Duration duration = new Duration(createDate, DateTime.now());
        return duration.getStandardDays() > 14;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }
}
