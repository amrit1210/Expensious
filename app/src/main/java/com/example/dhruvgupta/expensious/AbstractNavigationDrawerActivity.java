package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import br.liveo.interfaces.NavigationLiveoListener;
import br.liveo.navigationliveo.NavigationLiveo;

/**
 * Created by Amrit on 4/23/2015.
 */
public class AbstractNavigationDrawerActivity extends NavigationLiveo implements NavigationLiveoListener {

    Bitmap decodedByte = null;
    @Override
    public void onUserInformation() {
        SharedPreferences sp =getSharedPreferences("USER_PREFS",MODE_PRIVATE);
//        SharedPreferences sp1 =getSharedPreferences("USER_IMAGE",MODE_PRIVATE);
//
//        byte [] b = sp1.getString("UIMAGE", "").getBytes();
//        Log.i("b:",b+"");
//        BitmapFactory.Options bmo= new BitmapFactory.Options();
//        bmo.inPreferredConfig= Bitmap.Config.ARGB_8888;
//         decodedByte = BitmapFactory.decodeByteArray(b, 0, b.length,bmo);
//        Log.i("decoded byte:",decodedByte+"");

//        LoadImageFromURL load = new LoadImageFromURL();
//        load.execute();

        this.mUserName.setText(sp.getString("USERNAME","abc"));
        this.mUserEmail.setText(sp.getString("EMAIL","abc@xyz.com"));
        this.mUserPhoto.setImageURI(Uri.parse(sp.getString("UIMAGE", "")));
//        this.mUserPhoto.setImageBitmap(decodedByte);
//        Log.i("absImage", b+"");
//        this.mUserPhotodd.setImageResource(R.drawable.user_48);
        this.mUserBackground.setImageResource(R.drawable.background_small);

    }

    private class LoadImageFromURL extends AsyncTask<String, Void, Bitmap> {
//        ImageView bitmapImgView;
//        public LoadImageFromURL(ImageView bmImgView){
//            bitmapImgView = bmImgView;
//        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            SharedPreferences sp = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
            String urlStr = sp.getString("UIMAGE", "");
            Bitmap img = null;

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlStr);
            HttpResponse response;
            try {
                response = (HttpResponse)client.execute(request);
                HttpEntity entity = response.getEntity();
                BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(entity);
                InputStream inputStream = bufferedEntity.getContent();
                img = BitmapFactory.decodeStream(inputStream);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            decodedByte = img;
            return img;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap){
//            bitmapImgView.setImageBitmap(bitmap);
        }
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
//        Toast.makeText(this, "You pressed Settings", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickFooterItemNavigation(View view) {
//        Toast.makeText(this, "You pressed UserPhoto", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickUserPhotoNavigation(View view) {

    }
}