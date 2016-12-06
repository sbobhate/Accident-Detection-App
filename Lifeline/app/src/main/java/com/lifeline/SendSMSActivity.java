package com.lifeline;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class SendSMSActivity extends Activity {

    private GPSHandler mGPSHandler;
    Button send, cancel;
    TextView phone1, name1;
    TextView timerText,emergencymessage;
    private static final String FORMAT = "%02d:%02d";
    CountDownTimer timer;
    String names="",phones="", email="";
    String ph[]=new String[3];
    String n[]=new String[3];
    private FirebaseAuth firebaseAuth;
    private Typeface custom_font;
    String username="",location="", hospital="";
    Toast toast;
    TextView toast_text;
    Typeface toast_font;
    LayoutInflater inflater;
    View layout2;
    ImageView alarm;

    private MediaPlayer mediaPlayeralarm;
    public static int oneTimeOnly = 0;
    String message="";
    List<EmerContact> contact;
    ArrayList<EmerContact> add=new ArrayList<EmerContact>();
    DBEmergency db;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGPSHandler = new GPSHandler(this);

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        email=user.getEmail();
        
        location = mGPSHandler.getCurrentAddress();

        message = "Alert! It appears  that the USER may have been in a car accident. USER has chosen you as their emergency contact. USER's current location is " + location + " . Nearby hospitals include HOSPITAL1, HOSPITAL2";
        setContentView(R.layout.activity_sendsms);

        toast_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-Cn.otf");
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout2 = inflater.inflate(R.layout.custom_toast, (ViewGroup)this.findViewById(R.id.toast));
        toast_text = (TextView) layout2.findViewById(R.id.tv);
        toast = new Toast(this.getApplicationContext());
        timerText = (TextView) findViewById(R.id.timer);
        emergencymessage = (TextView) findViewById(R.id.emergencymessage);
        phone1 = (TextView) findViewById(R.id.phone1);
        name1 = (TextView) findViewById(R.id.name1);
        send = (Button) findViewById(R.id.send);
        cancel = (Button) findViewById(R.id.cancel);
        mediaPlayeralarm = MediaPlayer.create(this, R.raw.rising_swoops);
        db=new DBEmergency(this);


        contact = db.getContact(email);
        for (EmerContact cn : contact) {
            add.add(cn);
        }

        for (int i=0; i<add.size(); i++){
        EmerContact contact=add.get(i);
        n[i]=contact._name;
        ph[i]=contact._phone;
        names=names+n[i]+'\n';
        phones=phones+ph[i]+'\n';}

        name1.setText(names);
        phone1.setText(phones);

        emergencymessage.setText(message);

        final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        alarm = (ImageView) findViewById(R.id.imageView2);
        alarm.startAnimation(animShake);
        timerText = (TextView) findViewById(R.id.timer);
        emergencymessage = (TextView) findViewById(R.id.emergencymessage);
        phone1 = (TextView) findViewById(R.id.phone1);
        name1 = (TextView) findViewById(R.id.name1);
        cancel = (Button) findViewById(R.id.cancel);
        send = (Button) findViewById(R.id.send);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-UltLtCn.otf");
        phone1.setTypeface(custom_font,Typeface.BOLD);
        name1.setTypeface(custom_font,Typeface.BOLD);
        emergencymessage.setTypeface(custom_font, Typeface.BOLD);
        cancel.setTypeface(custom_font, Typeface.BOLD);
        send.setTypeface(custom_font, Typeface.BOLD);
        timerText.setTypeface(custom_font,Typeface.BOLD_ITALIC);

        //Toast variables initialisation
        toast_text.setTypeface(toast_font);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout2);

        timer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerText.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                mediaPlayeralarm.start();

                send.setEnabled(true);
            }

            public void onFinish() {
                mediaPlayeralarm.stop();
                mediaPlayeralarm.release();
                timer.cancel();
                //insertDummyContactWrapper();
                sendSMSMessage();

            }
        };

        timer.start();
    }

    public void cancelAlarm(View view) {
        Toast.makeText(this, "Alarm was cancelled", Toast.LENGTH_SHORT).show();
        mediaPlayeralarm.pause();
        timer.cancel();
        finish();
    }

    public void sendButtonPress(View view) {
        sendSMSMessage();
    }

    protected void sendSMSMessage() {
        Toast.makeText(this, "Sending SMS", Toast.LENGTH_SHORT).show();
        mediaPlayeralarm.pause();
        timer.cancel();
        try {
            for (int i=0; i<add.size(); i++){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(ph[i], null, message, null, null);
                toast.setText("SMS sent to "+n[i]);
                toast.show();}

        } catch (Exception e) {
            toast.setText("SMS failed, please try again. ");
            toast.show();
            e.printStackTrace();
        }
        finish();
    }
}
