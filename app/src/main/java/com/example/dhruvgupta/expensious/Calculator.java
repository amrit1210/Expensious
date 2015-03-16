package com.example.dhruvgupta.expensious;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

/**
 * Created by Gaurav on 3/14/15.
 */
public class Calculator extends ActionBarActivity
{
EditText minput;

    String s = "0";
    float result = 0;
    char lO=' ';

@Override
protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        minput=(EditText)findViewById(R.id.input);


        }

 public void onOperator(View v)
 {
     switch(v.getId())
     {
         case R.id.add:
             compute();
             lO = '+';
             break;
         case R.id.sub:
             compute();
             lO = '-';
             break;
         case R.id.div:
             compute();
             lO = '/';
             break;
         case R.id.mul:
             compute();
             lO = '*';
             break;
         case R.id.result:
             compute();
             lO = '=';
             break;
         case R.id.del:
             result = 0;
             s = "0";
             lO = ' ';
             minput.setText("0");
             break;
     }
 }
    private void compute()
    {
        // TODO Auto-generated method stub
        float inNum = Float.parseFloat(s);
        s = "0";
        if (lO == ' ')
        {
            result = inNum;
        }
        else if (lO == '+')
        {
            result += inNum;
        }
        else if (lO == '-')
        {
            result -= inNum;
        }
        else if (lO == '*')
        {
            result *= inNum;
        }
        else if (lO == '/')
        {
            result /= inNum;
        }
        else if (lO == '=')
        {
            // Keep the result for the next operation
        }
        minput.setText(String.valueOf(result));
    }


    public void  onOperand(View v)
{
<<<<<<< HEAD
    switch(v.getId())
    {
        case R.id.digit_0:
        case R.id.digit_1:
        case R.id.digit_2:
        case R.id.digit_3:
        case R.id.digit_4:
        case R.id.digit_5:
        case R.id.digit_6:
        case R.id.digit_7:
        case R.id.digit_8:
        case R.id.digit_9:
        case R.id.digit_point:
            String inDigit = ((Button) v).getText().toString();
            if (s.equals("0"))
            {
                s= inDigit;
            }
            else
            {
                s+=inDigit;
            }
            minput.setText(s);
            if(lO == '=')
            {
                result=0;
                lO =' ';
            }
            break;


    }
=======
>>>>>>> 2ae7a35e77b4beaf5ebd91823650389c9a33dfdf

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
