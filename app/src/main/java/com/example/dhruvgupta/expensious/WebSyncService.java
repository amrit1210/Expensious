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
                        // Set isdraft flag to false before syncing to Parse
                        // todo.setDraft(false);
                        todo.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
                                //if (e == null) {
                                // Let adapter know to update view
//                                if (!isFinishing()) {
//                                    //refresh list data
//                                    todo.unpinInBackground("pinPersons");
                                    System.out.println("Person DATA is saved ..... ");
//
//                                }
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
                                                            //if (e == null)
                                                            // Let adapter know to update view
                                                            todo1.unpinInBackground("pinPersonsUpdate");
//                                                            if (!isFinishing()) {
                                                                //refresh list data
                                                                System.out.println("Person DATA is updated ..... ");
//                                                            }
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
//                                                if (!isFinishing()) {
                                                    //refresh list data
                                                    System.out.println("Person DATA is deleted ..... ");
//                                                }
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

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
