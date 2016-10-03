package com.example.sanya.ec601_homework1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.util.Arrays;

import static com.google.android.gms.plus.Plus.PeopleApi;

public class MainActivity extends Activity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener
{
    private LoginButton loginBtn;
    Button text_login;
    TextView text_sign,or;
    EditText edit_user,edit_pass;
    DBCustomer db;
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    int flag;
    SessionCreator session1;
    private UiLifecycleHelper uiHelper;
    Toast toast;
    TextView toast_text;
    Typeface toast_font;
    LayoutInflater inflater;
    View layout;

    private GraphUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Declarations
        session1 = new SessionCreator(getApplicationContext());
        text_login = (Button) findViewById(R.id.text_login);
        text_sign = (TextView) findViewById(R.id.text_sign);
        or=(TextView)findViewById(R.id.or);
        edit_pass = (EditText) findViewById(R.id.edit_pass);
        edit_user = (EditText) findViewById(R.id.edit_user);
        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
        db=new DBCustomer(this);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-UltLtCn.otf");
        toast_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-Cn.otf");
        inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.custom_toast, (ViewGroup)this.findViewById(R.id.toast));
        toast_text = (TextView) layout.findViewById(R.id.tv);
        toast = new Toast(this.getApplicationContext());

        //Toast variables initialisation
        toast_text.setTypeface(toast_font);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

//        //setting edit text values
//            edit_pass.setText("hellodere");
//            edit_user.setText("7045462425");

        //onCLick Listeners
        findViewById(R.id.sign_in_button).setOnClickListener((android.view.View.OnClickListener) this);
        text_login.setOnClickListener(this);
        text_sign.setOnClickListener(this);

        //setting of typeface
        edit_user.setTypeface(custom_font);
        edit_pass.setTypeface(custom_font);
        text_sign.setTypeface(custom_font);
        or.setTypeface(custom_font);
        text_login.setTypeface(custom_font, Typeface.BOLD);

        //creation of database

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        loginBtn = (LoginButton) findViewById(R.id.login_button);
        loginBtn.setBackgroundResource(R.drawable.facebook);
        loginBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        loginBtn.setReadPermissions(Arrays.asList("email"));

    }


    /*
                Face book login implementation
            */
    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override

        public void call(Session session, SessionState state,
                         Exception exception) {

            if (state.isOpened()) {
                Request.newMeRequest(session, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                            String emailid = user.getProperty("email").toString();

                            session1.createLoginSession(user.getName(), emailid);
                            Intent i = new Intent(MainActivity.this, HomeActivity.class);
                           Toast.makeText(MainActivity.this, " Welcome " + emailid+ " (Facebook)", Toast.LENGTH_SHORT).show();
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            db.addCustomer(new Customer(user.getName(), emailid, "", ""));
                            Bundle b = new Bundle();
                            b.putString("phone", "");
                            b.putString("email", emailid);
                            i.putExtras(b);
                            startActivity(i);
                        }
                    }
                }).executeAsync();

                Log.d("MainActivity", "Facebook session opened.");
            } else if (state.isClosed()) {
                Log.d("MainActivity", "Facebook session closed.");
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }

    /*
    Google +login implementation
     */

    protected void onStart() {
        super.onStart();
    }
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    //onClick
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button
                && !mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            if(mConnectionResult!=null)
                resolveSignInError();
            else
                mGoogleApiClient.connect();
        }
        else
        if(v.getId()==R.id.text_login)
        {
            flag=exist();
            if(flag==0)
            {
                toast_text.setText("Invalid username and password");
                toast.show();
            }
            else if(flag==1)
            {
                if(edit_user.getText().toString().contains("@"))
                    session1.createLoginSession(edit_user.getText().toString(),edit_user.getText().toString());
                else
                    session1.createLoginSession(edit_user.getText().toString(), db.getCustomerInfo("email","mobileno",edit_user.getText().toString()));
                Intent i = new Intent(this, HomeActivity.class);
                Toast.makeText(MainActivity.this,"You have successfully logged in",Toast.LENGTH_SHORT).show();
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        }
        else if(v.getId()==R.id.text_sign)
        {
            Intent i=new Intent(this,SignupActivity.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        }
    }

    public int exist() {
        if(edit_user.getText().toString().contains("@"))
        {
            if (edit_pass.getText().toString().equals(db.getCustomerInfo("password","email",edit_user.getText().toString())))
                flag=1;
        }
        else if (edit_pass.getText().toString().equals(db.getCustomerInfo("password","mobileno",edit_user.getText().toString())))
            flag=1;
        else
            flag=0;
        return flag;
    }



    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
//        Toast.makeText(this, "User is connected!", Toast.LENGTH_SHORT).show();
        if (PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = PeopleApi.getCurrentPerson(mGoogleApiClient);
            String personName = currentPerson.getDisplayName();
            String personGooglePlusProfile = currentPerson.getUrl();

            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
            db.addCustomer(new Customer(personName,email,"",""));
            session1.createLoginSession(personName, email);
            Toast.makeText(MainActivity.this," Welcome "+personName+" (Google)", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle b=new Bundle();
            b.putString("phone","");
            b.putString("email",email);
            i.putExtras(b);
            startActivity(i);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!mIntentInProgress) {
            // Store the ConnectionResult so that we can use it later when the user clicks
            // 'sign-in'.
            mConnectionResult = connectionResult;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
        else
        {
            super.onActivityResult(requestCode,responseCode,intent);
            uiHelper.onActivityResult(requestCode, responseCode, intent);
        }

    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    public void onBackPressed() {
        Intent i=new Intent(this,HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
