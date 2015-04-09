package com.example.dhruvgupta.expensious;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by dhruvgupta on 4/8/2015.
 */
public class RecursiveService extends Service {

    DBHelper dbHelper = new DBHelper(RecursiveService.this);
    ArrayList <RecursiveDB> al;
    Date start, end, next;
    String mNextDate;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    Iterator<RecursiveDB> iterator;
    SharedPreferences sp;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "YES! YES! YES!", Toast.LENGTH_SHORT).show();
        Log.i("service: ", "YES! YES! YES!");
        sp = getSharedPreferences("USER_PREFS",MODE_PRIVATE);
        al = dbHelper.getAllRecursive(sp.getInt("UID", 0));
        iterator = al.iterator();

        while (iterator.hasNext())
        {
            Date date = new Date();
            String dt = null, ndate = null;
            RecursiveDB recDB = iterator.next();
            try {
                start = sdf.parse(recDB.rec_start_date);
                next = sdf.parse(recDB.rec_next_date);
                ndate = sdf.format(next);
                dt = sdf.format(date);
                Log.i("recdb.rec_next_date:", " : " + dt);
                end = sdf.parse(recDB.rec_end_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if ((!date.before(start)) && (!date.after(end)))
            {
                Log.i("Date check",dt + " : " + next);
                if (dt.equals(ndate))
                {
                    Log.i("Date comparison","true");
                    boolean a = dbHelper.addTransaction(sp.getInt("UID", 0), recDB.rec_from_acc, recDB.rec_to_acc, recDB.rec_balance,
                            recDB.rec_note, recDB.rec_p_id, recDB.rec_c_id, recDB.rec_sub_id, recDB.rec_show, recDB.rec_type,
                            recDB.rec_next_date, recDB.rec_time, recDB.rec_id);
                    if(a) {
                        Toast.makeText(RecursiveService.this, "Transaction Added", Toast.LENGTH_LONG).show();

                        if (recDB.rec_alert ==1)
                            showNotification();

                        Calendar c = Calendar.getInstance();

                        try
                        {
                            c.setTime(sdf.parse(recDB.rec_next_date));
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }

                        switch (recDB.rec_recurring)
                        {
                            case 0 :  c.add(Calendar.YEAR, 1);
                                mNextDate = sdf.format(c.getTime());
                                break;
                            case 1 : c.add(Calendar.MONTH, 1);
                                mNextDate = sdf.format(c.getTime());
                                break;
                            case 2 : c.add(Calendar.DAY_OF_MONTH, 7);
                                mNextDate = sdf.format(c.getTime());
                                break;
                            case 3 : c.add(Calendar.DAY_OF_MONTH, 1);
                                mNextDate = sdf.format(c.getTime());
                                break;
                        }

                        boolean b = dbHelper.updateRecursiveData(recDB.rec_id, recDB.rec_from_acc, recDB.rec_to_acc, recDB.rec_p_id,
                                recDB.rec_c_id, recDB.rec_sub_id, recDB.rec_balance, recDB.rec_note, recDB.rec_show, recDB.rec_type,
                                recDB.rec_start_date, recDB.rec_end_date, mNextDate, recDB.rec_time, recDB.rec_recurring,
                                recDB.rec_alert, sp.getInt("UID", 0));

                        if (b) {
                            Toast.makeText(RecursiveService.this, "Recursive Transaction Updated", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }

        return Service.START_STICKY;
    }

    public void showNotification(){

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // intent triggered, you can add other intent for other actions
        Intent intent = new Intent(RecursiveService.this, TransactionsActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(RecursiveService.this, 0, intent, 0);
        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0

        NotificationCompat.Builder  mBuilder =
                new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("Recursive Transaction");
        mBuilder.setContentText("Your recursive transaction has been performed.");
        mBuilder.setTicker("Recursive Transaction");
        mBuilder.setSmallIcon(R.drawable.user_48);
        mBuilder.setSound(soundUri);
        mBuilder.setContentIntent(pIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // If you want to hide the notification after it was selected, do the code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, mBuilder.build());

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
