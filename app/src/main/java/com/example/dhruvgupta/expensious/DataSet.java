package com.example.dhruvgupta.expensious;

import android.text.method.DateTimeKeyListener;

/**
 * Created by Gaurav on 3/11/15.
 */
class SignUpDB
{
    String u_name,u_email,u_password;
    int u_id,u_fid;
}

class AccountsDB
{
    int acc_id,acc_u_id,acc_show;
    float acc_balance;
    String acc_name,acc_note,acc_currency;
}

class CategoryDB
{
    int c_id,c_u_id;
    String c_name,c_type,c_icon;
}

class SubCategoryDB
{
    int sub_id,sub_c_id;
    String sub_name,sub_icon;
}

class TransactionsDB
{
    int t_id,u_id,t_c_id,t_sub_id,t_amount,t_p_id,t_show;
    String t_type,t_from_acc,t_to_acc,t_note,t_datetime;
}