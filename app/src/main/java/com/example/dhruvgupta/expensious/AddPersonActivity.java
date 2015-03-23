package com.example.dhruvgupta.expensious;

import android.content.Intent;
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
    ImageView mImgColor;
    EditText mPerson;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        mImgColor=(ImageView)findViewById(R.id.add_person_icon);
        mPerson=(EditText)findViewById(R.id.add_person_name);
        dbHelper =new DBHelper(AddPersonActivity.this);
        int color_id = getResources().getIdentifier("user_48", "drawable", getPackageName());
        mImgColor.setBackgroundColor(color_id);
    }

    public void onSavePerson(View v)
    {
        if(mPerson.length()>0 )
        {
            dbHelper.addPerson(mPerson.getText().toString(), mImgColor.getBackground().toString());
            dbHelper.getAllPerson();
            Toast.makeText(AddPersonActivity.this,"Person Added",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(AddPersonActivity.this,PersonActivity.class);
            startActivity(intent);
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
