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
public class DBHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="Expensious";

    public static final String USER_TABLE="User";
    public static final String USER_COL_ID="uid";
    public static final String USER_COL_IMAGE="userimage";
    public static final String USER_COL_NAME="username";
    public static final String USER_COL_EMAIL="email";
    public static final String USER_COL_PASSWORD="password";
    public static final String USER_COL_FID="fid";

    public static final String ACCOUNTS_TABLE="Accounts";
    public static final String ACCOUNTS_COL_ACC_ID ="acc_id";
    public static final String ACCOUNTS_COL_ACC_UID ="acc_uid";
    public static final String ACCOUNTS_COL_ACC_NAME ="acc_name";
    public static final String ACCOUNTS_COL_ACC_BALANCE ="acc_balance";
    public static final String ACCOUNTS_COL_ACC_SHOW ="acc_show";
    public static final String ACCOUNTS_COL_ACC_NOTE ="acc_note";

    public static final String BUDGETS_TABLE="Budget";
    public static final String BUDGETS_COL_B_ID ="b_id";
    public static final String BUDGETS_COL_B_UID ="b_uid";
    public static final String BUDGETS_COL_B_AMOUNT ="b_amount";
    public static final String BUDGETS_COL_START_DATE="b_startDate";
    public static final String BUDGETS_COL_END_DATE="b_endDate";

    public static final String TRANSACTION_TABLE="Transactions";
    public static final String TRANSACTION_COL_ID ="trans_id";
    public static final String TRANSACTION_COL_UID ="trans_uid";
    public static final String TRANSACTION_COL_RECID ="trans_rec_id";
    public static final String TRANSACTION_COL_FROM_ACC ="trans_from_acc";
    public static final String TRANSACTION_COL_TO_ACC ="trans_to_acc";
    public static final String TRANSACTION_COL_SHOW ="trans_show";
    public static final String TRANSACTION_COL_DATE="trans_date";
    public static final String TRANSACTION_COL_TIME="trans_time";
    public static final String TRANSACTION_COL_CATEGORY="trans_category";
    public static final String TRANSACTION_COL_SUBCATEGORY="trans_subcategory";
    public static final String TRANSACTION_COL_NOTE ="trans_note";
    public static final String TRANSACTION_COL_TYPE="trans_type";
    public static final String TRANSACTION_COL_PERSON="trans_person";
    public static final String TRANSACTION_COL_BALANCE="trans_balance";

    public static final String RECURSIVE_TABLE="Recursive";
    public static final String RECURSIVE_COL_ID ="rec_id";
    public static final String RECURSIVE_COL_UID ="rec_uid";
    public static final String RECURSIVE_COL_FROM_ACC ="rec_from_acc";
    public static final String RECURSIVE_COL_TO_ACC ="rec_to_acc";
    public static final String RECURSIVE_COL_SHOW ="rec_show";
    public static final String RECURSIVE_COL_START_DATE="rec_start_date";
    public static final String RECURSIVE_COL_END_DATE="rec_end_date";
    public static final String RECURSIVE_COL_NEXT_DATE="rec_next_date";
    public static final String RECURSIVE_COL_TIME="rec_time";
    public static final String RECURSIVE_COL_RECURRING="rec_recurring";
    public static final String RECURSIVE_COL_ALERT="rec_alert";
    public static final String RECURSIVE_COL_CATEGORY="rec_category";
    public static final String RECURSIVE_COL_SUBCATEGORY="rec_subcategory";
    public static final String RECURSIVE_COL_NOTE ="rec_note";
    public static final String RECURSIVE_COL_TYPE="rec_type";
    public static final String RECURSIVE_COL_PERSON="rec_person";
    public static final String RECURSIVE_COL_BALANCE="rec_balance";

    public static final String PERSON_TABLE ="Persons";
    public static final String PERSON_COL_ID ="p_id";
    public static final String PERSON_COL_NAME ="p_name";
    public static final String PERSON_COL_COLOR ="p_color";
    public static final String PERSON_COL_COLOR_CODE ="p_color_code";
    public static final String PERSON_COL_UID ="p_uid";

    public static final String CATEGORY_MASTER="Category_master";
    public static final String CATEGORY_SPECIFIC ="Category_specific";
    public static final String CATEGORY_COL_C_UID ="c_uid";
    public static final String CATEGORY_COL_C_ID ="c_id";
    public static final String CATEGORY_COL_C_NAME ="c_name";
    public static final String CATEGORY_COL_C_TYPE ="c_type";
    public static final String CATEGORY_COL_C_ICON ="c_icon";

    public static final String SUBCATEGORY_TABLE="Sub_category";
    public static final String SUBCATEGORY_COL_SUB_ID ="sub_id";
    public static final String SUBCATEGORY_COL_SUB_CID ="sub_c_id";
    public static final String SUBCATEGORY_COL_SUB_NAME ="sub_name";
    public static final String SUBCATEGORY_COL_SUB_ICON ="sub_icon";
    public static final String SUBCATEGORY_COL_SUB_UID="sub_uid";

    public static final String LOAN_DEBT_TABLE="Loan_debt";
    public static final String LOAN_DEBT_COL_UID="loan_debt_uid";
    public static final String LOAN_DEBT_COL_ID="loan_debt_id";
    public static final String LOAN_DEBT_COL_FROM_ACC="loan_debt_from_acc";
    public static final String LOAN_DEBT_COL_TO_ACC="loan_debt_to_acc";
    public static final String LOAN_DEBT_COL_PERSON="loan_debt_person";
    public static final String LOAN_DEBT_COL_NOTE="loan_debt_note";
    public static final String LOAN_DEBT_COL_TYPE="loan_debt_type";
    public static final String LOAN_DEBT_COL_BALANCE="loan_debt_balance";
    public static final String LOAN_DEBT_COL_DATE="loan_debt_date";
    public static final String LOAN_DEBT_COL_TIME="loan_debt_time";
    public static final String LOAN_DEBT_COL_PARENT="loan_debt_parent";

    public static final String SETTINGS_TABLE="Settings";
    public static final String SETTINGS_COL_UID="settings_uid";
    public static final String SETTINGS_COL_CUR_CODE="settings_cur_code";

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
                + ACCOUNTS_COL_ACC_SHOW +" INTEGER)";
        db.execSQL(create_table_accounts);

        String create_table_budgets="CREATE TABLE IF NOT EXISTS "+ BUDGETS_TABLE
                +"("+ BUDGETS_COL_B_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BUDGETS_COL_B_UID +" INTEGER,"
                + BUDGETS_COL_B_AMOUNT +" REAL,"
                + BUDGETS_COL_START_DATE +" TEXT,"
                + BUDGETS_COL_END_DATE +" TEXT)";
        db.execSQL(create_table_budgets);

        String create_table_transactions="CREATE TABLE IF NOT EXISTS "+ TRANSACTION_TABLE
                +"("+ TRANSACTION_COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TRANSACTION_COL_UID +" INTEGER,"
                + TRANSACTION_COL_RECID +" INTEGER,"
                + TRANSACTION_COL_FROM_ACC +" INTEGER,"
                + TRANSACTION_COL_TO_ACC +" INTEGER,"
                + TRANSACTION_COL_PERSON +" INTEGER,"
                + TRANSACTION_COL_CATEGORY +" INTEGER,"
                + TRANSACTION_COL_SUBCATEGORY +" INTEGER,"
                + TRANSACTION_COL_NOTE +" TEXT,"
                + TRANSACTION_COL_TYPE +" TEXT,"
                + TRANSACTION_COL_DATE +" TEXT,"
                + TRANSACTION_COL_TIME +" TEXT,"
                + TRANSACTION_COL_BALANCE + " REAL,"
                + TRANSACTION_COL_SHOW +" INTEGER)";
        db.execSQL(create_table_transactions);

        String create_table_recursive="CREATE TABLE IF NOT EXISTS "+ RECURSIVE_TABLE
                +"("+ RECURSIVE_COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RECURSIVE_COL_UID +" INTEGER,"
                + RECURSIVE_COL_FROM_ACC +" INTEGER,"
                + RECURSIVE_COL_TO_ACC +" INTEGER,"
                + RECURSIVE_COL_PERSON+" INTEGER,"
                + RECURSIVE_COL_CATEGORY +" INTEGER,"
                + RECURSIVE_COL_SUBCATEGORY +" INTEGER,"
                + RECURSIVE_COL_NOTE +" TEXT,"
                + RECURSIVE_COL_TYPE +" TEXT,"
                + RECURSIVE_COL_START_DATE +" TEXT,"
                + RECURSIVE_COL_END_DATE +" TEXT,"
                + RECURSIVE_COL_NEXT_DATE +" TEXT,"
                + RECURSIVE_COL_TIME +" TEXT,"
                + RECURSIVE_COL_BALANCE + " REAL,"
                + RECURSIVE_COL_RECURRING + " INTEGER,"
                + RECURSIVE_COL_ALERT + " INTEGER,"
                + RECURSIVE_COL_SHOW +" INTEGER)";
        db.execSQL(create_table_recursive);

        String create_table_category_master="CREATE TABLE IF NOT EXISTS "+ CATEGORY_MASTER
                +"("+ CATEGORY_COL_C_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CATEGORY_COL_C_NAME +" TEXT,"
                + CATEGORY_COL_C_TYPE +" TEXT,"
                + CATEGORY_COL_C_ICON +" TEXT)";
        db.execSQL(create_table_category_master);

        String create_table_category_specific="CREATE TABLE IF NOT EXISTS "+ CATEGORY_SPECIFIC
                +"("+ CATEGORY_COL_C_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CATEGORY_COL_C_UID +" INTEGER,"
                + CATEGORY_COL_C_NAME +" TEXT,"
                + CATEGORY_COL_C_TYPE +" TEXT,"
                + CATEGORY_COL_C_ICON +" TEXT)";
        db.execSQL(create_table_category_specific);

        String create_table_subcategory="CREATE TABLE IF NOT EXISTS "+ SUBCATEGORY_TABLE
                +"("+ SUBCATEGORY_COL_SUB_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SUBCATEGORY_COL_SUB_CID +" INTEGER REFERENCES "+ CATEGORY_SPECIFIC + "(" + CATEGORY_COL_C_ID + "),"
                + SUBCATEGORY_COL_SUB_NAME +" TEXT,"
                + SUBCATEGORY_COL_SUB_UID + " INTEGER,"
                + SUBCATEGORY_COL_SUB_ICON + " TEXT )";
        db.execSQL(create_table_subcategory);

        String create_table_persons="CREATE TABLE IF NOT EXISTS "+ PERSON_TABLE
                +"("+ PERSON_COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PERSON_COL_UID +" INTEGER,"
                + PERSON_COL_NAME +" TEXT,"
                + PERSON_COL_COLOR +" TEXT,"
                + PERSON_COL_COLOR_CODE +" TEXT)";
        db.execSQL(create_table_persons);

        String create_table_loan_debt="CREATE TABLE IF NOT EXISTS "+ LOAN_DEBT_TABLE
                +"(" + LOAN_DEBT_COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + LOAN_DEBT_COL_UID +" INTEGER,"
                + LOAN_DEBT_COL_BALANCE +" REAL,"
                + LOAN_DEBT_COL_DATE +" TEXT,"
                + LOAN_DEBT_COL_TIME +" TEXT,"
                + LOAN_DEBT_COL_FROM_ACC +" INTEGER,"
                + LOAN_DEBT_COL_TO_ACC +" INTEGER,"
                + LOAN_DEBT_COL_PERSON +" INTEGER,"
                + LOAN_DEBT_COL_NOTE +" TEXT,"
                + LOAN_DEBT_COL_TYPE+" TEXT,"
                + LOAN_DEBT_COL_PARENT+" INTEGER )";
        db.execSQL(create_table_loan_debt);

        String create_table_settings="CREATE TABLE IF NOT EXISTS "+ SETTINGS_TABLE
                +"("+ SETTINGS_COL_UID+" INTEGER PRIMARY KEY,"
                + SETTINGS_COL_CUR_CODE +" TEXT)";
        db.execSQL(create_table_settings);
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

    public boolean addAccount(int u_id,String name,float balance,String note,int show)
    {

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(ACCOUNTS_COL_ACC_UID,u_id);
        contentValues.put(ACCOUNTS_COL_ACC_NAME,name);
        contentValues.put(ACCOUNTS_COL_ACC_BALANCE,balance);
        contentValues.put(ACCOUNTS_COL_ACC_NOTE,note);
        contentValues.put(ACCOUNTS_COL_ACC_SHOW,show);

        return db.insert(ACCOUNTS_TABLE, null, contentValues) > 0;
    }

    public boolean updateAccountData(int acc_id, String name, float balance ,String note,int show,int u_id )
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(ACCOUNTS_COL_ACC_NAME,name);
        contentValues.put(ACCOUNTS_COL_ACC_BALANCE,balance);
        contentValues.put(ACCOUNTS_COL_ACC_NOTE,note);
        contentValues.put(ACCOUNTS_COL_ACC_SHOW,show);

        return db.update(ACCOUNTS_TABLE, contentValues, ACCOUNTS_COL_ACC_ID +"="+ acc_id +" and "
                + ACCOUNTS_COL_ACC_UID +"="+ u_id, null) > 0;
    }

    public int deleteAccount(int id,int u_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(ACCOUNTS_TABLE, ACCOUNTS_COL_ACC_ID +"="+ id +" and "+ ACCOUNTS_COL_ACC_UID +"="+ u_id, null);
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
                a1.acc_note=c.getString(c.getColumnIndex(ACCOUNTS_COL_ACC_NOTE));
                a1.acc_show=c.getInt(c.getColumnIndex(ACCOUNTS_COL_ACC_SHOW));
                arrayList.add(a1);
//                Log.i("ACCOUNT :", a1.acc_id +"\t"+ a1.acc_u_id +"\t"+ a1.acc_name +"\t"+ a1.acc_balance
//                        +"\t"+ a1.acc_note +"\t"+ a1.acc_show);
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

    public boolean addBudget(int u_id,float amount,String s_date,String e_date)
    {

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(BUDGETS_COL_B_UID,u_id);
        contentValues.put(BUDGETS_COL_B_AMOUNT,amount);
        contentValues.put(BUDGETS_COL_START_DATE,s_date);
        contentValues.put(BUDGETS_COL_END_DATE,e_date);

        return db.insert(BUDGETS_TABLE, null, contentValues) > 0;
    }

    public boolean updateBudgetData(int u_id,int b_id,float amount,String s_date,String e_date)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(BUDGETS_COL_B_AMOUNT,amount);
        contentValues.put(BUDGETS_COL_START_DATE,s_date);
        contentValues.put(BUDGETS_COL_END_DATE,e_date);

        return db.update(BUDGETS_TABLE, contentValues, BUDGETS_COL_B_ID +"="+ b_id +" and "
                + BUDGETS_COL_B_UID +"="+ u_id, null) > 0;
    }

    public int deleteBudget(int b_id,int u_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(BUDGETS_TABLE, BUDGETS_COL_B_ID +"="+ b_id +" and "+ BUDGETS_COL_B_UID +"="+ u_id, null);
    }

    public Cursor getBudgetData(int b_id)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select * from "+ BUDGETS_TABLE +" where "+ BUDGETS_COL_B_ID +"="+ b_id, null);
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }

    public int getBudgetColId(int u_id,String start, String end)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ BUDGETS_TABLE, null);
            String s, e;
            int b_id = 0,user_id;
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                user_id=c.getInt(c.getColumnIndex(BUDGETS_COL_B_UID));
                if(user_id==u_id)
                {
                    s = c.getString(c.getColumnIndex(BUDGETS_COL_START_DATE));
                    e = c.getString(c.getColumnIndex(BUDGETS_COL_END_DATE));

                    if (s.equals(start) && e.equals(end)) {
                        b_id = c.getInt(c.getColumnIndex(BUDGETS_COL_B_ID));
                        break;
                    }
                }
                c.moveToNext();
            }
            c.close();
            return b_id;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return 0;
        }
    }

    public ArrayList<BudgetDB> getAllBudgets(int u_id)
    {
        ArrayList<BudgetDB> arrayList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ BUDGETS_TABLE +" where "+ BUDGETS_COL_B_UID +"="+ u_id, null);
            c.moveToFirst();
            while(!c.isAfterLast())
            {
                BudgetDB b1=new BudgetDB();
                b1.b_id=c.getInt(c.getColumnIndex(BUDGETS_COL_B_ID));
                b1.b_u_id=c.getInt(c.getColumnIndex(BUDGETS_COL_B_UID));
                b1.b_amount=c.getFloat(c.getColumnIndex(BUDGETS_COL_B_AMOUNT));
                b1.b_startDate=c.getString(c.getColumnIndex(BUDGETS_COL_START_DATE));
                b1.b_endDate=c.getString(c.getColumnIndex(BUDGETS_COL_END_DATE));
                arrayList.add(b1);
                Log.i("BUDGET :", b1.b_id +"\t"+ b1.b_u_id +"\t"+ b1.b_amount +"\t"+ b1.b_startDate +"\t"+ b1.b_endDate);
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
                                  int show,String type,String date,String time, int rec_id)
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
        contentValues.put(TRANSACTION_COL_RECID,rec_id);

        return db.insert(TRANSACTION_TABLE, null, contentValues) > 0;
    }

    public boolean updateTransactionData(int id, int from_acc,int to_acc,int p_id,int cat_id, int sub_id, float balance ,
                                         String note,int show,String type,String date,String time,int u_id, int rec_id)
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
        contentValues.put(TRANSACTION_COL_RECID,rec_id);

        return db.update(TRANSACTION_TABLE, contentValues, TRANSACTION_COL_ID +"="+ id +" and "
                + TRANSACTION_COL_UID + "=" + u_id, null) > 0;
    }

    public int deleteTransaction(int id,int u_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TRANSACTION_TABLE, TRANSACTION_COL_ID +"="+ id +" and "+ TRANSACTION_COL_UID +"="+ u_id, null);
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

    public int getTransactionsColId(int u_id)
    {
        int tid = 0;
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor c = db.rawQuery("select * from "+ TRANSACTION_TABLE +" where "+ TRANSACTION_COL_UID +"="+ u_id, null);
            c.moveToFirst();
            while(!c.isAfterLast())
            {
                tid=c.getInt(c.getColumnIndex(TRANSACTION_COL_ID));
                c.moveToNext();
            }
            c.close();
            return  tid;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return 0;
        }
    }

    public ArrayList<TransactionsDB> getAllTransactions(int u_id)
    {
        ArrayList<TransactionsDB> arrayList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
//            Log.d("DB",""+db.isOpen());
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
                t1.t_rec_id=c.getInt(c.getColumnIndex(TRANSACTION_COL_RECID));
                arrayList.add(t1);
//                Log.i("Transaction :", t1.t_id +"\t"+ t1.t_u_id +"\t"+t1.t_from_acc +"\t"+t1.t_to_acc+"\t"+ t1.t_balance+"\t"+ t1.t_type
//                        +"\t"+ t1.t_c_id+"\t"+ t1.t_sub_id+"\t"+ t1.t_p_id+"\t"+ t1.t_date+"\t"+ t1.t_time +"\t"+ t1.t_note +"\t"+
//                        t1.t_show + "\t" + t1.t_rec_id);
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

    public ArrayList<TransactionsDB> getAllRecTrans(int u_id, int rec_id)
    {
        ArrayList<TransactionsDB> arrayList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
//            Log.d("DB",""+db.isOpen());
            Cursor c = db.rawQuery("select * from "+ TRANSACTION_TABLE +" where "+ TRANSACTION_COL_UID +"="+ u_id + " and " +
                    TRANSACTION_COL_RECID + "=" + rec_id, null);
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
                t1.t_rec_id=c.getInt(c.getColumnIndex(TRANSACTION_COL_RECID));
                arrayList.add(t1);
//                Log.i("Transaction :", t1.t_id +"\t"+ t1.t_u_id +"\t"+t1.t_from_acc +"\t"+t1.t_to_acc+"\t"+ t1.t_balance+"\t"+ t1.t_type
//                        +"\t"+ t1.t_c_id+"\t"+ t1.t_sub_id+"\t"+ t1.t_p_id+"\t"+ t1.t_date+"\t"+ t1.t_time +"\t"+ t1.t_note +"\t"+
//                        t1.t_show + "\t" + t1.t_rec_id);
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

    public boolean addRecursive(int u_id,int from_acc,int to_acc,float balance,String note,int p_id,int cat_id,int sub_id,
                                int show,String type,String s_date,String e_date, String n_date, String time, int recurring, int alert)
    {

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(RECURSIVE_COL_UID,u_id);
        contentValues.put(RECURSIVE_COL_FROM_ACC,from_acc);
        contentValues.put(RECURSIVE_COL_TO_ACC,to_acc);
        contentValues.put(RECURSIVE_COL_BALANCE,balance);
        contentValues.put(RECURSIVE_COL_PERSON,p_id);
        contentValues.put(RECURSIVE_COL_NOTE,note);
        contentValues.put(RECURSIVE_COL_SHOW,show);
        contentValues.put(RECURSIVE_COL_TYPE,type);
        contentValues.put(RECURSIVE_COL_CATEGORY,cat_id);
        contentValues.put(RECURSIVE_COL_SUBCATEGORY,sub_id);
        contentValues.put(RECURSIVE_COL_START_DATE,s_date);
        contentValues.put(RECURSIVE_COL_END_DATE,e_date);
        contentValues.put(RECURSIVE_COL_NEXT_DATE,n_date);
        contentValues.put(RECURSIVE_COL_TIME,time);
        contentValues.put(RECURSIVE_COL_RECURRING,recurring);
        contentValues.put(RECURSIVE_COL_ALERT,alert);

        return db.insert(RECURSIVE_TABLE, null, contentValues) > 0;
    }

    public boolean updateRecursiveData(int id, int from_acc,int to_acc,int p_id,int cat_id, int sub_id, float balance ,
                                       String note,int show,String type,String s_date,String e_date, String n_date, String time,
                                       int recurring, int alert,int u_id )
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(RECURSIVE_COL_FROM_ACC,from_acc);
        contentValues.put(RECURSIVE_COL_TO_ACC,to_acc);
        contentValues.put(RECURSIVE_COL_BALANCE,balance);
        contentValues.put(RECURSIVE_COL_PERSON,p_id);
        contentValues.put(RECURSIVE_COL_NOTE,note);
        contentValues.put(RECURSIVE_COL_SHOW,show);
        contentValues.put(RECURSIVE_COL_TYPE,type);
        contentValues.put(RECURSIVE_COL_CATEGORY,cat_id);
        contentValues.put(RECURSIVE_COL_SUBCATEGORY,sub_id);
        contentValues.put(RECURSIVE_COL_START_DATE,s_date);
        contentValues.put(RECURSIVE_COL_END_DATE,e_date);
        contentValues.put(RECURSIVE_COL_NEXT_DATE,n_date);
        contentValues.put(RECURSIVE_COL_TIME,time);
        contentValues.put(RECURSIVE_COL_RECURRING,recurring);
        contentValues.put(RECURSIVE_COL_ALERT,alert);

        return db.update(RECURSIVE_TABLE, contentValues, RECURSIVE_COL_ID +"="+ id +" and "
                + RECURSIVE_COL_UID + "=" + u_id, null) > 0;
    }

    public int deleteRecursive(int id,int u_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(RECURSIVE_TABLE, RECURSIVE_COL_ID +"="+ id +" and "+ RECURSIVE_COL_UID +"="+ u_id, null);
    }

    public Cursor getRecursiveData(int id)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select * from "+ RECURSIVE_TABLE +" where "+ RECURSIVE_COL_ID +"="+ id, null);
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }

    public int getRecursiveColId(int u_id)
    {
        int recid = 0;
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor c = db.rawQuery("select * from "+ RECURSIVE_TABLE +" where "+ RECURSIVE_COL_UID +"="+ u_id, null);
            c.moveToFirst();
            while(!c.isAfterLast())
            {
                recid=c.getInt(c.getColumnIndex(RECURSIVE_COL_ID));
                c.moveToNext();
            }
            c.close();
            return  recid;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return 0;
        }
    }

    public ArrayList<RecursiveDB> getAllRecursive(int u_id)
    {
        ArrayList<RecursiveDB> arrayList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
//            Log.d("DB",""+db.isOpen());
            Cursor c = db.rawQuery("select * from "+ RECURSIVE_TABLE +" where "+ RECURSIVE_COL_UID +"="+ u_id, null);
            c.moveToFirst();
            while(!c.isAfterLast())
            {
                RecursiveDB rec1=new RecursiveDB();
                rec1.rec_id=c.getInt(c.getColumnIndex(RECURSIVE_COL_ID));
                rec1.rec_u_id=c.getInt(c.getColumnIndex(RECURSIVE_COL_UID));
                rec1.rec_from_acc=c.getInt(c.getColumnIndex(RECURSIVE_COL_FROM_ACC));
                rec1.rec_to_acc=c.getInt(c.getColumnIndex(RECURSIVE_COL_TO_ACC));
                rec1.rec_balance=c.getFloat(c.getColumnIndex(RECURSIVE_COL_BALANCE));
                rec1.rec_p_id=c.getInt(c.getColumnIndex(RECURSIVE_COL_PERSON));
                rec1.rec_note=c.getString(c.getColumnIndex(RECURSIVE_COL_NOTE));
                rec1.rec_show=c.getInt(c.getColumnIndex(RECURSIVE_COL_SHOW));
                rec1.rec_start_date=c.getString(c.getColumnIndex(RECURSIVE_COL_START_DATE));
                rec1.rec_end_date=c.getString(c.getColumnIndex(RECURSIVE_COL_END_DATE));
                rec1.rec_next_date=c.getString(c.getColumnIndex(RECURSIVE_COL_NEXT_DATE));
                rec1.rec_time=c.getString(c.getColumnIndex(RECURSIVE_COL_TIME));
                rec1.rec_type=c.getString(c.getColumnIndex(RECURSIVE_COL_TYPE));
                rec1.rec_recurring=c.getInt(c.getColumnIndex(RECURSIVE_COL_RECURRING));
                rec1.rec_alert=c.getInt(c.getColumnIndex(RECURSIVE_COL_ALERT));
                rec1.rec_c_id=c.getInt(c.getColumnIndex(RECURSIVE_COL_CATEGORY));
                rec1.rec_sub_id=c.getInt(c.getColumnIndex(RECURSIVE_COL_SUBCATEGORY));
                arrayList.add(rec1);
//                Log.i("Recursive :", rec1.rec_id +"\t"+ rec1.rec_u_id +"\t"+rec1.rec_from_acc +"\t"+rec1.rec_to_acc+"\t"+ rec1.rec_balance
//                        +"\t"+ rec1.rec_type +"\t"+ rec1.rec_c_id+"\t"+ rec1.rec_sub_id+"\t"+ rec1.rec_p_id+"\t"+ rec1.rec_start_date +"\t"+
//                        rec1.rec_end_date +"\t"+ rec1.rec_time +"\t"+ rec1.rec_note +"\t"+ rec1.rec_show +"\t"+ rec1.rec_recurring +"\t"+
//                        rec1.rec_alert + "\t" + rec1.rec_next_date);
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

    public boolean addPerson(String name, String color, String colorCode, int u_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(PERSON_COL_NAME,name);
        contentValues.put(PERSON_COL_COLOR,color);
        contentValues.put(PERSON_COL_COLOR_CODE,colorCode);
        contentValues.put(PERSON_COL_UID, u_id);

        return db.insert(PERSON_TABLE, null, contentValues) > 0;
    }

    public boolean updatePersonData(int id, String name,String color, String colorCode)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(PERSON_COL_NAME,name);
        contentValues.put(PERSON_COL_COLOR,color);
        contentValues.put(PERSON_COL_COLOR_CODE,colorCode);

        return db.update(PERSON_TABLE, contentValues, PERSON_COL_ID +"="+ id, null) > 0;
    }

    public int deletePerson(int id,int u_id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        return db.delete(PERSON_TABLE, PERSON_COL_ID +"="+ id +" and "+ PERSON_COL_UID +"="+ u_id, null);
    }

    public Cursor getPersonData(int id, int u_id)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select *from "+ PERSON_TABLE +" where "+ PERSON_COL_ID +"="+ id + " and " + PERSON_COL_UID + "=" + u_id, null);
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

    public ArrayList getPersonColName(int u_id)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select " + PERSON_COL_NAME + " from "+ PERSON_TABLE + " where " + PERSON_COL_UID + "="
                    + u_id, null);
            ArrayList al = new ArrayList();
            String s;
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                s = c.getString(c.getColumnIndex(PERSON_COL_NAME));
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
                p1.p_color_code = c.getString(c.getColumnIndex(PERSON_COL_COLOR_CODE));
                p1.p_u_id = c.getInt(c.getColumnIndex(PERSON_COL_UID));
                arrayList.add(p1);
