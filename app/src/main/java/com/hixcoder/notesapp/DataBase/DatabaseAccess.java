package com.hixcoder.notesapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static com.hixcoder.notesapp.DataBase.MyDataBase.NOTES_CLN_ID;
import static com.hixcoder.notesapp.DataBase.MyDataBase.NOTES_CLN_NOTE;
import static com.hixcoder.notesapp.DataBase.MyDataBase.NOTES_CLN_TITLE;
import static com.hixcoder.notesapp.DataBase.MyDataBase.NOTES_TB_NAME;

public class DatabaseAccess {

    private SQLiteDatabase dataBase;
    private SQLiteOpenHelper openHelper;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context){
        this.openHelper = new MyDataBase(context);
    }

    // دالة تساعدك في عدم تكرار عملية انشاء object من DatabaseAccess
    public static DatabaseAccess getInstance(Context context){

        if (instance == null){
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    // دالة لفتح dataBase
    public void open(){
        this.dataBase = this.openHelper.getWritableDatabase();
    }

    // دالة لغلق dataBase
    public void close(){
       if (this.dataBase != null){
           this.dataBase.close();
       }

    }

    // MyDataBase يجب انشاء دالة تكون وسبط بين الكلاسات الخرى وكلاس
    // الدالة يجب ان تضيف سيارة جديدة
    //دالة الاضافة
    public boolean insertNote(String title ,String note){

        ContentValues values = new ContentValues();
        values.put(NOTES_CLN_TITLE,title);
        values.put(NOTES_CLN_NOTE,note);

        long result = dataBase.insert(NOTES_TB_NAME,null,values);

        return result != -1;
    }

    //دالة التعديل
    public boolean updateNote(Note note){

        ContentValues values = new ContentValues();
        values.put(NOTES_CLN_TITLE,note.getTitle());
        values.put(NOTES_CLN_NOTE,note.getNote());

        String args[] = {note.getId()+""};  //لاجل تفادي sql injection
        long result = dataBase.update(NOTES_TB_NAME,values,"id=?",args);

        return result > 0;
    }

    // ارجاع عدد الصفوف في جدول معين
    public long getNotescount(){
        return DatabaseUtils.queryNumEntries(dataBase,NOTES_TB_NAME);
    }

    //دالة الحذف
    public boolean deleteNote(Note note){

        // هنا القيمة التي أحذف بناء علبها مثل model,id...
        String[] args = {note.getId() + ""};  //لاجل تفادي sql injection
        long result = dataBase.delete(NOTES_TB_NAME,"id=?",args);

        return result > 0;
    }

    //دالة الاسترجاع
    public ArrayList<Note> getAllNotes(){

        ArrayList<Note> notes = new ArrayList<>();

        Cursor cursor = dataBase.rawQuery("SELECT * FROM "+NOTES_TB_NAME,null);
        // كود التعامل مع الكيرسر وتحويله الى مصفوفة من نوع Note
        // لاجل فحص هل الكيرسر بحتوي على بيانات
        if (cursor != null && cursor.moveToFirst()){
            do {
                int id       = cursor.getInt(cursor.getColumnIndex(NOTES_CLN_ID));
                String title = cursor.getString(cursor.getColumnIndex(NOTES_CLN_TITLE));
                String note = cursor.getString(cursor.getColumnIndex(NOTES_CLN_NOTE));

                // here we store those info in notes arrayList
                notes.add(new Note(id,title,note));

            }while (cursor.moveToNext());
            // من الافضل ان تغلق الكيرسر بشكل يدوي
            cursor.close();
        }

        return notes;
    }

    //دالة البحث
    public ArrayList<Note> getNote(String modelSearch){

        ArrayList<Note> notes = new ArrayList<>();

        Cursor cursor = dataBase.rawQuery("SELECT * FROM "+NOTES_TB_NAME+" WHERE "+NOTES_CLN_TITLE+"=?",new String[]{modelSearch});
        // كود التعامل مع الكيرسر وتحويله الى مصفوفة من نوع Note
        // لاجل فحص هل الكيرسر بحتوي على بيانات
        if (cursor != null && cursor.moveToFirst()){
            do {
                int id       = cursor.getInt(cursor.getColumnIndex(NOTES_CLN_ID));
                String title = cursor.getString(cursor.getColumnIndex(NOTES_CLN_TITLE));
                String note  = cursor.getString(cursor.getColumnIndex(NOTES_CLN_NOTE));

                // here we store those info in notes arrayList
                notes.add(new Note(id,title,note));

            }while (cursor.moveToNext());
            // من الافضل ان تغلق الكيرسر بشكل يدوي
            cursor.close();
        }

        return notes;
    }
}
