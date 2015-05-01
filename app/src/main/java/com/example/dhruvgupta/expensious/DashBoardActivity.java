package com.example.dhruvgupta.expensious;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Amrit on 5/2/2015.
 */
public class DashBoardActivity extends AbstractNavigationDrawerActivity {
    public void onInt(Bundle bundle) {
        super.onInt(bundle);

        this.setDefaultStartPositionNavigation(1);
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        AccountsFragment fragment = new AccountsFragment();
//        fragmentTransaction.replace(R.id.container, fragment);
//        fragmentTransaction.commit();
    }
    public static class DashBoardFragment extends Fragment
    {
        Button mAccount, mTransaction, mBudget,mLoanDebt,mRecursive,mReport,mFamilySharing,mSettings;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
             super.onCreateView(inflater, container, savedInstanceState);
            return inflater.inflate(R.layout.activity_dashboard, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View rootView=getView();

            mAccount=(Button)rootView.findViewById(R.id.dash_acc);
            mTransaction=(Button)rootView.findViewById(R.id.dash_trans);
            mBudget=(Button)rootView.findViewById(R.id.dash_budget);
            mLoanDebt=(Button)rootView.findViewById(R.id.dash_loandebt);
            mRecursive=(Button)rootView.findViewById(R.id.dash_recursive);
            mReport=(Button)rootView.findViewById(R.id.dash_reports);
            mFamilySharing=(Button)rootView.findViewById(R.id.dash_familysharing);
            mSettings=(Button)rootView.findViewById(R.id.dash_settings);

            mAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), AccountsActivity.class);
                    startActivity(i);
                }
            });

            mTransaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), TransactionsActivity.class);
                    startActivity(i);
                }
            });

            mBudget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), BudgetsActivity.class);
                    startActivity(i);
                }
            });

            mLoanDebt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), LoanDebtActivity.class);
                    startActivity(i);
                }
            });

            mRecursive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), RecursiveActivity.class);
                    startActivity(i);
                }
            });

            mReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), PieChartActivity.class);
                    startActivity(i);
                }
            });

            mFamilySharing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), EnableFamilyActivity.class);
                    startActivity(i);
                }
            });

            mSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(i);
                }
            });
        }
    }
}
