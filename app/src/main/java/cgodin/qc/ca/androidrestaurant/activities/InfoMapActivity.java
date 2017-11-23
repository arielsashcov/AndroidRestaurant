package cgodin.qc.ca.androidrestaurant.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;

import cgodin.qc.ca.androidrestaurant.R;
import cgodin.qc.ca.androidrestaurant.fragments.RestaurantDetailFragment;
import cgodin.qc.ca.androidrestaurant.model.Restaurant;

public class InfoMapActivity extends AppCompatActivity {

    // LocationService
    private FusedLocationProviderClient mFusedLocationClient;

    private static FragmentManager fragmentManager;
    Restaurant restaurant = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_map);
        fragmentManager = getSupportFragmentManager();//Get Fragment Manager

        restaurant = getIntent().getExtras().getParcelable("restaurant");

        Log.e("TEST", String.valueOf(restaurant));

        sendData();


    }

    private void sendData() {
        //PACK DATA IN A BUNDLE
        Bundle bundle = new Bundle();
        bundle.putString("name", restaurant.getName());

        //PASS OVER THE BUNDLE TO OUR FRAGMENT
        RestaurantDetailFragment myFragment = new RestaurantDetailFragment();
        myFragment.setArguments(bundle);

        Log.e("BUNDLE---->", bundle.toString());


        //THEN NOW SHOW OUR FRAGMENT
        //getSupportFragmentManager().beginTransaction().replace(R.id.container,myFragment).commit();
    }


}
