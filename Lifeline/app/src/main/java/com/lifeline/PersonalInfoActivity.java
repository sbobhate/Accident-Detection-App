package com.lifeline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PersonalInfoActivity extends AppCompatActivity {

    private static final String KEY = "com.lifeline.secret";
    private static final String STATE = "com.lifeline.state";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private EditText editTextFirstName, editTextLastName, editTextPolicyNumber, editTextPhoneNumber;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        SharedPreferences.Editor editor = getSharedPreferences(KEY, MODE_PRIVATE).edit();
        editor.putString(STATE, "personalInfo");
        editor.commit();

        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextPolicyNumber = (EditText) findViewById(R.id.editTextPolicyNumber);
        editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user == null) {
            finish();
            startActivity(new Intent(this, LoginScreenActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> values = new ArrayList<String>(4);
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    values.add(child.getValue().toString());
                }

                if (!values.isEmpty()) {
                    editTextFirstName.setText(values.get(0));
                    editTextLastName.setText(values.get(1));
                    editTextPhoneNumber.setText(values.get(2));
                    editTextPolicyNumber.setText(values.get(3));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PersonalInfoActivity.this, "Could not retrieve data.", Toast.LENGTH_SHORT).show();
            }
        });

        progressDialog.dismiss();
    }

    public void goToVerification(View view)
    {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String policyNumber = editTextPolicyNumber.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        UserInformation userInformation = new UserInformation(firstName, lastName, policyNumber, phoneNumber);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        progressDialog.setMessage("Saving...");
        progressDialog.show();

        databaseReference.child(user.getUid()).setValue(userInformation);

        progressDialog.dismiss();
        Toast.makeText(PersonalInfoActivity.this, "Save Successful!", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(this, PhoneVerificationActivity.class));
    }
}
