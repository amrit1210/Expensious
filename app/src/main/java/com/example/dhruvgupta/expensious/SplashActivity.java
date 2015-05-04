package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Gaurav on 3/11/15.
 */
public class SplashActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread timer=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(1500);
                }
                catch (InterruptedException e)
                {
                    Log.i("Exception :", e.getMessage());
                }
                finally
                {
                    Intent i=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(i);
                    Intent intent = new Intent(SplashActivity.this, WebSyncService.class);
                    startService(intent);
                    SplashActivity.this.finish();
                }
            }
        });
        timer.start();
    }


}
