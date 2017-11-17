package cgodin.qc.ca.androidrestaurant.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import cgodin.qc.ca.androidrestaurant.R;
import cgodin.qc.ca.androidrestaurant.fragments.ListFragment;
import cgodin.qc.ca.androidrestaurant.model.Restaurant;

/**
 * Created by Ariel S on 2017-10-31.
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private TextView nameView;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public ArrayList<Restaurant> restaurantList = new ArrayList<Restaurant>();

    // LocationService
    private FusedLocationProviderClient mFusedLocationClient;


    //TODO: Make methods for each logged user - divide code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //new JSONGetRequest(this).execute();

        // LocationService
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        requestPermission();


        setTitle("Restaurant Finder");//

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

            case R.id.change_radius:
                showCustomDialogRadiusChange();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showCustomDialogRadiusChange(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.change_radius);
        alert.setMessage(R.string.text_select_value_km);

        LinearLayout linear = new LinearLayout(this);

        linear.setOrientation(LinearLayout.VERTICAL);
        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMaxValue(50);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(true);

        linear.addView(numberPicker);
        alert.setView(linear);

        alert.setPositiveButton("Set",new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int id)
            {
                Toast.makeText(getApplicationContext(), "Radius has changed to " + numberPicker.getValue() + "km",Toast.LENGTH_LONG).show();
            }
        });

        alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int id)
            {
                //do nothing...
            }
        });

        alert.show();
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
                case 0: //list of restaurants
                    return new ListFragment();
                case 1: //favorites
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
