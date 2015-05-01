package com.example.dhruvgupta.expensious;

import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Shubhz on 4/28/2015.
 */
public class HelpActivity extends AbstractNavigationDrawerActivity {

    TextView mA1,mA2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        mA1 = (TextView) findViewById(R.id.faq1);
        mA2 = (TextView) findViewById(R.id.faq2);

    }
}
