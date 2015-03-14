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
public class SubCategoryDBHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="Expensious";
    public static final String SUBCATEGORY_TABLE="sub_category";
    public static final String SUBCATEGORY_COL_SUB_ID ="sub_id";
    public static final String SUBCATEGORY_COL_SUB_CID ="sub_c_id";
    public static final String SUBCATEGORY_COL_SUB_NAME ="sub_name";
    public static final String SUBCATEGORY_COL_SUB_ICON ="sub_icon";

    public SubCategoryDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String create_table_subcategory="CREATE TABLE "+SUBCATEGORY_TABLE
                +"("+ SUBCATEGORY_COL_SUB_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SUBCATEGORY_COL_SUB_CID +" INTEGER,"
                + SUBCATEGORY_COL_SUB_NAME +" TEXT,"
                + SUBCATEGORY_COL_SUB_ICON +" TEXT)";
        db.execSQL(create_table_subcategory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public boolean addSubCategory(String name,String icon)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(SUBCATEGORY_COL_SUB_NAME,name);
        contentValues.put(SUBCATEGORY_COL_SUB_ICON,icon);

        db.insert(SUBCATEGORY_TABLE,null,contentValues);
        return true;
    }
    public ArrayList<SubCategoryDB> getAllSubCategories()
    {
        ArrayList<SubCategoryDB> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+SUBCATEGORY_TABLE, null);
        c.moveToFirst();
        while(c.isAfterLast()==false)
        {
            SubCategoryDB sub1=new SubCategoryDB();
            sub1.sub_name=c.getString(c.getColumnIndex(SUBCATEGORY_COL_SUB_NAME));
            sub1.sub_icon=c.getString(c.getColumnIndex(SUBCATEGORY_COL_SUB_ICON));
            arrayList.add(sub1);
            Log.i("SUBCATEGORY 1:", sub1.sub_id + sub1.sub_c_id + sub1.sub_name+ sub1.sub_icon);
            c.moveToNext();
        }
        return  arrayList;
    }
}