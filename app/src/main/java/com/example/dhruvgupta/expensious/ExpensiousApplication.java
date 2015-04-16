package com.example.dhruvgupta.expensious;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by dhruvgupta on 4/15/2015.
 */
public class ExpensiousApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "eOl0Gn8cU0t5LdRFa5SSVO5K8i1C9auHRP5vkYGh", "wqUGDGihxG9eRCmjt20OdEzLkmqG0R4DdAKcIO65");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}

