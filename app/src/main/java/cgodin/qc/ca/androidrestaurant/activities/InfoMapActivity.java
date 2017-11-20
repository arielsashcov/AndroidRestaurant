package cgodin.qc.ca.androidrestaurant.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.wearable.Channel;

import java.util.List;

import cgodin.qc.ca.androidrestaurant.R;

public class InfoMapActivity extends AppCompatActivity {

    // LocationService
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_map);

        Bundle getBundle = this.getIntent().getExtras();
        List<Channel> channelsList = getBundle.getParcelableArrayList("channel");

        //Restaurant current_restaurant = (Restaurant) getIntent().getSerializableExtra("CURRENT_RESTAURANT");

        //Log.e("InfoMapActivity", current_restaurant.toString());

    }
}
