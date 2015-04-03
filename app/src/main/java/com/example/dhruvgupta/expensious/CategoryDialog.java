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

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_person);
    }

    public void onDialogPerson(View v) throws IOException {
        int id = v.getId();
        fileUri= Uri.parse("");
        if (id == R.id.amber_yellow)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.amber_yellow);
        }
        else if (id == R.id.blood_red)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.blood_red);
        }
        else if (id == R.id.blue)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.blue);
        }
        else if (id == R.id.blue_grey)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.blue_grey);
        }
        else if (id == R.id.brown)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.brown);
        }
        else if (id == R.id.brownie)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.brownie);
        }
        else if (id == R.id.cyan)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.cyan);
        }
        else if (id == R.id.dark_green)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.dark_green);
        }
        else if (id == R.id.deep_purple)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.deep_purple);
        }
        else if (id == R.id.green)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.green);
        }
        else if (id == R.id.grey)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
        }
        else if (id == R.id.indigo)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.indigo);
        }
        else if (id == R.id.light_blue)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.light_blue);
        }
        else if (id == R.id.light_green)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.light_green);
        }
        else if (id == R.id.lime)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.lime);
        }
        else if (id == R.id.orange)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.orange);
        }
        else if (id == R.id.pink)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.pink);
        }
        else if (id == R.id.purple)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.purple);
        }
        else if (id == R.id.red)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.red);
        }
        else if (id == R.id.teal)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.teal);
        }
        else if (id == R.id.yellow)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.yellow);
        }

        Intent i = new Intent();
        i.putExtra("Uri",fileUri+"");
        setResult(1, i);
        this.finish();
    }

}

