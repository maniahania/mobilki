package maniananana.chm;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatmap);
        lpr.loadData(getApplicationContext());
        MapFragment mapFragment = (MapFragment)
                getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        List<WeightedLatLng> data = generateHeatMapData();
        if (!data.isEmpty()) {
            HeatmapTileProvider heatMapTileProvider = new HeatmapTileProvider.Builder()
                    .weightedData(data) // load our weighted data
                    .radius(RADIUS) // optional, in pixels, can be anything between 20 and 50
                    .maxIntensity(MAX_INTENSITY)
                    .build();
            googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(heatMapTileProvider));
        }
        LatLng lodzLatLng = new LatLng(51.47, 19.28);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lodzLatLng, 5f));

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