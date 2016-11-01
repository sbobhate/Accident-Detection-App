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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAccount extends Activity implements View.OnClickListener{


    Button text_signup;

    EditText edit_email,edit_pass,edit_pass2;
    String email,pass,random,vcode;
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
        setContentView(R.layout.activity_create_account);
        Log.d("Entered", "Still on");
         session=new SessionCreator(getApplicationContext());
        Log.d("Entered", "Still on");

//        Initialisation of buttons and editText
        text_signup=(Button)findViewById(R.id.text_signup);
        text_signup.setOnClickListener(this);
        edit_email=(EditText)findViewById(R.id.edit_email);
        edit_pass=(EditText)findViewById(R.id.edit_pass);
        edit_pass2=(EditText)findViewById(R.id.edit_pass2);
        db=new DBCustomer(this);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-UltLtCn.otf");


        edit_email.setTypeface(custom_font);
        edit_pass.setTypeface(custom_font);
        text_signup.setTypeface(custom_font, Typeface.BOLD);
        edit_email.getText();
        edit_pass.getText();
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
        email=edit_email.getText().toString();
        pass=edit_pass.getText().toString();

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
        if(v.getId()==R.id.text_signup) {
            String email = edit_email.getText().toString();
            String pass = edit_pass.getText().toString();
            String pass2 = edit_pass2.getText().toString();
            //String verified="false";
            String pattern1 = "[A-Za-z]+";


            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
             {toast_text.setText("Invalid Email,Try again");
                 toast.show();}
            else if(pass.length()<8)
             { toast_text.setText("Password must be at least 8 characters");
                 toast.show();}
            else if(pass != pass2)
             { toast_text.setText("Passwords must match");
                 toast.show();}
            else if (edit_email.getText().toString().length() == 0 ||
                    edit_pass.getText().toString().length()==0)
             { toast_text.setText("Fill all the details");
                toast.show();}
            else
            {
                //flag=db.existingCustomer(email,username);
                flag=0;
                if(flag==0)
                {
                    db.addCustomer(new Customer("",email,pass));
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

                    session.createLoginSession(edit_email.getText().toString(),
                            edit_email.getText().toString());
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
                    toast_text.setText("This email is already registered");
                    toast.show();
                }
                                }
            }
        }







}
