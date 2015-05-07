//package com.example.dhruvgupta.expensious;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//
//import com.parse.ParseQuery;
//import com.parse.ParseUser;
//
///**
// * Created by dhruvgupta on 4/25/2015.
// */
//public class HeadFamilyView extends AbstractNavigationDrawerActivity {
//
//    public void onInt(Bundle bundle) {
//        super.onInt(bundle);
//
//        this.setDefaultStartPositionNavigation(5);
////        FragmentManager fragmentManager = getFragmentManager();
////        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////
////        AccountsFragment fragment = new AccountsFragment();
////        fragmentTransaction.replace(R.id.container, fragment);
////        fragmentTransaction.commit();
//    }
//
//    public static class InviteFamilyFragment extends Fragment {
//
//        SharedPreferences sp;
//        EditText mEmail;
//        LinearLayout mInviteLl;
//        Button mInviteBtn, mAddBtn, mNext;
//
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            super.onCreateView(inflater, container, savedInstanceState);
//            // layout is inflated here
//            return inflater.inflate(R.layout.activity_accept, container, false);
//        }
//
//        public void onActivityCreated(Bundle savedInstanceState) {
//            super.onActivityCreated(savedInstanceState);
//            View rootView = getView();
//            mEmail = (EditText) rootView.findViewById(R.id.invite_name);
//            mInviteBtn = (Button) rootView.findViewById(R.id.invite_btn);
//            mInviteLl = (LinearLayout) rootView.findViewById(R.id.invite_ll);
//            mAddBtn = (Button) rootView.findViewById(R.id.invite_add);
//            mNext = (Button) rootView.findViewById(R.id.invite_next);
//
//            mInviteBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mInviteLl.setVisibility(View.VISIBLE);
//                }
//            });
//
//            mAddBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onInviteAdd(v);
//                }
//            });
//        }
//
//        public void onInviteAdd (View view)
//        {
//            ParseQuery<ParseUser> invite = ParseQuery.getQuery("_User");
//
//        }
//    }
//}
//
