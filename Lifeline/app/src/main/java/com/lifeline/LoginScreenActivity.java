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

public class LoginScreenActivity extends AppCompatActivity {

    private static final String KEY = "com.lifeline.secret";
    private static final String STATE = "com.lifeline.state";
    private EditText editTextEmail, editTextPassword;
    private Button btnLogin;
    Toast toast;
    TextView toast_text;
    Typeface toast_font;
    LayoutInflater inflater;
    View layout;
    private TextView textViewRegister,textView1;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        SharedPreferences.Editor editor = getSharedPreferences(KEY, MODE_PRIVATE).edit();
        editor.putString(STATE, "login");
        editor.commit();


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
        btnLogin = (Button) findViewById(R.id.btnLogin);
        textViewRegister = (TextView)findViewById(R.id.textViewRegister);
        textView1 = (TextView) findViewById(R.id.textView1);


        //Changing font of all layout components
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-UltLtCn.otf");
        editTextEmail.setTypeface(custom_font);
        editTextPassword.setTypeface(custom_font);
        textViewRegister.setTypeface(custom_font);
        btnLogin.setTypeface(custom_font, Typeface.BOLD);
        textView1.setTypeface(custom_font, Typeface.BOLD);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            // Start Dashboard Activity
//            toast_text.setText("Already Logged In!");
//            toast.show();
            finish();
            startActivity(new Intent(this, com.lifeline.DashboardActivity.class));
        }
    }

    public void login(View view)
    {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

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

//        progressDialog.setMessage("Logging In...");
//        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Start Dashboard Activity
                            toast_text.setText("Logged In!");
                            toast.show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        }
                        else
                        {
                            toast_text.setText("Incorrect Credentials.");
                            toast.show();
                            //Toast.makeText(LoginScreenActivity.this, "Incorrect Credentials.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void goToRegister(View view)
    {
        finish();
        startActivity(new Intent(this,PersonalInfoActivity.class));
    }
}
