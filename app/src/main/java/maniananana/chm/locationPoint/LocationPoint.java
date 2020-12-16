package maniananana.chm.locationPoint;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.io.Serializable;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class LocationPoint implements Serializable {

    private String pointId;
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

    public LocationPoint(String pointId, String name, double latitude, double longitude, DateTime date, String creatorID) {
        this.pointId = pointId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createDate = date;
        this.creatorID = creatorID;
    }

    public LocationPoint(String name, double latitude, double longitude, String creatorID) {
        this.pointId = UUID.randomUUID().toString();
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createDate = DateTime.now();
        this.creatorID = creatorID;
    }

    public LocationPoint(){}

    public boolean isOutdated() {
        Duration duration = new Duration(createDate, DateTime.now());
        return duration.getStandardDays() > 14;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }
}
