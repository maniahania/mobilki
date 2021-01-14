package maniananana.chm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import maniananana.chm.addLocation.AddLocationActivity;
import maniananana.chm.locationPoint.LocationPointRepository;
import maniananana.chm.locationPoint.Storage;
import maniananana.chm.locationlist.LocationListActivity;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ImageButton showHeatMapBtn, addLocBtn, helpBtn, listBtn;
    Button logOutBtn, updateDbBtn;
    LocationPointRepository lpr = Storage.getLocationPointRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        showHeatMapBtn = findViewById(R.id.showHeatMapBtn);
        addLocBtn = findViewById(R.id.addLocBtn);
        listBtn = findViewById(R.id.listBtn);
        helpBtn = findViewById(R.id.helpBtn);
        logOutBtn = findViewById(R.id.logOutBtn);
        updateDbBtn = findViewById(R.id.updateDbBtn);
        checkIfAdmin(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());
        lpr.loadDataFromFirebase();

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

        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showLocationsIntent = new Intent(getApplicationContext(), LocationListActivity.class);
                startActivity(showLocationsIntent);
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
                lpr.deleteOutdatedPoints();
                lpr.saveDataToFirebase();
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
        return true;
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

    private void checkIfAdmin(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                updateDbBtn.setEnabled(Objects.equals(documentSnapshot.getString("isAdmin"), "1"));
            }
        });
    }

}