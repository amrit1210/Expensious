package com.example.dhruvgupta.expensious;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Gaurav on 3/14/15.
 */
public class Calculator extends ActionBarActivity
{
    TextView mInput;

    String s = "0";
    float result = 0;
    char Logic =' ';
    String inDigit=null;
    int flag;
    float inNum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        mInput =(TextView)findViewById(R.id.input);
    }

    public void onOperator(View v)
    {
        switch(v.getId())
        {
            case R.id.add:
                compute();
                Logic = '+';
                s="0";
                break;
            case R.id.sub:
                compute();
                Logic = '-';
                s="0";
                break;
            case R.id.div:
                compute();
                Logic = '/';
                s="1";
                break;
            case R.id.mul:
                compute();
                Logic = '*';
                s="1";
                break;
            case R.id.result:
                compute();
                Logic = '=';
                break;
            case R.id.del:
                result = 0;
                s = "0";
                Logic = ' ';
                mInput.setText("0");
                break;
        }
    }

    private void compute()
    {
        // TODO Auto-generated method stub
        inNum = Float.parseFloat(s);
        s="0";
        if (Logic == ' ')
        {
            result = inNum;
        }
        else if (Logic == '+')
        {
            result += inNum;
        }
        else if (Logic == '-')
        {
            result -= inNum;
        }
        else if (Logic == '*')
        {
            s="1";
            result *= inNum;
        }
        else if (Logic == '/')
        {
            s="1";
            result /= inNum;
        }
        else if (Logic == '=')
        {
            // Keep the result for the next operation
        }
        mInput.setText(String.valueOf(result));
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
                if((s.equals("0")&& Logic =='+')||(s.equals("0")&& Logic =='-'))
                {
                    s=inDigit;
                }
                else if (s.equals("0")||((s.equals("1")&&(Logic !=' '))))
                {
                    s= inDigit;
                }
                else if((s.equals("1")&& Logic =='*')||(s.equals("1")&& Logic =='/'))
                {
                    s=inDigit;
                }
                else
                {
                     s+=inDigit;
                }
                mInput.setText(s);

                if(Logic == '=')
                {
                    result=0;
                    Logic =' ';
                }
                break;
            case R.id.digit_point:
                String point = ((Button) v).getText().toString();
                if(((Logic =='*')||(Logic =='/'))&&(s.equals("1")))
                {
                    if(inDigit.equals("1")&&(flag==1))
                    {
                        if(inNum==1.00)
                        {
                            s="0";
                        }
                        else
                        {
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
                mInput.setText(s);
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
