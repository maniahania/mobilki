package maniananana.chm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;
import java.util.List;

import maniananana.chm.locationPoint.LocationPointRepository;
import maniananana.chm.locationPoint.Storage;

public class HeatmapActivity extends FragmentActivity implements OnMapReadyCallback {
    private final static int RADIUS = 30;
    private final static double MAX_INTENSITY = 5000.0;
    private final static double DENSITY = 5000;
    private final LocationPointRepository lpr = Storage.getLocationPointRepository();
    private LatLng mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatmap);
        lpr.loadDataFromFirebase();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (checkPermissions()) {
            getLastLocation();
        } else {
            loadMap();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        List<WeightedLatLng> data = generateHeatMapData();
        if (!data.isEmpty()) {
            HeatmapTileProvider heatMapTileProvider = new HeatmapTileProvider.Builder()
                    .weightedData(data)
                    .radius(RADIUS)
                    .maxIntensity(MAX_INTENSITY)
                    .build();
            if (mLastLocation != null) {
                googleMap.addMarker(new MarkerOptions().position(mLastLocation).title("My Location"));
            }
            googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(heatMapTileProvider));
        }
    }

    private List<WeightedLatLng> generateHeatMapData() {
        List<WeightedLatLng> data = new ArrayList<>();
        List<LatLng> latLngs = lpr.getPointsLatLng();
        for (LatLng it : latLngs) {
            WeightedLatLng weightedLatLng = new WeightedLatLng(it, DENSITY);
            data.add(weightedLatLng);
        }
        return data;
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location location = task.getResult();
                            mLastLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            loadMap();
                        }
                    }
                });
    }

    private boolean checkPermissions() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void loadMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.usersMap);
        mapFragment.getMapAsync(this);
    }
}