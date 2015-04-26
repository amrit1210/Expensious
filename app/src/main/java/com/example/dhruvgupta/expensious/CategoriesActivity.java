package com.example.dhruvgupta.expensious;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
* Created by Gaurav on 19-Mar-15.
*/
public class CategoriesActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        this.setDefaultStartPositionNavigation(8);
//
//        android.support.v4.app.FragmentManager mFragmentManager = getSupportFragmentManager();
//        android.support.v4.app.Fragment mFragment=null;
//        mFragmentManager.beginTransaction().replace(layoutId, new PersonsFragment()).commit();
//
        CategoriesFragment fragment = new CategoriesFragment();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }

public static class CategoriesFragment extends Fragment
{
    Button mCat_Income,mCat_Expense;
    int c_type;
    CategoriesAdapter ad;
    ArrayList<String> al;
    ArrayList<SubCategoryDB>sub_cat_al;
    ArrayList<CategoryDB_Specific>cat_al;
    static List<String> s;
    DBHelper dbHelper;
    SharedPreferences sp;
    ListView mList_cat;
    Button category_btn_expense,category_btn_income;
    Set ids;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_categories,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View rootView=getView();
        al=new ArrayList();
        ids=new HashSet();
//            mapExpense=new HashMap<>();
//            mapIncome=new HashMap<>();
        s=new ArrayList();
        mList_cat = (ListView)rootView.findViewById(R.id.category_list);
        mCat_Income = (Button)rootView. findViewById(R.id.category_btn_income);
        mCat_Expense = (Button)rootView.findViewById(R.id.category_btn_expense);
        sp =getActivity(). getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        dbHelper =new DBHelper(getActivity());

