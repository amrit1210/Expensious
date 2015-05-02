package com.example.dhruvgupta.expensious;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.pkmmte.view.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaurav on 3/11/15.
 */
public class SignUpActivity extends ActionBarActivity implements PopupMenu.OnMenuItemClickListener
{
    EditText mName,mEmail,mPassword,mConfirm_Password;
    CircularImageView mImage;
    String image = "",cur="INR",c_img_string=null;
    Uri fileUri;
    byte[] b;
    DBHelper dbHelper;
    ArrayList<SignUpDB> al;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mName=(EditText)findViewById(R.id.signUp_name);
        mEmail=(EditText)findViewById(R.id.signUp_email);
        mPassword=(EditText)findViewById(R.id.signUp_password);
        mConfirm_Password=(EditText)findViewById(R.id.signUp_confirm_password);
        mImage = (CircularImageView)findViewById(R.id.signup_image);

        dbHelper =new DBHelper(SignUpActivity.this);
        al=new ArrayList<>();

        mName.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(!hasFocus)
                {
                    if (mName.length() == 0)
                    {
                        mName.setError("Enter UserName");
                    }
                    else if (!mName.getText().toString().matches("[a-zA-Z][a-zA-Z0-9 ]+"))
                    {
                        mName.setError("Enter valid UserName");
                        if(mName.length() <21)
                        {
                            mName.setError("Enter valid UserName");
                        }
                    }
                    else
                    {
                        mName.setError(null);
                    }
                }
            }
        });

        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(!hasFocus)
                {
                    if (mEmail.length() == 0)
                    {
                        mEmail.setError("Enter Email");
                    }
                    else if (mEmail.length() > 0)
                    {
//                        Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches()
                        if (mEmail.getText().toString().matches("[a-zA-Z0-9\\.\\_]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9]{0,64}" +
                                "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"))
                        {
                            mEmail.setError(null);
                        }
                        else
                        {
                            mEmail.setError("Invalid Email");
                        }
                    }
                }
            }
        });

        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(!hasFocus)
                {
                    if(mPassword.length()==0)
                    {
                        mPassword.setError("Enter Password");
                    }
                    else
                    {
                        if(mPassword.length()<8)
                        {
                            mPassword.setError("Min Password length is 8");
                        }
                        else
                        {
                            mPassword.setError(null);
                        }
                    }
                }
            }
        });

        mConfirm_Password.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(!hasFocus)
                {
                    if (mPassword.length() == 0)
                    {
                        mConfirm_Password.setError("Enter Password First");
                        mPassword.setError("Enter Password");
                    }
                    else
                    {
                        if(mConfirm_Password.length()==0)
                        {
                            mConfirm_Password.setError("Enter Confirm Password");
                        }
                        else
                        {
                            mConfirm_Password.setError(null);
                        }
                    }
                    if(mPassword.length()>0 && mPassword.length()<8)
                    {
                        mPassword.setError("Min Password length is 8");
                    }
                }
            }
        });
    }

    public void showPopup (View v)
    {
        PopupMenu popup = new PopupMenu(this, v);

        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu_gallery_camera);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.Gallery:
                Intent i_gallery=new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i_gallery, 2);
                return true;
            case R.id.Camera:
                Intent i_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                i_camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(i_camera, 1);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles())
                {
                    if (temp.getName().equals("temp.jpg"))
                    {
                        f = temp;
                        break;
                    }
                }
                try
                {
                    Bitmap bm;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(),bitmapOptions);
                    bm = Bitmap.createScaledBitmap(bm, 200, 200, true);

                    String path = android.os.Environment.getExternalStorageDirectory()+ File.separator + "Phoenix"
                            + File.separator + "default";
                    f.delete();
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try
                    {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        b = baos.toByteArray();
                        image = Base64.encodeToString(b, Base64.DEFAULT);

                        byte[] decodedString = Base64.decode(image.trim(),Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        mImage.setImageBitmap(decodedByte);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (requestCode == 2)
            {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bm = BitmapFactory.decodeFile(picturePath);
                bm = Bitmap.createScaledBitmap(bm, 200, 200, true);
                try
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    b = baos.toByteArray();
                    image = Base64.encodeToString(b, Base64.DEFAULT);

                    byte[] decodedString = Base64.decode(image.trim(),Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    mImage.setImageBitmap(decodedByte);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onSignUp(View v) throws FileNotFoundException {
        if(!(mName.length()==0 || mEmail.length()==0 || mPassword.length()==0 || mConfirm_Password.length()==0))
        {
//            ArrayList al1 = dbHelper.getUserColEmail();
//            if(al1!=null)
//            if (al1.contains(mEmail.getText().toString()))
//            {
//                    mEmail.setError("Email already exists");
//                    mEmail.setText(null);
//            }

            if(image.equals(""))
            {
                Uri fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.user_48);
                InputStream image_stream = getContentResolver().openInputStream(fileUri);
                Bitmap decodedByte= BitmapFactory.decodeStream(image_stream );

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                b = baos.toByteArray();
                image = Base64.encodeToString(b, Base64.DEFAULT);

                mImage.setImageURI(fileUri);
            }

            if(mEmail.getError()==null && mPassword.getError()==null)
            {
                if(mPassword.getText().toString().equals(mConfirm_Password.getText().toString()))
                {
                    if(mName.getError()==null && mEmail.getError()==null
                            && mPassword.getError()==null && mConfirm_Password.getError()==null)
                    {
//                        Async async = new Async();
//                        async.execute();
                        final ProgressDialog Dialog = new ProgressDialog(SignUpActivity.this);
                        Dialog.setMessage("Please Wait");
                        Dialog.show();
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                        query.whereExists("uid");
                        query.countInBackground(new CountCallback() {
                            public void done(int count, ParseException e) {
                                if (e == null)
                                {
                                    // The count request succeeded. Log the count
                                    Log.d("No. of users : ", count +"");

                                    final ParseUser user = new ParseUser();
                                    user.setUsername(mEmail.getText().toString());
                                    user.setPassword(mPassword.getText().toString());
                                    user.setEmail(mEmail.getText().toString());

                                    final int r = count + 1;
                                    String img = "image" + r + ".png";
                                    final ParseFile file = new ParseFile(img, b);
                                    file.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null)
                                            {
                                                user.put("uid", r);
                                                user.put("uname", mName.getText().toString());
                                                user.put("fid", 0);
                                                user.put("userimage", file);

                                                user.signUpInBackground(new SignUpCallback() {
                                                    public void done(ParseException e) {
                                                        Dialog.dismiss();
                                                        if (e == null) {
                                                            // Hooray! Let them use the app now.
                                                            Log.i("Signed Up", "YES! YES! YES!");
                                                            Toast.makeText(SignUpActivity.this,"You are Signed Up!!", Toast.LENGTH_LONG).show();
                                                            mName.setText(null);
                                                            mEmail.setText(null);
                                                            mPassword.setText(null);
                                                            mConfirm_Password.setText(null);
                                                            Intent i=new Intent(SignUpActivity.this,LoginActivity.class);
                                                            startActivity(i);

                                                        } else {
                                                            // Sign up didn't succeed. Look at the ParseException
                                                            // to figure out what went wrong
                                                            e.printStackTrace();
                                                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                                ParseObject settings = new ParseObject("Settings");
                                                settings.put("settings_uid", r);
                                                settings.put("settings_cur_code", cur);
                                                settings.pinInBackground("pinSettings");
                                                settings.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Settings saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.food);
                                                InputStream image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                Bitmap decodedByte= BitmapFactory.decodeStream(image_stream );
                                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addCategorySpecific(r,"Food",0,c_img_string);
                                                ParseObject category = new ParseObject("Category_specific");
                                                category.put("c_uid", r);
                                                category.put("c_id", 1);
                                                category.put("c_name","Food" );
                                                category.put("c_type", 0);
                                                category.put("c_icon",c_img_string);
                                                category.pinInBackground("pinCategory");
                                                category.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                 image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                 baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(1,"Vegetables",r,c_img_string);
                                                ParseObject subcategory1 = new ParseObject("Sub_category");
                                                subcategory1.put("sub_uid", r);
                                                subcategory1.put("sub_c_id", 1);
                                                subcategory1.put("sub_name","Vegetables" );
                                                subcategory1.put("sub_id", 1);
                                               subcategory1.put("sub_icon",c_img_string);
                                                subcategory1.pinInBackground("pinSubCategory");
                                                subcategory1.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(1,"Fruits",r,c_img_string);
                                                ParseObject subcategory2 = new ParseObject("Sub_category");
                                                subcategory2.put("sub_uid", r);
                                                subcategory2.put("sub_c_id", 1);
                                                subcategory2.put("sub_name","Fruits" );
                                                subcategory2.put("sub_id", 2);
                                               subcategory2.put("sub_icon",c_img_string);
                                                subcategory2.pinInBackground("pinSubCategory");
                                                subcategory2.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.entertainment);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                 baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addCategorySpecific(r,"Entertainment",0,c_img_string);
                                                ParseObject category1 = new ParseObject("Category_specific");
                                                category1.put("c_uid", r);
                                                category1.put("c_id", 2);
                                                category1.put("c_name","Entertainment" );
                                                category1.put("c_type", 0);
                                                category1.put("c_icon",c_img_string);
                                                category1.pinInBackground("pinCategory");
                                                category1.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(2,"Movies",r,c_img_string);
                                                ParseObject subcategory3 = new ParseObject("Sub_category");
                                                subcategory3.put("sub_uid", r);
                                                subcategory3.put("sub_c_id", 2);
                                                subcategory3.put("sub_name","Movies" );
                                                subcategory3.put("sub_id", 3);
                                               subcategory3.put("sub_icon",c_img_string);
                                                subcategory3.pinInBackground("pinSubCategory");
                                                subcategory3.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(2,"Television",r,c_img_string);
                                                ParseObject subcategory4 = new ParseObject("Sub_category");
                                                subcategory4.put("sub_uid", r);
                                                subcategory4.put("sub_c_id", 2);
                                                subcategory4.put("sub_name","Television" );
                                                subcategory4.put("sub_id", 4);
                                               subcategory4.put("sub_icon",c_img_string);
                                                subcategory4.pinInBackground("pinSubCategory");
                                                subcategory4.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.travel);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addCategorySpecific(r,"Travel",0,c_img_string);
                                                ParseObject category2 = new ParseObject("Category_specific");
                                                category2.put("c_uid", r);
                                                category2.put("c_id", 3);
                                                category2.put("c_name","Travel" );
                                                category2.put("c_type", 0);
                                                category2.put("c_icon",c_img_string);
                                                category2.pinInBackground("pinCategory");
                                                category2.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(3,"Auto Fare",r,c_img_string);
                                                ParseObject subcategory5 = new ParseObject("Sub_category");
                                                subcategory5.put("sub_uid", r);
                                                subcategory5.put("sub_c_id", 3);
                                                subcategory5.put("sub_name","Auto Fare" );
                                                subcategory5.put("sub_id", 5);
                                               subcategory5.put("sub_icon",c_img_string);
                                                subcategory5.pinInBackground("pinSubCategory");
                                                subcategory5.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(3,"Bus Fare",r,c_img_string);
                                                ParseObject subcategory6 = new ParseObject("Sub_category");
                                                subcategory6.put("sub_uid", r);
                                                subcategory6.put("sub_c_id", 3);
                                                subcategory6.put("sub_name","Bus Fare" );
                                                subcategory6.put("sub_id", 6);
                                               subcategory6.put("sub_icon",c_img_string);
                                                subcategory6.pinInBackground("pinSubCategory");
                                                subcategory6.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(3,"Taxi Fare",r,c_img_string);
                                                ParseObject subcategory7 = new ParseObject("Sub_category");
                                                subcategory7.put("sub_uid", r);
                                                subcategory7.put("sub_c_id", 3);
                                                subcategory7.put("sub_name","Taxi Fare" );
                                                subcategory7.put("sub_id", 7);
                                               subcategory7.put("sub_icon",c_img_string);
                                                subcategory7.pinInBackground("pinSubCategory");
                                                subcategory7.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(3,"Flight Fare",r,c_img_string);
                                                ParseObject subcategory8 = new ParseObject("Sub_category");
                                                subcategory8.put("sub_uid", r);
                                                subcategory8.put("sub_c_id", 3);
                                                subcategory8.put("sub_name","Flight Fare" );
                                                subcategory8.put("sub_id", 8);
                                               subcategory8.put("sub_icon",c_img_string);
                                                subcategory8.pinInBackground("pinSubCategory");
                                                subcategory8.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });

                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.bill);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addCategorySpecific(r,"Tax Payment",0,c_img_string);
                                                ParseObject category3 = new ParseObject("Category_specific");
                                                category3.put("c_uid", r);
                                                category3.put("c_id", 4);
                                                category3.put("c_name","Tax Payment" );
                                                category3.put("c_type", 0);
                                                category3.put("c_icon",c_img_string);
                                                category3.pinInBackground("pinCategory");
                                                category3.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(4,"Income Tax",r,c_img_string);
                                                ParseObject subcategory9 = new ParseObject("Sub_category");
                                                subcategory9.put("sub_uid", r);
                                                subcategory9.put("sub_c_id", 4);
                                                subcategory9.put("sub_name","Income Tax" );
                                                subcategory9.put("sub_id", 9);
                                               subcategory9.put("sub_icon",c_img_string);
                                                subcategory9.pinInBackground("pinSubCategory");
                                                subcategory9.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(4,"Sales Tax",r,c_img_string);
                                                ParseObject subcategory10 = new ParseObject("Sub_category");
                                                subcategory10.put("sub_uid", r);
                                                subcategory10.put("sub_c_id", 4);
                                                subcategory10.put("sub_name","Sales Tax" );
                                                subcategory10.put("sub_id", 10);
                                               subcategory10.put("sub_icon",c_img_string);
                                                subcategory10.pinInBackground("pinSubCategory");
                                                subcategory10.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(4,"Property Tax",r,c_img_string);
                                                ParseObject subcategory11 = new ParseObject("Sub_category");
                                                subcategory11.put("sub_uid", r);
                                                subcategory11.put("sub_c_id", 4);
                                                subcategory11.put("sub_name","Property Tax" );
                                                subcategory11.put("sub_id", 11);
                                               subcategory11.put("sub_icon",c_img_string);
                                                subcategory11.pinInBackground("pinSubCategory");
                                                subcategory11.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.medical);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);

                                                dbHelper.addCategorySpecific(r,"Health",0,c_img_string);
                                                ParseObject category4 = new ParseObject("Category_specific");
                                                category4.put("c_uid", r);
                                                category4.put("c_id", 5);
                                                category4.put("c_name","Health" );
                                                category4.put("c_type", 0);
                                                category4.put("c_icon",c_img_string);
                                                category4.pinInBackground("pinCategory");
                                                category4.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                Log.i("Surgery","sur");
                                                dbHelper.addSubCategory(5,"Surgery",r,c_img_string);
                                                ParseObject subcategory12 = new ParseObject("Sub_category");
                                                subcategory12.put("sub_uid", r);
                                                subcategory12.put("sub_c_id", 5);
                                                subcategory12.put("sub_name","Surgery" );
                                                subcategory12.put("sub_id", 12);
                                               subcategory12.put("sub_icon",c_img_string);
                                                subcategory12.pinInBackground("pinSubCategory");
                                                subcategory12.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                Log.i("Surgery1","sur1");
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                Log.i("Medicines","Medicines");
                                                dbHelper.addSubCategory(5,"Medicines",r,c_img_string);
                                                ParseObject subcategory13 = new ParseObject("Sub_category");
                                                subcategory13.put("sub_uid", r);
                                                subcategory13.put("sub_c_id", 5);
                                                subcategory13.put("sub_name","Medicines" );
                                                subcategory13.put("sub_id", 13);
                                               subcategory13.put("sub_icon",c_img_string);
                                                subcategory13.pinInBackground("pinSubCategory");
                                                subcategory13.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                Log.i("Medicines1","Medicines1");
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.gift);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addCategorySpecific(r,"Beauty",0,c_img_string);
                                                ParseObject category5 = new ParseObject("Category_specific");
                                                category5.put("c_uid", r);
                                                category5.put("c_id", 6);
                                                category5.put("c_name","Beauty" );
                                                category5.put("c_type", 0);
                                                category5.put("c_icon",c_img_string);
                                                category5.pinInBackground("pinCategory");
                                                category5.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(6,"Manicure",r,c_img_string);
                                                ParseObject subcategory14 = new ParseObject("Sub_category");
                                                subcategory14.put("sub_uid", r);
                                                subcategory14.put("sub_c_id", 6);
                                                subcategory14.put("sub_name","Manicure" );
                                                subcategory14.put("sub_id", 14);
                                               subcategory14.put("sub_icon",c_img_string);
                                                subcategory14.pinInBackground("pinSubCategory");
                                                subcategory14.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(6,"Padicure",r,c_img_string);
                                                ParseObject subcategory15 = new ParseObject("Sub_category");
                                                subcategory15.put("sub_uid", r);
                                                subcategory15.put("sub_c_id", 6);
                                                subcategory15.put("sub_name","Padicure" );
                                                subcategory15.put("sub_id", 15);
                                               subcategory15.put("sub_icon",c_img_string);
                                                subcategory15.pinInBackground("pinSubCategory");
                                                subcategory15.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(6,"HairDressing",r,c_img_string);
                                                ParseObject subcategory16 = new ParseObject("Sub_category");
                                                subcategory16.put("sub_uid", r);
                                                subcategory16.put("sub_c_id", 6);
                                                subcategory16.put("sub_name","HairDressing" );
                                                subcategory16.put("sub_id", 16);
                                               subcategory16.put("sub_icon",c_img_string);
                                                subcategory16.pinInBackground("pinSubCategory");
                                                subcategory16.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.cloth);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addCategorySpecific(r,"Clothing",0,c_img_string);
                                                ParseObject category6 = new ParseObject("Category_specific");
                                                category6.put("c_uid", r);
                                                category6.put("c_id", 7);
                                                category6.put("c_name","Clothing" );
                                                category6.put("c_type", 0);
                                                category6.put("c_icon",c_img_string);
                                                category6.pinInBackground("pinCategory");
                                                category6.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(7,"Shirts",r,c_img_string);
                                                ParseObject subcategory17 = new ParseObject("Sub_category");
                                                subcategory17.put("sub_uid", r);
                                                subcategory17.put("sub_c_id", 7);
                                                subcategory17.put("sub_name","Shirts" );
                                                subcategory17.put("sub_id", 17);
                                               subcategory17.put("sub_icon",c_img_string);
                                                subcategory17.pinInBackground("pinSubCategory");
                                                subcategory17.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(7,"Pants",r,c_img_string);
                                                ParseObject subcategory18 = new ParseObject("Sub_category");
                                                subcategory18.put("sub_uid", r);
                                                subcategory18.put("sub_c_id", 7);
                                                subcategory18.put("sub_name","Pants" );
                                                subcategory18.put("sub_id", 18);
                                               subcategory18.put("sub_icon",c_img_string);
                                                subcategory18.pinInBackground("pinSubCategory");
                                                subcategory18.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(7,"T-shirts",r,c_img_string);
                                                ParseObject subcategory19 = new ParseObject("Sub_category");
                                                subcategory19.put("sub_uid", r);
                                                subcategory19.put("sub_c_id", 7);
                                                subcategory19.put("sub_name","T-shirts" );
                                                subcategory19.put("sub_id", 19);
                                               subcategory19.put("sub_icon",c_img_string);
                                                subcategory19.pinInBackground("pinSubCategory");
                                                subcategory19.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(7,"Shoes",r,c_img_string);
                                                ParseObject subcategory20 = new ParseObject("Sub_category");
                                                subcategory20.put("sub_uid", r);
                                                subcategory20.put("sub_c_id", 7);
                                                subcategory20.put("sub_name","Shoes" );
                                                subcategory20.put("sub_id", 20);
                                               subcategory20.put("sub_icon",c_img_string);
                                                subcategory20.pinInBackground("pinSubCategory");
                                                subcategory20.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(7,"Sweater",r,c_img_string);
                                                ParseObject subcategory21 = new ParseObject("Sub_category");
                                                subcategory21.put("sub_uid", r);
                                                subcategory21.put("sub_c_id", 7);
                                                subcategory21.put("sub_name","Sweater" );
                                                subcategory21.put("sub_id", 21);
                                               subcategory21.put("sub_icon",c_img_string);
                                                subcategory21.pinInBackground("pinSubCategory");
                                                subcategory21.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(7,"Skirts",r,c_img_string);
                                                ParseObject subcategory22 = new ParseObject("Sub_category");
                                                subcategory22.put("sub_uid", r);
                                                subcategory22.put("sub_c_id", 7);
                                                subcategory22.put("sub_name","Skirts" );
                                                subcategory22.put("sub_id", 22);
                                               subcategory22.put("sub_icon",c_img_string);
                                                subcategory22.pinInBackground("pinSubCategory");
                                                subcategory22.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(7,"Jackets",r,c_img_string);
                                                ParseObject subcategory23 = new ParseObject("Sub_category");
                                                subcategory23.put("sub_uid", r);
                                                subcategory23.put("sub_c_id", 7);
                                                subcategory23.put("sub_name","Jackets" );
                                                subcategory23.put("sub_id", 23);
                                               subcategory23.put("sub_icon",c_img_string);
                                                subcategory23.pinInBackground("pinSubCategory");
                                                subcategory23.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.bill);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addCategorySpecific(r,"Bills",0,c_img_string);
                                                ParseObject category7 = new ParseObject("Category_specific");
                                                category7.put("c_uid", r);
                                                category7.put("c_id", 8);
                                                category7.put("c_name","Bills" );
                                                category7.put("c_type", 0);
                                                category7.put("c_icon",c_img_string);
                                                category7.pinInBackground("pinCategory");
                                                category7.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(8,"Phone Bill",r,c_img_string);
                                                ParseObject subcategory24 = new ParseObject("Sub_category");
                                                subcategory24.put("sub_uid", r);
                                                subcategory24.put("sub_c_id", 8);
                                                subcategory24.put("sub_name","Phone Bill" );
                                                subcategory24.put("sub_id", 24);
                                               subcategory24.put("sub_icon",c_img_string);
                                                subcategory24.pinInBackground("pinSubCategory");
                                                subcategory24.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(8,"Electricity Bill",r,c_img_string);
                                                ParseObject subcategory25 = new ParseObject("Sub_category");
                                                subcategory25.put("sub_uid", r);
                                                subcategory25.put("sub_c_id", 8);
                                                subcategory25.put("sub_name","Electricity Bill" );
                                                subcategory25.put("sub_id", 25);
                                               subcategory25.put("sub_icon",c_img_string);
                                                subcategory25.pinInBackground("pinSubCategory");
                                                subcategory25.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
//                                                dbHelper.addSubCategory(8,"Water Bill",r);
//                                                ParseObject subcategory26 = new ParseObject("Sub_category");
//                                                subcategory26.put("sub_uid", r);
//                                                subcategory26.put("sub_c_id", 8);
//                                                subcategory26.put("sub_name","Water Bill" );
//                                                subcategory26.put("sub_id", 26);
////                                               subcategory26.put("sub_icon",);
//                                                subcategory26.pinInBackground("pinSubCategory");
//                                                subcategory26.saveEventually(new SaveCallback() {
//                                                    @Override
//                                                    public void done(com.parse.ParseException e) {
//                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
//                                                    }
//                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.book);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);

                                                dbHelper.addCategorySpecific(r,"Stationary",0,c_img_string);
                                                ParseObject category8 = new ParseObject("Category_specific");
                                                category8.put("c_uid", r);
                                                category8.put("c_id", 9);
                                                category8.put("c_name","Stationary" );
                                                category8.put("c_type", 0);
                                                category8.put("c_icon",c_img_string);
                                                category8.pinInBackground("pinCategory");
                                                category8.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                dbHelper.addSubCategory(9,"Copy",r,c_img_string);
                                                ParseObject subcategory27 = new ParseObject("Sub_category");
                                                subcategory27.put("sub_uid", r);
                                                subcategory27.put("sub_c_id", 9);
                                                subcategory27.put("sub_name","Copy" );
                                                subcategory27.put("sub_id", 27);
                                               subcategory27.put("sub_icon",c_img_string);
                                                subcategory27.pinInBackground("pinSubCategory");
                                                subcategory27.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                dbHelper.addSubCategory(9,"Pen",r,c_img_string);
                                                ParseObject subcategory28 = new ParseObject("Sub_category");
                                                subcategory28.put("sub_uid", r);
                                                subcategory28.put("sub_c_id", 9);
                                                subcategory28.put("sub_name","Pen" );
                                                subcategory28.put("sub_id", 28);
                                               subcategory28.put("sub_icon",c_img_string);
                                                subcategory28.pinInBackground("pinSubCategory");
                                                subcategory28.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(9,"Pencil",r,c_img_string);
                                                ParseObject subcategory29 = new ParseObject("Sub_category");
                                                subcategory29.put("sub_uid", r);
                                                subcategory29.put("sub_c_id", 9);
                                                subcategory29.put("sub_name","Pencil" );
                                                subcategory29.put("sub_id", 29);
                                               subcategory29.put("sub_icon",c_img_string);
                                                subcategory29.pinInBackground("pinSubCategory");
                                                subcategory29.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.education);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addCategorySpecific(r,"Educational",0,c_img_string);
                                                ParseObject category9 = new ParseObject("Category_specific");
                                                category9.put("c_uid", r);
                                                category9.put("c_id", 10);
                                                category9.put("c_name","Educational" );
                                                category9.put("c_type", 0);
                                                category9.put("c_icon",c_img_string);
                                                category9.pinInBackground("pinCategory");
                                                category9.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                dbHelper.addSubCategory(10,"Tution Fee",r,c_img_string);
                                                ParseObject subcategory30 = new ParseObject("Sub_category");
                                                subcategory30.put("sub_uid", r);
                                                subcategory30.put("sub_c_id", 10);
                                                subcategory30.put("sub_name","Tution Fee" );
                                                subcategory30.put("sub_id", 30);
                                               subcategory30.put("sub_icon",c_img_string);
                                                subcategory30.pinInBackground("pinSubCategory");
                                                subcategory30.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                dbHelper.addSubCategory(10,"Admission Fee",r,c_img_string);
                                                ParseObject subcategory31 = new ParseObject("Sub_category");
                                                subcategory31.put("sub_uid", r);
                                                subcategory31.put("sub_c_id", 10);
                                                subcategory31.put("sub_name","Admission Fee" );
                                                subcategory31.put("sub_id", 31);
                                               subcategory31.put("sub_icon",c_img_string);
                                                subcategory31.pinInBackground("pinSubCategory");
                                                subcategory31.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.bill);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);

                                                dbHelper.addCategorySpecific(r,"Salary",1,c_img_string);
                                                ParseObject category10 = new ParseObject("Category_specific");
                                                category10.put("c_uid", r);
                                                category10.put("c_id", 11);
                                                category10.put("c_name","Salary" );
                                                category10.put("c_type", 1);
                                                category10.put("c_icon",c_img_string);
                                                category10.pinInBackground("pinCategory");
                                                category10.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.annuity);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);

                                                dbHelper.addCategorySpecific(r,"Wages",1,c_img_string);
                                                ParseObject category11 = new ParseObject("Category_specific");
                                                category11.put("c_uid", r);
                                                category11.put("c_id", 12);
                                                category11.put("c_name","Wages" );
                                                category11.put("c_type", 1);
                                                category11.put("c_icon",c_img_string);
                                                category11.pinInBackground("pinCategory");
                                                category11.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(12,"Daily",r,c_img_string);
                                                ParseObject subcategory32 = new ParseObject("Sub_category");
                                                subcategory32.put("sub_uid", r);
                                                subcategory32.put("sub_c_id", 12);
                                                subcategory32.put("sub_name","Daily" );
                                                subcategory32.put("sub_id", 32);
                                               subcategory32.put("sub_icon",c_img_string);
                                                subcategory32.pinInBackground("pinSubCategory");
                                                subcategory32.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.grey);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);
                                                dbHelper.addSubCategory(12,"Weekly",r,c_img_string);
                                                ParseObject subcategory33 = new ParseObject("Sub_category");
                                                subcategory33.put("sub_uid", r);
                                                subcategory33.put("sub_c_id", 12);
                                                subcategory33.put("sub_name","Weekly" );
                                                subcategory33.put("sub_id", 33);
                                               subcategory33.put("sub_icon",c_img_string);
                                                subcategory33.pinInBackground("pinSubCategory");
                                                subcategory33.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("SubCat saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.capital);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);

                                                dbHelper.addCategorySpecific(r,"Provident Fund",1,c_img_string);
                                                ParseObject category12 = new ParseObject("Category_specific");
                                                category12.put("c_uid", r);
                                                category12.put("c_id", 13);
                                                category12.put("c_name","Provident Fund" );
                                                category12.put("c_type", 1);
                                                category12.put("c_icon",c_img_string);
                                                category12.pinInBackground("pinCategory");
                                                category12.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.medical);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);

                                                dbHelper.addCategorySpecific(r,"Med_Claim",1,c_img_string);
                                                ParseObject category13 = new ParseObject("Category_specific");
                                                category13.put("c_uid", r);
                                                category13.put("c_id", 14);
                                                category13.put("c_name","Med_Claim" );
                                                category13.put("c_type", 1);
                                                category13.put("c_icon",c_img_string);
                                                category13.pinInBackground("pinCategory");
                                                category13.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.annuity);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);

                                                dbHelper.addCategorySpecific(r,"Pension",1,c_img_string);
                                                ParseObject category14 = new ParseObject("Category_specific");
                                                category14.put("c_uid", r);
                                                category14.put("c_id", 15);
                                                category14.put("c_name","Pension" );
                                                category14.put("c_type", 1);
                                                category14.put("c_icon",c_img_string);
                                                category14.pinInBackground("pinCategory");
                                                category14.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.capital);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);

                                                dbHelper.addCategorySpecific(r,"Capital Gains",1,c_img_string);
                                                ParseObject category15 = new ParseObject("Category_specific");
                                                category15.put("c_uid", r);
                                                category15.put("c_id", 16);
                                                category15.put("c_name","Capital Gains" );
                                                category15.put("c_type", 1);
                                                category15.put("c_icon",c_img_string);
                                                category15.pinInBackground("pinCategory");
                                                category15.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });
                                                fileUri = Uri.parse("android.resource://com.example.dhruvgupta.expensious/" + R.drawable.house);
                                                image_stream = null;
                                                try
                                                {
                                                    image_stream = getContentResolver().openInputStream(fileUri);
                                                }
                                                catch (FileNotFoundException f)
                                                {
                                                    f.printStackTrace();
                                                }
                                                decodedByte= BitmapFactory.decodeStream(image_stream );
                                                baos = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                b = baos.toByteArray();
                                                c_img_string = Base64.encodeToString(b, Base64.DEFAULT);

                                                dbHelper.addCategorySpecific(r,"House Property",1,c_img_string);
                                                ParseObject category16 = new ParseObject("Category_specific");
                                                category16.put("c_uid", r);
                                                category16.put("c_id", 17);
                                                category16.put("c_name","House Property" );
                                                category16.put("c_type", 1);
                                                category16.put("c_icon",c_img_string);
                                                category16.pinInBackground("pinCategory");
                                                category16.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        Log.i("Category saveEventually", "YES! YES! YES!");
                                                    }
                                                });

                                            }
                                            else
                                            {
                                                e.printStackTrace();
                                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                } else {
                                    // The request failed
                                }
                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(SignUpActivity.this,"Password and Confirm password must be same",
                            Toast.LENGTH_LONG).show();
                    mConfirm_Password.setError("Confirm Password must be same");
                    mConfirm_Password.setText(null);
                }
            }
        }
        else
        {
            if(mName.length()==0)
            {
                mName.setError("Enter Username");
            }
            if(mEmail.length()==0)
            {
                mEmail.setError("Enter Email");
            }
            if(mPassword.length()==0)
            {
                mPassword.setError("Enter Password");
            }
            if(mConfirm_Password.length()==0)
            {
                mConfirm_Password.setError("Enter Confirm Password");
            }
        }
        al= dbHelper.getAllUsers();
    }

    class Async extends AsyncTask<String, String, String>
    {
        private ProgressDialog Dialog = new ProgressDialog(SignUpActivity.this);

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Dialog.setMessage("Please Wait....");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {

            }
            catch (Exception e)
            {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            Dialog.dismiss();

        }
    }
}