package cgodin.qc.ca.androidrestaurant.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import cgodin.qc.ca.androidrestaurant.R;
import cgodin.qc.ca.androidrestaurant.model.User;
import cgodin.qc.ca.androidrestaurant.sql.DatabaseHelper;
import cgodin.qc.ca.androidrestaurant.utilities.Utilities;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    public static final int GOOGLE_SIGN_IN_CODE = 777;
    public static final int FACEBOOK_SIGN_IN_CODE = 64206;

    //Views
    private View currentView;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private LoginButton btnLoginFacebook;
    private SignInButton btnLoginGoogle;
    private TextView tvRegisterLink;

    //Objects
    private User user;
    private DatabaseHelper databaseHelper; //db
    private CallbackManager callbackManager; //fb
    private GoogleApiClient googleApiClient; //google
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();



        //progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        user = new User(); // exception: user object must be initialized before all to avoid null exceptions
        initViews();    //initialize the views
        initListener(); //initialize the listeners
        initObjects();  //initialize the objects

        /* Assuming at this step all views and objects are initialized */
        printHashKey();

    }

    /* Resizes the paddings (left and right) of a tablet */
    private void resizeViews(){
        //If a tablet is detected, resize paddingStart and paddingEnd
        if (Utilities.isTablet(this)) {
            int paddingPixel = 180;
            float density = getResources().getDisplayMetrics().density;
            int paddingDp = (int)(paddingPixel * density); //calculates pixel to dp
            currentView.setPadding(paddingDp,0,paddingDp,0);
        }
    }

    /* DO NOT REMOVE! */
    /* PRINT HASHKEY FOR FACEBOOK */
    public void printHashKey() {
        String TAG = "MY HASH KEY"; //
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void initViews() {
        currentView = findViewById(R.id.nestedScrollViewLogin);
        etEmail = (EditText) findViewById(R.id.etLoginEmail);       //Email
        etPassword = (EditText) findViewById(R.id.etLoginPassword); //Password
        btnLogin = (Button) findViewById(R.id.btnLogin);            //Login
        btnLoginFacebook = (LoginButton) findViewById(R.id.btnLoginFacebook); //Login Facebook
        btnLoginGoogle = (SignInButton) findViewById(R.id.btnLoginGoogle); //Login Google
        tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);  //Register

        resizeViews();
    }

    private void initListener() {
        btnLogin.setOnClickListener(this);
        btnLoginGoogle.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser result = firebaseAuth.getCurrentUser();
                if(result != null){

                    // Create a new user if he doesn't already exist...
                    if(!databaseHelper.checkUid(result.getUid())){
                        user.setUtype(3); // 3 = Facebook user
                        user.setUid(result.getUid());
                        user.setName( result.getDisplayName() );
                        user.setEmail( result.getEmail() );
                        user.setPassword( null );
                        databaseHelper.addUser(user);
                    }

                    Log.wtf("LoginActivity", "Facebook uid:" + result.getUid());
                    Log.wtf("LoginActivity", "Facebook name:" + result.getDisplayName());
                    Log.wtf("LoginActivity", "Facebook email:" + result.getEmail());
                    goToMainScreen();
                }
            }
        };

    }

    private void initObjects() {
        databaseHelper = new DatabaseHelper(LoginActivity.this);


        //facebook callback
        callbackManager = CallbackManager.Factory.create();

        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCancel() {
                Utilities.makeSnackbar(currentView, getString(R.string.error_logging_in),getColor(R.color.colorAccent), getColor(R.color.colorText) );
                Utilities.hideSoftKeyboard(LoginActivity.this);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onError(FacebookException error) {
                Utilities.makeSnackbar(currentView, getString(R.string.error_logging_in),getColor(R.color.colorAccent), getColor(R.color.colorText) );
                Utilities.hideSoftKeyboard(LoginActivity.this);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        /*Ask for facebook permissions*/
        btnLoginFacebook.setReadPermissions(Arrays.asList("public_profile", "email"));


        /* Google */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }

    private void handleFacebookAccessToken(AccessToken accessToken) {

       // progressBar.setVisibility(View.VISIBLE);
       // btnLoginFacebook.setVisibility(View.GONE);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Utilities.makeSnackbar(currentView, getString(R.string.error_logging_in),getColor(R.color.colorAccent), getColor(R.color.colorText) );
                    Utilities.hideSoftKeyboard(LoginActivity.this);
                }
                //progressBar.setVisibility(View.GONE);
                //btnLoginFacebook.setVisibility(View.VISIBLE);
            }
        });
    }

    private void goToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent){
        super.onActivityResult(requestCode, responseCode, intent);


        switch(requestCode){
            case FACEBOOK_SIGN_IN_CODE: //Facebook login
                callbackManager.onActivityResult(requestCode, responseCode, intent);
                break;
            case GOOGLE_SIGN_IN_CODE: //Google login
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
                handleSignInResult(result);
                break;
        }

        //Toast.makeText(getApplicationContext(), "requestCode:"+requestCode+" responseCode:"+responseCode + "intent"+intent.toString(), Toast.LENGTH_LONG).show();
        //Log.wtf("Intent->", intent.getExtras().toString());

    }

    //Google: handling sign-in result
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){

            Log.e("LoginActivity", "Google handSignInResult == true");

            // Create a new user if he doesn't already exist...
            if(!databaseHelper.checkUid(result.getSignInAccount().getId())){
                //Create a google user
                user.setUtype(2); // 2 = Google user
                user.setUid(result.getSignInAccount().getId());
                user.setName( result.getSignInAccount().getDisplayName() );
                user.setEmail( result.getSignInAccount().getEmail() );
                user.setPassword( null );
                databaseHelper.addUser(user);
            }

            Log.wtf("LoginActivity", "Google email:"+result.getSignInAccount().getEmail());
            Log.wtf("LoginActivity", "Google uid:"+result.getSignInAccount().getId());
            Log.wtf("LoginActivity", "Google name:"+result.getSignInAccount().getDisplayName());
            nextActivity();
        }else{
            Log.e("LoginActivity", "Google handSignInResult == false");
            Utilities.makeSnackbar(currentView, getString(R.string.error_logging_in),getColor(R.color.colorAccent), getColor(R.color.colorText) );
            Utilities.hideSoftKeyboard(LoginActivity.this);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }


    private void nextActivity(){

        Intent main = new Intent(LoginActivity.this, MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(main);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                verifyFromSQLite();
                break;
            case R.id.tvRegisterLink:
                Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.btnLoginGoogle:
                if(Utilities.isNetworkAvailable(getApplicationContext())){
                    Intent intentSignIn = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    startActivityForResult(intentSignIn, GOOGLE_SIGN_IN_CODE);
                }else{
                    Utilities.makeSnackbar(currentView, getString(R.string.error_network),getColor(R.color.colorAccent), getColor(R.color.colorText) );
                    Utilities.hideSoftKeyboard(LoginActivity.this);
                }
                break;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verifyFromSQLite() {

        //Validate email and password
        if(!Utilities.validateEmail(etEmail.getText().toString().trim())){
            Utilities.makeSnackbar(currentView, getString(R.string.error_message_email),getColor(R.color.colorAccent), getColor(R.color.colorText) );
            Utilities.hideSoftKeyboard(LoginActivity.this);
        }else if(etPassword.getText().toString().isEmpty()){
            Utilities.makeSnackbar(currentView, getString(R.string.error_message_password),getColor(R.color.colorAccent), getColor(R.color.colorText) );
            Utilities.hideSoftKeyboard(LoginActivity.this);
        }else{
            //Verify user and password from SQLite
            if(databaseHelper.checkUser(etEmail.getText().toString().trim().toLowerCase(), etPassword.getText().toString().trim())){
                Intent userActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                userActivityIntent.putExtra("EMAIL", etEmail.getText().toString().trim());

                Utilities.hideSoftKeyboard(LoginActivity.this);

                etEmail.setText(null);
                etPassword.setText(null);

                startActivity(userActivityIntent);
            }else{
                Utilities.makeSnackbar(currentView, getString(R.string.error_valid_email_password),getColor(R.color.colorAccent), getColor(R.color.colorText) );
                Utilities.hideSoftKeyboard(LoginActivity.this);
            }
        }

    }

    //Google
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
