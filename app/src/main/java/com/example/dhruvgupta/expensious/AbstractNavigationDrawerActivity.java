package com.example.dhruvgupta.expensious;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import br.liveo.interfaces.NavigationLiveoListener;
import br.liveo.navigationliveo.NavigationLiveo;

/**
 * Created by Amrit on 4/23/2015.
 */
public class AbstractNavigationDrawerActivity extends NavigationLiveo implements NavigationLiveoListener {

    Bitmap decodedByte = null;
    SharedPreferences sp, sp1;
    int fid, has_req, is_head;

    @Override
    public void onUserInformation() {
        sp =getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        sp1 =getSharedPreferences("USER_IMAGE", MODE_PRIVATE);

        ParseUser user = ParseUser.getCurrentUser();
        fid = user.getInt("fid");
        is_head = user.getInt("is_head");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Family_request");
        query.whereEqualTo("uid", sp.getInt("UID", 0));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null)
                    has_req = 0;
                else
                    has_req = parseObject.getInt("has_request");
            }
        });

        this.mUserName.setText(sp.getString("USERNAME","abc"));
        this.mUserEmail.setText(sp.getString("EMAIL","abc@xyz.com"));
        this.mUserPhoto.setImageURI(Uri.parse(sp1.getString("UIMAGE", "")));
        this.mUserBackground.setImageResource(R.drawable.background_small);

    }

    @Override
    public void onInt(Bundle bundle) {

        this.setNavigationListener(this);

        // name of the list items
        List<String> mListNameItem = new ArrayList<>();

        mListNameItem.add(0, "Accounts");
        mListNameItem.add(1, "Transactions");
        mListNameItem.add(2, "Recursive Transaction");
        mListNameItem.add(3, "Budget");
        mListNameItem.add(4, "Loan/Debt");
        mListNameItem.add(5, "Family Sharing");
        mListNameItem.add(6, "Reports");
        mListNameItem.add(7, "Settings");
        mListNameItem.add(8, "Logout");

        // icons list items
        List<Integer> mListIconItem = new ArrayList<>();

        mListIconItem.add(0,R.drawable.accounts ); //Item no icon set 0
        mListIconItem.add(1, R.drawable.trans); //Item no icon set 0
        mListIconItem.add(2, R.drawable.recursive);
        mListIconItem.add(3, R.drawable.budget); //When the item is a subHeader the value of the icon 0
        mListIconItem.add(4, R.drawable.loandebt);
        mListIconItem.add(5, R.drawable.family);
        mListIconItem.add(6, R.drawable.piechart);
        mListIconItem.add(7,R.drawable.settings);
        mListIconItem.add(8, R.drawable.settings);

        SparseIntArray mSparseCounterItem = new SparseIntArray(); //indicate all items that have a counter

        if (has_req != 0)
            mSparseCounterItem.put(5, 1);
        else
            mSparseCounterItem.clear();

        //If not please use the FooterDrawer use the setFooterVisible(boolean visible) method with value false
       // this.setFooterInformationDrawer(R.string.settings, R.drawable.ic_settings_black_24dp);

        this.setFooterNavigationVisible(false);

        this.setNavigationAdapter(mListNameItem, mListIconItem);
    }


    @Override
    public void onItemClickNavigation(int position, int layoutContainerId) {

        FragmentManager mFragmentManager = getSupportFragmentManager();
        Fragment mFragment=null;
        Intent intent=null;
        switch (position) {


            case 0:
                if (this instanceof AccountsActivity) {
                    mFragment = new AccountsActivity.AccountsFragment();
                } else {
                    intent = new Intent(this, AccountsActivity.class);
                }
                break;
            case 1:
                if (this instanceof TransactionsActivity) {
                    mFragment = new TransactionsActivity.TransactionFragment();
                } else {
                    intent = new Intent(this, TransactionsActivity.class);
                }
                break;
            case 2:
                if (this instanceof RecursiveActivity) {
                    mFragment = new RecursiveActivity.RecursiveFragment();
                } else {
                    intent = new Intent(this,RecursiveActivity.class);
                }
                break;
            case 3:
                if (this instanceof BudgetsActivity ) {
                    mFragment = new BudgetsActivity.BudgetFragment();
                } else {
                    intent = new Intent(this, BudgetsActivity.class);
                }
                break;
            case 4:
                if (this instanceof LoanDebtActivity) {
                    mFragment = new LoanDebtActivity.LoanDebtFragment();
                } else {
                    intent = new Intent(this, LoanDebtActivity.class);
                }
                break;
            case 5:
                if (CheckInternet(AbstractNavigationDrawerActivity.this))
                {
                    if (has_req != 0)
                    {
                        if (this instanceof AcceptFamilyActivity) {
                            mFragment = new AcceptFamilyActivity.AcceptFamilyFragment();
                        } else {
                            intent = new Intent(this, AcceptFamilyActivity.class);
                        }
                    }
                    else if (fid == 0)
                    {
                        if (this instanceof EnableFamilyActivity) {
                            mFragment = new EnableFamilyActivity.EnableFamilyFragment();
                        } else {
                            intent = new Intent(this, EnableFamilyActivity.class);
                        }
                    }
                    else if (is_head == 1)
                    {
                        if (this instanceof HeadFamilyView) {
                            mFragment = new HeadFamilyView.HeadFamilyViewFragment();
                        } else {
                            intent = new Intent(this, HeadFamilyView.class);
                        }
                    }
                    else
                    {

                    }
                }
                else
                    Toast.makeText(AbstractNavigationDrawerActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                break;
            case 6:
                if (this instanceof PieChartActivity) {
                    mFragment = new PieChartActivity.PieChartFragment();
                } else {
                    intent = new Intent(this, PieChartActivity.class);
                }
                break;
            case 7:
                if (this instanceof SettingsActivity) {
                    mFragment = new SettingsActivity.SettingsFragment();
                } else {
                    intent = new Intent(this, SettingsActivity.class);
                }
                break;
            case 8:
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null)
                        {
                            Toast.makeText(AbstractNavigationDrawerActivity.this, "Logout", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(AbstractNavigationDrawerActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                SharedPreferences.Editor spEdit= sp.edit();
                spEdit.clear();
                spEdit.commit();
                SharedPreferences.Editor spEdit1= sp1.edit();
                spEdit1.clear();
                spEdit1.commit();
                Intent i = new Intent(AbstractNavigationDrawerActivity.this, LoginActivity.class);
                startActivity(i);
                break;

        }

        if (mFragment != null){
            mFragmentManager.beginTransaction().replace(layoutContainerId, mFragment).commit();
        }
        else if (intent != null){
            startActivity(intent);
        }
    }

    @Override
    public void onPrepareOptionsMenuNavigation(Menu menu, int i, boolean b) {
//        Toast.makeText(this, "You pressed Settings", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickFooterItemNavigation(View view) {
//        Toast.makeText(this, "You pressed UserPhoto", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickUserPhotoNavigation(View view) {

    }

    public boolean CheckInternet(Context ctx) {
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // Check if wifi or mobile network is available or not. If any of them is
        // available or connected then it will return true, otherwise false;
        return wifi.isConnected() || mobile.isConnected();
    }
}
