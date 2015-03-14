package com.example.dhruvgupta.expensious;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Gaurav on 3/11/15.
 */
public class CategoryDBHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="Expensious";
    public static final String CATEGORY_TABLE="category";
    public static final String CATEGORY_COL_C_UID ="c_u_id";
    public static final String CATEGORY_COL_C_ID ="c_id";
    public static final String CATEGORY_COL_C_NAME ="c_name";
    public static final String CATEGORY_COL_C_TYPE ="c_type";
    public static final String CATEGORY_COL_C_ICON ="c_icon";

    public CategoryDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String create_table_category="CREATE TABLE "+CATEGORY_TABLE
                +"("+ CATEGORY_COL_C_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CATEGORY_COL_C_UID +" INTEGER,"
                + CATEGORY_COL_C_NAME +" TEXT,"
                + CATEGORY_COL_C_TYPE +" TEXT,"
                + CATEGORY_COL_C_ICON +" TEXT)";
        db.execSQL(create_table_category);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public boolean addCategory(String name,String type,String icon)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(CATEGORY_COL_C_NAME,name);
        contentValues.put(CATEGORY_COL_C_TYPE,type);
        contentValues.put(CATEGORY_COL_C_ICON,icon);

        db.insert(CATEGORY_TABLE,null,contentValues);
        return true;
    }
    public ArrayList<CategoryDB> getAllCategories()
    {
        ArrayList<CategoryDB> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+CATEGORY_TABLE, null);
        c.moveToFirst();
        while(c.isAfterLast()==false)
        {
            CategoryDB c1=new CategoryDB();
            c1.c_name=c.getString(c.getColumnIndex(CATEGORY_COL_C_NAME));
            c1.c_type=c.getString(c.getColumnIndex(CATEGORY_COL_C_TYPE));
            c1.c_icon=c.getString(c.getColumnIndex(CATEGORY_COL_C_ICON));
            arrayList.add(c1);
            Log.i("CATEGORY 1:", c1.c_u_id + c1.c_id + c1.c_type + c1.c_name+ c1.c_icon);
            c.moveToNext();
        }
        return  arrayList;
    }
}