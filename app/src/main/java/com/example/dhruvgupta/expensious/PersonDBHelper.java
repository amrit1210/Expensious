package com.example.dhruvgupta.expensious;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Amrit on 3/19/2015.
 */
public class PersonDBHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="Expensious";
    public static final String PERSON_TABLE ="Person";
    public static final String PERSON_COL_ID ="p_id";
    public static final String PERSON_COL_NAME ="p_name";
    public static final String PERSON_COL_COLOR ="p_color";
    public static final String PERSON_COL_UID ="p_uid";

    public PersonDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String create_table_users="CREATE TABLE "+ PERSON_TABLE
                +"("+ PERSON_COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PERSON_COL_NAME +" TEXT,"
                + PERSON_COL_COLOR +" TEXT,"
                + PERSON_COL_UID +" INTEGER)";
        db.execSQL(create_table_users);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public boolean addPerson(String name, String color)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(PERSON_COL_NAME,name);
        contentValues.put(PERSON_COL_COLOR,color);
        db.insert(PERSON_TABLE,null,contentValues);
        return true;
    }

    public Cursor getPersonData(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select *from "+ PERSON_TABLE +" where "+ PERSON_COL_ID +"="+id,null);
        return c;
    }


    public int deletePerson(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        return db.delete(PERSON_TABLE, PERSON_COL_ID +"=?",new String[] {Integer.toString(id)});
    }
    public boolean updatePersonData(int id, String name,String color)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(PERSON_COL_NAME,name);
        contentValues.put(PERSON_COL_COLOR,color);
        db.update(PERSON_TABLE,contentValues,PERSON_COL_ID+"="+id,null);
        return true;
    }
    public ArrayList<PersonDB> getAllPerson()
    {
        ArrayList<PersonDB> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+ PERSON_TABLE, null);
        c.moveToFirst();
        while(c.isAfterLast()==false)
        {
            PersonDB p1=new PersonDB();
            p1.p_id=c.getInt(c.getColumnIndex(PERSON_COL_ID));
            p1.p_name=c.getString(c.getColumnIndex(PERSON_COL_NAME));
            p1.p_color=c.getString(c.getColumnIndex(PERSON_COL_COLOR));
            p1.p_u_id=c.getInt(c.getColumnIndex(PERSON_COL_UID));
            arrayList.add(p1);
            Log.i("USER 1:", p1.p_id + "\t" + p1.p_name + "\t" + p1.p_color + "\t" + p1.p_u_id + "\t" );
            c.moveToNext();
        }
        return  arrayList;
    }
}
