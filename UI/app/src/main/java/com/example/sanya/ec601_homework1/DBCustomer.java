package com.example.sanya.ec601_homework1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBCustomer extends SQLiteOpenHelper {
    private Context context;
    int flag = 0, flag2 = 0;
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "CustomerDatabase";

    // Customer table name
    private static final String TABLE_CUSTOMER = "customers";

    // Customer Table Columns names
    private static final String CUSTOMER_NAME="name";
    private static final String CUSTOMER_EMAIL = "email";
    private static final String CUSTOMER_PASSWORD = "password";
    private static final String CUSTOMER_PHONE = "mobileno";
    //private static final String CUSTOMER_VERIFIED = "verified";


    public DBCustomer(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //Creating of databases
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CUSTOMER + "("
                +CUSTOMER_NAME +" VARCHAR(50),"
                + CUSTOMER_EMAIL + " VARCHAR(30),"
                + CUSTOMER_PASSWORD + " VARCHAR(30),"
                + CUSTOMER_PHONE + " VARCHAR(13)"
                //+ CUSTOMER_VERIFIED + " VARCHAR(5),"
                + ")";

        db.execSQL(CREATE_CUSTOMER_TABLE);
    }

    //Function of Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        // Create tables again
        onCreate(db);
    }

    //Adding values to databases
    public void addCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        flag = exist(db, customer);
        if (flag == 1) {

            ContentValues values = new ContentValues();
            values.put(CUSTOMER_NAME,customer.getName());
            values.put(CUSTOMER_EMAIL, customer.getEmail());
            values.put(CUSTOMER_PASSWORD,customer.getPassword());
            values.put(CUSTOMER_PHONE,customer.getPhone());
            //values.put(CUSTOMER_VERIFIED,customer.getVerified());


            // Inserting Row
            db.insert(TABLE_CUSTOMER, null, values);
            db.close();

        }
    }

    //to see if the record exists.
    public int exist(SQLiteDatabase db, Customer customer) {
        String emailsearchQuery = "SELECT * FROM " + TABLE_CUSTOMER + " where " + CUSTOMER_EMAIL + " = '" + customer.getEmail() + "'";
        Cursor c = db.rawQuery(emailsearchQuery, null);
        if (c.getCount() == 0 )
            flag =1;
        else
            flag=0;
        c.close();

        return flag;
    }


    //Search
    public List<Customer> getCustomer() {
        List<Customer> customerList = new ArrayList<Customer>();
        SQLiteDatabase db = getWritableDatabase();

        String searchquery = "SELECT * FROM " + TABLE_CUSTOMER ;
        Cursor cursor = db.rawQuery(searchquery, null);
        if (cursor.moveToFirst()) {
            do {
               Customer customer=new Customer();
                customer.setName(cursor.getString(0));
                customer.setEmail(cursor.getString(1));
                customer.setPassword(cursor.getString(2));
                customer.setPhone(cursor.getString(3));
                //customer.setVerified(cursor.getString(4));
                customerList.add(customer);

            } while (cursor.moveToNext());
        }

        db.close();

        return customerList;
    }

//    public String getCustomerEnP(String value) {
//        String s;
//        SQLiteDatabase db = getReadableDatabase();
//        String where = "SELECT " + CUSTOMER_PASSWORD + " FROM " + TABLE_CUSTOMER + " WHERE " + CUSTOMER_EMAIL + " LIKE '" + value+ "' OR "+CUSTOMER_PHONE+ " LIKE '" + value+ "';";
//        Cursor c = db.rawQuery(where, null);
//        if (c.moveToFirst())
//            s = c.getString(c.getColumnIndex(CUSTOMER_PASSWORD));
//        else
//            s = "Not Found";
//        return s;
//    }


    //Deleting single record
    public void deleteCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CUSTOMER, CUSTOMER_EMAIL + " = ?",
                new String[]{String.valueOf(customer.getEmail())});
        db.close();

    }

//    public  void updateVerification(String column,String value)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String updateQuery="UPDATE "+TABLE_CUSTOMER+" SET "+CUSTOMER_VERIFIED+"='true' WHERE "+column+" LIKE '"+value+"'";
//        db.execSQL(updateQuery);
//        db.close();
//    }
    public String getCustomerInfo(String column,String constant,String value)
    {
        String s;
        SQLiteDatabase db = getReadableDatabase();
        String where = "SELECT " + column + " FROM " + TABLE_CUSTOMER + " WHERE " + constant + " LIKE '" + value+ "';";
        Cursor c = db.rawQuery(where, null);
        if (c.moveToFirst())
            s = c.getString(c.getColumnIndex(column));
        else
            s = "Not Found";
        return s;
    }
    public  void updateCustomerInfo(String c1,String c2,String c3,String v1, String v2,String v3,String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery="UPDATE "+TABLE_CUSTOMER+" SET "
                            +c1+"='"+v1+"',"
                            +c2+"='"+v2+"',"
                            +c3+"='"+v3+"'"+
                             " WHERE email = '"+email+"'";
        db.execSQL(updateQuery);
        db.close();
    }
    //Counting records
    public int getCustomerCount() {

        String countQuery = "SELECT  * FROM " + TABLE_CUSTOMER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();

    }

    //Deleting all records
    public void deleteAllCustomers() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_CUSTOMER;
        db.execSQL(delete);
    }


    public int existingCustomer(String email,String phone) {
        String emailsearchQuery = "SELECT * FROM " + TABLE_CUSTOMER + " where " + CUSTOMER_EMAIL + " = '" + email + "'";
        String phonesearchQuery = "SELECT * FROM " + TABLE_CUSTOMER + " where " + CUSTOMER_PHONE + " = '" + phone + "'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(emailsearchQuery, null);
        Cursor c1 = db.rawQuery(phonesearchQuery, null);
        if (c.getCount() == 0 && c1.getCount()==0)
            flag =0;
        else if(c.getCount()!=0)
            flag=1;
        else if(c1.getCount()!=0)
            flag=2;

        c.close();
        c1.close();
        return flag;
    }

    public int existPh(String column, String s) {
        SQLiteDatabase db = getWritableDatabase();
        String phonesearchQuery = "SELECT " + column + " FROM " + TABLE_CUSTOMER + " WHERE " + column + " = '" + s+ "';";
        Cursor cur1 = db.rawQuery(phonesearchQuery, null);
        if (cur1.getCount() == 0 ) {
            flag = 0;
        } else if (cur1.getCount() != 0) {
            flag = 2;
        }
        cur1.close();

        return flag;
    }
    public int existE(String column, String s) {
        SQLiteDatabase db = getWritableDatabase();
        String emailsearchQuery = "SELECT " + column + " FROM " + TABLE_CUSTOMER + " WHERE " + column + " = '" + s+ "';";
        Cursor cur1 = db.rawQuery(emailsearchQuery, null);
        if (cur1.getCount() == 0 ) {
            flag = 2;
        } else if (cur1.getCount() != 0) {
            flag = 1;
        }
        cur1.close();

        return flag;
    }

    public void update(String column, String p_new,String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery="UPDATE "+TABLE_CUSTOMER+" SET password ='"+p_new+"' WHERE "+column+"= '"+value+"'";
        db.execSQL(updateQuery);
        db.close();
    }
}
