package maniananana.chm;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HeatmapActivity extends FragmentActivity implements OnMapReadyCallback {
    private final static int RADIUS = 30;
    private final static double MAX_INTENSITY = 5000.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatmap);

        MapFragment mapFragment = (MapFragment)
                getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            List<WeightedLatLng> data = generateHeatMapData();
            HeatmapTileProvider heatMapTileProvider = new HeatmapTileProvider.Builder()
                    .weightedData(data) // load our weighted data
                    .radius(RADIUS) // optional, in pixels, can be anything between 20 and 50
                    .maxIntensity(MAX_INTENSITY)
                    .build();
            TileOverlay tileOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(heatMapTileProvider));
            LatLng indiaLatLng = new LatLng(20.5937, 78.9629);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indiaLatLng, 10f));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONArray getJsonDataFromAsset() throws JSONException {
        String json = null;
        try {
            InputStream is = getAssets().open("district_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return new JSONArray(json);
    }

    private List<WeightedLatLng> generateHeatMapData() throws JSONException {
        List<WeightedLatLng> data = new ArrayList<>();
        JSONArray jsonData = getJsonDataFromAsset();
        if (jsonData != null) {
            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject entry = jsonData.getJSONObject(i);
                double lat = entry.getDouble("lat");
                double lon = entry.getDouble("lon");
                double density = entry.getDouble("density");
                if (density != 0.0) {
                    WeightedLatLng weightedLatLng = new WeightedLatLng(new LatLng(lat, lon), density);
                    data.add(weightedLatLng);
                }
            }
        }

        return data;
    }

}