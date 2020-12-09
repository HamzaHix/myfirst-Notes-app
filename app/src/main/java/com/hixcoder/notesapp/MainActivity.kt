package com.hixcoder.notesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hixcoder.notesapp.DataBase.DatabaseAccess
import com.hixcoder.notesapp.DataBase.Note


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // for use the data base
        var db = DatabaseAccess.getInstance(this)
        var notes = ArrayList<Note>()

        db.open()
        notes.addAll(db.allNotes)
        db.close()

    }
}
