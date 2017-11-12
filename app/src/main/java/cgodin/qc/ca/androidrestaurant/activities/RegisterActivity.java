package cgodin.qc.ca.androidrestaurant.activities;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cgodin.qc.ca.androidrestaurant.R;
import cgodin.qc.ca.androidrestaurant.model.User;
import cgodin.qc.ca.androidrestaurant.sql.DatabaseHelper;
import cgodin.qc.ca.androidrestaurant.utilities.Utilities;

/**
 * Created by Ariel S on 2017-10-29.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //Views
    private View currentView;
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private Button btnRegister;
    private TextView tvLoginLink;

    //Objects
    private DatabaseHelper databaseHelper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    private void initViews() {
        currentView = findViewById(R.id.nestedScrollViewRegister);
        etName = (EditText) findViewById(R.id.etRegisterName);
        etEmail = (EditText) findViewById(R.id.etRegisterEmail);
        etPassword = (EditText) findViewById(R.id.etRegisterPassword);
        etPasswordConfirm = (EditText) findViewById(R.id.etRegisterPasswordConfirm);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvLoginLink = (TextView) findViewById(R.id.tvLoginLink);
        resizeViews();
    }

    private void initListeners() {
        btnRegister.setOnClickListener(this);
        tvLoginLink.setOnClickListener(this);
    }

    private void initObjects() {
        databaseHelper = new DatabaseHelper(RegisterActivity.this);
        user = new User();
    }

    /* Resizes the paddings (left and right) of a tablet */
    private void resizeViews(){
        //If a tablet is detected, resize paddingStart and paddingEnd
        if (Utilities.isTablet(this)) {
            int paddingPixel = 180;
            float density = getResources().getDisplayMetrics().density;
            int paddingDp = (int)(paddingPixel * density);
            currentView.setPadding(paddingDp,0,paddingDp,0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                postDataToSQLite();
                break;
            case R.id.tvLoginLink:
                finish();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void postDataToSQLite() {

        if(etName.getText().toString().trim().isEmpty()){
            Utilities.makeSnackbar(currentView, getString(R.string.error_message_name),getColor(R.color.colorAccent), getColor(R.color.colorText) );
            Utilities.hideSoftKeyboard(RegisterActivity.this);
        }else if(!Utilities.validateEmail(etEmail.getText().toString().trim())){
            Utilities.makeSnackbar(currentView, getString(R.string.error_message_email),getColor(R.color.colorAccent), getColor(R.color.colorText) );
            Utilities.hideSoftKeyboard(RegisterActivity.this);
        }else if(etPassword.getText().toString().isEmpty()){
            Utilities.makeSnackbar(currentView, getString(R.string.error_message_password),getColor(R.color.colorAccent), getColor(R.color.colorText) );
            Utilities.hideSoftKeyboard(RegisterActivity.this);
        }else if(!etPassword.getText().toString().equals(etPasswordConfirm.getText().toString())){
            Utilities.makeSnackbar(currentView, getString(R.string.error_password_match),getColor(R.color.colorAccent), getColor(R.color.colorText) );
            Utilities.hideSoftKeyboard(RegisterActivity.this);
        }else{

            if(!databaseHelper.checkUser(etEmail.getText().toString().trim())){

                user.setUtype(1); // 1 = Normal user
                user.setUid(null);
                user.setName( etName.getText().toString().trim() );
                user.setEmail( etEmail.getText().toString().trim().toLowerCase() );
                user.setPassword( etPassword.getText().toString().trim() );

                databaseHelper.addUser(user);
                Utilities.makeSnackbar(currentView, getString(R.string.success_message),getColor(R.color.colorAccent), getColor(R.color.colorText) );
                Utilities.hideSoftKeyboard(RegisterActivity.this);

                //empty fields
                etName.setText(null);
                etEmail.setText(null);
                etPassword.setText(null);
                etPasswordConfirm.setText(null);

            }else{
                Utilities.makeSnackbar(currentView, getString(R.string.error_email_exists),getColor(R.color.colorAccent), getColor(R.color.colorText) );
                Utilities.hideSoftKeyboard(RegisterActivity.this);
            }

        }

    }
}
