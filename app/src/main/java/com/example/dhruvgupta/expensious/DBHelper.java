package com.example.dhruvgupta.expensious;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.method.DateTimeKeyListener;
import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gaurav on 3/11/15.
 */
public class DBHelper extends SQLiteOpenHelper
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

    public static final String ACCOUNTS_TABLE="accounts";
    public static final String ACCOUNTS_COL_ACC_ID ="acc_id";
    public static final String ACCOUNTS_COL_ACC_UID ="acc_u_id";
    public static final String ACCOUNTS_COL_ACC_NAME ="acc_name";
    public static final String ACCOUNTS_COL_ACC_BALANCE ="acc_balance";
    public static final String ACCOUNTS_COL_ACC_SHOW ="acc_show";
    public static final String ACCOUNTS_COL_ACC_CURRENCY="acc_currency";
    public static final String ACCOUNTS_COL_ACC_NOTE ="acc_note";

    public static final String TRANSACTION_TABLE="transactions";
    public static final String TRANSACTION_COL_ID ="trans_id";
    public static final String TRANSACTION_COL_UID ="trans_u_id";
    public static final String TRANSACTION_COL_FROM_ACC ="trans_from_acc_name";
    public static final String TRANSACTION_COL_TO_ACC ="trans_to_acc_balance";
    public static final String TRANSACTION_COL_SHOW ="trans_show";
    public static final String TRANSACTION_COL_DATE="trans_date";
    public static final String TRANSACTION_COL_TIME="trans_time";
    public static final String TRANSACTION_COL_CATEGORY="trans_category";
    public static final String TRANSACTION_COL_SUBCATEGORY="trans_subcategory";
    public static final String TRANSACTION_COL_NOTE ="trans_note";
    public static final String TRANSACTION_COL_TYPE="trans_type";
    public static final String TRANSACTION_COL_PERSON="trans_person";
    public static final String TRANSACTION_COL_BALANCE="trans_balance";

    public static final String PERSON_TABLE ="persons";
    public static final String PERSON_COL_ID ="p_id";
    public static final String PERSON_COL_NAME ="p_name";
    public static final String PERSON_COL_COLOR ="p_color";
    public static final String PERSON_COL_UID ="p_uid";

    public static final String CATEGORY_TABLE="category";
    public static final String CATEGORY_COL_C_UID ="c_u_id";
    public static final String CATEGORY_COL_C_ID ="c_id";
    public static final String CATEGORY_COL_C_NAME ="c_name";
    public static final String CATEGORY_COL_C_TYPE ="c_type";
    public static final String CATEGORY_COL_C_ICON ="c_icon";

    public static final String SUBCATEGORY_TABLE="sub_category";
    public static final String SUBCATEGORY_COL_SUB_ID ="sub_id";
    public static final String SUBCATEGORY_COL_SUB_CID ="sub_c_id";
    public static final String SUBCATEGORY_COL_SUB_NAME ="sub_name";
    public static final String SUBCATEGORY_COL_SUB_ICON ="sub_icon";

    public DBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String create_table_users="CREATE TABLE IF NOT EXISTS "+ USER_TABLE
                +"("+ USER_COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_COL_NAME +" TEXT,"
                + USER_COL_IMAGE +" TEXT,"
                + USER_COL_EMAIL +" TEXT,"
                + USER_COL_PASSWORD +" TEXT,"
                + USER_COL_FID +" INTEGER)";
        db.execSQL(create_table_users);

        String create_table_accounts="CREATE TABLE IF NOT EXISTS "+ ACCOUNTS_TABLE
                +"("+ ACCOUNTS_COL_ACC_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ACCOUNTS_COL_ACC_UID +" INTEGER,"
                + ACCOUNTS_COL_ACC_NAME +" TEXT,"
                + ACCOUNTS_COL_ACC_BALANCE +" REAL,"
                + ACCOUNTS_COL_ACC_NOTE +" TEXT,"
                + ACCOUNTS_COL_ACC_CURRENCY +" TEXT,"
                + ACCOUNTS_COL_ACC_SHOW +" INTEGER)";
        db.execSQL(create_table_accounts);

        String create_table_transactions="CREATE TABLE IF NOT EXISTS "+ TRANSACTION_TABLE
                +"("+ TRANSACTION_COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TRANSACTION_COL_UID+" INTEGER,"
                + TRANSACTION_COL_FROM_ACC +" INTEGER,"
                + TRANSACTION_COL_TO_ACC +" INTEGER,"
                + TRANSACTION_COL_PERSON+" INTEGER,"
                +TRANSACTION_COL_CATEGORY +" INTEGER,"
                +TRANSACTION_COL_SUBCATEGORY +" INTEGER,"
                + TRANSACTION_COL_NOTE +" TEXT,"
                + TRANSACTION_COL_TYPE +" TEXT,"
                + TRANSACTION_COL_DATE +" TEXT,"
                + TRANSACTION_COL_TIME +" TEXT,"
                + TRANSACTION_COL_BALANCE + " REAL,"
                + TRANSACTION_COL_SHOW +" INTEGER)";
        db.execSQL(create_table_transactions);

        String create_table_category="CREATE TABLE IF NOT EXISTS "+ CATEGORY_TABLE
                +"("+ CATEGORY_COL_C_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CATEGORY_COL_C_UID +" INTEGER,"
                + CATEGORY_COL_C_NAME +" TEXT,"
                + CATEGORY_COL_C_TYPE +" TEXT,"
                + CATEGORY_COL_C_ICON +" TEXT)";
        db.execSQL(create_table_category);

        String create_table_subcategory="CREATE TABLE IF NOT EXISTS "+ SUBCATEGORY_TABLE
                +"("+ SUBCATEGORY_COL_SUB_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SUBCATEGORY_COL_SUB_CID +" INTEGER REFERENCES "+ CATEGORY_TABLE + "(" + CATEGORY_COL_C_ID + "),"
                + SUBCATEGORY_COL_SUB_NAME +" TEXT,"
                + SUBCATEGORY_COL_SUB_ICON +" TEXT)";
        db.execSQL(create_table_subcategory);

        String create_table_persons="CREATE TABLE IF NOT EXISTS "+ PERSON_TABLE
                +"("+ PERSON_COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PERSON_COL_UID +" INTEGER,"
                + PERSON_COL_NAME +" TEXT,"
                + PERSON_COL_COLOR +" TEXT)";
        db.execSQL(create_table_persons);
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

        return db.insert(USER_TABLE, null, contentValues) > 0;
    }

    public Cursor getUserData(int id)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select * from "+ USER_TABLE +" where "+ USER_COL_ID +"="+ id, null);
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }

    public int getUserColId(String email)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ USER_TABLE, null);
            String s;
            int user_id = 0;
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                s = c.getString(c.getColumnIndex(USER_COL_EMAIL));
                if (s.equals(email))
                {
                    user_id = c.getInt(c.getColumnIndex(USER_COL_ID));
                    break;
                }
                c.moveToNext();
            }
            c.close();
            return user_id;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return 0;
        }
    }

    public ArrayList getUserColEmail()
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ USER_TABLE, null);
            ArrayList al = new ArrayList();
            String s;
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                s = c.getString(c.getColumnIndex(USER_COL_EMAIL));
                al.add(s);
                c.moveToNext();
            }
            c.close();
            return al;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }
    
    public int deleteUser(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        return db.delete(USER_TABLE, USER_COL_ID +"="+ id, null);
    }

    public ArrayList<SignUpDB> getAllUsers()
    {
        ArrayList<SignUpDB> arrayList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ USER_TABLE, null);
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                SignUpDB s1 = new SignUpDB();
                s1.u_id = c.getInt(c.getColumnIndex(USER_COL_ID));
                s1.u_name = c.getString(c.getColumnIndex(USER_COL_NAME));
                s1.u_email = c.getString(c.getColumnIndex(USER_COL_EMAIL));
                s1.u_password = c.getString(c.getColumnIndex(USER_COL_PASSWORD));
                s1.u_image = c.getString(c.getColumnIndex(USER_COL_IMAGE));
                s1.u_fid = c.getInt(c.getColumnIndex(USER_COL_FID));
                arrayList.add(s1);
                Log.i("USER :", s1.u_id +"\t"+ s1.u_name +"\t"+ s1.u_email
                        +"\t"+ s1.u_password +"\t"+ s1.u_image +"\t"+ s1.u_fid);
                c.moveToNext();
            }
            c.close();
            return arrayList;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }

    public boolean addAccount(int u_id,String name,float balance,String note,String currency,int show)
    {

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(ACCOUNTS_COL_ACC_UID,u_id);
        contentValues.put(ACCOUNTS_COL_ACC_NAME,name);
        contentValues.put(ACCOUNTS_COL_ACC_BALANCE,balance);
        contentValues.put(ACCOUNTS_COL_ACC_CURRENCY,currency);
        contentValues.put(ACCOUNTS_COL_ACC_NOTE,note);
        contentValues.put(ACCOUNTS_COL_ACC_SHOW,show);

        return db.insert(ACCOUNTS_TABLE, null, contentValues) > 0;
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

        return db.update(ACCOUNTS_TABLE, contentValues, ACCOUNTS_COL_ACC_ID +"="+ id, null) > 0;
    }

    public int deleteAccount(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(ACCOUNTS_TABLE, ACCOUNTS_COL_ACC_ID +"="+ id, null);
    }

    public Cursor getAccountData(int id)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select * from "+ ACCOUNTS_TABLE +" where "+ ACCOUNTS_COL_ACC_ID +"="+ id, null);
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }
    public int getAccountColId(int u_id,String acc_name)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ ACCOUNTS_TABLE, null);
            String s;
            int acc_id = 0,user_id;
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                user_id=c.getInt(c.getColumnIndex(ACCOUNTS_COL_ACC_UID));
                if(user_id==u_id)
                {
                    s = c.getString(c.getColumnIndex(ACCOUNTS_COL_ACC_NAME));
                    if (s.equals(acc_name)) {
                        acc_id = c.getInt(c.getColumnIndex(ACCOUNTS_COL_ACC_ID));
                        break;
                    }
                }
                c.moveToNext();
            }
            c.close();
            return acc_id;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return 0;
        }
    }
    public ArrayList<AccountsDB> getAllAccounts(int u_id)
    {
        ArrayList<AccountsDB> arrayList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ ACCOUNTS_TABLE +" where "+ ACCOUNTS_COL_ACC_UID +"="+ u_id, null);
            c.moveToFirst();
            while(!c.isAfterLast())
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
                Log.i("ACCOUNT :", a1.acc_id +"\t"+ a1.acc_u_id +"\t"+ a1.acc_name +"\t"+ a1.acc_balance
                        +"\t"+ a1.acc_currency +"\t"+ a1.acc_note +"\t"+ a1.acc_show);
                c.moveToNext();
            }
            c.close();
            return  arrayList;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }

    public boolean addTransaction(int u_id,int from_acc,int to_acc,float balance,String note,int p_id,int cat_id,int sub_id,
                                  int show,String type,String date,String time)
    {

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(TRANSACTION_COL_UID,u_id);
        contentValues.put(TRANSACTION_COL_FROM_ACC,from_acc);
        contentValues.put(TRANSACTION_COL_TO_ACC,to_acc);
        contentValues.put(TRANSACTION_COL_BALANCE,balance);
        contentValues.put(TRANSACTION_COL_PERSON,p_id);
        contentValues.put(TRANSACTION_COL_NOTE,note);
        contentValues.put(TRANSACTION_COL_SHOW,show);
        contentValues.put(TRANSACTION_COL_TYPE,type);
        contentValues.put(TRANSACTION_COL_CATEGORY,cat_id);
        contentValues.put(TRANSACTION_COL_SUBCATEGORY,sub_id);
        contentValues.put(TRANSACTION_COL_DATE,date);
        contentValues.put(TRANSACTION_COL_TIME,time);

        return db.insert(TRANSACTION_TABLE, null, contentValues) > 0;
    }

    public boolean updateTransactionData(int id, int from_acc,int to_acc,int p_id,int cat_id, int sub_id, float balance ,String note,int show,String type,String date,String time )
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(TRANSACTION_COL_FROM_ACC,from_acc);
        contentValues.put(TRANSACTION_COL_BALANCE,balance);
        contentValues.put(TRANSACTION_COL_TO_ACC,to_acc);
        contentValues.put(TRANSACTION_COL_TYPE,type);
        contentValues.put(TRANSACTION_COL_PERSON,p_id);
        contentValues.put(TRANSACTION_COL_CATEGORY,cat_id);
        contentValues.put(TRANSACTION_COL_SUBCATEGORY,sub_id);
        contentValues.put(TRANSACTION_COL_DATE,date);
        contentValues.put(TRANSACTION_COL_TIME,time);
        contentValues.put(TRANSACTION_COL_NOTE,note);
        contentValues.put(TRANSACTION_COL_SHOW,show);
        contentValues.put(TRANSACTION_COL_TYPE,type);

        return db.update(TRANSACTION_TABLE, contentValues, TRANSACTION_COL_ID +"="+ id, null) > 0;
    }

    public int deleteTransaction(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TRANSACTION_TABLE, TRANSACTION_COL_ID +"="+ id, null);
    }

    public Cursor getTransactionData(int id)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select * from "+ TRANSACTION_TABLE +" where "+ TRANSACTION_COL_ID +"="+ id, null);
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }

    public ArrayList<TransactionsDB> getAllTransactions(int u_id)
    {
        ArrayList<TransactionsDB> arrayList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Log.d("DB",""+db.isOpen());
            Cursor c = db.rawQuery("select * from "+ TRANSACTION_TABLE +" where "+ TRANSACTION_COL_UID +"="+ u_id, null);
            c.moveToFirst();
            while(!c.isAfterLast())
            {
                TransactionsDB t1=new TransactionsDB();
                t1.t_id=c.getInt(c.getColumnIndex(TRANSACTION_COL_ID));
                t1.t_u_id=c.getInt(c.getColumnIndex(TRANSACTION_COL_UID));
                t1.t_from_acc=c.getInt(c.getColumnIndex(TRANSACTION_COL_FROM_ACC));
                t1.t_to_acc=c.getInt(c.getColumnIndex(TRANSACTION_COL_TO_ACC));
                t1.t_balance=c.getFloat(c.getColumnIndex(TRANSACTION_COL_BALANCE));
                t1.t_p_id=c.getInt(c.getColumnIndex(TRANSACTION_COL_PERSON));
                t1.t_note=c.getString(c.getColumnIndex(TRANSACTION_COL_NOTE));
                t1.t_show=c.getInt(c.getColumnIndex(TRANSACTION_COL_SHOW));
                t1.t_date=c.getString(c.getColumnIndex(TRANSACTION_COL_DATE));
                t1.t_time=c.getString(c.getColumnIndex(TRANSACTION_COL_TIME));
                t1.t_type=c.getString(c.getColumnIndex(TRANSACTION_COL_TYPE));
                t1.t_c_id=c.getInt(c.getColumnIndex(TRANSACTION_COL_CATEGORY));
                t1.t_sub_id=c.getInt(c.getColumnIndex(TRANSACTION_COL_SUBCATEGORY));
                arrayList.add(t1);
                Log.i("Transaction :", t1.t_id +"\t"+ t1.t_u_id +"\t"+t1.t_from_acc +"\t"+t1.t_to_acc+"\t"+ t1.t_balance+"\t"+ t1.t_type
                        +"\t"+ t1.t_c_id+"\t"+ t1.t_sub_id+"\t"+ t1.t_p_id+"\t"+ t1.t_date+"\t"+ t1.t_time +"\t"+ t1.t_note +"\t"+ t1.t_show);
                c.moveToNext();
            }
            c.close();
            return  arrayList;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }
    public boolean addPerson(String name, String color,int u_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(PERSON_COL_NAME,name);
        contentValues.put(PERSON_COL_COLOR,color);
        contentValues.put(PERSON_COL_UID, u_id);

        return db.insert(PERSON_TABLE, null, contentValues) > 0;
    }

    public boolean updatePersonData(int id, String name,String color)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(PERSON_COL_NAME,name);
        contentValues.put(PERSON_COL_COLOR,color);

        return db.update(PERSON_TABLE, contentValues, PERSON_COL_ID +"="+ id, null) > 0;
    }

    public int deletePerson(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        return db.delete(PERSON_TABLE, PERSON_COL_ID +"="+ id, null);
    }

    public Cursor getPersonData(int id)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select *from "+ PERSON_TABLE +" where "+ PERSON_COL_ID +"="+ id, null);
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }
    public int getPersonColId(int u_id,String person_name)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ PERSON_TABLE, null);
            String s;
            int p_id = 0,user_id;
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                user_id=c.getInt(c.getColumnIndex(PERSON_COL_UID));
                if(user_id==u_id)
                {
                    s = c.getString(c.getColumnIndex(PERSON_COL_NAME));
                    if (s.equals(person_name)) {
                        p_id = c.getInt(c.getColumnIndex(PERSON_COL_ID));
                        break;
                    }
                }
                c.moveToNext();
            }
            c.close();
            return p_id;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return 0;
        }
    }

    public ArrayList<PersonDB> getAllPersons(int u_id)
    {
        ArrayList<PersonDB> arrayList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ PERSON_TABLE +" where "+ PERSON_COL_UID +"="+ u_id, null);
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                PersonDB p1 = new PersonDB();
                p1.p_id = c.getInt(c.getColumnIndex(PERSON_COL_ID));
                p1.p_name = c.getString(c.getColumnIndex(PERSON_COL_NAME));
                p1.p_color = c.getString(c.getColumnIndex(PERSON_COL_COLOR));
                p1.p_u_id = c.getInt(c.getColumnIndex(PERSON_COL_UID));
                arrayList.add(p1);
                Log.i("PERSON :", p1.p_id +"\t"+ p1.p_name +"\t"+ p1.p_color +"\t"+ p1.p_u_id);
                c.moveToNext();
            }
            c.close();
            return arrayList;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }

    public boolean addCategory(int u_id,String name,String type,String icon)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(CATEGORY_COL_C_UID,u_id);
        contentValues.put(CATEGORY_COL_C_NAME,name);
        contentValues.put(CATEGORY_COL_C_TYPE,type);
        contentValues.put(CATEGORY_COL_C_ICON,icon);

        Log.i("Add Category",contentValues+"");

        return db.insert(CATEGORY_TABLE, null, contentValues) > 0;
    }

    public boolean updateCategory(int id,String name,String type,String icon)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(CATEGORY_COL_C_NAME,name);
        contentValues.put(CATEGORY_COL_C_TYPE,type);
        contentValues.put(CATEGORY_COL_C_ICON,icon);

        return db.update(CATEGORY_TABLE, contentValues, CATEGORY_COL_C_ID +"="+ id, null) > 0;
    }

    public int deleteCategory(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(CATEGORY_TABLE, CATEGORY_COL_C_ID +"="+ id, null);
    }

    public Cursor getCategoryData(int id)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select * from "+ CATEGORY_TABLE +" where "+ CATEGORY_COL_C_ID +"="+ id, null);
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }

    public int getCategoryColId(String c_name)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ CATEGORY_TABLE, null);
            String s;
            int cat_id = 0;
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                s = c.getString(c.getColumnIndex(CATEGORY_COL_C_NAME));
                if (s.equals(c_name))
                {
                    cat_id = c.getInt(c.getColumnIndex(CATEGORY_COL_C_ID));
                    break;
                }
                c.moveToNext();
            }
            c.close();
            return cat_id;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return 0;
        }
    }

    public ArrayList<CategoryDB> getAllCategories(int u_id,String type)
    {
        ArrayList<CategoryDB> arrayList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ CATEGORY_TABLE +" where "+ CATEGORY_COL_C_TYPE + "=" + type
                    + " and " + CATEGORY_COL_C_UID + "=" + u_id, null);
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                CategoryDB c1 = new CategoryDB();
                c1.c_id = c.getInt(c.getColumnIndex(CATEGORY_COL_C_ID));
                c1.c_u_id = c.getInt(c.getColumnIndex(CATEGORY_COL_C_UID));
                c1.c_name = c.getString(c.getColumnIndex(CATEGORY_COL_C_NAME));
                c1.c_type = c.getString(c.getColumnIndex(CATEGORY_COL_C_TYPE));
                c1.c_icon = c.getString(c.getColumnIndex(CATEGORY_COL_C_ICON));
                arrayList.add(c1);
                Log.i("CATEGORY :", c1.c_u_id +"\t"+ c1.c_id +"\t"+ c1.c_type +"\t"+ c1.c_name +"\t"+ c1.c_icon);
                c.moveToNext();
            }
            c.close();
            return arrayList;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }

    public boolean addSubCategory(int c_id,String name,String icon)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(SUBCATEGORY_COL_SUB_CID,c_id);
        contentValues.put(SUBCATEGORY_COL_SUB_NAME,name);
        contentValues.put(SUBCATEGORY_COL_SUB_ICON,icon);

        return db.insert(SUBCATEGORY_TABLE, null, contentValues) > 0;
    }

    public boolean updateSubCategory(int id,String name,String icon)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(SUBCATEGORY_COL_SUB_NAME,name);
        contentValues.put(SUBCATEGORY_COL_SUB_ICON,icon);

        return db.update(SUBCATEGORY_TABLE, contentValues, SUBCATEGORY_COL_SUB_ID +"="+ id, null) > 0;
    }

    public ArrayList<SubCategoryDB> getAllSubCategories()
    {
        ArrayList<SubCategoryDB> arrayList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ SUBCATEGORY_TABLE, null);
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                SubCategoryDB sub1 = new SubCategoryDB();
                sub1.sub_id = c.getInt(c.getColumnIndex(SUBCATEGORY_COL_SUB_ID));
                sub1.sub_c_id = c.getInt(c.getColumnIndex(SUBCATEGORY_COL_SUB_CID));
                sub1.sub_name = c.getString(c.getColumnIndex(SUBCATEGORY_COL_SUB_NAME));
                sub1.sub_icon = c.getString(c.getColumnIndex(SUBCATEGORY_COL_SUB_ICON));
                arrayList.add(sub1);
                Log.i("SUBCATEGORY :", sub1.sub_id +"\t"+ sub1.sub_c_id +"\t"+ sub1.sub_name +"\t"+ sub1.sub_icon);
                c.moveToNext();
            }
            c.close();
            return arrayList;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }
}