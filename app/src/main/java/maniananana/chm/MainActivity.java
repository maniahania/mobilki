package maniananana.chm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import maniananana.chm.locationPoint.LocationPoint;
import maniananana.chm.locationPoint.LocationPointRepository;
import maniananana.chm.locationPoint.Storage;

public class MainActivity extends AppCompatActivity {

    EditText lat;
    EditText lon;
    Button submitBtn;
    Storage storage = new Storage();
    LocationPointRepository lpr = storage.getLocationPointRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button showMapBtn = findViewById(R.id.showMapBtn);
        Button addLocBtn = findViewById(R.id.addLocBtn);
        submitBtn = findViewById(R.id.submitBtn);
        lat = findViewById(R.id.inputLat);
        lon = findViewById(R.id.inputLon);
        lat.setVisibility(View.INVISIBLE);
        lon.setVisibility(View.INVISIBLE);
        submitBtn.setVisibility(View.INVISIBLE);
        lpr.loadData(getApplicationContext());

        showMapBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent showMapIntent = new Intent(getApplicationContext(), HeatmapActivity.class);
                startActivity(showMapIntent);
            }
        });

        addLocBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lat.setVisibility(View.VISIBLE);
                lon.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.VISIBLE);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                double num1 = Double.parseDouble(lat.getText().toString());
                double num2 = Double.parseDouble(lon.getText().toString());
                lpr.add(new LocationPoint(num1, num2));
                lpr.saveData(getApplicationContext());
                lat.setVisibility(View.INVISIBLE);
                lon.setVisibility(View.INVISIBLE);
                submitBtn.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}