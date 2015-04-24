package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.liveo.interfaces.NavigationLiveoListener;
import br.liveo.navigationliveo.NavigationLiveo;

/**
 * Created by Amrit on 4/23/2015.
 */
public class AbstractNavigationDrawerActivity extends NavigationLiveo implements NavigationLiveoListener {
    @Override
    public void onUserInformation() {
        SharedPreferences sp =getSharedPreferences("USER_PREFS",MODE_PRIVATE);
        this.mUserName.setText(sp.getString("USERNAME","abc"));
        this.mUserEmail.setText(sp.getString("EMAIL","abc@xyz.com"));
       // this.mUserPhoto.setImageResource(R.drawable.ic_rudsonlive);
       this.mUserBackground.setImageResource(R.drawable.background_small);

    }

    @Override
    public void onInt(Bundle bundle) {

        this.setNavigationListener(this);

        // name of the list items
        List<String> mListNameItem = new ArrayList<>();
        mListNameItem.add(0, "DashBoard");
        mListNameItem.add(1, "Accounts");
        mListNameItem.add(2, "Transactions");
        mListNameItem.add(3, "Recursive Transaction");
        mListNameItem.add(4, "Budget");
        mListNameItem.add(5, "Loan/Debt");
        mListNameItem.add(6, "Family Sharing");
        mListNameItem.add(7, "Reports");
        mListNameItem.add(8, "Settings");

        // icons list items
        List<Integer> mListIconItem = new ArrayList<>();
        mListIconItem.add(0, 0);
        mListIconItem.add(1, 0); //Item no icon set 0
        mListIconItem.add(2, 0); //Item no icon set 0
        mListIconItem.add(3, 0);
        mListIconItem.add(4, 0); //When the item is a subHeader the value of the icon 0
        mListIconItem.add(5, 0);
        mListIconItem.add(6, 0);
        mListIconItem.add(7, 0);
        mListIconItem.add(8, 0);


        //If not please use the FooterDrawer use the setFooterVisible(boolean visible) method with value false
       // this.setFooterInformationDrawer(R.string.settings, R.drawable.ic_settings_black_24dp);

        this.setNavigationAdapter(mListNameItem, mListIconItem);
    }


    @Override
    public void onItemClickNavigation(int position, int layoutContainerId) {

        FragmentManager mFragmentManager = getSupportFragmentManager();
        Fragment mFragment=null;
        Intent intent=null;
        switch (position) {
            case 0:
//                if (this instanceof DashBoardActivity) {
//                    mFragment = new DashBoardActivity.DashBoardFragment();
//                } else {
//                    intent = new Intent(this, DashBoardActivity.class);
//                }
                break;
            case 1:
                if (this instanceof AccountsActivity) {
                    mFragment = new AccountsActivity.AccountsFragment();
                } else {
                    intent = new Intent(this, AccountsActivity.class);
                }
                break;
            case 2:
                if (this instanceof TransactionsActivity) {
                    mFragment = new TransactionsActivity.TransactionFragment();
                } else {
                    intent = new Intent(this, TransactionsActivity.class);
                }
                break;
            case 3:
                if (this instanceof RecursiveActivity) {
                    mFragment = new RecursiveActivity.RecursiveFragment();
                } else {
                    intent = new Intent(this,RecursiveActivity.class);
                }
                break;
            case 4:
                if (this instanceof BudgetsActivity ) {
                    mFragment = new BudgetsActivity.BudgetFragment();
                } else {
                    intent = new Intent(this, BudgetsActivity.class);
                }
                break;
            case 5:
                if (this instanceof LoanDebtActivity) {
                    mFragment = new LoanDebtActivity.LoanDebtFragment();
                } else {
                    intent = new Intent(this, LoanDebtActivity.class);
                }
                break;
            case 6:
//                if (this instanceof FamilySharingActivity) {
//                    mFragment = new FamilySharingActivity.FamilySharingFragment();
//                } else {
//                    intent = new Intent(this, FamilySharingActivity.class);
//                }
                break;
            case 7:
                if (this instanceof PieChartActivity) {
                    mFragment = new PieChartActivity.PieChartFragment();
                } else {
                    intent = new Intent(this, PieChartActivity.class);
                }
                break;
            case 8:
                if (this instanceof SettingsActivity) {
                    mFragment = new SettingsActivity.SettingsFragment();
                } else {
                    intent = new Intent(this, SettingsActivity.class);
                }
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
        Toast.makeText(this, "You pressed Settings", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickFooterItemNavigation(View view) {
        Toast.makeText(this, "You pressed UserPhoto", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickUserPhotoNavigation(View view) {

    }
}
