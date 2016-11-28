package com.lifeline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by sanya on 27/5/15.
 */
public class Add_EmergencyActivity extends Activity {
    EditText editText1,editText10;
    TextView btn1;
    Toast toast;
    TextView toast_text;
    Typeface toast_font;
    LayoutInflater inflater;
    View layout;
    int flag=1;
    DBEmergency db;
    FirebaseAuth firebaseAuth;
    String email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactadd_activity);
         firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        email=user.getEmail();

        toast_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-Cn.otf");
        inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) this.findViewById(R.id.toast));
        toast_text = (TextView) layout.findViewById(R.id.tv);
        toast = new Toast(this.getApplicationContext());
        editText1 = (EditText)findViewById(R.id.add_name);
        editText10=(EditText)findViewById(R.id.add_phone);
        editText10.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        btn1=(TextView)findViewById(R.id.text_add);
        db=new DBEmergency(this);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-UltLtCn.otf");
        editText1.setTypeface(custom_font);
        editText10.setTypeface(custom_font);
        btn1.setTypeface(custom_font, Typeface.BOLD);
        //Toast variables initialisation
        toast_text.setTypeface(toast_font);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText1.getText().toString().length()==0)
                {
                    toast_text.setText("Enter Name");
                    toast.show();
                }
                else if(editText10.toString().length()==0)
                {
                    toast_text.setText("Enter phone number");
                    toast.show();
                }
                else {
                    String text = db.addContact(new EmerContact(editText1.getText().toString(),editText10.getText().toString(),email));
                    toast_text.setText(text);
                    toast.show();
                    Intent intent = new Intent(Add_EmergencyActivity.this, MyEmerContActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }



}
