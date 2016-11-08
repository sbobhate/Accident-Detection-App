package shantanubobhate.firebaseauth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;

public class DashboardActivity extends AppCompatActivity {

    private TextView textViewEmail;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private EditText editTextFirstName, editTextLastName, editTextPolicyNumber, editTextPhoneNumber;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
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

        textViewEmail.setText("Welcome " + user.getEmail());

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
                Toast.makeText(DashboardActivity.this, "Could not retrieve data.", Toast.LENGTH_SHORT).show();
            }
        });

        progressDialog.dismiss();
    }

    public void logout(View view)
    {
        try {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginScreenActivity.class));
        } catch (Exception e) {
            Toast.makeText(this, "Unsuccessful", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void saveToDatabase(View view)
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

        //EmergencyContacts emergencyContacts = new EmergencyContacts("value1", "value2", "value3");
        //databaseReference.child(user.getUid()).setValue(emergencyContacts);

        progressDialog.dismiss();
        Toast.makeText(DashboardActivity.this, "Save Successful!", Toast.LENGTH_SHORT).show();
    }
}
