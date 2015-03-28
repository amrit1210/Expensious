package com.example.dhruvgupta.expensious;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

/**
 * Created by Amrit on 3/19/2015.
 */
public class AddPersonActivity  extends ActionBarActivity
{
    ImageView mPerson_Color;
    EditText mPerson_Name;

    DBHelper dbHelper;

    SharedPreferences sp;

    int flag,p_id,u_id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        mPerson_Color =(ImageView)findViewById(R.id.add_person_icon);
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
            int color_id = getResources().getIdentifier("user_48", "drawable", getPackageName());
            mPerson_Color.setBackgroundColor(color_id);
        }
    }

    public void onPersonIcon(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPersonActivity.this);

        LayoutInflater inflater = getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_person, null));

//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                LayoutInflater inflater = getLayoutInflater();
//                View layout = inflater.inflate(R.layout.custom_toast,
//                        (ViewGroup) findViewById(R.id.toast_layout_root));
//                SharedPreferences sharedPreferences = getSharedPreferences("MY_PREFS",MODE_PRIVATE);
//                String s=sharedPreferences.getString("REG_NO","1110");
//                s= s+sharedPreferences.getString("NAME","ABC");
//
//                TextView text = (TextView) layout.findViewById(R.id.text);
//                text.setText(s);
//
//                Toast toast = new Toast(MainActivity.this);
//                toast.setGravity(Gravity.CENTER_VERTICAL, 100, -100);
//                toast.setDuration(Toast.LENGTH_SHORT);
//                toast.setView(layout);
//                toast.show();
//            }
//        });
//        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//            }
//        });
        builder.create();
        builder.show();
    }

    public void onDialogPerson(View v)
    {
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

        Toast.makeText(AddPersonActivity.this,"click working fine"+ v.getResources(), Toast.LENGTH_LONG).show();
        mPerson_Color.setImageURI(fileUri);
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
                if(dbHelper.updatePersonData(p_id,mPerson_Name.getText().toString(),mPerson_Color.toString()))
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
                if(dbHelper.addPerson(mPerson_Name.getText().toString(),mPerson_Color.toString(),sp.getInt("UID",0)))
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
