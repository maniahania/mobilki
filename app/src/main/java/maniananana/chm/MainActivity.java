package maniananana.chm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import maniananana.chm.addLocation.AddLocationActivity;
import maniananana.chm.locationPoint.LocationPointRepository;
import maniananana.chm.locationPoint.Storage;

public class MainActivity extends AppCompatActivity {

    Button showMapBtn, addLocBtn, helpBtn, logOutBtn;
    TextView aboutTextView;
    LocationPointRepository lpr = Storage.getLocationPointRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        showMapBtn = findViewById(R.id.showMapBtn);
        addLocBtn = findViewById(R.id.addLocBtn);
        helpBtn = findViewById(R.id.helpBtn);
        aboutTextView = findViewById(R.id.helpTextView);
        logOutBtn = findViewById(R.id.logOutBtn);
        lpr.loadData(getApplicationContext());

        showMapBtn.setOnClickListener(new View.OnClickListener() {
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
                if (aboutTextView.getVisibility() == View.VISIBLE) {
                    aboutTextView.setVisibility(View.INVISIBLE);
                } else {
                    aboutTextView.setVisibility(View.VISIBLE);
                }
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
}