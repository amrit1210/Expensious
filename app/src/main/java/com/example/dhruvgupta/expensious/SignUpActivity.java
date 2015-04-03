package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Gaurav on 3/11/15.
 */
public class SignUpActivity extends ActionBarActivity implements PopupMenu.OnMenuItemClickListener
{
    EditText mName,mEmail,mPassword,mConfirm_Password;
    CircularImageView mImage;
    String image = "";
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
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();
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
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
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

    public void onSignUp(View v)
    {
        if(!(mName.length()==0 || mEmail.length()==0 || mPassword.length()==0 || mConfirm_Password.length()==0))
        {
            ArrayList al1= dbHelper.getUserColEmail();
            if (al1.contains(mEmail.getText().toString()))
            {
                mEmail.setError("Email already exists");
                mEmail.setText(null);
            }

            if(image.equals(""))
            {
                int id = getResources().getIdentifier("user_48", "drawable", getPackageName());
                mImage.setImageResource(id);
                image="Image";
            }

            if(mEmail.getError()==null && mPassword.getError()==null)
            {
                if(mPassword.getText().toString().equals(mConfirm_Password.getText().toString()))
                {
                    if(mName.getError()==null && mEmail.getError()==null
                            && mPassword.getError()==null && mConfirm_Password.getError()==null)
                    {
                        if(dbHelper.addUser(mName.getText().toString(), mEmail.getText().toString(),
                                mPassword.getText().toString(),image))
                        {
                            Toast.makeText(SignUpActivity.this,"You are Signed Up!!", Toast.LENGTH_LONG).show();
                            mName.setText(null);
                            mEmail.setText(null);
                            mPassword.setText(null);
                            mConfirm_Password.setText(null);
                            Intent i=new Intent(SignUpActivity.this,LoginActivity.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(SignUpActivity.this, "Error Signing Up", Toast.LENGTH_LONG).show();
                        }
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