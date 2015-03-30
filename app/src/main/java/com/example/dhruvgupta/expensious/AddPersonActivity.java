package com.example.dhruvgupta.expensious;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Amrit on 3/19/2015.
 */
public class AddPersonActivity  extends ActionBarActivity
{
    CircularImageView mPerson_Color;
    EditText mPerson_Name;
    String color = "";
    AlertDialog.Builder builder;
    AlertDialog ad;
    DBHelper dbHelper;

    SharedPreferences sp;

    int flag,p_id,u_id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        mPerson_Color =(CircularImageView)findViewById(R.id.add_person_icon);
        mPerson_Name =(EditText)findViewById(R.id.add_person_name);

        dbHelper =new DBHelper(AddPersonActivity.this);
        sp = getSharedPreferences("USER_PREFS",MODE_PRIVATE);

        flag=0;

        if(getIntent().getStringExtra("p_name")!=null)
        {
            flag=1;
            p_id=getIntent().getIntExtra("p_id",0);
            u_id=getIntent().getIntExtra("p_uid",0);
            mPerson_Name.setText(getIntent().getStringExtra("p_name"));
            byte[] decodedString = Base64.decode(getIntent().getStringExtra("p_color").trim(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            mPerson_Color.setImageBitmap(decodedByte);
        }
    }

    public void onPersonIcon(View v)
    {
        builder = new AlertDialog.Builder(AddPersonActivity.this);

        LayoutInflater inflater = getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_person, null));

        ad = builder.show();
    }

    public void onDialogPerson(View v) throws IOException {
        int id = v.getId();
        Uri fileUri= Uri.parse("");
        if (id == R.id.amber_yellow)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.amber_yellow);
        else if (id == R.id.blood_red)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.blood_red);
        else if (id == R.id.blue)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.blue);
        else if (id == R.id.blue_grey)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.blue_grey);
        else if (id == R.id.brown)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.brown);
        else if (id == R.id.brownie)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.brownie);
        else if (id == R.id.cyan)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.cyan);
        else if (id == R.id.dark_green)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.dark_green);
        else if (id == R.id.deep_purple)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.deep_purple);
        else if (id == R.id.green)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.green);
        else if (id == R.id.grey)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
        else if (id == R.id.indigo)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.indigo);
        else if (id == R.id.light_blue)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.light_blue);
        else if (id == R.id.light_green)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.light_green);
        else if (id == R.id.lime)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.lime);
        else if (id == R.id.orange)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.orange);
        else if (id == R.id.pink)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.pink);
        else if (id == R.id.purple)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.purple);
        else if (id == R.id.red)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.red);
        else if (id == R.id.teal)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.teal);
        else if (id == R.id.yellow)
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.yellow);

//        Bitmap decodedByte = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
        InputStream image_stream = getContentResolver().openInputStream(fileUri);
        Bitmap decodedByte= BitmapFactory.decodeStream(image_stream );

        //mPerson_Color.setImageURI(fileUri);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        color = Base64.encodeToString(b, Base64.DEFAULT);

        byte[] decodedString = Base64.decode(color.trim(), Base64.DEFAULT);
        Bitmap decodedByt = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        mPerson_Color.setBorderColor(getResources().getColor(R.color.accent_color_200));
        mPerson_Color.setBorderWidth(10);
        mPerson_Color.setSelectorColor(getResources().getColor(R.color.main_color_grey_500));
        mPerson_Color.setSelectorStrokeColor(getResources().getColor(R.color.main_color_indigo_200));
        mPerson_Color.setSelectorStrokeWidth(10);
        mPerson_Color.setImageBitmap(decodedByt);

        ad.dismiss();
    }

    public void onSavePerson(View v)
    {
        if (mPerson_Name.length() < 0)
        {
            mPerson_Name.setError("Enter Person name");
        }
        if(mPerson_Name.getError() == null )
        {
            if(flag==1)
            {
                if(dbHelper.updatePersonData(p_id,mPerson_Name.getText().toString(),color))
                {
                    Toast.makeText(AddPersonActivity.this, "Person Added", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddPersonActivity.this, PersonsActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(AddPersonActivity.this, "Error adding Person", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddPersonActivity.this, PersonsActivity.class);
                    startActivity(intent);
                }
            }
            else
            {
                if(dbHelper.addPerson(mPerson_Name.getText().toString(),color,sp.getInt("UID",0)))
                {
                    Toast.makeText(AddPersonActivity.this, "Person Updated", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddPersonActivity.this, PersonsActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(AddPersonActivity.this, "Error Updating Person", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddPersonActivity.this, PersonsActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
