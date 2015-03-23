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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
* Created by Gaurav on 19-Mar-15.
*/
public class AddCategoryActivity extends ActionBarActivity
{
    EditText mCat_Name;
    ImageView mCat_image;
    RadioGroup mCat_rg_ie,mCat_rg_type;
    RadioButton mCat_Income,mCat_Expense,mCat_Main,mCat_Sub;
    Spinner mCat_Spinner_Sub;

    String c_IE_type;

    DBHelper dbHelper;
    CategoriesAdapter ad;

    ArrayList<CategoryDB> al_main;
    ArrayList<SubCategoryDB> al_sub;

    int flag,c_id,c_u_id;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        mCat_Name=(EditText)findViewById(R.id.add_category_name);
        mCat_image=(ImageView)findViewById(R.id.add_category_icon);
        mCat_rg_ie=(RadioGroup)findViewById(R.id.add_category_ie_type);
        mCat_rg_type=(RadioGroup)findViewById(R.id.add_category_type);
        mCat_Income=(RadioButton)findViewById(R.id.add_cat_radio_income);
        mCat_Expense=(RadioButton)findViewById(R.id.add_cat_radio_expense);
        mCat_Main=(RadioButton)findViewById(R.id.add_cat_radio_main);
        mCat_Sub=(RadioButton)findViewById(R.id.add_cat_radio_sub);
        mCat_Spinner_Sub=(Spinner)findViewById(R.id.spinner_category);

        flag=0;
        i=0;

        mCat_Spinner_Sub.setEnabled(false);

        dbHelper =new DBHelper(AddCategoryActivity.this);
        al_main =new ArrayList<>();
        al_sub =new ArrayList<>();

        if(getIntent().getStringExtra("c_name")!=null)
        {
            flag=1;
            c_id=getIntent().getIntExtra("c_id",0);
            c_u_id=getIntent().getIntExtra("c_u_id",0);
            mCat_Name.setText(getIntent().getStringExtra("c_name"));
            c_IE_type = getIntent().getStringExtra("c_type");
            int id = getResources().getIdentifier("user_48", "drawable", getPackageName());
            mCat_image.setImageResource(id);
            if(c_IE_type.equals("Income"))
            {
                mCat_Income.setChecked(true);
                mCat_Expense.setChecked(false);
            }
            else if(c_IE_type.equals("Expense"))
            {
                mCat_Expense.setChecked(true);
                mCat_Income.setChecked(false);
            }
        }
    }

    public  void onSaveCategory(View v)
    {
        SharedPreferences sp = getSharedPreferences("USER_PREFS",MODE_PRIVATE);

        if (mCat_Name.length() < 0)
        {
            mCat_Name.setError("Enter Category name");
        }

        int id = getResources().getIdentifier("user_48", "drawable", getPackageName());
        mCat_image.setImageResource(id);

        if (mCat_rg_ie.getCheckedRadioButtonId() == mCat_Income.getId())
        {
            c_IE_type="Income";
        }
        else if (mCat_rg_ie.getCheckedRadioButtonId() == mCat_Expense.getId())
        {
            c_IE_type="Expense";
        }

        if(mCat_rg_type.getCheckedRadioButtonId()== mCat_Main.getId())
        {
            if(dbHelper.addCategory(sp.getInt("UID",0),mCat_Name.getText().toString(),c_IE_type,mCat_image.toString()))
            {
                Toast.makeText(AddCategoryActivity.this,"Main Category Added",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(AddCategoryActivity.this,CategoriesActivity.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(AddCategoryActivity.this,"Error creating category",Toast.LENGTH_SHORT).show();
            }
        }
        else if(mCat_rg_type.getCheckedRadioButtonId()== mCat_Sub.getId())
        {
            mCat_Spinner_Sub.setEnabled(true);

            dbHelper =new DBHelper(AddCategoryActivity.this);
            al_main= dbHelper.getAllCategories(c_IE_type);
            ad=new CategoriesAdapter(AddCategoryActivity.this,R.layout.list_category,al_main);
            mCat_Spinner_Sub.setAdapter(ad);

            int colId= dbHelper.getCategoryColId(mCat_Spinner_Sub.getSelectedItem().toString());
            if(dbHelper.addSubCategory(colId,mCat_Name.getText().toString(),mCat_image.toString()))
            {
                Toast.makeText(AddCategoryActivity.this,"Sub Category Added",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(AddCategoryActivity.this,CategoriesActivity.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(AddCategoryActivity.this,"Error creating category",Toast.LENGTH_SHORT).show();
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
