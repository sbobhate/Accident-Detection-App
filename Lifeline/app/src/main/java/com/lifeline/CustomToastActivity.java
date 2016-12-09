package com.lifeline;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sanya on 6/12/16.
 */

public class CustomToastActivity  {

    private static Toast mToast;
    private static TextView textViewToast;

    private static Context mContext;
    private static Activity mActivity;

    public static void CustomToastActivity(Activity activity ) {
        mActivity = activity;

        mToast = new Toast(mActivity);

        setLayoutAttributes();
    }
    
    private static void setLayoutAttributes(){
        // Setup Layout
        LayoutInflater mLayoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = mLayoutInflater.inflate(R.layout.custom_toast, (ViewGroup) mActivity.findViewById(R.id.toast));
        
        // Setup Text and Font
        textViewToast = (TextView) layout.findViewById(R.id.tv);
        Typeface fontToast = Typeface.createFromAsset(mActivity.getAssets(), "AvenirNextLTPro-Cn.otf");
        textViewToast.setTypeface(fontToast);
        mToast.setGravity(Gravity.BOTTOM, 0, 100);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(layout);
    }

    public static void showCustomToast(String text) {
        textViewToast.setText(text);
        mToast.show();
    }
}
