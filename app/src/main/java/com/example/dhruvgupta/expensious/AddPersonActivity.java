package com.example.dhruvgupta.expensious;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
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

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.pkmmte.view.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Amrit on 3/19/2015.
 */
public class AddPersonActivity  extends ActionBarActivity
{
    CircularImageView mPerson_Color;
    EditText mPerson_Name;
    String color = "";
    String colorCode = "";
    String name = "";
    AlertDialog.Builder builder;
    AlertDialog ad;
    Uri fileUri;
    byte[] b;
    DBHelper dbHelper;

    SharedPreferences sp;

    int flag,p_id,u_id,b_flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        this.getSupportActionBar().setTitle("Add Person");
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
            name = mPerson_Name.getText().toString();
            color = getIntent().getStringExtra("p_color");
            colorCode = getIntent().getStringExtra("p_color_code");
            byte[] decodedString = Base64.decode(color.trim(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            mPerson_Color.setImageBitmap(decodedByte);
        }
        else
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.amber_yellow);
            colorCode = "#ffc200";
            mPerson_Color.setImageURI(fileUri);

            InputStream image_stream = null;
            try {
                image_stream = getContentResolver().openInputStream(fileUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap decodedByte= BitmapFactory.decodeStream(image_stream );
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
            b = baos.toByteArray();
            color = Base64.encodeToString(b, Base64.DEFAULT);
        }
    }

    public void onPersonIcon(View v)
    {
        try
        {
            if (b_flag == 0)
            {
                Intent dialog = new Intent(AddPersonActivity.this, PersonDialog.class);
                startActivityForResult(dialog, 1);
                b_flag=1;
            }
        }
        catch (Exception e){
            Log.i("Exception:AddPersonImg", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 1)
        {
            fileUri= Uri.parse(data.getExtras().getString("Uri"));
            colorCode = data.getExtras().getString("ColorCode");

            try{
                InputStream image_stream = getContentResolver().openInputStream(fileUri);
                Bitmap decodedByte= BitmapFactory.decodeStream(image_stream );

                mPerson_Color.setImageURI(fileUri);
//        mPerson_Color.setBackgroundColor(Color.parseColor(colorCode));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                b = baos.toByteArray();
                color = Base64.encodeToString(b, Base64.DEFAULT);
            }
            catch (Exception e){
                Log.i("Excep:AddPersonSetImg",e.getMessage());
            }
        }
        b_flag = 0;
    }

    public void onSavePerson()
    {
        ArrayList arrayList = dbHelper.getPersonColName(sp.getInt("UID",0));

        if (mPerson_Name.length() <= 0)
        {
            mPerson_Name.setError("Enter Person Name");
        }
        else if (!mPerson_Name.getText().toString().matches("[a-zA-Z][a-zA-Z ]+"))
        {
            mPerson_Name.setError("Enter valid Person Name");
        }
        else
        {
            mPerson_Name.setError(null);
        }

        if(mPerson_Name.getError() == null )
        {
            if(flag==1)
            {
                if (arrayList.contains(mPerson_Name.getText().toString()))
                {
                    if (mPerson_Name.getText().toString().equals(name))
                    {
                        mPerson_Name.setError(null);
                    }
                    else
                    {
                        mPerson_Name.setError("Person Already Exists");
                    }
                }
                if (mPerson_Name.getError() == null)
                {
                    if(dbHelper.updatePersonData(p_id,mPerson_Name.getText().toString(),color,colorCode, sp.getInt("UID", 0)))
                    {
                        Toast.makeText(AddPersonActivity.this, "Person Updated", Toast.LENGTH_LONG).show();

                        ParseObject person = new ParseObject("Persons");
                        person.put("p_id", p_id);
                        person.put("p_uid", sp.getInt("UID",0));
                        person.put("p_name", mPerson_Name.getText().toString());
                        person.put("p_color", color);
                        person.put("p_color_code", colorCode);
                        person.pinInBackground("pinPersonsUpdate");

                        Intent intent = new Intent();
                        this.finish();
                    }
                    else
                    {
                        Toast.makeText(AddPersonActivity.this, "Error Updating Person", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        this.finish();
                    }
                }

            }
            else
            {
                if(arrayList.contains(mPerson_Name.getText().toString()))
                {
                    mPerson_Name.setError("Person already exists");
                }
                else
                {
                    mPerson_Name.setError(null);

                    if(dbHelper.addPerson(mPerson_Name.getText().toString(),color,colorCode,sp.getInt("UID",0)))
                    {
                        Toast.makeText(AddPersonActivity.this, "Person Added", Toast.LENGTH_LONG).show();

                        final int pid = dbHelper.getPersonColId(sp.getInt("UID", 0), mPerson_Name.getText().toString());

                        ParseObject person = new ParseObject("Persons");
                        person.put("p_id", pid);
                        person.put("p_uid", sp.getInt("UID",0));
                        person.put("p_name", mPerson_Name.getText().toString());
                        person.put("p_color", color);
                        person.put("p_color_code", colorCode);
                        person.pinInBackground("pinPersons");
                        person.saveEventually(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.i("Person saveEventually", "YES! YES! YES!");
                            }
                        });

                        Intent intent = new Intent();
                        this.finish();
                    }
                    else
                    {
                        Toast.makeText(AddPersonActivity.this, "Error Adding Person", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        this.finish();
                    }
                }
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        Drawable saveBtn = new IconDrawable(this, Iconify.IconValue.fa_check_circle_o)
                .colorRes(R.color.accent_color_200)
                .actionBarSize();
        menu.findItem(R.id.action_done).setIcon(saveBtn);

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
        if (id == R.id.action_done)
        {
            onSavePerson();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
