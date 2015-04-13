package com.example.dhruvgupta.expensious;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by dhruvgupta on 4/9/2015.
 */
public class RebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myIntent = new Intent(context, RecursiveService.class);
        context.startService(myIntent);

    }
}

