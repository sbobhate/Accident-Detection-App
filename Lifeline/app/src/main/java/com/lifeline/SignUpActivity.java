package com.lifeline;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private static final String KEY = "com.lifeline.secret";
    private static final String STATE = "com.lifeline.state";

    private EditText editTextEmail, editTextPassword;
    private Button buttonRegister;
    private Toast toast;
    private TextView toast_text;
    private Typeface toast_font;
    private LayoutInflater inflater;
    private View layout;
    private TextView textViewTitle;

    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private String firstName, lastName, phoneNumber, policyNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SharedPreferences.Editor editor = getSharedPreferences(KEY, MODE_PRIVATE).edit();
        editor.putString(STATE, "signUp");
        editor.commit();

        Bundle mBundle = getIntent().getExtras();
        firstName = mBundle.getString("firstName");
        lastName = mBundle.getString("lastName");
        phoneNumber = mBundle.getString("phoneNumber");
        policyNumber = mBundle.getString("policyNumber");

        //Custom Toast
        toast_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-Cn.otf");
        inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) this.findViewById(R.id.toast));
        toast_text = (TextView) layout.findViewById(R.id.tv);
        toast = new Toast(this.getApplicationContext());
        toast_text.setTypeface(toast_font);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        //Initialisation of all the components
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonRegister = (Button) findViewById(R.id.btnRegister);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);

        //Changing font of all layout components
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-UltLtCn.otf");
        editTextEmail.setTypeface(custom_font);
        editTextPassword.setTypeface(custom_font);
        buttonRegister.setTypeface(custom_font, Typeface.BOLD);
        textViewTitle.setTypeface(custom_font, Typeface.BOLD);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void register(View view)
    {

        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            toast_text.setText("Invalid Email,Try again");
            toast.show();
            return;
        }

        if(password.length()<8)
        {
            toast_text.setText("Password must be of 8 characters");
            toast.show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            toast_text.setText("No Email Entered");
            toast.show();
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            toast_text.setText("No Password Entered");
            toast.show();
            return;
        }

        progressDialog.setMessage("Registering...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Login with details
                            firebaseAuth.signInWithEmailAndPassword(email, password);
                            UserInformation userInformation = new UserInformation(firstName, lastName, policyNumber, phoneNumber);
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            databaseReference.child(user.getUid()).setValue(userInformation);
                            progressDialog.dismiss();
                            toast_text.setText("Welcome!!");
                            toast.show();
                            startActivity(new Intent(SignUpActivity.this, DashboardActivity.class));
                            finish();

                        } else {
                            toast_text.setText("Oops!! Try again later!");
                            toast.show();
                        }
                    }
                });
    }
}
