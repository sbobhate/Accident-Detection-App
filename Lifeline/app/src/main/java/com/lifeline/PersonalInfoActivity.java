package com.lifeline;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
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
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private EditText editTextFirstName, editTextLastName, editTextPolicyNumber, editTextPhoneNumber;
    private Button btnPersonal;
    Toast toast;
    TextView toast_text;
    Typeface toast_font;
    LayoutInflater inflater;
    View layout;
    TextView textView1;
    private ProgressDialog progressDialog;
    String email="",pass="";

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
        textView1 = (TextView) findViewById(R.id.textView1);
        editTextPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        //Changing font of all layout components
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-UltLtCn.otf");
        editTextFirstName.setTypeface(custom_font);
        editTextLastName.setTypeface(custom_font);
        editTextPhoneNumber.setTypeface(custom_font);
        editTextPolicyNumber.setTypeface(custom_font);
        btnPersonal.setTypeface(custom_font, Typeface.BOLD);
        textView1.setTypeface(custom_font, Typeface.BOLD);

        firebaseAuth = FirebaseAuth.getInstance();

//        final FirebaseUser user = firebaseAuth.getCurrentUser();
//
//        if (user == null) {
//            finish();
//            startActivity(new Intent(this, LoginScreenActivity.class));
//        }
//
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//
//        progressDialog = new ProgressDialog(this);
//
//        progressDialog.setMessage("Fetching Data...");
//        progressDialog.show();
//
//        databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                ArrayList<String> values = new ArrayList<String>(4);
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    values.add(child.getValue().toString());
//                }
//
//                if (!values.isEmpty()) {
//                    editTextFirstName.setText(values.get(0));
//                    editTextLastName.setText(values.get(1));
//                    editTextPhoneNumber.setText(values.get(2));
//                    editTextPolicyNumber.setText(values.get(3));
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                toast_text.setText("Could not retrieve data!! ");
//                toast.show();
//            }
//        });

        //progressDialog.dismiss();
    }

    public void goToHome(View view)
    {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String policyNumber = editTextPolicyNumber.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();


            Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
            Bundle b=new Bundle();
            b.putString("Fname",firstName);
            b.putString("Lname",lastName);
            b.putString("policy",policyNumber);
            b.putString("Phone",phoneNumber);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtras(b);
            startActivity(intent);

    }
}
