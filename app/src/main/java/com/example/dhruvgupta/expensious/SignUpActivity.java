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
    String image = "";
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
                        if (Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches())
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
                        Async async = new Async();
                        async.execute();
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
                                        user.put("userimage", file);

                                        user.signUpInBackground(new SignUpCallback() {
                                            public void done(ParseException e) {
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