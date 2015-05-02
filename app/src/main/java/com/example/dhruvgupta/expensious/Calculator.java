package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    Button mEquals;
    int flag;
    float inNum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        mInput =(TextView)findViewById(R.id.input);
        mEquals = (Button)findViewById(R.id.result);
    }

    public void onOperator(View v)
    {
        switch(v.getId())
        {
            case R.id.add:
                compute();
                Logic = '+';
                mEquals.setText("=");
                s="0";
                break;
            case R.id.sub:
                compute();
                Logic = '-';
                mEquals.setText("=");
                s="0";
                break;
            case R.id.div:
                compute();
                Logic = '/';
                mEquals.setText("=");
                s="1";
                break;
            case R.id.mul:
                compute();
                Logic = '*';
                mEquals.setText("=");
                s="1";
                break;
            case R.id.result:
                if(mEquals.getText() == "=")
                {
                    compute();
                    Logic = '=';
                    mEquals.setText("Done");
                }
                else
                {
                    Intent i = new Intent();
                    i.putExtra("RESULT",result);
                    setResult(1, i);
                    this.finish();
                }
                break;
            case R.id.del:
                result = 0;
                s = "0";
                Logic = ' ';
                mEquals.setText("=");
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
                mEquals.setText("=");
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
                        if(inNum==1)
                        {
                            if(s=="1")
                                s="0";
//                            else
//                                s="1";
                        }
//                        else
//                        {
//                            s = "1";
//                        }
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


}
