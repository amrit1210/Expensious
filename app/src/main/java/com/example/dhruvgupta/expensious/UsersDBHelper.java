package com.example.dhruvgupta.expensious;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Blob;
import java.util.ArrayList;

/**
 * Created by Gaurav on 3/11/15.
 */
public class UsersDBHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="Expensious";
    public static final String USER_TABLE="users";
    public static final String USER_COL_ID="u_id";
    public static final String USER_COL_IMAGE="u_image";
    public static final String USER_COL_NAME="u_name";
    public static final String USER_COL_EMAIL="u_email";
    public static final String USER_COL_PASSWORD="u_pass";
    public static final String USER_COL_FID="u_fid";

    public UsersDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String create_table_users="CREATE TABLE "+USER_TABLE
                +"("+USER_COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +USER_COL_NAME+" TEXT,"
                +USER_COL_IMAGE+" TEXT,"
                +USER_COL_EMAIL+" TEXT,"
                +USER_COL_PASSWORD+" TEXT,"
                +USER_COL_FID+" INTEGER)";
        db.execSQL(create_table_users);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public boolean addUser(String name, String email, String password, String image)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(USER_COL_NAME,name);
        contentValues.put(USER_COL_EMAIL,email);
        contentValues.put(USER_COL_PASSWORD,password);
        contentValues.put(USER_COL_IMAGE,image);

        db.insert(USER_TABLE,null,contentValues);
        return true;
    }

    public Cursor getUserData(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select *from "+USER_TABLE+" where "+USER_COL_ID+"="+id,null);
        return c;
    }

    public ArrayList getUserColEmail()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c= db.rawQuery("select * from "+USER_TABLE,null);
        ArrayList al=new ArrayList();
        String s=null;
        c.moveToFirst();
        while (c.isAfterLast()==false)
        {
            s=c.getString(c.getColumnIndex(USER_COL_EMAIL));
            al.add(s);
            c.moveToNext();
        }
        return al;
    }
    
    public int deleteUser(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        return db.delete(USER_TABLE,USER_COL_ID+"=?",new String[] {Integer.toString(id)});
    }

    public ArrayList<SignUpDB> getAllUsers()
    {
        ArrayList<SignUpDB> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+USER_TABLE, null);
        c.moveToFirst();
        while(c.isAfterLast()==false)
        {
            SignUpDB s1=new SignUpDB();
            s1.u_id=c.getInt(c.getColumnIndex(USER_COL_ID));
            s1.u_name=c.getString(c.getColumnIndex(USER_COL_NAME));
            s1.u_email=c.getString(c.getColumnIndex(USER_COL_EMAIL));
            s1.u_password=c.getString(c.getColumnIndex(USER_COL_PASSWORD));
            s1.u_image=c.getString(c.getColumnIndex(USER_COL_IMAGE));
            s1.u_fid=c.getInt(c.getColumnIndex(USER_COL_FID));
            arrayList.add(s1);
            Log.i("USER 1:", s1.u_id +"\t"+ s1.u_name +"\t"+ s1.u_email +"\t"+ s1.u_password +"\t"+ s1.u_image +"\t"+ s1.u_fid);
            c.moveToNext();
        }
        return  arrayList;
    }
}