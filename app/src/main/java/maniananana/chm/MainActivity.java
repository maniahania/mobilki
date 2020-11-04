package maniananana.chm;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import maniananana.chm.locationPoint.LocationPoint;
import maniananana.chm.locationPoint.LocationPointRepository;
import maniananana.chm.locationPoint.Storage;

public class MainActivity extends AppCompatActivity {

    EditText lat;
    EditText lon;
    Button submitBtn, showMapBtn, addLocBtn, helpBtn;
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
        submitBtn = findViewById(R.id.submitBtn);
        helpBtn = findViewById(R.id.helpBtn);
        lat = findViewById(R.id.inputLat);
        lon = findViewById(R.id.inputLon);
        aboutTextView = findViewById(R.id.helpTextView);
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
                if (submitBtn.getVisibility() == View.VISIBLE) {
                    lat.setVisibility(View.INVISIBLE);
                    lon.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.INVISIBLE);
                } else {
                    lat.setVisibility(View.VISIBLE);
                    lon.setVisibility(View.VISIBLE);
                    submitBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!lat.getText().toString().equals("") && !lon.getText().toString().equals("")) {
                    double num1 = Double.parseDouble(lat.getText().toString());
                    double num2 = Double.parseDouble(lon.getText().toString());
                    lpr.add(new LocationPoint(num1, num2));
                    lpr.saveData(getApplicationContext());
                }
                lat.setVisibility(View.INVISIBLE);
                lon.setVisibility(View.INVISIBLE);
                submitBtn.setVisibility(View.INVISIBLE);
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