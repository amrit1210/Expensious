package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
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

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.pkmmte.view.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
* Created by Gaurav on 19-Mar-15.
*/
public class AddCategoryActivity extends ActionBarActivity
{
    EditText mCat_Name;
    CircularImageView mCat_image;
    RadioGroup mCat_rg_ie_type,mCat_rg_type;
    RadioButton mCat_Income,mCat_Expense,mCat_Main,mCat_Sub;
    Spinner mCat_Spinner_Sub;
    SharedPreferences sp;

    String name= "",c_name="",c_img_string=null;
    int c_IE_type, colId=0;
    Uri fileUri;
    byte[] b;

    DBHelper dbHelper;
    CategoriesAdapter ad;
	ArrayList arrayList;

    ArrayList<CategoryDB_Specific> categoryDBSpecificArrayList;
    String col=null,sub_name=null;
    int flag,c_id,c_u_id,sub_id=0,b_flag=0;
    int i,c_type=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        mCat_Name=(EditText)findViewById(R.id.add_category_name);
        mCat_image=(CircularImageView)findViewById(R.id.add_category_icon);
        mCat_rg_ie_type =(RadioGroup)findViewById(R.id.add_category_ie_type);
        mCat_rg_type=(RadioGroup)findViewById(R.id.add_category_type);
        mCat_Income=(RadioButton)findViewById(R.id.add_cat_radio_income);
        mCat_Expense=(RadioButton)findViewById(R.id.add_cat_radio_expense);
        mCat_Main=(RadioButton)findViewById(R.id.add_cat_radio_main);
        mCat_Sub=(RadioButton)findViewById(R.id.add_cat_radio_sub);
        mCat_Spinner_Sub=(Spinner)findViewById(R.id.spinner_category);
        c_img_string=null;

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

