package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Gaurav on 20-Apr-15.
 */
public class CategoriesViewList extends ActionBarActivity
{
    int c_type;
    CategoriesAdapter ad;
    ArrayList<String> al;
    ArrayList<SubCategoryDB>sub_cat_al;
    ArrayList<CategoryDB_Specific>cat_al;
    List<String> s;


    Set ids;

    static HashMap<String,String> mapExpense = new HashMap<>();
    static HashMap<String ,String>mapIncome=new HashMap<>();

    ListView categoryList;
    CategoriesAdapter categoriesAdapter;
    DBHelper dbHelper;
    ArrayList<String> al2;
    ArrayList<String> al1;
    SharedPreferences sp;
    ArrayList<String> keys;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        categoryList = (ListView) findViewById(R.id.category_list);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);

        sp = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        dbHelper = new DBHelper(CategoriesViewList.this);

        al=new ArrayList();
        ids=new HashSet();
//            mapExpense=new HashMap<>();
//            mapIncome=new HashMap<>();

        c_type=0;
//        cat_al= dbHelper.getAllCategories(sp.getInt("UID",0),0);
//
//        for(int i=0;i<cat_al.size();i++)
//        {
//            CategoryDB_Specific categoryDB_specific=cat_al.get(i);
//            ids.add(categoryDB_specific.c_id);
//
//            sub_cat_al=dbHelper.getAllSubCategories(sp.getInt("UID",0),categoryDB_specific.c_id);
//            al.add(categoryDB_specific.c_id+"");
//            mapExpense.put(categoryDB_specific.c_id+"",categoryDB_specific.c_name);
//            // al.add(categoryDB_specific.c_name);
//            for(int j=0;j<sub_cat_al.size();j++)
//            {
//                SubCategoryDB subCategoryDB=sub_cat_al.get(j);
//                al.add(categoryDB_specific.c_id+"."+subCategoryDB.sub_id+"");
//                mapExpense.put(categoryDB_specific.c_id+"."+subCategoryDB.sub_id,subCategoryDB.sub_name);
//                //  al.add(subCategoryDB.sub_name);
//                ids.add(categoryDB_specific.c_id + "." + subCategoryDB.sub_id);
//
//            }
//
//        }
//        //  idList=toList(ids);
//        Log.i(" ON CREATE ID:", ids + "");
//        Log.i("ArrayList:",al+"");
//        toList();
//
//
//        Log.i("Arraylist al:",al+"");
//        al2=(ArrayList)s;
//        categoriesAdapter = new CategoriesAdapter(CategoriesViewList.this, R.layout.list_category,al2);
//        categoryList.setAdapter(categoriesAdapter);
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
             //   String s = categoriesAdapter.getItem(position);

              String str=s.get(position);
                Intent i = new Intent();
                i.putExtra("Category_Id",str);
              //  i.putExtra("Cat_Id",)
                setResult(1, i);
                CategoriesViewList.this.finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CategoriesViewList.this,AddCategoryActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        cat_al= dbHelper.getAllCategories(sp.getInt("UID",0),0);

        for(int i=0;i<cat_al.size();i++)
        {
            CategoryDB_Specific categoryDB_specific=cat_al.get(i);
            ids.add(categoryDB_specific.c_id);

            sub_cat_al=dbHelper.getAllSubCategories(sp.getInt("UID",0),categoryDB_specific.c_id);
            al.add(categoryDB_specific.c_id+"");
            mapExpense.put(categoryDB_specific.c_id+"",categoryDB_specific.c_name);
            // al.add(categoryDB_specific.c_name);
            for(int j=0;j<sub_cat_al.size();j++)
            {
                SubCategoryDB subCategoryDB=sub_cat_al.get(j);
                al.add(categoryDB_specific.c_id+"."+subCategoryDB.sub_id+"");
                mapExpense.put(categoryDB_specific.c_id+"."+subCategoryDB.sub_id,subCategoryDB.sub_name);
                //  al.add(subCategoryDB.sub_name);
                ids.add(categoryDB_specific.c_id + "." + subCategoryDB.sub_id);

            }

        }
        //  idList=toList(ids);
        Log.i(" ON CREATE ID:", ids + "");
        Log.i("ArrayList:",al+"");
        toList();


        Log.i("Arraylist al:",al+"");
        al2=(ArrayList)s;
        categoriesAdapter = new CategoriesAdapter(CategoriesViewList.this, R.layout.list_category,al2);
        categoryList.setAdapter(categoriesAdapter);
    }
    public void onExpenseBtnClick(View v)
    {

        c_type=0;
        //al1= setCategoryGroups();
        al.clear();
        ids.clear();
        s.clear();
        cat_al= dbHelper.getAllCategories(sp.getInt("UID",0),0);
        int k=0;
        for(int i=0;i<cat_al.size();i++)
        {
            CategoryDB_Specific categoryDB_specific=cat_al.get(i);
            sub_cat_al=dbHelper.getAllSubCategories(sp.getInt("UID",0),categoryDB_specific.c_id);
            al.add(categoryDB_specific.c_id+"");
            // al.add(categoryDB_specific.c_name);
            mapExpense.put(categoryDB_specific.c_id+"",categoryDB_specific.c_name);
            ids.add(categoryDB_specific.c_id);
            for(int j=0;j<sub_cat_al.size();j++)
            {
                SubCategoryDB subCategoryDB=sub_cat_al.get(j);
                al.add(categoryDB_specific.c_id+"."+subCategoryDB.sub_id);
                al.add(subCategoryDB.sub_name);
                ids.add(categoryDB_specific.c_id + "." + subCategoryDB.sub_id);
                mapExpense.put(categoryDB_specific.c_id+"."+subCategoryDB.sub_id,subCategoryDB.sub_name);
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
        toList();
        Log.i(" EXPENSE ID:",ids+"");
//        ad=new CategoriesAdapter(CategoriesActivity.this,R.layout.list_category,null);
//        ad=new CategoriesAdapter(CategoriesActivity.this,R.layout.list_category,al);
        al2=(ArrayList)s;
        categoriesAdapter = new CategoriesAdapter(CategoriesViewList.this, R.layout.list_category,al2);
        categoryList.setAdapter(categoriesAdapter);



        //al=dbHelper.getCategoryColName(sp.getInt("UID",0),0);
       // al = (ArrayList) CategoriesActivity.s;
    }
    public void onIncomeBtnClick(View v)

    {

        c_type=1;
        al.clear();
        ids.clear();
        s.clear();
        // al1= setCategoryGroups();
        cat_al= dbHelper.getAllCategories(sp.getInt("UID",0),1);
        for(int i=0;i<cat_al.size();i++)
        {
            CategoryDB_Specific categoryDB_specific=cat_al.get(i);
            sub_cat_al=dbHelper.getAllSubCategories(sp.getInt("UID",0),categoryDB_specific.c_id);
            // al.add(categoryDB_specific.c_name);
            al.add(categoryDB_specific.c_id+"");
            ids.add(categoryDB_specific.c_id);
            mapIncome.put(categoryDB_specific.c_id+"",categoryDB_specific.c_name);
            for(int j=0;j<sub_cat_al.size();j++)
            {
                SubCategoryDB subCategoryDB=sub_cat_al.get(j);
                // al.add(subCategoryDB.sub_name);
                al.add(categoryDB_specific.c_id+"."+subCategoryDB.sub_id+"");
                ids.add( categoryDB_specific.c_id + "." + subCategoryDB.sub_id);
                mapIncome.put(categoryDB_specific.c_id+"."+subCategoryDB.sub_id,subCategoryDB.sub_name);
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
        toList();
        Log.i(" INCOME ID:",ids+"");
//        ad=new CategoriesAdapter(CategoriesActivity.this,R.layout.list_category,null);
//        ad=new CategoriesAdapter(CategoriesActivity.this,R.layout.list_category,al);
        al2=(ArrayList)s;
        categoriesAdapter = new CategoriesAdapter(CategoriesViewList.this, R.layout.list_category,al2);
        categoryList.setAdapter(categoriesAdapter);

      //  al=dbHelper.getCategoryColName(sp.getInt("UID",0),1);
        //al = (ArrayList) CategoriesActivity.s;
    }
    public void toList()
    {
        s=new ArrayList<>();
        Map map=new HashMap();
        cat_al= dbHelper.getAllCategories(sp.getInt("UID",0),c_type);
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
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
    }
}
