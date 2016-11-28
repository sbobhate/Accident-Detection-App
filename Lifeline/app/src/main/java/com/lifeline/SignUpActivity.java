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
    private Button btnRegister;
    Toast toast;
    TextView toast_text;
    Typeface toast_font;
    LayoutInflater inflater;
    View layout;
    private DatabaseReference databaseReference;
    private TextView textViewSignin,textView1;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    String firstname="",lastname="",phone="",policyno="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SharedPreferences.Editor editor = getSharedPreferences(KEY, MODE_PRIVATE).edit();
        editor.putString(STATE, "signUp");
        editor.commit();

        Bundle b=getIntent().getExtras();
        firstname=b.getString("Fname");
        lastname=b.getString("Lname");
        phone=b.getString("Phone");
        policyno=b.getString("policy");

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
        btnRegister= (Button) findViewById(R.id.btnRegister);
        //textViewSignin = (TextView)findViewById(R.id.textViewSignin);
        textView1 = (TextView) findViewById(R.id.textView1);

        //Changing font of all layout components
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-UltLtCn.otf");
        editTextEmail.setTypeface(custom_font);
        editTextPassword.setTypeface(custom_font);
        //textViewSignin.setTypeface(custom_font);
        btnRegister.setTypeface(custom_font, Typeface.BOLD);
        textView1.setTypeface(custom_font, Typeface.BOLD);
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
        }

        else if(password.length()<8)
        {
            toast_text.setText("Password must be of 8 characters");
            toast.show();
        }

        else if (TextUtils.isEmpty(email)) {
            toast_text.setText("No Email Entered");
            toast.show();
            return;
        }

        else if (TextUtils.isEmpty(password))
        {
            toast_text.setText("No Password Entered");
            toast.show();
            return;
        }

//        progressDialog.setMessage("Registering...");
//        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Login with details
                            firebaseAuth.signInWithEmailAndPassword(email, password);
                            UserInformation userInformation = new UserInformation(firstname, lastname, policyno, phone);
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            databaseReference.child(user.getUid()).setValue(userInformation);
                            progressDialog.dismiss();
                            toast_text.setText("Welcome!!");
                            toast.show();
                            startActivity(new Intent(SignUpActivity.this, DashboardActivity.class));
                            finish();

                        }
                        else
                        {
                            toast_text.setText("Oops!! Try again later!");
                            toast.show();
                        }
                    }
                });
    }


}
