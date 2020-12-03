package maniananana.chm.locationPoint;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.io.Serializable;
import java.util.UUID;

import lombok.Data;

@Data
public class LocationPoint implements Serializable {

    private UUID pointId;
    private String name;
    private double latitude;
    private double longitude;
    private DateTime createDate;
    private String creatorID;

    @NonNull
    @Override
    public String toString() {
        return "Name: " + name + ", Latitude: " + latitude + ", Longitude: " + longitude + ", Creation Date: " + createDate.toString() ;
    }

    public LocationPoint(String name, double latitude, double longitude, String creatorID) {
        this.pointId = UUID.randomUUID();
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createDate = DateTime.now();
        this.creatorID = creatorID;
    }

    public boolean isOutdated() {
        Duration duration = new Duration(createDate, DateTime.now());
        return duration.getStandardDays() > 14;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }
}
