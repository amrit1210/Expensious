package com.example.dhruvgupta.expensious;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

/**
* Created by dhruvgupta on 4/20/2015.
*/
public class WebSyncService extends Service {
    SharedPreferences sp;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sp=getSharedPreferences("USER_PREFS",MODE_PRIVATE);

        Toast.makeText(WebSyncService.this, "Service Started", Toast.LENGTH_LONG).show();

        //Add Person
        ParseQuery<ParseObject> addPer = ParseQuery.getQuery("Persons");
        addPer.fromPin("pinPersons");
        addPer.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> todos, ParseException e) {
                if (e == null) {
                    for (final ParseObject todo : todos) {
                        todo.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
//                                    todo.unpinInBackground("pinPersons");
                                    System.out.println("Person DATA is saved ..... ");
                            }
                        });
                    }
                } else {
                    Log.i("MainActivity", "syncTodosToPerAdd: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        });

        //Update person
        ParseQuery<ParseObject> updatePer = ParseQuery.getQuery("Persons");
        updatePer.whereEqualTo("p_uid", sp.getInt("UID", 0));
        updatePer.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> updatePer2 = ParseQuery.getQuery("Persons");
                        updatePer2.fromPin("pinPersonsUpdate");
                        updatePer2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    if ((todo.getInt("p_uid") == todo1.getInt("p_uid")) && (todo.getInt("p_id") == todo1.getInt("p_id")))
                                    {
                                        todo.fetchInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject parseObject, ParseException e) {
                                                if (e==null)
                                                {
                                                    parseObject.put("objectId", todo.getObjectId());
                                                    parseObject.put("p_id", todo1.getInt("p_id"));
                                                    parseObject.put("p_uid", todo1.getInt("p_uid"));
                                                    parseObject.put("p_name", todo1.getString("p_name"));
                                                    parseObject.put("p_color", todo1.getString("p_color"));
                                                    parseObject.put("p_color_code", todo1.getString("p_color_code"));
                                                    parseObject.saveInBackground(new SaveCallback() {

                                                        @Override
                                                        public void done(ParseException e) {
                                                            todo1.unpinInBackground("pinPersonsUpdate");
                                                                System.out.println("Person DATA is updated ..... ");
                                                        }
                                                    });
                                                }
                                                else
                                                {

                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    Log.i("MainActivity", "syncTodosToPerUpdate: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //Delete person
        ParseQuery<ParseObject> deletePer = ParseQuery.getQuery("Persons");
        deletePer.whereEqualTo("p_uid", sp.getInt("UID", 0));
        deletePer.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> deletePer2 = ParseQuery.getQuery("Persons");
                        deletePer2.fromPin("pinPersonsDelete");
                        deletePer2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    if ((todo.getInt("p_uid") == todo1.getInt("p_uid")) && (todo.getInt("p_id") == todo1.getInt("p_id")))
                                    {
                                        todo.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                todo1.unpinInBackground("pinPersonsDelete");
                                                    System.out.println("Person DATA is deleted ..... ");
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    Log.i("MainActivity", "syncTodosToPerDelete: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //Add Account
        ParseQuery<ParseObject> addAcc = ParseQuery.getQuery("Accounts");
        addAcc.fromPin("pinAccounts");
        addAcc.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> todos, ParseException e) {
                if (e == null) {
                    for (final ParseObject todo : todos) {
                        todo.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
//                                    todo.unpinInBackground("pinAccounts");
                                System.out.println("Account DATA is saved ..... ");
                            }
                        });
                    }
                } else {
                    Log.i("MainActivity", "syncTodosToAccAdd: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        });

        //Update Account
        ParseQuery<ParseObject> updateAcc = ParseQuery.getQuery("Accounts");
        updateAcc.whereEqualTo("acc_uid", sp.getInt("UID", 0));
        updateAcc.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> updateAcc2 = ParseQuery.getQuery("Accounts");
                        updateAcc2.fromPin("pinAccountsUpdate");
                        updateAcc2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    if ((todo.getInt("acc_uid") == todo1.getInt("acc_uid")) && (todo.getInt("acc_id") == todo1.getInt("acc_id")))
                                    {
                                        todo.fetchInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject parseObject, ParseException e) {
                                                if (e==null)
                                                {
                                                    parseObject.put("objectId", todo.getObjectId());
                                                    parseObject.put("acc_id", todo1.getInt("acc_id"));
                                                    parseObject.put("acc_uid", todo1.getInt("acc_uid"));
                                                    parseObject.put("acc_name", todo1.getString("acc_name"));
                                                    parseObject.put("acc_balance", todo1.getDouble("acc_balance"));
                                                    parseObject.put("acc_note", todo1.getString("acc_note"));
                                                    parseObject.put("acc_show", todo1.getInt("acc_show"));
                                                    parseObject.saveInBackground(new SaveCallback() {

                                                        @Override
                                                        public void done(ParseException e) {
                                                            todo1.unpinInBackground("pinAccountsUpdate");
                                                            System.out.println("Account DATA is updated ..... ");
                                                        }
                                                    });
                                                }
                                                else
                                                {

                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    Log.i("MainActivity", "syncTodosToAccUpdate: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //Delete Account
        ParseQuery<ParseObject> deleteAcc = ParseQuery.getQuery("Accounts");
        deleteAcc.whereEqualTo("acc_uid", sp.getInt("UID", 0));
        deleteAcc.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> deleteAcc2 = ParseQuery.getQuery("Accounts");
                        deleteAcc2.fromPin("pinAccountsDelete");
                        deleteAcc2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    if ((todo.getInt("acc_uid") == todo1.getInt("acc_uid")) && (todo.getInt("acc_id") == todo1.getInt("acc_id")))
                                    {
                                        todo.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                todo1.unpinInBackground("pinAccountsDelete");
                                                System.out.println("Account DATA is deleted ..... ");
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    Log.i("MainActivity", "syncTodosToAccDelete: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //Add Budget
        ParseQuery<ParseObject> addBud = ParseQuery.getQuery("Budget");
        addBud.fromPin("pinBudget");
        addBud.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> todos, ParseException e) {
                if (e == null) {
                    for (final ParseObject todo : todos) {
                        todo.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
//                                    todo.unpinInBackground("pinBudget");
                                System.out.println("Budget DATA is saved ..... ");
                            }
                        });
                    }
                } else {
                    Log.i("MainActivity", "syncTodosToBudAdd: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        });

        //Update Budget
        ParseQuery<ParseObject> updateBud = ParseQuery.getQuery("Budget");
        updateBud.whereEqualTo("b_uid", sp.getInt("UID", 0));
        updateBud.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> updateBud2 = ParseQuery.getQuery("Budget");
                        updateBud2.fromPin("pinBudgetUpdate");
                        updateBud2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    if ((todo.getInt("b_uid") == todo1.getInt("b_uid")) && (todo.getInt("b_id") == todo1.getInt("b_id")))
                                    {
                                        todo.fetchInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject parseObject, ParseException e) {
                                                if (e==null)
                                                {
                                                    parseObject.put("objectId", todo.getObjectId());
                                                    parseObject.put("b_id", todo1.getInt("b_id"));
                                                    parseObject.put("b_uid", todo1.getInt("b_uid"));
                                                    parseObject.put("b_amount", todo1.getDouble("b_amount"));
                                                    parseObject.put("b_startDate", todo1.getString("b_startDate"));
                                                    parseObject.put("b_endDate", todo1.getString("b_endDate"));
                                                    parseObject.saveInBackground(new SaveCallback() {

                                                        @Override
                                                        public void done(ParseException e) {
                                                            todo1.unpinInBackground("pinBudgetUpdate");
                                                            System.out.println("Budget DATA is updated ..... ");
                                                        }
                                                    });
                                                }
                                                else
                                                {

                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    Log.i("MainActivity", "syncTodosToBudUpdate: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //Delete Budget
        ParseQuery<ParseObject> deleteBud = ParseQuery.getQuery("Budget");
        deleteBud.whereEqualTo("b_uid", sp.getInt("UID", 0));
        deleteBud.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> deleteBud2 = ParseQuery.getQuery("Budget");
                        deleteBud2.fromPin("pinBudgetDelete");
                        deleteBud2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    if ((todo.getInt("b_uid") == todo1.getInt("b_uid")) && (todo.getInt("b_id") == todo1.getInt("b_id")))
                                    {
                                        todo.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                todo1.unpinInBackground("pinBudgetDelete");
                                                System.out.println("Budget DATA is deleted ..... ");
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    Log.i("MainActivity", "syncTodosToBudDelete: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //Add Transaction
        ParseQuery<ParseObject> addTrans = ParseQuery.getQuery("Transactions");
        addTrans.fromPin("pinTransactions");
        addTrans.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> todos, ParseException e) {
                if (e == null) {
                    for (final ParseObject todo : todos) {
                        todo.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
//                                    todo.unpinInBackground("pinTransactions");
                                System.out.println("Transactions DATA is saved ..... ");
                            }
                        });
                    }
                } else {
                    Log.i("MainActivity", "syncTodosToTransAdd: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        });

        //Update Transactions
        ParseQuery<ParseObject> updateTrans = ParseQuery.getQuery("Transactions");
        updateTrans.whereEqualTo("trans_uid", sp.getInt("UID", 0));
        updateTrans.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> updateTrans2 = ParseQuery.getQuery("Transactions");
                        updateTrans2.fromPin("pinTransactionsUpdate");
                        updateTrans2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    if ((todo.getInt("trans_uid") == todo1.getInt("trans_uid")) && (todo.getInt("trans_id") == todo1.getInt("trans_id")))
                                    {
                                        todo.fetchInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject parseObject, ParseException e) {
                                                if (e==null)
                                                {
                                                    parseObject.put("objectId", todo.getObjectId());
                                                    parseObject.put("trans_id", todo1.getInt("trans_id"));
                                                    parseObject.put("trans_uid", todo1.getInt("trans_uid"));
                                                    parseObject.put("trans_rec_id", todo1.getInt("trans_rec_id"));
                                                    parseObject.put("trans_from_acc", todo1.getInt("trans_from_acc"));
                                                    parseObject.put("trans_to_acc", todo1.getInt("trans_to_acc"));
                                                    parseObject.put("trans_show", todo1.getInt("trans_show"));
                                                    parseObject.put("trans_date", todo1.getString("trans_date"));
                                                    parseObject.put("trans_time", todo1.getString("trans_time"));
                                                    parseObject.put("trans_note", todo1.getString("trans_note"));
                                                    parseObject.put("trans_category", todo1.getInt("trans_category"));
                                                    parseObject.put("trans_subcategory", todo1.getInt("trans_subcategory"));
                                                    parseObject.put("trans_balance", todo1.getDouble("trans_balance"));
                                                    parseObject.put("trans_type", todo1.getString("trans_type"));
                                                    parseObject.put("trans_person", todo1.getInt("trans_person"));
                                                    parseObject.saveInBackground(new SaveCallback() {

                                                        @Override
                                                        public void done(ParseException e) {
                                                            todo1.unpinInBackground("pinTransactionsUpdate");
                                                            System.out.println("Transactions DATA is updated ..... ");
                                                        }
                                                    });
                                                }
                                                else
                                                {

                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    Log.i("MainActivity", "syncTodosToTransUpdate: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //Delete Transactions
        ParseQuery<ParseObject> deleteTrans = ParseQuery.getQuery("Transactions");
        deleteTrans.whereEqualTo("trans_uid", sp.getInt("UID", 0));
        deleteTrans.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> deleteTrans2 = ParseQuery.getQuery("Transactions");
                        deleteTrans2.fromPin("pinTransactionsDelete");
                        deleteTrans2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    if ((todo.getInt("trans_uid") == todo1.getInt("trans_uid")) && (todo.getInt("trans_id") == todo1.getInt("trans_id")))
                                    {
                                        todo.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                todo1.unpinInBackground("pinTransactionsDelete");
                                                System.out.println("Transactions DATA is deleted ..... ");
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    Log.i("MainActivity", "syncTodosToTransDelete: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //Add Category
        ParseQuery<ParseObject> addCategory = ParseQuery.getQuery("Category_specific");
        addCategory.fromPin("pinCategory");
        addCategory.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> todos, ParseException e) {
                if (e == null) {
                    for (final ParseObject todo : todos) {
                        todo.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
//                                    todo.unpinInBackground("pinTransactions");
                                System.out.println("Category DATA is saved ..... ");
                            }
                        });
                    }
                } else {
                    Log.i("MainActivity", "syncTodosToCategoryAdd: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        });

        //Update Category
        ParseQuery<ParseObject> updateCategory = ParseQuery.getQuery("Category_specific");
        updateCategory.whereEqualTo("c_uid", sp.getInt("UID", 0));
        updateCategory.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> updateCategory2 = ParseQuery.getQuery("Category_specific");
                        updateCategory2.fromPin("pinCategoryUpdate");
                        updateCategory2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    if ((todo.getInt("c_uid") == todo1.getInt("c_uid")) && (todo.getInt("c_id") == todo1.getInt("c_id")))
                                    {
                                        todo.fetchInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject parseObject, ParseException e) {
                                                if (e==null)
                                                {
                                                    parseObject.put("objectId", todo.getObjectId());
                                                    parseObject.put("c_id", todo1.getInt("c_id"));
                                                    parseObject.put("c_uid", todo1.getInt("c_uid"));
                                                    parseObject.put("c_name", todo1.getInt("c_name"));
                                                    parseObject.put("c_type", todo1.getInt("c_type"));
                                                    parseObject.put("c_icon", todo1.getInt("c_icon"));

                                                    parseObject.saveInBackground(new SaveCallback() {

                                                        @Override
                                                        public void done(ParseException e) {
                                                            todo1.unpinInBackground("pinCategoryUpdate");
                                                            System.out.println("Category DATA is updated ..... ");
                                                        }
                                                    });
                                                }
                                                else
                                                {

                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    Log.i("MainActivity", "syncTodosToCategoryUpdate: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //Delete Category
        final ParseQuery<ParseObject> deleteCategory = ParseQuery.getQuery("Category_specific");
        deleteCategory.whereEqualTo("c_uid", sp.getInt("UID", 0));
        deleteCategory.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> deleteCategory2 = ParseQuery.getQuery("Category_specific");
                        deleteCategory2.fromPin("pinCategoryDelete");
                        deleteCategory2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    if ((todo.getInt("c_uid") == todo1.getInt("c_uid")) && (todo.getInt("c_id") == todo1.getInt("c_id")))
                                    {
                                        todo.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                todo1.unpinInBackground("pinCategoryDelete");
                                                System.out.println("Category DATA is deleted ..... ");
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    Log.i("MainActivity", "syncTodosToCategoryDelete: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //Add SubCategory
        ParseQuery<ParseObject> addSubcategory = ParseQuery.getQuery("Sub_category");
        addSubcategory.fromPin("pinSubCategory");
        addSubcategory.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> todos, ParseException e) {
                if (e == null) {
                    for (final ParseObject todo : todos) {
                        todo.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
//                                    todo.unpinInBackground("pinTransactions");
                                System.out.println("SubCategory DATA is saved ..... ");
                            }
                        });
                    }
                } else {
                    Log.i("MainActivity", "syncTodosToTransAdd: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        });

        //Update SubCategory
        ParseQuery<ParseObject> updateSubcategory = ParseQuery.getQuery("Sub_category");
        updateSubcategory.whereEqualTo("sub_uid", sp.getInt("UID", 0));
        updateSubcategory.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> updateSubcategory2 = ParseQuery.getQuery("Sub_category");
                        updateSubcategory2.fromPin("pinSubCategoryUpdate");
                        updateSubcategory2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    if ((todo.getInt("sub_uid") == todo1.getInt("sub_uid")) && (todo.getInt("sub_id") == todo1.getInt("sub_id")))
                                    {
                                        todo.fetchInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject parseObject, ParseException e) {
                                                if (e==null)
                                                {
                                                    parseObject.put("objectId", todo.getObjectId());
                                                    parseObject.put("sub_id", todo1.getInt("sub_id"));
                                                    parseObject.put("sub_uid", todo1.getInt("sub_uid"));
                                                    parseObject.put("sub_c_id", todo1.getInt("sub_c_id"));
                                                    parseObject.put("sub_name", todo1.getInt("sub_name"));
                                                    parseObject.saveInBackground(new SaveCallback() {

                                                        @Override
                                                        public void done(ParseException e) {
                                                            todo1.unpinInBackground("pinSubCategoryUpdate");
                                                            System.out.println("Subcategory DATA is updated ..... ");
                                                        }
                                                    });
                                                }
                                                else
                                                {

                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    Log.i("MainActivity", "syncTodosToSubcategoryUpdate: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //Delete SubCategory
        ParseQuery<ParseObject> deleteSubcategory = ParseQuery.getQuery("Sub_category");
        deleteSubcategory.whereEqualTo("sub_uid", sp.getInt("UID", 0));
        deleteSubcategory.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> deleteSubCategory2 = ParseQuery.getQuery("Sub_category");
                        deleteSubCategory2.fromPin("pinSubCategoryDelete");
                        deleteSubCategory2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    if ((todo.getInt("sub_uid") == todo1.getInt("sub_uid")) && (todo.getInt("sub_id") == todo1.getInt("sub_id")))
                                    {
                                        todo.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                todo1.unpinInBackground("pinSubCategoryDelete");
                                                System.out.println("SubCategory DATA is deleted ..... ");
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    Log.i("MainActivity", "syncTodosToSubCategoryDelete: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });


        //Add Recursive
        ParseQuery<ParseObject> addRec = ParseQuery.getQuery("Recursive");
        addRec.fromPin("pinRecursive");
        addRec.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> todos, ParseException e) {
                if (e == null) {
                    for (final ParseObject todo : todos) {
                        todo.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
//                                    todo.unpinInBackground("pinRecursive");
                                System.out.println("Recursive DATA is saved ..... ");
                            }
                        });
                    }
                } else {
                    Log.i("MainActivity", "syncTodosToRecAdd: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        });

        //Update Recursive
        ParseQuery<ParseObject> updateRec = ParseQuery.getQuery("Recursive");
        updateRec.whereEqualTo("rec_uid", sp.getInt("UID", 0));
        updateRec.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> updateRec2 = ParseQuery.getQuery("Recursive");
                        updateRec2.fromPin("pinRecursiveUpdate");
                        updateRec2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    if ((todo.getInt("rec_uid") == todo1.getInt("rec_uid")) && (todo.getInt("rec_id") == todo1.getInt("rec_id")))
                                    {
                                        todo.fetchInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject parseObject, ParseException e) {
                                                if (e==null)
                                                {
                                                    parseObject.put("objectId", todo.getObjectId());
                                                    parseObject.put("rec_id", todo1.getInt("rec_id"));
                                                    parseObject.put("rec_uid", todo1.getInt("rec_uid"));
                                                    parseObject.put("rec_from_acc", todo1.getInt("rec_from_acc"));
                                                    parseObject.put("rec_to_acc", todo1.getInt("rec_to_acc"));
                                                    parseObject.put("rec_show", todo1.getInt("rec_show"));
                                                    parseObject.put("rec_start_date", todo1.getString("rec_start_date"));
                                                    parseObject.put("rec_end_date", todo1.getString("rec_end_date"));
                                                    parseObject.put("rec_next_date", todo1.getString("rec_next_date"));
                                                    parseObject.put("rec_time", todo1.getString("rec_time"));
                                                    parseObject.put("rec_recurring", todo1.getInt("rec_recurring"));
                                                    parseObject.put("rec_alert", todo1.getInt("rec_alert"));
                                                    parseObject.put("rec_category", todo1.getInt("rec_category"));
                                                    parseObject.put("rec_subcategory", todo1.getInt("rec_subcategory"));
                                                    parseObject.put("rec_note", todo1.getString("rec_note"));
                                                    parseObject.put("rec_balance", todo1.getDouble("rec_balance"));
                                                    parseObject.put("rec_type", todo1.getString("rec_type"));
                                                    parseObject.put("rec_person", todo1.getInt("rec_person"));
                                                    parseObject.saveInBackground(new SaveCallback() {

                                                        @Override
                                                        public void done(ParseException e) {
                                                            todo1.unpinInBackground("pinRecursiveUpdate");
                                                            System.out.println("Recursive DATA is updated ..... ");
                                                        }
                                                    });
                                                }
                                                else
                                                {

                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    Log.i("MainActivity", "syncTodosToRecUpdate: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //Delete Recursive
        ParseQuery<ParseObject> deleteRec = ParseQuery.getQuery("Recursive");
        deleteRec.whereEqualTo("rec_uid", sp.getInt("UID", 0));
        deleteRec.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> deleteRec2 = ParseQuery.getQuery("Recursive");
                        deleteRec2.fromPin("pinRecursiveDelete");
                        deleteRec2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    if ((todo.getInt("rec_uid") == todo1.getInt("rec_uid")) && (todo.getInt("rec_id") == todo1.getInt("rec_id")))
                                    {
                                        todo.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                todo1.unpinInBackground("pinRecursiveDelete");
                                                System.out.println("Recursive DATA is deleted ..... ");
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    Log.i("MainActivity", "syncTodosToRecDelete: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //addLoanDebts
        ParseQuery<ParseObject> addLoanDebts = ParseQuery.getQuery("Loan_debt");
        addLoanDebts.fromPin("pinLoanDebts");
        addLoanDebts.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> todos, ParseException e) {
                if (e == null) {
                    for (final ParseObject todo : todos) {
                        todo.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
//                                    todo.unpinInBackground("pinLoanDebts");
                                System.out.println("LoanDebts DATA is saved ..... ");
                            }
                        });
                    }
                } else {
                    Log.i("MainActivity", "syncTodosToLoanDebtsAdd: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        });

        //Update LoanDebts
        final ParseQuery<ParseObject> updateLoanDebts = ParseQuery.getQuery("Loan_debt");
        updateLoanDebts.whereEqualTo("loan_debt_uid", sp.getInt("UID", 0));
        updateLoanDebts.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> updateLoanDebts2 = ParseQuery.getQuery("Loan_debt");
                        updateLoanDebts2.fromPin("pinLoanDebtsUpdate");
                        updateLoanDebts2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    if ((todo.getInt("loan_debt_uid") == todo1.getInt("loan_debt_uid")) && (todo.getInt("loan_debt_id") == todo1.getInt("loan_debt_id")))
                                    {
                                        todo.fetchInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject parseObject, ParseException e) {
                                                if (e==null)
                                                {
                                                    parseObject.put("objectId", todo.getObjectId());
                                                    parseObject.put("loan_debt_id", todo1.getInt("loan_debt_id"));
                                                    parseObject.put("loan_debt_uid", todo1.getInt("loan_debt_uid"));
                                                    parseObject.put("loan_debt_parent", todo1.getInt("loan_debt_parent"));
                                                    parseObject.put("loan_debt_from_acc", todo1.getInt("loan_debt_from_acc"));
                                                    parseObject.put("loan_debt_to_acc", todo1.getInt("loan_debt_to_acc"));
                                                    parseObject.put("loan_debt_date", todo1.getString("loan_debt_date"));
                                                    parseObject.put("loan_debt_time", todo1.getString("loan_debt_time"));
                                                    parseObject.put("loan_debt_note", todo1.getString("loan_debt_note"));
                                                    parseObject.put("loan_debt_balance", todo1.getDouble("loan_debt_balance"));
                                                    parseObject.put("loan_debt_type", todo1.getString("loan_debt_type"));
                                                    parseObject.put("loan_debt_person", todo1.getInt("loan_debt_person"));
                                                    parseObject.saveInBackground(new SaveCallback() {

                                                        @Override
                                                        public void done(ParseException e) {
                                                            todo1.unpinInBackground("pinLoanDebtsUpdate");
                                                            System.out.println("LoanDebts DATA is updated ..... ");
                                                        }
                                                    });
                                                }
                                                else
                                                {

                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    Log.i("MainActivity", "syncTodosToLoanDebtsUpdate: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //Delete LoanDebts
        ParseQuery<ParseObject> deleteLD = ParseQuery.getQuery("Loan_debt");
        deleteLD.whereEqualTo("loan_debt_uid", sp.getInt("UID", 0));
        deleteLD.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> deleteLD2 = ParseQuery.getQuery("Loan_debt");
                        deleteLD2.fromPin("pinLoanDebtsDelete");
                        deleteLD2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    Log.i("vals", todo.getInt("loan_debt_uid") + ":" + todo1.getInt("loan_debt_uid") + ":" + todo.getInt("loan_debt_id") + ":" + todo1.getInt("loan_debt_id"));
                                    if ((todo.getInt("loan_debt_uid") == todo1.getInt("loan_debt_uid")) && (todo.getInt("loan_debt_id") == todo1.getInt("loan_debt_id")))
                                    {
                                        todo.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                todo1.unpinInBackground("pinLoanDebtsDelete");
                                                System.out.println("Loan debt DATA is deleted ..... ");
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    Log.i("MainActivity", "syncTodosToLdDelete: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //Add Settings
        ParseQuery<ParseObject> addSettings = ParseQuery.getQuery("Settings");
        addSettings.fromPin("pinSettings");
        addSettings.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> todos, ParseException e) {
                if (e == null) {
                    for (final ParseObject todo : todos) {
                        todo.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
//                                    todo.unpinInBackground("pinSettings");
                                System.out.println("Settings DATA is saved ..... ");
                            }
                        });
                    }
                } else {
                    Log.i("MainActivity", "syncTodosToAddSettings: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        });

        //UpdateSettings
        final ParseQuery<ParseObject> updateSettings = ParseQuery.getQuery("Settings");
        updateSettings.whereEqualTo("settings_uid", sp.getInt("UID", 0));
        updateSettings.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (final ParseObject todo : parseObjects) {

                        ParseQuery<ParseObject> updateSettings2 = ParseQuery.getQuery("Settings");
                        updateSettings2.fromPin("pinSettingsUpdate");
                        updateSettings2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                for (final ParseObject todo1 : parseObjects1) {
                                    if ((todo.getInt("settings_uid") == todo1.getInt("settings_uid")) && (todo.getInt("settings_id") == todo1.getInt("settings_id")))
                                    {
                                        todo.fetchInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject parseObject, ParseException e) {
                                                if (e==null)
                                                {
                                                    parseObject.put("objectId", todo.getObjectId());
                                                    parseObject.put("settings_uid", todo1.getInt("settings_uid"));
                                                    parseObject.put("settings_cur_code", todo1.getString("settings_cur_code"));
                                                    parseObject.saveInBackground(new SaveCallback() {

                                                        @Override
                                                        public void done(ParseException e) {
                                                            todo1.unpinInBackground("pinSettingssUpdate");
                                                            System.out.println("Settings DATA is updated ..... ");
                                                        }
                                                    });
                                                }
                                                else
                                                {

                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                else {
                    Log.i("MainActivity", "syncTodosToSettingsUpdate: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        });
        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
