package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    RadioGroup mCat_rg_ie_type,mCat_rg_type;
    RadioButton mCat_Income,mCat_Expense,mCat_Main,mCat_Sub;
    Spinner mCat_Spinner_Sub;
    SharedPreferences sp;

    String name= "",c_name=null;
    int c_IE_type, colId=0;

    DBHelper dbHelper;
    CategoriesAdapter ad;

    ArrayList<CategoryDB_Specific> categoryDBSpecificArrayList;
    String col=null,sub_name=null;
    int flag,c_id,c_u_id,sub_id=0;
    int i,c_type=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        mCat_Name=(EditText)findViewById(R.id.add_category_name);
        mCat_image=(ImageView)findViewById(R.id.add_category_icon);
        mCat_rg_ie_type =(RadioGroup)findViewById(R.id.add_category_ie_type);
        mCat_rg_type=(RadioGroup)findViewById(R.id.add_category_type);
        mCat_Income=(RadioButton)findViewById(R.id.add_cat_radio_income);
        mCat_Expense=(RadioButton)findViewById(R.id.add_cat_radio_expense);
        mCat_Main=(RadioButton)findViewById(R.id.add_cat_radio_main);
        mCat_Sub=(RadioButton)findViewById(R.id.add_cat_radio_sub);
        mCat_Spinner_Sub=(Spinner)findViewById(R.id.spinner_category);

        sp = getSharedPreferences("USER_PREFS",MODE_PRIVATE);
        mCat_Spinner_Sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                col = (String) parent.getItemAtPosition(position);
                Log.i("COL:",col+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                System.out.println("Nothing Selected");
            }
        });
        flag=0;
        i=0;

        dbHelper =new DBHelper(AddCategoryActivity.this);
        categoryDBSpecificArrayList =new ArrayList<>();

        int id = getResources().getIdentifier("user_48", "drawable", getPackageName());
        mCat_image.setImageResource(id);

        mCat_rg_ie_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if(group.getCheckedRadioButtonId()==mCat_Expense.getId())
                {
                    c_IE_type=0;
                    ArrayList arrayList = dbHelper.getCategoryColName(sp.getInt("UID",0),c_IE_type);
                    Log.i("ArrayList :",arrayList+"");
                    try
                    {
                        ArrayAdapter arrayAdapter=new ArrayAdapter(AddCategoryActivity.this,android.R.layout.simple_spinner_item, arrayList);
                        mCat_Spinner_Sub.setAdapter(arrayAdapter);
                    }
                    catch (Exception e)
                    {
                        Log.i("Error:",e.getMessage());
                    }
                }
                else if(group.getCheckedRadioButtonId()==mCat_Income.getId())
                {
                    c_IE_type=1;
                    ArrayList arrayList = dbHelper.getCategoryColName(sp.getInt("UID",0),c_IE_type);
                    Log.i("ArrayList :",arrayList+"");
                    try
                    {
                        ArrayAdapter arrayAdapter=new ArrayAdapter(AddCategoryActivity.this,android.R.layout.simple_spinner_item, arrayList);
                        mCat_Spinner_Sub.setAdapter(arrayAdapter);
                    }
                    catch (Exception e)
                    {
                        Log.i("Error:",e.getMessage());
                    }
                }
            }
        });
        mCat_rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (group.getCheckedRadioButtonId() == mCat_Main.getId())
                {
                    mCat_Spinner_Sub.setEnabled(false);
                    mCat_image.setVisibility(View.VISIBLE);
                }
                else if (group.getCheckedRadioButtonId() == mCat_Sub.getId())
                {
                    mCat_Spinner_Sub.setEnabled(true);
                    mCat_image.setVisibility(View.GONE);
                    if (mCat_rg_ie_type.getCheckedRadioButtonId() == mCat_Income.getId())
                    {
                        c_IE_type = 1;
                        ArrayList arrayList = dbHelper.getCategoryColName(sp.getInt("UID", 0), c_IE_type);
                        Log.i("ArrayList :", arrayList + "");
                        ArrayAdapter arrayAdapter = new ArrayAdapter(AddCategoryActivity.this, android.R.layout.simple_spinner_item, arrayList);
                        mCat_Spinner_Sub.setAdapter(arrayAdapter);
                    }
                    else if (mCat_rg_ie_type.getCheckedRadioButtonId() == mCat_Expense.getId())
                    {
                        c_IE_type = 0;
                        ArrayList arrayList = dbHelper.getCategoryColName(sp.getInt("UID", 0), c_IE_type);
                        Log.i("ArrayList :", arrayList + "");
                        try
                        {
                            ArrayAdapter arrayAdapter = new ArrayAdapter(AddCategoryActivity.this, android.R.layout.simple_spinner_item, arrayList);
                            mCat_Spinner_Sub.setAdapter(arrayAdapter);
                        }
                        catch (Exception e)
                        {
                            Log.i("Error:", e.getMessage());
                        }
                    }
                }
            }
        });

        if(getIntent().getIntExtra("cat_id",0)>0)
        {
            flag=1;
            c_id=getIntent().getIntExtra("cat_id",0);
            c_u_id=getIntent().getIntExtra("c_u_id",0);
            c_name=getIntent().getStringExtra("c_name");
            if(c_name!=null)
            {
                mCat_Name.setText(c_name);
            }
            //c_IE_type=getIntent().getIntExtra("c_type");
            name=mCat_Name.getText().toString();

            int id1 = getResources().getIdentifier("user_48", "drawable", getPackageName());
            mCat_image.setImageResource(id1);
            c_type=getIntent().getIntExtra("c_type",0);
            if(c_type==1)
            {
                mCat_Income.setChecked(true);
                c_IE_type=1;
            }
            else
            {
                mCat_Expense.setChecked(true);
                c_IE_type=0;
            }
            sub_name=getIntent().getStringExtra("sub_name");
            if(sub_name != null)
            {
                mCat_Name.setText(sub_name);
                mCat_Sub.setChecked(true);

            }
            else
            {
                mCat_Main.setChecked(true);
            }
            sub_id=getIntent().getIntExtra("sub_id",0);
            if (sub_id != 0) {
                mCat_Spinner_Sub.setSelection(sub_id);
            }
        }
    }

    public  void onSaveCategory(View v)
    {
        SharedPreferences sp = getSharedPreferences("USER_PREFS",MODE_PRIVATE);
        if (mCat_Name.length() <= 0)
        {
            mCat_Name.setError("Enter Category name");
        }
        else
        {
            mCat_Name.setError(null);
        }
        if (mCat_rg_ie_type.getCheckedRadioButtonId() == mCat_Income.getId())
        {
            c_IE_type=1;
        }
        else if (mCat_rg_ie_type.getCheckedRadioButtonId() == mCat_Expense.getId())
        {
            c_IE_type=0;
        }

        if(mCat_Name.getError()== null)
        {
            ArrayList arrayList = dbHelper.getCategoryColName(sp.getInt("UID",0),c_IE_type);
            if(flag==1)
            {
                if (arrayList.contains(mCat_Name.getText().toString()))
                {
                    if (mCat_Name.getText().toString().equals(name))
                    {
                        mCat_Name.setError(null);
                    }
                    else
                    {
                        mCat_Name.setError("Category Already Exists");
                    }
                }
                if(mCat_Name.getError()==null)
                {
                    if(mCat_rg_type.getCheckedRadioButtonId()== mCat_Main.getId())
                    {
                        if (dbHelper.updateCategory(c_u_id,c_id, mCat_Name.getText().toString(), c_IE_type, "Image"))
                        {
                            Log.i("Category 1", mCat_Name.getText().toString() + c_IE_type + mCat_image.toString());
                            Toast.makeText(AddCategoryActivity.this, "Category Updated " + c_IE_type, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AddCategoryActivity.this, CategoriesActivity.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(AddCategoryActivity.this, "Error updating category", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(mCat_rg_type.getCheckedRadioButtonId()== mCat_Sub.getId())
                    {
                        dbHelper =new DBHelper(AddCategoryActivity.this);
                        colId = dbHelper.getCategoryColId(col);
                        Log.i("COLID SUBCATEGORY:",colId+"");
                        if(colId>0)
                        {
                            if(dbHelper.updateSubCategory(sub_id,mCat_Name.getText().toString(),sp.getInt("UID",0)))
                            {
                                Toast.makeText(AddCategoryActivity.this,"Sub Category Updated",Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(AddCategoryActivity.this,CategoriesActivity.class);
                                startActivity(i);
                            }
                        }
                        else
                        {
                            Log.i("COLID SUBCATEGORY:",colId+"");
                            Toast.makeText(AddCategoryActivity.this,"Error updating subcategory",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            else
            {
                if(mCat_rg_type.getCheckedRadioButtonId()== mCat_Main.getId())
                {
                    if (dbHelper.addCategorySpecific(sp.getInt("UID", 0), mCat_Name.getText().toString(), c_IE_type, "Image"))
                    {
                        Log.i("Category 1", mCat_Name.getText().toString() + c_IE_type + mCat_image.toString());
                        Toast.makeText(AddCategoryActivity.this, "Main Category Added " + c_IE_type, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AddCategoryActivity.this, CategoriesActivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(AddCategoryActivity.this, "Error creating category", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(mCat_rg_type.getCheckedRadioButtonId()== mCat_Sub.getId())
                {
                    dbHelper =new DBHelper(AddCategoryActivity.this);

                    if(!col.equals(null))
                    {
                        colId = dbHelper.getCategoryColId(col);
                        if(dbHelper.addSubCategory(colId,mCat_Name.getText().toString(),sp.getInt("UID",0)))
                        {
                            dbHelper.getAllSubCategories(sp.getInt("UID",0),colId);
                            Toast.makeText(AddCategoryActivity.this,"Sub Category Added",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(AddCategoryActivity.this,CategoriesActivity.class);
                            startActivity(i);
                        }
                    }
                    else
                    {
                        Toast.makeText(AddCategoryActivity.this,"Error creating subcategory",Toast.LENGTH_SHORT).show();
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
