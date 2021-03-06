/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.fashiontec.bodyapps.db.DBContract;
import org.fashiontec.bodyapps.db.DatabaseHandler;
import org.fashiontec.bodyapps.models.Person;

/**
 * Manages the DB requests to person table
 */
public class PersonManager {
    private SQLiteDatabase database;
    private DatabaseHandler dbHandler;
    private static PersonManager PersonManager;
    static final String TAG = PersonManager.class.getName();

    private PersonManager(Context context) {
        dbHandler = DatabaseHandler.getInstance(context);
    }

    public static PersonManager getInstance(Context context) {
        if (PersonManager == null) {
            PersonManager = new PersonManager(context);
        }
        return PersonManager;
    }

    /**
     * Gets the person ID from email
     *
     * @param person
     * @return
     */
    public int getPerson(Person person) {
        Log.d(TAG, "getPerson");
        this.database = this.dbHandler.getReadableDatabase();
        Cursor cursor = database.query(DBContract.Person.TABLE_NAME,
                new String[]{DBContract.Person.COLUMN_NAME_ID},
                DBContract.Person.COLUMN_NAME_EMAIL + " ='" + person.getEmail()
                        + "'", null, null, null, null
        );

        if (cursor.moveToFirst()) {

            int out = cursor.getInt(0);
            cursor.close();
            database.close();
            return out;
        } else {
            return -1;
        }
    }

    /**
     * Adds a person to the DB
     *
     * @param person
     */
    public void addPerson(Person person) {
        Log.d(TAG, "addPerson");
        this.database = this.dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Person.COLUMN_NAME_EMAIL, person.getEmail());
        values.put(DBContract.Person.COLUMN_NAME_NAME, person.getName());
        values.put(DBContract.Person.COLUMN_NAME_GENDER, person.getGender());
        values.put(DBContract.Person.COLUMN_NAME_DOB, person.getDob());
        database.insert(DBContract.Person.TABLE_NAME, null, values);
        database.close();
    }

    /**
     * deletes person from DB
     *
     * @param person
     */
    public void delPerson(Person person) {
        this.database = this.dbHandler.getWritableDatabase();
        database.delete(DBContract.Person.TABLE_NAME,
                DBContract.User.COLUMN_NAME_ID + " ='" + person.getID() + "'",
                null);
        database.close();
    }

    public Person getPersonbyID(int id) {
        Log.d(TAG, "getPerson");
        this.database = this.dbHandler.getReadableDatabase();
        Cursor cursor = database.query(DBContract.Person.TABLE_NAME,
                DBContract.Person.allColumns,
                DBContract.Person.COLUMN_NAME_ID + " =" + id
                , null, null, null, null);

        if (cursor.moveToFirst()) {

            Person out = new Person(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getLong(3));
            cursor.close();
            database.close();
            return out;
        } else {
            return null;
        }
    }
}