        category_btn_expense=(Button)rootView.findViewById(R.id.category_btn_expense);
        category_btn_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExpenseBtnClick(v);
            }
        });
        category_btn_income=(Button)rootView.findViewById(R.id.category_btn_income);
        category_btn_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIncomeBtnClick(v);
            }
        });
        c_type=0;
        cat_al= dbHelper.getAllCategories(sp.getInt("UID",0),0);

        for(int i=0;i<cat_al.size();i++)
        {
            CategoryDB_Specific categoryDB_specific=cat_al.get(i);
            ids.add(categoryDB_specific.c_id);

            sub_cat_al=dbHelper.getAllSubCategories(sp.getInt("UID",0),categoryDB_specific.c_id);
            al.add(categoryDB_specific.c_id+"");

            // al.add(categoryDB_specific.c_name);
            for(int j=0;j<sub_cat_al.size();j++)
            {
                SubCategoryDB subCategoryDB=sub_cat_al.get(j);
                al.add(categoryDB_specific.c_id+"."+subCategoryDB.sub_id+"");

                //  al.add(subCategoryDB.sub_name);
                ids.add(categoryDB_specific.c_id + "." + subCategoryDB.sub_id);

            }

        }
        //  idList=toList(ids);
        Log.i(" ON CREATE ID:", ids + "");
        Log.i("ArrayList:",al+"");
        ad=new CategoriesAdapter(getActivity(),R.layout.list_category,al);
        mList_cat.setAdapter(ad);


        registerForContextMenu(mList_cat);
        toList();
    }
    public void onIncomeBtnClick(View v)
    {
        c_type=1;
        al.clear();
        ids.clear();
        // al1= setCategoryGroups();
        cat_al= dbHelper.getAllCategories(sp.getInt("UID",0),1);
        for(int i=0;i<cat_al.size();i++)
        {
            CategoryDB_Specific categoryDB_specific=cat_al.get(i);
            sub_cat_al=dbHelper.getAllSubCategories(sp.getInt("UID",0),categoryDB_specific.c_id);
            // al.add(categoryDB_specific.c_name);
            al.add(categoryDB_specific.c_id+"");
            ids.add(categoryDB_specific.c_id);

            for(int j=0;j<sub_cat_al.size();j++)
            {
                SubCategoryDB subCategoryDB=sub_cat_al.get(j);
                // al.add(subCategoryDB.sub_name);
                al.add(categoryDB_specific.c_id+"."+subCategoryDB.sub_id+"");
                ids.add( categoryDB_specific.c_id + "." + subCategoryDB.sub_id);

            }

        }
//        int j=0;
//        for( int i=0;i<al.size();i++)
//        {
//            TextView tv= (TextView) mList_cat.getChildAt(i);
//            tv.getText().equals(cat_al.get(j));
//            j++;
//            tv.setTypeface(null,Typeface.BOLD);
//
//        }
        //idList=toList(ids);
        Log.i(" INCOME ID:",ids+"");
        ad=new CategoriesAdapter(getActivity(),R.layout.list_category,null);
        ad=new CategoriesAdapter(getActivity(),R.layout.list_category,al);
        mList_cat.setAdapter(ad);
        registerForContextMenu(mList_cat);
        toList();
    }

    public void onExpenseBtnClick(View v)
    {
        c_type=0;
        //al1= setCategoryGroups();
        al.clear();
        cat_al= dbHelper.getAllCategories(sp.getInt("UID",0),0);
        int k=0;
        for(int i=0;i<cat_al.size();i++)
        {
            CategoryDB_Specific categoryDB_specific=cat_al.get(i);
            sub_cat_al=dbHelper.getAllSubCategories(sp.getInt("UID",0),categoryDB_specific.c_id);
            al.add(categoryDB_specific.c_id+"");
            // al.add(categoryDB_specific.c_name);

            ids.add(categoryDB_specific.c_id);
            for(int j=0;j<sub_cat_al.size();j++)
            {
                SubCategoryDB subCategoryDB=sub_cat_al.get(j);
                al.add(categoryDB_specific.c_id+"."+subCategoryDB.sub_id);
                al.add(subCategoryDB.sub_name);
                ids.add(categoryDB_specific.c_id + "." + subCategoryDB.sub_id);

            }

        }
//       mList_cat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//           View v;
//           @Override
//           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//               int count=parent.getChildCount();
//               v=parent.getChildAt(position);
//               parent.requestChildFocus(v, view);
//               v.setBackground(getDrawable(R.drawable.teal));
//
//               for (int i=0; i<count; i++)
//               {
//                   if (i!= position)
//                   {
//                       v = parent.getChildAt(i);
//                       v.setBackground(getDrawable(R.drawable.travel));
//
//                   }
//               }
//
//           }
//       });

        Log.i(" EXPENSE ID:",ids+"");
        ad=new CategoriesAdapter(getActivity(),R.layout.list_category,null);
        ad=new CategoriesAdapter(getActivity(),R.layout.list_category,al);
        mList_cat.setAdapter(ad);
        registerForContextMenu(mList_cat);
        toList();
    }
    public void toList()
    {
        Map map=new HashMap();
        cat_al= dbHelper.getCategories(sp.getInt("UID",0));
        //  int k=0;
        for(int i=0;i<cat_al.size();i++)
        {
            CategoryDB_Specific categoryDB_specific=cat_al.get(i);
            sub_cat_al=dbHelper.getAllSubCategories(sp.getInt("UID",0),categoryDB_specific.c_id);
            s.add(categoryDB_specific.c_id+"");

            for(int j=0;j<sub_cat_al.size();j++)
            {
                SubCategoryDB subCategoryDB=sub_cat_al.get(j);
                s.add(categoryDB_specific.c_id + "." + subCategoryDB.sub_id);

            }

        }
        //idList=toList(ids);
        Log.i(" ALL ID:",s+"");

    }

    public boolean onContextItemSelected(MenuItem item)
    {
        String cat[]=null;
        int cat_id,subcat_id=0;
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition = info.position;
        String str1 = (String) mList_cat.getAdapter().getItem(listPosition);
        Log.i("str1:",str1);
        int id=item.getItemId();
        if(str1.contains("."))
        {
            cat= str1.split("\\.");
            // Log.i("cat[]", cat[0]);
            cat_id = Integer.parseInt(cat[0]);
            subcat_id = Integer.parseInt(cat[1]);
        }
        else
        {
            cat_id = Integer.parseInt(str1);
        }
        if(id==R.id.Edit)
        {
            Log.i("SUBID:",subcat_id+"");
            if(subcat_id==0) {
                Cursor c = dbHelper.getCategoryData(cat_id,sp.getInt("UID",0));
                c.moveToFirst();
                int c_uid = c.getInt(c.getColumnIndex(DBHelper.CATEGORY_COL_C_UID));
                String c_name = c.getString(c.getColumnIndex(DBHelper.CATEGORY_COL_C_NAME));
                int c_type = c.getInt(c.getColumnIndex(DBHelper.CATEGORY_COL_C_TYPE));
                String c_icon = c.getString(c.getColumnIndex(DBHelper.CATEGORY_COL_C_ICON));
                Intent i = new Intent(getActivity(), AddCategoryActivity.class);
                i.putExtra("cat_id", cat_id);
                i.putExtra("c_u_id", c_uid);
                i.putExtra("c_name", c_name);
                i.putExtra("c_type", c_type);
                i.putExtra("c_icon", c_icon);
                startActivity(i);
            }
            else if(subcat_id>0)
            {

                Cursor c = dbHelper.getSubCategoryData(subcat_id,sp.getInt("UID",0));
                c.moveToFirst();
                int sub_uid = c.getInt(c.getColumnIndex(DBHelper.SUBCATEGORY_COL_SUB_UID));
                String sub_name = c.getString(c.getColumnIndex(DBHelper.SUBCATEGORY_COL_SUB_NAME));
                String sub_icon = c.getString(c.getColumnIndex(DBHelper.SUBCATEGORY_COL_SUB_ICON));
                c.close();
                Cursor c1=dbHelper.getCategoryData(cat_id,sp.getInt("UID",0));
                c1.moveToFirst();
                String c_name=c1.getString(c1.getColumnIndex(DBHelper.CATEGORY_COL_C_NAME));
                c1.close();
                Intent i = new Intent(getActivity(), AddCategoryActivity.class);
                i.putExtra("cat_id", cat_id);
                i.putExtra("c_u_id", sub_uid);
                i.putExtra("sub_name", sub_name);
                i.putExtra("sub_id", subcat_id);
                i.putExtra("sub_icon", sub_icon);
                i.putExtra("c_name",c_name);

                startActivity(i);

            }
        }

        if(id==R.id.Delete)
        {
            Log.i("SUBID:",subcat_id+"");
            if(subcat_id==0) {
                if( dbHelper.deleteCategory(cat_id, sp.getInt("UID", 0))>0) {

                    Intent i = new Intent(getActivity(), CategoriesActivity.class);
                    startActivity(i);
                    Toast.makeText(getActivity(), "Category Deleted", Toast.LENGTH_LONG).show();
                }
            }
            else if(subcat_id>0)
            {
                if(dbHelper.deleteSubCategory(subcat_id,sp.getInt("UID",0))>0) {
                    Intent i = new Intent(getActivity(), CategoriesActivity.class);
                    startActivity(i);
                    Toast.makeText(getActivity(), "SubCategory Deleted", Toast.LENGTH_LONG).show();
                }
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater=getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu,menu);
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
