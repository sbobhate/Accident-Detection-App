package com.example.sanya.ec601_homework1;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class HomeActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        ImageView imageView;
//        getSupportActionBar().setDisplayOptions(getSupportActionBar().getDisplayOptions()
//                | ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        imageView = new ImageView(getSupportActionBar().getThemedContext());
//        imageView.setImageResource(R.drawable.img_i_2);
//        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
//                810,
//                180, Gravity.CENTER_HORIZONTAL);
//        imageView.setLayoutParams(layoutParams);
//        getSupportActionBar().setCustomView(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.text_contact) {

        }
        else if (v.getId() == R.id.text_policy) {

        }
        else if (v.getId() == R.id.text_drivinginfo) {

        }
    }
}