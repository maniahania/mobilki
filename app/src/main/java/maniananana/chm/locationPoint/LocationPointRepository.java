package maniananana.chm.locationPoint;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class LocationPointRepository implements Serializable {
    public List<LocationPoint> locationPoints = new ArrayList<>();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("LocationPoints");

    public void add(LocationPoint locationPoint) {
        locationPoints.add(locationPoint);
    }

    public List<LatLng> getPointsLatLng() {
        List<LatLng> latLngs = new ArrayList<>();
        for (LocationPoint it : locationPoints) {
            latLngs.add(it.getLatLng());
        }
        return latLngs;
    }

    public void delete(LocationPoint locationPoint) {
        locationPoints.remove(find(locationPoint.getPointId()));
    }

    public void delete(String uuid) {
        locationPoints.remove(find(uuid));
    }

    public LocationPoint find(String uuid) {
        for (LocationPoint it : locationPoints) {
            if (it.getPointId().equals(uuid)) {
                return it;
            }
        }
        return null;
    }

    public void deleteOutdatedPoints() {
        for (LocationPoint it : locationPoints) {
            if (it.isOutdated()) {
                delete(it);
            }
        }
    }

    public void saveDataToFirebase() {
        reference.setValue(locationPoints);
    }

    public void loadDataFromFirebase() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    locationPoints.clear();
                    for (DataSnapshot dss : snapshot.getChildren()) {
                        String pointId = dss.child("pointId").getValue(String.class);
                        String name = dss.child("name").getValue(String.class);
                        Double latitude = dss.child("latitude").getValue(Double.class);
                        Double longitude = dss.child("longitude").getValue(Double.class);
                        DateTime date = dss.child("createdate").getValue(DateTime.class);
                        String creatorID = dss.child("creatorID").getValue(String.class);
                        if (longitude != null && latitude != null) {
                            locationPoints.add(new LocationPoint(pointId, name, latitude, longitude, date, creatorID));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
