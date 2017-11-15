package cgodin.qc.ca.androidrestaurant.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import cgodin.qc.ca.androidrestaurant.R;
import cgodin.qc.ca.androidrestaurant.fragments.ListFragment;
import cgodin.qc.ca.androidrestaurant.model.Restaurant;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Ariel S on 2017-10-31.
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private TextView nameView;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    // LocationService
    private FusedLocationProviderClient mFusedLocationClient;


    //TODO: Make methods for each logged user - divide code



    private ArrayList<Restaurant> getRestaurantsList(){

        final ArrayList<Restaurant> restaurantList = new ArrayList<Restaurant>();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object

                            final String API_KEY = "AIzaSyABNpwVRjWbPr8zHc5-ZKa8yuLffZmVKKE";
                            final String RADIUS = "5000";
                            final String LONGTITUDE = "";
                            final String LATITUDE = "";

                            final String wtv;

                            AsyncHttpClient placeSearch = new AsyncHttpClient();
                            placeSearch.get(
                                    "https://maps.googleapis.com/maps/api/place/textsearch/json?query=restaurant&location=42.3675294,-71.186966&radius=" + RADIUS + "&key=" + API_KEY,
                                    new TextHttpResponseHandler() {
                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                        }

                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                            Gson gson = new GsonBuilder().create();

                                            //Log.i("InfoMapActivity ", "" + responseString);

                                            /*JSONParser parser = new JSONParser();
                                            JSONObject json = (JSONObject) parser.parse(stringToParse);*/

                                            JsonParser parser = new JsonParser();

                                            JsonElement placeSearchJsonResult = parser.parse(responseString);

                                            if(placeSearchJsonResult.isJsonObject()){
                                                JsonObject jsonObject = placeSearchJsonResult.getAsJsonObject();

                                                JsonElement resultsJson = jsonObject.get("results");

                                                if(resultsJson.isJsonArray()){
                                                    JsonArray resultsJsonArray = resultsJson.getAsJsonArray();


                                                    for (int i = 0; i < resultsJsonArray.size(); i++){

                                                        JsonObject restaurantJsonObject = resultsJsonArray.get(i).getAsJsonObject();

                                                        String formatted_address = restaurantJsonObject.get("formatted_address").getAsString();
                                                        String name = restaurantJsonObject.get("name").getAsString();
                                                        double rating = restaurantJsonObject.get("rating").getAsDouble();
                                                        String place_id = restaurantJsonObject.get("place_id").getAsString();
                                                        String photo_reference = restaurantJsonObject.get("photo_reference").getAsString();
                                                        String img_link = restaurantJsonObject.get("photos").getAsJsonArray().getAsJsonObject().get("photo_reference").getAsString();

                                                        AsyncHttpClient placeDetail = new AsyncHttpClient();

                                                        placeDetail.get(
                                                                "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place_id + "&key=" + API_KEY,
                                                                new TextHttpResponseHandler() {

                                                            @Override
                                                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                                            }

                                                            @Override
                                                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                                                JsonParser parser = new JsonParser();

                                                                JsonElement placeDetailJsonResult = parser.parse(responseString);

                                                                if(placeDetailJsonResult.isJsonObject()){

                                                                    JsonObject jsonObject = placeDetailJsonResult.getAsJsonObject();

                                                                    JsonObject resultsJson = jsonObject.get("results").getAsJsonObject();

                                                                    String formatted_phone_number = resultsJson.get("formatted_phone_number").getAsString();
                                                                    String place_url = resultsJson.get("url").getAsString();

                                                                }


                                                            }
                                                        });

                                                        Restaurant restaurant = new Restaurant(formatted_address,name,rating,place_id,photo_reference,img_link,"","");
                                                        restaurantList.add(restaurant);

                                                    }


                                                    Log.i("InfoMapActivity ", "restaurant address " + resultsJsonArray.get(0).getAsJsonObject().get("formatted_address"));
                                                }

                                            }



                                            //Log.i("InfoMapActivity ", "restaurant object " + restaurant);

                                        }

                                    });
                        }
                    }

                });

        for (int i = 0; i < restaurantList.size(); i++){
            Log.i("InfoMapActivity ", "restaurant array lst: " + restaurantList.get(i));
        }

        return restaurantList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // LocationService
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        requestPermission();

        setTitle("Restaurant Finder");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        nameView = (TextView) findViewById(R.id.nameeAndUsername);

        /*Normal user - retrieving sign-in information*/
        String emailFromIntent = getIntent().getStringExtra("EMAIL");

        if (emailFromIntent != null) {
            nameView.setText("Welcome " + emailFromIntent);
        }

             /*Google user - retrieving sign-in information*/
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

        /*Facebook user - retrieving sign-in information*/
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                String name = user.getDisplayName();
                String email = user.getEmail();
                String uid = user.getUid();
                nameView.setText("Facebook: " + email);


                Log.wtf("Facebook email: ", email);
                Log.wtf("Facebook uid: ", uid);
                Log.wtf("Facebook name", name);

            } else {
                //goToLoginScreen();
            }

        getRestaurantsList();

    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    /*Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();*/


                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void logoutFromAll(){
        FirebaseAuth.getInstance().signOut(); //Disconnect from Facebook using FirebaseAuth service
        LoginManager.getInstance().logOut(); //Disconnect from google using LoginManager => Might have a logout issue...
        goToLoginScreen();
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_logout:
                logoutFromAll();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }




    class PagerAdapter extends FragmentPagerAdapter {

        String tabTitles[] = new String[]{"Restaurants", "Favorites"};
        Context context;

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new ListFragment();
                case 1:
                    return new ListFragment();
                case 2:
                    return new ListFragment();
            }

            return null;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            tv.setText(tabTitles[position]);
            return tab;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //google
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }



    private void handleSignInResult(GoogleSignInResult result) {

        Log.wtf("MainActivity ---> ", "handleSignInResult : " + result.isSuccess());

        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            nameView.setText("Google: " + account.getEmail());
            Log.wtf("Google email: ", account.getEmail());
            Log.wtf("Google uid: ", account.getId());
            Log.wtf("Google name", account.getDisplayName());
        }else{
           // goToLoginScreen();
        }
    }

    private void goToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //google
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
