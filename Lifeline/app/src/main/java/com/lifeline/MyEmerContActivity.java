package com.lifeline;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class MyEmerContActivity extends ActionBarActivity
{

    private FirebaseAuth firebaseAuth;

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
    Button btn_add;
    RecyclerView dataList;
    List<EmerContact> contact;
    ArrayList<EmerContact> add=new ArrayList<EmerContact>();
    DBEmergency db;
    private ContactListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        custom_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-MediumCn.otf");

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        email=user.getEmail();

        prepareListDataSignin();
        listAdapter=new ExpandableListAdapter1(this,listDataHeader,login_icons,custom_font,custom_font);

        setContentView(R.layout.mycontacts);

        //declarations
        toast_font = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-Cn.otf");
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout2 = inflater.inflate(R.layout.custom_toast, (ViewGroup)this.findViewById(R.id.toast));
        toast_text = (TextView) layout2.findViewById(R.id.tv);
        toast = new Toast(this.getApplicationContext());
        btn_add=(Button)findViewById(R.id.btn_add);
        dataList = (RecyclerView) findViewById(R.id.listView);
        db=new DBEmergency(this);

        //recycler View implementation
        dataList.setLayoutManager(new LinearLayoutManager(this));
        dataList.setItemAnimator(new DefaultItemAnimator());

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
                    startActivity(new Intent(MyEmerContActivity.this, DashboardActivity.class));
                    mDrawerLayout.closeDrawer(mDrawerexpList);
                }
                if (groupPosition == 1) {
                    mDrawerLayout.closeDrawer(mDrawerexpList);
                }
                if (groupPosition == 2) {
                    mDrawerLayout.closeDrawer(mDrawerexpList);
                }
                if (groupPosition==3)
                {
                    try {
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(MyEmerContActivity.this, LoginScreenActivity.class));
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




        //to show image in action bar
//        getSupportActionBar().setDisplayOptions(getSupportActionBar().getDisplayOptions()
//                | ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        //imageView = new ImageView(getSupportActionBar().getThemedContext());
//        //imageView.setImageResource(R.drawable.img_i_4);
//        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
//                810,
//                180,Gravity.CENTER_HORIZONTAL);
//        imageView.setLayoutParams(layoutParams);
//        getSupportActionBar().setCustomView(imageView);



        // enable ActionBar app icon to behave as action to toggle nav drawer
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

        contact = db.getContact(email);
        for (EmerContact cn : contact) {
            add.add(cn);
        }
        adapter = new ContactListAdapter(add, R.layout.emercontact_list_item,MyEmerContActivity.this,email);
        dataList.setAdapter(adapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyEmerContActivity.this,Add_EmergencyActivity.class);
                startActivity(intent);
            }
        });
    }

    // Expandable list items initialisation
    private void prepareListDataSignin() {
        listDataHeader =new  ArrayList<String>();
        //listDataChild = new HashMap<String, List<String>>();

        // Adding group data
        listDataHeader.add("Start Driving");
        listDataHeader.add("Emergency Contacts");
        listDataHeader.add("Policy Info");
        //listDataHeader.add("My Account");
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