//                Log.i("PERSON :", p1.p_id +"\t"+ p1.p_name +"\n\t"+ p1.p_color_code +"\t"+ p1.p_u_id);
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

    public boolean addCategoryMaster(String name,int type,String icon)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(CATEGORY_COL_C_NAME,name);
        contentValues.put(CATEGORY_COL_C_TYPE,type);
        contentValues.put(CATEGORY_COL_C_ICON,icon);

        Log.i("Add Category Master:",contentValues+"");

        return db.insert(CATEGORY_MASTER, null, contentValues) > 0;
    }

    public boolean addCategorySpecific(int u_id,String name,int type,String icon)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(CATEGORY_COL_C_UID,u_id);
        contentValues.put(CATEGORY_COL_C_NAME,name);
        contentValues.put(CATEGORY_COL_C_TYPE,type);
        contentValues.put(CATEGORY_COL_C_ICON,icon);

        Log.i("Add Category",contentValues+"");

        return db.insert(CATEGORY_SPECIFIC, null, contentValues) > 0;
    }

    public boolean updateCategory(int u_id,int id,String name,int type,String icon)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(CATEGORY_COL_C_NAME,name);
        contentValues.put(CATEGORY_COL_C_TYPE,type);
        contentValues.put(CATEGORY_COL_C_ICON,icon);

        return db.update(CATEGORY_SPECIFIC, contentValues, CATEGORY_COL_C_ID +"="+ id
                + " and " + CATEGORY_COL_C_UID + "=" +u_id, null) > 0;
    }

    public int deleteCategory(int id,int u_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(CATEGORY_SPECIFIC, CATEGORY_COL_C_ID +"="+ id +" and "+ CATEGORY_COL_C_UID +"="+ u_id, null);
    }

    public int getCategoryColIduid(int u_id)
    {
        int cid = 0;
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor c = db.rawQuery("select * from "+ CATEGORY_SPECIFIC+" where "+ CATEGORY_COL_C_UID +"="+ u_id, null);
            c.moveToFirst();
            while(!c.isAfterLast())
            {
                cid=c.getInt(c.getColumnIndex(CATEGORY_COL_C_ID));
                c.moveToNext();
            }
            c.close();
            return  cid;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return 0;
        }
    }

    public Cursor getCategoryData(int id,int u_id)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select * from "+ CATEGORY_SPECIFIC +" where "+ CATEGORY_COL_C_ID +"="+ id + " and " +
                    CATEGORY_COL_C_UID +" = " +u_id, null);
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getCategoryNames(int u_id)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select " + CATEGORY_COL_C_NAME + " from "+ CATEGORY_SPECIFIC + " where "
                    + CATEGORY_COL_C_UID + "=" + u_id, null);
            ArrayList al = new ArrayList();
            String s;
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                s = c.getString(c.getColumnIndex(PERSON_COL_NAME));
                al.add(s);
                c.moveToNext();
            }
            c.close();
            Cursor c1 = db.rawQuery("select " + SUBCATEGORY_COL_SUB_NAME + " from "+ SUBCATEGORY_TABLE + " where "
                    + SUBCATEGORY_COL_SUB_UID + "=" + u_id, null);
            c1.moveToFirst();
            while (!c1.isAfterLast())
            {
                s = c1.getString(c.getColumnIndex(PERSON_COL_NAME));
                al.add(s);
                c1.moveToNext();
            }
            c1.close();

            return al;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getCategoryIds(int u_id,int type)
    {
        ArrayList<String> arrayList = new ArrayList<>();
        String c_id=null;
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ CATEGORY_SPECIFIC +" where "+ CATEGORY_COL_C_TYPE + "=" + type
                    + " and " + CATEGORY_COL_C_UID + "=" + u_id, null);
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                c_id = c.getString(c.getColumnIndex(CATEGORY_COL_C_ID));

                arrayList.add(c_id);
                Log.i("CATEGORY NAME :", c_id );
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

    public int getCategoryColId(String c_name)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ CATEGORY_SPECIFIC, null);
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

    public ArrayList<String> getCategoryColName(int u_id,int type)
    {
        ArrayList<String> arrayList = new ArrayList<>();
        String c_name=null;
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ CATEGORY_SPECIFIC +" where "+ CATEGORY_COL_C_TYPE + "=" + type
                    + " and " + CATEGORY_COL_C_UID + "=" + u_id, null);
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                c_name = c.getString(c.getColumnIndex(CATEGORY_COL_C_NAME));

                arrayList.add(c_name);
                Log.i("CATEGORY NAME :", c_name );
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

    public ArrayList<CategoryDB_Specific> getCategories(int u_id)
    {
        ArrayList<CategoryDB_Specific> arrayList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ CATEGORY_SPECIFIC +" where "+  CATEGORY_COL_C_UID + "=" + u_id, null);
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                CategoryDB_Specific c1 = new CategoryDB_Specific();
                c1.c_id = c.getInt(c.getColumnIndex(CATEGORY_COL_C_ID));
                c1.c_u_id = c.getInt(c.getColumnIndex(CATEGORY_COL_C_UID));
                c1.c_name = c.getString(c.getColumnIndex(CATEGORY_COL_C_NAME));
                c1.c_type = c.getString(c.getColumnIndex(CATEGORY_COL_C_TYPE));
                c1.c_icon = c.getString(c.getColumnIndex(CATEGORY_COL_C_ICON));
                arrayList.add(c1);
                Log.i("CATEGORY :", c1.c_u_id +"\t"+ c1.c_id +"\t"+ c1.c_type +"\t"+ c1.c_name);
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

    public ArrayList<CategoryDB_Specific> getAllCategories(int u_id,int type)
    {
        ArrayList<CategoryDB_Specific> arrayList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ CATEGORY_SPECIFIC +" where "+ CATEGORY_COL_C_TYPE + "=" + type
                    + " and " + CATEGORY_COL_C_UID + "=" + u_id, null);
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                CategoryDB_Specific c1 = new CategoryDB_Specific();
                c1.c_id = c.getInt(c.getColumnIndex(CATEGORY_COL_C_ID));
                c1.c_u_id = c.getInt(c.getColumnIndex(CATEGORY_COL_C_UID));
                c1.c_name = c.getString(c.getColumnIndex(CATEGORY_COL_C_NAME));
                c1.c_type = c.getString(c.getColumnIndex(CATEGORY_COL_C_TYPE));
                c1.c_icon = c.getString(c.getColumnIndex(CATEGORY_COL_C_ICON));
                arrayList.add(c1);
                Log.i("CATEGORY :", c1.c_u_id +"\t"+ c1.c_id +"\t"+ c1.c_type +"\t"+ c1.c_name);
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
    public boolean addSubCategory(int c_id,String name,int u_id,String img)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();


        contentValues.put(SUBCATEGORY_COL_SUB_CID,c_id);
        contentValues.put(SUBCATEGORY_COL_SUB_NAME,name);
        contentValues.put(SUBCATEGORY_COL_SUB_UID,u_id);
        contentValues.put(SUBCATEGORY_COL_SUB_ICON,img);
        return db.insert(SUBCATEGORY_TABLE, null, contentValues) > 0;
    }

    public boolean updateSubCategory(int id,int c_id,String name,int u_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(SUBCATEGORY_COL_SUB_CID,c_id);
        contentValues.put(SUBCATEGORY_COL_SUB_NAME,name);

        return db.update(SUBCATEGORY_TABLE, contentValues, SUBCATEGORY_COL_SUB_ID +"="+ id+" and "+SUBCATEGORY_COL_SUB_UID+" = "+u_id, null) > 0;
    }
    public ArrayList<String> getSubCategoryColName(int u_id,int c_id)
    {
        ArrayList<String> arrayList = new ArrayList<>();
        String c_name=null;
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ SUBCATEGORY_TABLE +" where "+  SUBCATEGORY_COL_SUB_UID + "=" + u_id + " and "+ SUBCATEGORY_COL_SUB_CID +"="+c_id, null);
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                c_name = c.getString(c.getColumnIndex(SUBCATEGORY_COL_SUB_NAME));

                arrayList.add(c_name);
                Log.i("SUBCATEGORY NAME :", c_name );
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

    public int deleteSubCategory(int id,int u_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(SUBCATEGORY_TABLE, SUBCATEGORY_COL_SUB_ID +"="+ id +" and "+ SUBCATEGORY_COL_SUB_UID +"="+ u_id, null);
    }

    public int getSubCategoryColId(int u_id)
    {
        int subid = 0;
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor c = db.rawQuery("select * from "+ SUBCATEGORY_TABLE+" where "+ SUBCATEGORY_COL_SUB_UID +"="+ u_id, null);
            c.moveToFirst();
            while(!c.isAfterLast())
            {
                subid=c.getInt(c.getColumnIndex(SUBCATEGORY_COL_SUB_ID));
                c.moveToNext();
            }
            c.close();
            return  subid;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return 0;
        }
    }
    public Cursor getSubCategoryData(int id, int u_id)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select * from "+ SUBCATEGORY_TABLE +" where "+ SUBCATEGORY_COL_SUB_ID +"="+ id+" and "+SUBCATEGORY_COL_SUB_UID+" = "+u_id, null);
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }

    public int getSubCategoryColIds(String c_name)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ SUBCATEGORY_TABLE, null);
            String s;
