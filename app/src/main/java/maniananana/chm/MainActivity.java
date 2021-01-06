package maniananana.chm;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import maniananana.chm.addLocation.AddLocationActivity;
import maniananana.chm.locationPoint.LocationPointRepository;
import maniananana.chm.locationPoint.Storage;
import maniananana.chm.locationlist.LocationListActivity;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore fStore;
    Button showUserHeatMapBtn, showHeatMapBtn, addLocBtn, helpBtn, logOutBtn, updateDbBtn;
    LocationPointRepository lpr = Storage.getLocationPointRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        requestPermissions();
        showUserHeatMapBtn = findViewById(R.id.showUserHeatMapBtn);
        showHeatMapBtn = findViewById(R.id.showHeatMapBtn);
        addLocBtn = findViewById(R.id.addLocBtn);
        helpBtn = findViewById(R.id.helpBtn);
        logOutBtn = findViewById(R.id.logOutBtn);
        updateDbBtn = findViewById(R.id.updateDbBtn);
        lpr.loadDataFromFirebase(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            updateDbBtn.setVisibility(bundle.getBoolean("isAdmin") ? View.VISIBLE : View.INVISIBLE);
        }

        showUserHeatMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showMapIntent = new Intent(getApplicationContext(), UsersHeatmapActivity.class);
                startActivity(showMapIntent);
            }
        });

        showHeatMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showMapIntent = new Intent(getApplicationContext(), HeatmapActivity.class);
                startActivity(showMapIntent);
            }
        });

        addLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addLocationIntent = new Intent(getApplicationContext(), AddLocationActivity.class);
                startActivity(addLocationIntent);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showHelpPageIntent = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(showHelpPageIntent);
            }
        });

        updateDbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_viewMyLocations) {
            Intent listIntent = new Intent(getApplicationContext(), LocationListActivity.class);
            startActivity(listIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            startLocationPermissionRequest();
        } else {
            startLocationPermissionRequest();
        }
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                111);
    }

}