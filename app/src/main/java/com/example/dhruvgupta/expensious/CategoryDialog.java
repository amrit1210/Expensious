package com.example.dhruvgupta.expensious;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import java.io.IOException;

/**
 * Created by dhruvgupta on 4/3/2015.
 */
public class CategoryDialog extends Activity {

    Uri fileUri;

    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_category);
    }

    public void onDialogCategory(View v) throws IOException {
        int id = v.getId();
        fileUri= Uri.parse("");
        if (id == R.id.annuity)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.annuity);
        }
        else if (id == R.id.bill)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.bill);
        }
        else if (id == R.id.book)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.book);
        }
        else if (id == R.id.capital)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.capital);
        }
        else if (id == R.id.cloth)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.cloth);
        }
        else if (id == R.id.education)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.education);
        }
        else if (id == R.id.electronics)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.electronics);
        }
        else if (id == R.id.entertainment)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.entertainment);
        }
        else if (id == R.id.food)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.food);
        }
        else if (id == R.id.gift)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.gift);
        }
        else if (id == R.id.grocery)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grocery);
        }
        else if (id == R.id.house)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.house);
        }
        else if (id == R.id.medical)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.medical);
        }
        else if (id == R.id.personal)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.personal);
        }
        else if (id == R.id.travel)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.travel);
        }

        Intent i = new Intent();
        i.putExtra("Uri",fileUri+"");
        setResult(1, i);
        this.finish();
    }

}

