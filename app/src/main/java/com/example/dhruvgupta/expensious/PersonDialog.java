package com.example.dhruvgupta.expensious;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dhruvgupta on 4/1/2015.
 */
public class PersonDialog extends Activity {

    Uri fileUri;
    String colorCode;

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
            colorCode = "#ffc200";
        }
        else if (id == R.id.blood_red)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.blood_red);
            colorCode = "#980000";
        }
        else if (id == R.id.blue)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.blue);
            colorCode = "#0000ff";
        }
        else if (id == R.id.blue_grey)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.blue_grey);
            colorCode = "#a2a9af";
        }
        else if (id == R.id.brown)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.brown);
            colorCode = "#a52929";
        }
        else if (id == R.id.brownie)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.brownie);
            colorCode = "#3c241a";
        }
        else if (id == R.id.cyan)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.cyan);
            colorCode = "#00ffff";
        }
        else if (id == R.id.dark_green)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.dark_green);
            colorCode = "#006400";
        }
        else if (id == R.id.deep_purple)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.deep_purple);
            colorCode = "#663299";
        }
        else if (id == R.id.green)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.green);
            colorCode = "#00ff00";
        }
        else if (id == R.id.grey)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
            colorCode = "#a9a9a9";
        }
        else if (id == R.id.indigo)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.indigo);
            colorCode = "#4a0082";
        }
        else if (id == R.id.light_blue)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.light_blue);
            colorCode = "#add8e6";
        }
        else if (id == R.id.light_green)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.light_green);
            colorCode = "#90ee90";
        }
        else if (id == R.id.lime)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.lime);
            colorCode = "#9efe37";
        }
        else if (id == R.id.orange)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.orange);
            colorCode = "#ff8000";
        }
        else if (id == R.id.pink)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.pink);
            colorCode = "#ffc0cb";
        }
        else if (id == R.id.purple)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.purple);
            colorCode = "#800080";
        }
        else if (id == R.id.red)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.red);
            colorCode = "#ff0000";
        }
        else if (id == R.id.teal)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.teal);
            colorCode = "#5f8a8b";
        }
        else if (id == R.id.yellow)
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.yellow);
            colorCode = "#ffff00";
        }

        Intent i = new Intent();
        i.putExtra("Uri",fileUri+"");
        i.putExtra("ColorCode",colorCode);
        setResult(1, i);
        this.finish();
    }

}
