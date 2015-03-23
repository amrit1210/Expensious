package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
