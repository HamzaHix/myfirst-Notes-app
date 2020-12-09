//*********************************************\\


// INTERNAL DATA BASE \\


//*********************************************\\

package com.hixcoder.notesapp.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDataBase extends SQLiteAssetHelper {

    public static final String DB_name = "Notes.db";
    public static final int DB_version = 1;
    public static final String NOTES_TB_NAME = "MyNotes";
    public static final String NOTES_CLN_ID = "ID";
    public static final String NOTES_CLN_TITLE = "Title";
    public static final String NOTES_CLN_NOTE = "Note";

    // constructor
    public MyDataBase(Context context) {
        super(context, DB_name, null, DB_version);
    }


    // يتم استدعاؤها عند تحدبث (تغيير الاصدار) database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + NOTES_TB_NAME);
        onCreate(db);
    }


}
