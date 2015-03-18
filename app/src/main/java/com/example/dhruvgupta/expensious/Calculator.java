package com.example.dhruvgupta.expensious;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by Gaurav on 3/14/15.
 */
public class Calculator extends ActionBarActivity
{
TextView minput;

    String s = "0";
    float result = 0;
    char lO=' ';
    String inDigit=null;
    int flag;
    float inNum=0;

@Override
protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        minput=(TextView)findViewById(R.id.input);


        }

 public void onOperator(View v)
 {
     switch(v.getId())
     {
         case R.id.add:
             compute();
             lO = '+';
             s="0";
             break;
         case R.id.sub:
             compute();
             lO = '-';
             s="0";
             break;
         case R.id.div:
             compute();
             lO = '/';
             s="1";
             break;
         case R.id.mul:
             compute();
             lO = '*';
             s="1";
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
         inNum = Float.parseFloat(s);
        s="0";
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
            s="1";
            result *= inNum;
        }
        else if (lO == '/')
        {
            s="1";
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
            inDigit = ((Button) v).getText().toString();
            if(inDigit.equals("1"))
            {
                flag=1;
            }
            if((s.equals("0")&& lO=='+')||(s.equals("0")&& lO=='-'))
            {
                s=inDigit;
            }
           else if (s.equals("0")||((s.equals("1")&&(lO!=' '))))
            {
                s= inDigit;
            }
            else if((s.equals("1")&& lO=='*')||(s.equals("1")&& lO=='/'))
            {
                s=inDigit;
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
        case R.id.digit_point:
            String point = ((Button) v).getText().toString();
            if(((lO=='*')||(lO=='/'))&&(s.equals("1")))
            {
                if(inDigit.equals("1")&&(flag==1))
                {
                    if(inNum==1.00)
                    {
                        s="0";
                    }
                    else {
                        s = "1";
                    }
                }
                else
                {

                    s="0";
                }
                s+=point;
            }

            if(!s.contains("."))
            {
                s+=point;

            }
            else
            {

            }
            minput.setText(s);
            break;


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
