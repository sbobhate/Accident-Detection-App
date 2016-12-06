package com.lifeline;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private final int MY_PERMISSION_REQUEST_CODE = 1;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    String email="";
    ExpandableListAdapter listAdapter;
    ExpandableListView mDrawerexpList;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    int[] login_icons=new int[]{
            R.drawable.carpool_32,
            R.drawable.myaccount,
            R.drawable.policy1,
            R.drawable.logout,
    };
    private Typeface custom_font;
    Toast toast;
    TextView toast_text;
    Typeface toast_font;
    LayoutInflater inflater;
    View layout2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        custom_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-MediumCn.otf");

        firebaseAuth = FirebaseAuth.getInstance();

        prepareListDataSignin();
        listAdapter=new ExpandableListAdapter1(this,listDataHeader,login_icons,custom_font,custom_font);

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user == null) {
            finish();
            startActivity(new Intent(this, LoginScreenActivity.class));
        }

        toast_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-Cn.otf");
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout2 = inflater.inflate(R.layout.custom_toast, (ViewGroup)this.findViewById(R.id.toast));
        toast_text = (TextView) layout2.findViewById(R.id.tv);
        toast = new Toast(this.getApplicationContext());

        //Toast variables initialisation
        toast_text.setTypeface(toast_font);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout2);

        mTitle = mDrawerTitle = getTitle();
        mDrawerexpList = (ExpandableListView)findViewById(R.id.left_drawer);
        mDrawerexpList.setGroupIndicator(null);
        custom_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-MediumCn.otf");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);


        //set drawer expandable list adapter
        mDrawerexpList.setAdapter(listAdapter);

        //Creation of expandable listView
        mDrawerexpList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if (groupPosition == 0) {
                    // Already in this Activity
                    mDrawerLayout.closeDrawer(mDrawerexpList);
                }
                if (groupPosition == 1) {
                    finish();
                    Intent intent=new Intent(DashboardActivity.this, MyEmerContActivity.class);
                    startActivity(intent);
                    mDrawerLayout.closeDrawer(mDrawerexpList);
                }
                if (groupPosition == 2) {
                    finish();
                    Intent intent=new Intent(DashboardActivity.this, MyAccount.class);
                    startActivity(intent);
                    mDrawerLayout.closeDrawer(mDrawerexpList);
                }
                if (groupPosition == 3){
                    try {
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(DashboardActivity.this, LoginScreenActivity.class));
                    } catch (Exception e) {
                        toast.setText("Unsuccessful");
                        toast.show();
                        e.printStackTrace();
                    }
                }
                return false;

            }
        });
        // Listview Group expanded listener
        mDrawerexpList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        // Listview Group collasped listener
        mDrawerexpList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close)
        {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().getThemedContext();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void startTrackingWrapper(View view) {
        // Ask for Permissions
        List<String> permissionsNeeded = new ArrayList<>();
        final List<String> permissionList = new ArrayList<>();

        if (!addPermission(permissionList, Manifest.permission.ACCESS_FINE_LOCATION)) permissionsNeeded.add("Location");
        if (!addPermission(permissionList, Manifest.permission.READ_PHONE_STATE)) permissionsNeeded.add("Read Phone State");
        if (!addPermission(permissionList, Manifest.permission.SEND_SMS)) permissionsNeeded.add("Send SMS");

        if (permissionList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int ii = 1; ii < permissionsNeeded.size(); ii++) {
                    message = message + ", " + permissionsNeeded.get(ii);
                }

                showRationaleMessage("App needs access to Location " + message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(DashboardActivity.this, permissionList.toArray(new String[permissionList.size()]), MY_PERMISSION_REQUEST_CODE);
                    }
                });
                return;
            }
            ActivityCompat.requestPermissions(DashboardActivity.this, permissionList.toArray(new String[permissionList.size()]), MY_PERMISSION_REQUEST_CODE);
            return;
        }

        // startTracking();
    }

    // Function to show dialog of the rationale for the permission
    private void showRationaleMessage(String message, DialogInterface.OnClickListener okListtener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListtener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean addPermission(List<String> permissionList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(permission);
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) return false;
        }
        return true;
    }

    private void startTracking() {
        Intent intent = new Intent(DashboardActivity.this, TrackingActivity.class);
        startActivity(intent);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                Map<String, Integer> perms = new HashMap<>();

                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);

                for (int ii = 0; ii < permissions.length; ii++) {
                    perms.put(permissions[ii], grantResults[ii]);
                }

                // Check for permissions granted
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
                {
                    // All permissions granted
                    startTracking();
                } else {
                    Toast.makeText(this, "You Suck.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Just Kidding. LOL...", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void prepareListDataSignin() {
        listDataHeader =new ArrayList<String>();
        //listDataChild = new HashMap<String, List<String>>();

        // Adding group data
        listDataHeader.add("Dashboard");
        listDataHeader.add("Emergency Contacts");
        listDataHeader.add("My Account");
        listDataHeader.add("Log Out");



    }


    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            //case R.id.search:
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void onBackPressed() {

        Log.d("Finish","Change Password   Activity");
        finish();
    }
}
