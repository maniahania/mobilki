package maniananana.chm.locationPoint;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class LocationPointRepository {
    private List<LocationPoint> locationPoints = new ArrayList<>();

    public void add(LocationPoint locationPoint){
            locationPoints.add(locationPoint);
    }

    public List<UUID> getPointsIds(){
        List<UUID> uuids = new ArrayList<>();
        for (LocationPoint it: locationPoints){
            uuids.add(it.getPointId());
        }
        return uuids;
    }

    public List<LatLng> getPointsLatLng(){
        List<LatLng> latLngs = new ArrayList<>();
        for (LocationPoint it: locationPoints){
            latLngs.add(it.getLatLng());
        }
        return latLngs;
    }

    public void delete(LocationPoint locationPoint){
        locationPoints.remove(find(locationPoint.getPointId()));
    }

    public void delete(UUID uuid){
        locationPoints.remove(find(uuid));
    }

    public LocationPoint find(UUID uuid){
        for (LocationPoint it: locationPoints){
            if(it.getPointId() == uuid){
                return it;
            }
        }
        return null;
    }
}
