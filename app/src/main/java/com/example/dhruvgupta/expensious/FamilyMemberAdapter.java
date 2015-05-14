package com.example.dhruvgupta.expensious;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.pkmmte.view.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Amrit on 3/19/2015.
 */
public class FamilyMemberAdapter extends ArrayAdapter<SignUpDB>
{
    Context context1;
    int layout;
    ArrayList<SignUpDB> al;

    public FamilyMemberAdapter(Context context, int resource, ArrayList <SignUpDB> objects)
    {
        super(context, resource , objects);
        context1=context;
        layout=resource;
        al=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater in = (LayoutInflater) context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = in.inflate(layout, null);
        }

        final CircularImageView mImageView=(CircularImageView)convertView.findViewById(R.id.list_person_color);
        final TextView  mUserName=(TextView)convertView.findViewById(R.id.list_person_name);

        SignUpDB db=al.get(position);
        Log.i("Position",position+"");

        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            query.whereEqualTo("uid", db.u_id);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    ParseFile file = parseObject.getParseFile("userimage");
                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArr);
                            String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "Title", null);
                            Uri image = Uri.parse(path);
                            Log.i("byte", image + " ; " + bytes);
                            mImageView.setImageURI(image);
                        }
                    });
                }
            });
            mUserName.setText(db.u_name);
        }
        catch (Exception e)
        {
            Log.i("Excep:FamMemberAdapter",e.getMessage());
        }
        return convertView;
    }
}
