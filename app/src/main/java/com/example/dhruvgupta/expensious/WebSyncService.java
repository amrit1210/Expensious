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

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
