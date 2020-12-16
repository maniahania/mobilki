package maniananana.chm.addLocation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

import maniananana.chm.R;
import maniananana.chm.locationPoint.LocationPoint;
import maniananana.chm.locationPoint.LocationPointRepository;
import maniananana.chm.locationPoint.Storage;

public class AddLocationActivity extends AppCompatActivity {

    LocationPointRepository lpr = Storage.getLocationPointRepository();

    Button submitBtn, pickLocationBtn;
    EditText lat, lon, name;
    TextView warningText;
    int PLACE_PICKER_REQUEST = 1;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        pickLocationBtn = findViewById(R.id.pickLocationBtn);
        submitBtn = findViewById(R.id.submitBtn);
        lat = findViewById(R.id.pickLatitude);
        lon = findViewById(R.id.pickLongitude);
        name = findViewById(R.id.pickName);
        warningText = findViewById(R.id.warningTextView);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        lpr.loadDataFromFirebase(getApplicationContext());

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
                    lpr.saveDataToFirebase(getApplicationContext());
                    df.update("Locations", FieldValue.arrayUnion(lp.toString()));
                    finish();
                }
                warningText.setVisibility(View.VISIBLE);
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
}