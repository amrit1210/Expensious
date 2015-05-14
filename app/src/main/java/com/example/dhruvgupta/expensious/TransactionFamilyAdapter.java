package com.example.dhruvgupta.expensious;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Amrit on 3/27/2015.
 */
public class TransactionFamilyAdapter extends ArrayAdapter {
    Context context1;
    int layout, uid;
    DBHelper dbHelper;
    ArrayList<TransactionsDB>al;
    ArrayList<CurrencyDB> al1;
    String curCode;

    public TransactionFamilyAdapter(Context context, int resource, ArrayList<TransactionsDB> al1, int u_id) {
        super(context, resource, al1);
        context1=context;
        layout=resource;
        al=al1;
        uid = u_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater in=(LayoutInflater)context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= in.inflate(layout,null);
        }

        final CircularImageView image = (CircularImageView)convertView.findViewById(R.id.list_transaction_img);
        final TextView date=(TextView)convertView.findViewById(R.id.list_transaction_date);
        final TextView category=(TextView)convertView.findViewById(R.id.list_transaction_cat);
        final TextView note=(TextView)convertView.findViewById(R.id.list_transaction_note);
        final TextView time=(TextView)convertView.findViewById(R.id.list_transaction_time);
        final TextView currency=(TextView)convertView.findViewById(R.id.list_transaction_cur);
        final TextView account=(TextView)convertView.findViewById(R.id.list_transaction_acc);
        final TextView amount=(TextView)convertView.findViewById(R.id.list_transaction_amt);

        dbHelper = new DBHelper(getContext());

        final ParseQuery<ParseObject> curQuery = ParseQuery.getQuery("Settings");
        curQuery.whereEqualTo("settings_uid", uid);
        curQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                al1 = new ListOfCurrencies().getAllCurrencies();

                Iterator<CurrencyDB> iterator = al1.iterator();
                while (iterator.hasNext())
                {
                    CurrencyDB curDB = iterator.next();
                    if (curDB.c_code.equals(parseObject.getString("settings_cur_code")))
                        currency.setText(curDB.c_symbol);
                }
            }
        });

        final TransactionsDB trans_db=al.get(position);
        date.setText(trans_db.t_date);
        final int c_id= trans_db.t_c_id;
        final int sub_id= trans_db.t_sub_id;

        if(c_id!=0)
        {
            ParseQuery<ParseObject> catQuery = ParseQuery.getQuery("Category_specific");
            catQuery.whereEqualTo("c_uid", uid);
            catQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    for (ParseObject parseObject : list) {
                        if (c_id == parseObject.getInt("c_id"))
                        {
                            final String cat = parseObject.getString("c_name");

                            try {
                                String c_img = parseObject.getString("c_icon");
                                byte[] decodedString = Base64.decode(c_img.trim(), Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                image.setImageBitmap(decodedByte);

                                ParseQuery<ParseObject> perQuery = ParseQuery.getQuery("Persons");
                                perQuery.whereEqualTo("p_uid", uid);
                                perQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> list, ParseException e) {
                                        for (ParseObject parseObject1 : list) {
                                            if (trans_db.t_p_id == parseObject1.getInt("p_id"))
                                            {
                                                String colorCode = parseObject1.getString("p_color_code");
                                                image.setBorderColor(Color.parseColor(colorCode));
                                            }
                                        }
                                    }
                                });
                            } catch (Exception e1) {
                                Log.i("Excep:PersonAdapter", e1.getMessage());
                            }

                            if (sub_id != 0) {
                                ParseQuery<ParseObject> subQuery = ParseQuery.getQuery("Sub_category");
                                subQuery.whereEqualTo("sub_uid", uid);
                                subQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> list, ParseException e) {
                                        for (ParseObject parseObject1 : list)
                                        {
                                            if (sub_id == parseObject1.getInt("sub_id"))
                                            {
                                                String subcat = parseObject1.getString("sub_name");
                                                category.setText(cat + " : " + subcat);
                                            }
                                        }
                                    }
                                });
                            } else {
                                category.setText(cat);
                            }
                        }
                    }
                }
            });
        }

        note.setText(trans_db.t_note);
        time.setText(trans_db.t_time);
        amount.setText(trans_db.t_balance+"");

        if(trans_db.t_type.equals("Expense"))
        {
            if(trans_db.t_from_acc!=0) {
                ParseQuery<ParseObject> accQuery = ParseQuery.getQuery("Accounts");
                accQuery.whereEqualTo("acc_uid", uid);
                accQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        for (ParseObject parseObject : list)
                        {
                            if (trans_db.t_from_acc == parseObject.getInt("acc_id"))
                            {
                                account.setText(parseObject.getString("acc_name"));
                            }
                        }
                    }
                });
            }
            else
                account.setText(null);
        }
        else if(trans_db.t_type.equals("Income"))
        {
            if(trans_db.t_to_acc!=0) {
                ParseQuery<ParseObject> accQuery = ParseQuery.getQuery("Accounts");
                accQuery.whereEqualTo("acc_uid", uid);
                accQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        for (ParseObject parseObject : list) {
                            if (trans_db.t_to_acc == parseObject.getInt("acc_id")) {
                                account.setText(parseObject.getString("acc_name"));
                            }
                        }
                    }
                });
            }
            else
                account.setText(null);
        }
        else if(trans_db.t_type.equals("Transfer"))
        {
            String from_acc=null,to_acc=null;
            if(trans_db.t_from_acc!=0 && trans_db.t_to_acc!=0) {
                ParseQuery<ParseObject> accQuery = ParseQuery.getQuery("Accounts");
                accQuery.whereEqualTo("acc_uid", uid);
                accQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        for (ParseObject parseObject : list) {
                            if (trans_db.t_from_acc == parseObject.getInt("acc_id")) {
                                account.setText(parseObject.getString("acc_name"));

                                ParseQuery<ParseObject> accQuery1 = ParseQuery.getQuery("Accounts");
                                accQuery1.whereEqualTo("acc_uid", uid);
                                accQuery1.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> list, ParseException e) {
                                        for (ParseObject parseObject : list) {
                                            if (trans_db.t_to_acc == parseObject.getInt("acc_id")) {
                                                account.setText(account.getText() + "->" + parseObject.getString("acc_name"));
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
            else
                account.setText(null);
            category.setText(null);
        }

        return convertView;
    }
}
