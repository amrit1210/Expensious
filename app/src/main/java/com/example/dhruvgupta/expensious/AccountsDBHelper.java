package com.example.dhruvgupta.expensious;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Gaurav on 13-Mar-15.
 */
public class AccountsDBHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="Expensious";
    public static final String ACCOUNTS_TABLE="accounts";
    public static final String ACCOUNTS_COL_ACC_ID ="acc_id";
    public static final String ACCOUNTS_COL_ACC_UID ="acc_u_id";
    public static final String ACCOUNTS_COL_ACC_NAME ="acc_name";
    public static final String ACCOUNTS_COL_ACC_BALANCE ="acc_balance";
    public static final String ACCOUNTS_COL_ACC_SHOW ="acc_show";
    public static final String ACCOUNTS_COL_ACC_CURRENCY="acc_currency";
    public static final String ACCOUNTS_COL_ACC_NOTE ="acc_note";

    public AccountsDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String create_table_accounts="CREATE TABLE "+ACCOUNTS_TABLE
                +"("+ ACCOUNTS_COL_ACC_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ACCOUNTS_COL_ACC_UID +" INTEGER,"
                + ACCOUNTS_COL_ACC_NAME +" TEXT,"
                + ACCOUNTS_COL_ACC_BALANCE +" REAL,"
                + ACCOUNTS_COL_ACC_NOTE +" TEXT,"
                + ACCOUNTS_COL_ACC_CURRENCY +" TEXT,"
                + ACCOUNTS_COL_ACC_SHOW +" INTEGER)";
        db.execSQL(create_table_accounts);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public boolean addAccount(String name,float balance,String note,String currency,int show)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(ACCOUNTS_COL_ACC_NAME,name);
        contentValues.put(ACCOUNTS_COL_ACC_BALANCE,balance);
        contentValues.put(ACCOUNTS_COL_ACC_CURRENCY,currency);
        contentValues.put(ACCOUNTS_COL_ACC_NOTE,note);
        contentValues.put(ACCOUNTS_COL_ACC_SHOW,show);

        db.insert(ACCOUNTS_TABLE,null,contentValues);
        return true;
    }
    public boolean updateAccountData(int id, String name, float balance ,String note,String currency,int show )
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(ACCOUNTS_COL_ACC_NAME,name);
        contentValues.put(ACCOUNTS_COL_ACC_BALANCE,balance);
        contentValues.put(ACCOUNTS_COL_ACC_CURRENCY,currency);
        contentValues.put(ACCOUNTS_COL_ACC_NOTE,note);
        contentValues.put(ACCOUNTS_COL_ACC_SHOW,show);
        db.update(ACCOUNTS_TABLE,contentValues,ACCOUNTS_COL_ACC_ID+"="+id,null);
        return true;
    }
    public void deleteAccount(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(ACCOUNTS_TABLE,ACCOUNTS_COL_ACC_ID+"="+id,null);
    }
    public Cursor getAccountData(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c= db.rawQuery("select * from "+ACCOUNTS_TABLE+" where "+ACCOUNTS_COL_ACC_ID+"="+id,null);
        return c;
    }

    public ArrayList<AccountsDB> getAllAccounts()
    {
        ArrayList<AccountsDB> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+ACCOUNTS_TABLE, null);
        c.moveToFirst();
        while(c.isAfterLast()==false)
        {
            AccountsDB a1=new AccountsDB();
            a1.acc_id=c.getInt(c.getColumnIndex(ACCOUNTS_COL_ACC_ID));
            a1.acc_u_id=c.getInt(c.getColumnIndex(ACCOUNTS_COL_ACC_UID));
            a1.acc_name=c.getString(c.getColumnIndex(ACCOUNTS_COL_ACC_NAME));
            a1.acc_balance=c.getFloat(c.getColumnIndex(ACCOUNTS_COL_ACC_BALANCE));
            a1.acc_currency=c.getString(c.getColumnIndex(ACCOUNTS_COL_ACC_CURRENCY));
            a1.acc_note=c.getString(c.getColumnIndex(ACCOUNTS_COL_ACC_NOTE));
            a1.acc_show=c.getInt(c.getColumnIndex(ACCOUNTS_COL_ACC_SHOW));
            arrayList.add(a1);
            Log.i("ACCOUNT 1:", a1.acc_id + a1.acc_u_id + a1.acc_name + a1.acc_balance
                    + a1.acc_currency + a1.acc_note + a1.acc_show);
            c.moveToNext();
        }
        return  arrayList;
    }
}
