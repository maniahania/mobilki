package maniananana.chm;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private LatLng currentLocation = new LatLng(51.47, 19.28);
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatmap);
        lpr.loadDataFromFirebase(getApplicationContext());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                    }
                });
        if (currentLocation != null) {
            mGoogleMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location"));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 5f));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        getCurrentLocation();
        List<WeightedLatLng> data = generateHeatMapData();
        if (!data.isEmpty()) {
            HeatmapTileProvider heatMapTileProvider = new HeatmapTileProvider.Builder()
                    .weightedData(data) // load our weighted data
                    .radius(RADIUS) // optional, in pixels, can be anything between 20 and 50
                    .maxIntensity(MAX_INTENSITY)
                    .build();
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
}