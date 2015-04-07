package com.example.dhruvgupta.expensious;

/**
 * Created by Gaurav on 3/11/15.
 */
class SignUpDB
{
    String u_name,u_email,u_password,u_image;
    int u_id,u_fid;
}

class AccountsDB
{
    int acc_id,acc_u_id,acc_show;
    float acc_balance;
    String acc_name,acc_note,acc_currency;
}

class PersonDB
{
    int p_id,p_u_id;
    String p_name,p_color,p_color_code;
}

class CategoryDB_Specific
{
    int c_id,c_u_id;
    String c_name,c_type,c_icon;
}
class CategoryDB_Master
{
    int c_id,c_type;
    String c_name,c_icon;
}

class SubCategoryDB
{
    int sub_id,sub_c_id,sub_u_id;
    String sub_name;
}

class TransactionsDB
{
    int t_id,t_u_id,t_c_id,t_sub_id,t_p_id,t_from_acc,t_to_acc,t_show;
    float t_balance;
    String t_type,t_note,t_date,t_time;
}

class CurrencyDB
{
    String c_name=null, c_code=null, c_symbol=null;
}

class BudgetDB
{
    int b_id,b_u_id;
    float b_amount;
    String b_currency,b_endDate, b_startDate;
}