//            int k=countCategory();
            //int cat_id[] =new int[k];
            int cat_id=0;
            c.moveToFirst();
            int i=0;
            while (!c.isAfterLast())
            {
                s = c.getString(c.getColumnIndex(SUBCATEGORY_COL_SUB_NAME));
                if (s.equals(c_name))
                {
                    cat_id = c.getInt(c.getColumnIndex(SUBCATEGORY_COL_SUB_ID));
                    // i++;
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

//    public int countSubCategory(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        int numRows = (int) DatabaseUtils.queryNumEntries(db, SUBCATEGORY_TABLE);
//        return numRows;
//    }

    public ArrayList<SubCategoryDB> getAllSubCategories(int u_id, int c_id)
    {
        ArrayList<SubCategoryDB> arrayList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ SUBCATEGORY_TABLE +" where "+ SUBCATEGORY_COL_SUB_UID + " = "+u_id+ " and " + SUBCATEGORY_COL_SUB_CID +" = "+c_id, null);
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                SubCategoryDB sub1 = new SubCategoryDB();
                sub1.sub_id = c.getInt(c.getColumnIndex(SUBCATEGORY_COL_SUB_ID));
                sub1.sub_c_id = c.getInt(c.getColumnIndex(SUBCATEGORY_COL_SUB_CID));
                sub1.sub_name = c.getString(c.getColumnIndex(SUBCATEGORY_COL_SUB_NAME));
                sub1.sub_u_id=c.getInt(c.getColumnIndex(SUBCATEGORY_COL_SUB_UID));
                arrayList.add(sub1);
                Log.i("SUBCATEGORY :", sub1.sub_id +"\t"+ sub1.sub_c_id +"\t"+ sub1.sub_name+"\t"+sub1.sub_u_id);
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

    public ArrayList<SubCategoryDB> getSubCategories(int u_id)
    {
        ArrayList<SubCategoryDB> arrayList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ SUBCATEGORY_TABLE +" where "+ SUBCATEGORY_COL_SUB_UID + " = "+u_id, null);
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                SubCategoryDB sub1 = new SubCategoryDB();
                sub1.sub_id = c.getInt(c.getColumnIndex(SUBCATEGORY_COL_SUB_ID));
                sub1.sub_c_id = c.getInt(c.getColumnIndex(SUBCATEGORY_COL_SUB_CID));
                sub1.sub_name = c.getString(c.getColumnIndex(SUBCATEGORY_COL_SUB_NAME));
                sub1.sub_u_id=c.getInt(c.getColumnIndex(SUBCATEGORY_COL_SUB_UID));
                arrayList.add(sub1);
                Log.i("SUBCATEGORY :", sub1.sub_id +"\t"+ sub1.sub_c_id +"\t"+ sub1.sub_name+"\t"+sub1.sub_u_id);
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

    public boolean addLoanDebt(int u_id,float amt,String date,String time,int fromAcc,int toAcc,int person,String note, String type,
                               int parent){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(LOAN_DEBT_COL_UID,u_id);
        contentValues.put(LOAN_DEBT_COL_BALANCE,amt);
        contentValues.put(LOAN_DEBT_COL_DATE,date);
        contentValues.put(LOAN_DEBT_COL_TIME,time);
        contentValues.put(LOAN_DEBT_COL_FROM_ACC,fromAcc);
        contentValues.put(LOAN_DEBT_COL_TO_ACC,toAcc);
        contentValues.put(LOAN_DEBT_COL_PERSON,person);
        contentValues.put(LOAN_DEBT_COL_NOTE,note);
        contentValues.put(LOAN_DEBT_COL_TYPE, type);
        contentValues.put(LOAN_DEBT_COL_PARENT, parent);

        return db.insert(LOAN_DEBT_TABLE, null, contentValues) > 0;
    }

    public boolean updateLoanDebtData(int id, int from_acc,int to_acc,int p_id, float balance ,
                                      String note,String type,String date,String time, int u_id )
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(LOAN_DEBT_COL_FROM_ACC,from_acc);
        contentValues.put(LOAN_DEBT_COL_BALANCE,balance);
        contentValues.put(LOAN_DEBT_COL_TO_ACC,to_acc);
        contentValues.put(LOAN_DEBT_COL_TYPE,type);
        contentValues.put(LOAN_DEBT_COL_PERSON,p_id);
        contentValues.put(LOAN_DEBT_COL_DATE,date);
        contentValues.put(LOAN_DEBT_COL_TIME,time);
        contentValues.put(LOAN_DEBT_COL_NOTE,note);

        return db.update(LOAN_DEBT_TABLE, contentValues, LOAN_DEBT_COL_ID +"="+ id +" and "
                + LOAN_DEBT_COL_UID + "=" + u_id, null) > 0;
    }

    public ArrayList<LoanDebtDB> getAllLoanDebt(int u_id, int parent)
    {
        ArrayList<LoanDebtDB> arrayList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ LOAN_DEBT_TABLE +" where "+ LOAN_DEBT_COL_UID +"="+ u_id + " and " +
                    LOAN_DEBT_COL_PARENT + "=" + parent, null);
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                LoanDebtDB p1 = new LoanDebtDB();
                p1.l_id = c.getInt(c.getColumnIndex(LOAN_DEBT_COL_ID));
                p1.l_balance = c.getFloat(c.getColumnIndex(LOAN_DEBT_COL_BALANCE));
                p1.l_date = c.getString(c.getColumnIndex(LOAN_DEBT_COL_DATE));
                p1.l_time = c.getString(c.getColumnIndex(LOAN_DEBT_COL_TIME));
                p1.l_from_acc = c.getInt(c.getColumnIndex(LOAN_DEBT_COL_FROM_ACC));
                p1.l_to_acc = c.getInt(c.getColumnIndex(LOAN_DEBT_COL_TO_ACC));
                p1.l_person = c.getInt(c.getColumnIndex(LOAN_DEBT_COL_PERSON));
                p1.l_note = c.getString(c.getColumnIndex(LOAN_DEBT_COL_NOTE));
                p1.l_type = c.getString(c.getColumnIndex(LOAN_DEBT_COL_TYPE));
                p1.l_parent = c.getInt(c.getColumnIndex(LOAN_DEBT_COL_PARENT));
                arrayList.add(p1);
//                Log.i("PERSON :", p1.l_id +"\t"+ p1.l_balance +"\n\t"+ p1.l_date +"\t"+ p1.l_time +"\t"+ p1.l_from_acc +"\t"+
//                        p1.l_to_acc +"\t"+ p1.l_person +"\t"+ p1.l_person +"\t"+ p1.l_note +"\t"+ p1.l_type +"\t"+ p1.l_parent);
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

    public int getLoanDebtColId(int u_id)
    {
        int ldid = 0;
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor c = db.rawQuery("select * from "+ LOAN_DEBT_TABLE +" where "+ LOAN_DEBT_COL_UID +"="+ u_id, null);
            c.moveToFirst();
            while(!c.isAfterLast())
            {
                ldid=c.getInt(c.getColumnIndex(LOAN_DEBT_COL_ID));
                c.moveToNext();
            }
            c.close();
            return  ldid;
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return 0;
        }
    }
    public Cursor getLoanDebtData(int id)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select * from "+ LOAN_DEBT_TABLE +" where "+ LOAN_DEBT_COL_ID +"="+ id, null);
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }

    public int deleteLoanDebt(int id,int u_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(LOAN_DEBT_TABLE, LOAN_DEBT_COL_ID +"="+ id +" and "+ LOAN_DEBT_COL_UID +"="+ u_id, null);
    }

    public boolean addSettings(int u_id,String cur_code)
    {

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(SETTINGS_COL_UID,u_id);
        contentValues.put(SETTINGS_COL_CUR_CODE,cur_code);

        return db.insert(SETTINGS_TABLE, null, contentValues) > 0;
    }

    public boolean updateSettings(int u_id, String cur_code)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(SETTINGS_COL_CUR_CODE,cur_code);

        return db.update(SETTINGS_TABLE, contentValues, SETTINGS_COL_UID +"="+ u_id, null) > 0;
    }

    public int deleteSettings(int u_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SETTINGS_TABLE, SETTINGS_COL_UID + "=" + u_id, null);
    }

    public Cursor getSettingsData(int u_id)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select * from "+ SETTINGS_TABLE +" where "+ SETTINGS_COL_UID +"="+ u_id, null);
        }
        catch(Exception ae)
        {
            ae.printStackTrace();
            return null;
        }
    }

    public ArrayList getSettingsUid()
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+ SETTINGS_TABLE, null);
            ArrayList al = new ArrayList();
            int s;
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                s = c.getInt(c.getColumnIndex(SETTINGS_COL_UID));
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
}