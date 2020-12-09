package com.hixcoder.notesapp.Fragments


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.hixcoder.notesapp.DataBase.DatabaseAccess
import com.hixcoder.notesapp.DataBase.Note
import com.hixcoder.notesapp.R
import kotlinx.android.synthetic.main.fragment_add_note.*

class AddNoteFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    var id:Int?=null
    // for show the menu
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        // for add a note
        add_btn.setOnClickListener{
            addNote()
        }

        // check if it edit
        id = arguments?.getInt("id")

        if (id != 0) {

            val title = arguments?.getString("title")
            et_title.setText(title)
            val note = arguments?.getString("note")
            et_note.setText(note)

        }
    }

    // for add a note
    fun addNote(){

        var title = et_title.text.toString()
        var note = et_note.text.toString()

        if (id !=0){
            // for edit data from the database
            var db = DatabaseAccess.getInstance(context)

            db.open()
            db.updateNote(Note(id!!,title,note))
            db.close()

        }else{
            // for add data to the database
            var db = DatabaseAccess.getInstance(context)

            db.open()
            db.insertNote(title,note)
            db.close()
        }

        // for go back to note liste activity
        requireView().findNavController().navigate(R.id.notesListFragment)
    }

    // for link the menu file with this activity
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.add_notes_menu,menu)
    }

    // for use the items in the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {
            R.id.back_btn ->
            {
                requireView().findNavController().navigate(R.id.notesListFragment)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
