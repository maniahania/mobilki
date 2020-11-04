package maniananana.chm.locationPoint;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class LocationPointRepository implements Serializable {
    private static final String FILE_NAME = "/data.txt";
    private List<LocationPoint> locationPoints = new ArrayList<>();

    public void add(LocationPoint locationPoint) {
        locationPoints.add(locationPoint);
    }

    public List<UUID> getPointsIds() {
        List<UUID> uuids = new ArrayList<>();
        for (LocationPoint it : locationPoints) {
            uuids.add(it.getPointId());
        }
        return uuids;
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

    public void delete(UUID uuid) {
        locationPoints.remove(find(uuid));
    }

    public LocationPoint find(UUID uuid) {
        for (LocationPoint it : locationPoints) {
            if (it.getPointId() == uuid) {
                return it;
            }
        }
        return null;
    }

    public void saveData(Context context) {
        try {
            ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(new File(context.getFilesDir().getAbsolutePath() + FILE_NAME)));
            objectOut.writeObject(getLocationPoints());
            objectOut.close();
        } catch (IOException io) {
            System.out.println(io.getMessage());
        } catch (Exception se) {
            System.err.println("blad sec");
        }
    }

    @SuppressWarnings("unchecked")
    public void loadData(Context context) {
        try {
            ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(new File(context.getFilesDir().getAbsolutePath() + FILE_NAME)));
            setLocationPoints((List<LocationPoint>) objectIn.readObject());
            objectIn.close();
        } catch (FileNotFoundException io) {
            System.out.println(io.getMessage());
        } catch (IOException io) {
            System.out.println(io.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
