package com.example.sanya.ec601_homework1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationPage extends Activity implements View.OnClickListener{


    Button text_signup;

    EditText edit_name, edit_userpolicy,edit_phone;
    String userpolicy,phoneno,pass,random,vcode;
    DBCustomer db;
    int flag=0;
    SessionCreator session;
    Toast toast;
    TextView toast_text;
    Typeface toast_font;
    LayoutInflater inflater;
    View layout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("Entered", "Still on");
        setContentView(R.layout.activity_registration);
        Log.d("Entered", "Still on");
        session=new SessionCreator(getApplicationContext());
        Log.d("Entered", "Still on");

//        Initialisation of buttons and editText
        text_signup=(Button)findViewById(R.id.text_signup);
        text_signup.setOnClickListener(this);
        edit_name=(EditText)findViewById(R.id.edit_name);
        edit_userpolicy=(EditText)findViewById(R.id.edit_userpolicy);
        edit_phone=(EditText)findViewById(R.id.edit_phone);
        db=new DBCustomer(this);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-UltLtCn.otf");

        edit_name.setTypeface(custom_font);
        edit_userpolicy.setTypeface(custom_font);
        edit_phone.setTypeface(custom_font);
        text_signup.setTypeface(custom_font, Typeface.BOLD);
        edit_name.getText();
        edit_userpolicy.getText();
        edit_phone.getText();
        //      Initialisation of Toast
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
//        Taking user's input
        phoneno=edit_phone.getText().toString();


//        verification code generation
//        char[] chars1 = "acd0mz3g48jlq9bo6e1h2s8f7k6ga95i3pn".toCharArray();
//        StringBuilder sb1 = new StringBuilder();
//        Random random1 = new Random();
//        for (int i = 0; i < 6; i++)
//        {
//            char c1 = chars1[random1.nextInt(chars1.length)];
//            sb1.append(c1);
//        }
//        random= sb1.toString();
        Log.d("Entered","Still on");

    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.text_signup)
        {
            String name = edit_name.getText().toString();
            String userpolicy = edit_userpolicy.getText().toString();
            String phoneno = edit_phone.getText().toString();
            //String verified="false";
            String pattern1 = "[A-Za-z]+";

            if (!phoneno.matches("[0|(+91)]*[5-9]{1}[0-9]{9}"))
            { toast_text.setText("Enter a 10 digit number");
                toast.show();}
            else if (edit_name.getText().toString().length() == 0 ||
                    edit_phone.getText().toString().length() == 0||
                    edit_userpolicy.getText().toString().length()==0)

            { toast_text.setText("Fill all the details");
                toast.show();}
            else
            {
                flag=db.existingCustomer(userpolicy,phoneno);
                if(flag==0)
                {
                    db.addCustomer(new Customer(name,userpolicy,phoneno));
//                    vcode = "Your verification code is " + random;
//                    try {
//                        SmsManager smsManager = SmsManager.getDefault();
//                        smsManager.sendTextMessage(phoneno, null, vcode, null, null);
//                        toast_text.setText("Verification code sent");
//                        toast.show();
//                    } catch (Exception e) {
//                        toast_text.setText("Sms failed, Try Again Later");
//                        toast.show();
//                        e.printStackTrace();
//                    }
                    session.createLoginSession(edit_phone.getText().toString(),
                            edit_phone.getText().toString());
//                    session.createLoginSession(edit_email.getText().toString(),
//                            edit_email.getText().toString());
                    Intent i = new Intent(this, HomeActivity.class);
//                    Intent i = new Intent(this, VerificationActivity.class);
//                    Bundle b = new Bundle();
//                    b.putString("vcode", vcode);
//                    b.putString("random", random);
//                    b.putString("phoneNo", phoneno);
//                    b.putString("verified",verified);
//                    i.putExtras(b);
//                    startActivity(i);
                    finish();
                }
                else if(flag==1)
                {
                    toast_text.setText("This policy is already registered");
                    toast.show();
                }
                else if(flag==2)
                {
                    toast_text.setText("This phone number is already registered");
                    toast.show();
                }
            }
        }

    }





}
