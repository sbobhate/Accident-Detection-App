package com.lifeline;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class PersonalInfoActivity extends AppCompatActivity {

    private static final String KEY = "com.lifeline.secret";
    private static final String STATE = "com.lifeline.state";

    private EditText editTextFirstName, editTextLastName, editTextPolicyNumber, editTextPhoneNumber;
    private Button btnPersonal;
    private Toast toast;
    private TextView toast_text;
    private Typeface toast_font;
    private LayoutInflater inflater;
    private View layout;
    private TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        SharedPreferences.Editor editor = getSharedPreferences(KEY, MODE_PRIVATE).edit();
        editor.putString(STATE, "personalInfo");
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

        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextPolicyNumber = (EditText) findViewById(R.id.editTextPolicyNumber);
        editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        btnPersonal = (Button) findViewById(R.id.btnPersonal);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        editTextPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        //Changing font of all layout components
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-UltLtCn.otf");
        editTextFirstName.setTypeface(custom_font);
        editTextLastName.setTypeface(custom_font);
        editTextPhoneNumber.setTypeface(custom_font);
        editTextPolicyNumber.setTypeface(custom_font);
        btnPersonal.setTypeface(custom_font, Typeface.BOLD);
        textViewTitle.setTypeface(custom_font, Typeface.BOLD);
    }

    public void goToHome(View view)
    {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String policyNumber = editTextPolicyNumber.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Please enter your first name", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Please enter your last name", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(policyNumber)) {
            Toast.makeText(this, "Please enter your policy number", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("firstName", firstName);
        mBundle.putString("lastName", lastName);
        mBundle.putString("policyNumber", policyNumber);
        mBundle.putString("phoneNumber", phoneNumber);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(mBundle);
        startActivity(intent);

    }
}
