package maniananana.chm.addLocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.Objects;

import maniananana.chm.R;
import maniananana.chm.UserLocation;
import maniananana.chm.locationPoint.LocationPoint;
import maniananana.chm.locationPoint.LocationPointRepository;
import maniananana.chm.locationPoint.Storage;

public class AddLocationActivity extends AppCompatActivity {

    LocationPointRepository lpr = Storage.getLocationPointRepository();

    Button submitBtn, pickLocationBtn, currentLocationBtn;
    EditText lat, lon, name;
    TextView warningText;
    int PLACE_PICKER_REQUEST = 1;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    UserLocation userLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        pickLocationBtn = findViewById(R.id.pickLocationBtn);
        submitBtn = findViewById(R.id.submitBtn);
        currentLocationBtn = findViewById(R.id.currentLocationBtn);
        lat = findViewById(R.id.pickLatitude);
        lon = findViewById(R.id.pickLongitude);
        name = findViewById(R.id.pickName);
        warningText = findViewById(R.id.warningTextView);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        userLocation = new UserLocation();

        lpr.loadDataFromFirebase();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        currentLocationBtn.setEnabled(checkPermissions());

        pickLocationBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PickLocationFromMapActivity.class);
                startActivityForResult(intent, PLACE_PICKER_REQUEST);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lon.getText().toString().equals("") || lat.getText().toString().equals("") || name.getText().toString().equals("")) {
                    warningText.setText(R.string.warningEmpty);
                } else if (Double.parseDouble(lon.getText().toString().replace(',', '.')) < -180 || Double.parseDouble(lon.getText().toString().replace(',', '.')) > 180) {
                    warningText.setText(R.string.warningLongitude);
                } else if (Double.parseDouble(lat.getText().toString().replace(',', '.')) < -90 || Double.parseDouble(lat.getText().toString().replace(',', '.')) > 90) {
                    warningText.setText(R.string.warningLatitude);
                } else if (name.getText().toString().length() < 3 || name.getText().toString().length() > 60) {
                    warningText.setText(R.string.warningName);
                } else {
                    DocumentReference df = fStore.collection("Users").document(userID);
                    LocationPoint lp = new LocationPoint(name.getText().toString(), Double.parseDouble(lat.getText().toString().replace(',', '.')), Double.parseDouble(lon.getText().toString().replace(',', '.')), userID);
                    lpr.add(lp);
                    lpr.saveDataToFirebase();
                    userLocation.setName(lp.getName());
                    userLocation.setId(lp.getPointId());
                    df.update("Locations", FieldValue.arrayUnion(userLocation));
                    finish();
                }
                warningText.setVisibility(View.VISIBLE);
            }
        });

        currentLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Double returnedLatitude = data.getDoubleExtra("latitude", 0);
                Double returnedLongitude = data.getDoubleExtra("longitude", 0);
                lat.setText(new DecimalFormat("##.####").format(returnedLatitude));
                lon.setText(new DecimalFormat("##.####").format(returnedLongitude));
                name.setText(data.getStringExtra("name"));
            }
        }
    }

    private boolean checkPermissions() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location location = task.getResult();
                            lat.setText(new DecimalFormat("##.####").format(location.getLatitude()));
                            lon.setText(new DecimalFormat("##.####").format(location.getLongitude()));
                            name.setText("My Location");
                        }
                    }
                });
    }
}