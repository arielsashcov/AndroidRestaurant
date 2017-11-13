package cgodin.qc.ca.androidrestaurant.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cgodin.qc.ca.androidrestaurant.R;
import cgodin.qc.ca.androidrestaurant.fragments.ListFragment;

/**
 * Created by Ariel S on 2017-10-31.
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private TextView nameView;

    //TODO: Make methods for each logged user - divide code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