        mCat_rg_ie_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if(group.getCheckedRadioButtonId()==mCat_Expense.getId())
                {
                    c_IE_type=0;
                    arrayList = dbHelper.getCategoryColName(sp.getInt("UID",0),c_IE_type);
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
                    arrayList = dbHelper.getCategoryColName(sp.getInt("UID",0),c_IE_type);
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
                        arrayList = dbHelper.getCategoryColName(sp.getInt("UID", 0), c_IE_type);
                        Log.i("ArrayList @ 1:", arrayList + "");
                        ArrayAdapter arrayAdapter = new ArrayAdapter(AddCategoryActivity.this, android.R.layout.simple_spinner_item, arrayList);
                        mCat_Spinner_Sub.setAdapter(arrayAdapter);
                    }
                    else if (mCat_rg_ie_type.getCheckedRadioButtonId() == mCat_Expense.getId())
                    {
                        c_IE_type = 0;
                        arrayList = dbHelper.getCategoryColName(sp.getInt("UID", 0), c_IE_type);
                        Log.i("ArrayList @ 2:", arrayList + "");
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
            //c_name=getIntent().getStringExtra("c_name");
            mCat_Name.setText(getIntent().getStringExtra("c_name"));
            c_name = mCat_Name.getText().toString();
            c_img_string= getIntent().getExtras().getString("c_icon","");
            byte[] decodedString = Base64.decode(c_img_string.trim(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            mCat_image.setImageBitmap(decodedByte);
            if(c_name!=null)
            {
                mCat_Name.setText(c_name);
            }
            //c_IE_type=getIntent().getIntExtra("c_type");
            name=mCat_Name.getText().toString();

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
            if (c_id != 0 && arrayList != null) {
                Log.d("AddCat", "c_name = " + c_name);
                int tmpId = arrayList.indexOf(c_name);
                Log.d("AddCat", "tmpId = " + tmpId);
                mCat_Spinner_Sub.setSelection(tmpId);
            }
        }
        else
        {
            fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.misc);
            mCat_image.setImageURI(fileUri);

            InputStream image_stream = null;
            try
            {
                image_stream = getContentResolver().openInputStream(fileUri);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            Bitmap decodedByte= BitmapFactory.decodeStream(image_stream );
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
            b = baos.toByteArray();
            c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
        }
    }

    public void onCategoryIcon (View v)
    {
        try
        {
            if (b_flag == 0)
            {
                Intent dialog = new Intent(AddCategoryActivity.this, CategoryDialog.class);
                startActivityForResult(dialog, 1);
                b_flag=1;
            }
        }
        catch (Exception e)
        {
            Log.i("Exception:AddPersonImg", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 1)
        {
            fileUri= Uri.parse(data.getExtras().getString("Uri"));

            try
            {
                InputStream image_stream = getContentResolver().openInputStream(fileUri);
                Bitmap decodedByte= BitmapFactory.decodeStream(image_stream );

                mCat_image.setImageURI(fileUri);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                b = baos.toByteArray();
                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
            }
            catch (Exception e)
            {
                Log.i("Excep:AddPersonSetImg",e.getMessage());
            }
        }
        b_flag = 0;
    }

    public  void onSaveCategory()
    {
        sp = getSharedPreferences("USER_PREFS",MODE_PRIVATE);
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
                    if (mCat_Name.getText().toString().equals(c_name))
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
                        if (dbHelper.updateCategory(c_u_id,c_id, mCat_Name.getText().toString(), c_IE_type,c_img_string))
                        {
                            Log.i("Category 1", mCat_Name.getText().toString() + c_IE_type + mCat_image.toString());
                            Toast.makeText(AddCategoryActivity.this, "Category Updated " + c_IE_type, Toast.LENGTH_SHORT).show();
                            ParseObject category = new ParseObject("Category_specific");
                            category.put("c_id", c_id);
                            category.put("c_uid", sp.getInt("UID", 0));
                            category.put("c_name", mCat_Name.getText().toString());
                            category.put("c_type",c_IE_type);
                            category.put("c_icon", c_img_string);
                            category.pinInBackground("pinCategoryUpdate");
                            Intent i = new Intent();
                            this.finish();
                        }
                        else
                        {
                            Toast.makeText(AddCategoryActivity.this, "Error updating category", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent();
                            this.finish();
                        }
                    }
                    else if(mCat_rg_type.getCheckedRadioButtonId()== mCat_Sub.getId())
                    {
                        dbHelper =new DBHelper(AddCategoryActivity.this);
                        colId = dbHelper.getCategoryColId(col);
                        Log.i("COLID SUBCATEGORY:",colId+"");
                        if(colId>0)
                        {
                            if(dbHelper.updateSubCategory(sub_id,colId,mCat_Name.getText().toString(),sp.getInt("UID",0)))
                            {
                                Toast.makeText(AddCategoryActivity.this,"Sub Category Updated",Toast.LENGTH_SHORT).show();
                                ParseObject subcategory = new ParseObject("Sub_category");
                                subcategory.put("sub_id",sub_id);
                                subcategory.put("sub_uid", sp.getInt("UID", 0));
                                subcategory.put("sub_name", mCat_Name.getText().toString());
                                subcategory.put("sub_c_id",colId);
                                subcategory.put("c_icon", c_img_string);
                                subcategory.pinInBackground("pinSubCategoryUpdate");
                                Intent i=new Intent();
                                this.finish();
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
                if(arrayList.contains(mCat_Name.getText().toString()))
                {
                    mCat_Name.setError("Category already exists");
                }
                else
                {
                    mCat_Name.setError(null);
                    if(mCat_rg_type.getCheckedRadioButtonId()== mCat_Main.getId())
                    {
                        if (dbHelper.addCategorySpecific(sp.getInt("UID", 0), mCat_Name.getText().toString(), c_IE_type,c_img_string))
                        {
                            Log.i("Category 1", mCat_Name.getText().toString() + c_IE_type + mCat_image.toString());
                            Toast.makeText(AddCategoryActivity.this, "Main Category Added " + c_IE_type, Toast.LENGTH_SHORT).show();
                            int cid = dbHelper.getCategoryColIduid(sp.getInt("UID", 0));

                            ParseObject category = new ParseObject("Category_specific");
                            category.put("c_id", cid);
                            category.put("c_uid", sp.getInt("UID", 0));
                            category.put("c_name", mCat_Name.getText().toString());
                            category.put("c_type", c_IE_type);
                            category.put("c_icon", c_img_string);
                            category.pinInBackground("pinCategory");
                            category.saveEventually(new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    Log.i("Category saveEventually", "YES! YES! YES!");
                                }
                            });
                            Intent i = new Intent();
                            this.finish();
                        }
                        else
                        {
                            Toast.makeText(AddCategoryActivity.this, "Error creating category", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent();
                            this.finish();
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
                                int sub_id = dbHelper.getSubCategoryColId(sp.getInt("UID", 0));
                                dbHelper.getAllSubCategories(sp.getInt("UID",0),colId);
                                ParseObject subcategory = new ParseObject("Sub_category");
                                subcategory.put("sub_id",sub_id );
                                subcategory.put("sub_uid", sp.getInt("UID", 0));
                                subcategory.put("sub_name", mCat_Name.getText().toString());
                                subcategory.put("sub_c_id",colId);

                                subcategory.pinInBackground("pinSubCategory");
                                subcategory.saveEventually(new SaveCallback() {
                                    @Override
                                    public void done(com.parse.ParseException e) {
                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                    }
                                });
                                Toast.makeText(AddCategoryActivity.this,"Sub Category Added",Toast.LENGTH_SHORT).show();
                                Intent i=new Intent();
                                this.finish();
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
        if (id == R.id.action_settings)
        {
            return true;
        }
        if (id == R.id.action_done)
        {   onSaveCategory();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
