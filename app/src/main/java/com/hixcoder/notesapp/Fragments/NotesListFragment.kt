package com.hixcoder.notesapp.Fragments


import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hixcoder.notesapp.DataBase.DatabaseAccess
import com.hixcoder.notesapp.DataBase.Note
import com.hixcoder.notesapp.R
import kotlinx.android.synthetic.main.fragment_notes_list.*
import kotlinx.android.synthetic.main.noteslist_cardview.*

class NotesListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_list, container, false)
    }




    var notelist = ArrayList<Note>()
    // for use our codes
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // for show the menu
        setHasOptionsMenu(true)

        // for use the data base
        var db = DatabaseAccess.getInstance(context)

        db.open()
        notelist.addAll(db.allNotes)
        db.close()

        // here we use the RecyclerView
        RecyclerView_NoteList.layoutManager = GridLayoutManager(context,1,LinearLayoutManager.VERTICAL,false)

        var noteadapter = adapter_NotesList(notelist)
        RecyclerView_NoteList.adapter = noteadapter


    }



    // for link the menu file with this activity
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.notes_list_menu,menu)
    }

    // for use the items in the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {
            R.id.AddNotesBtn ->
            {
                requireView().findNavController().navigate(R.id.action_notesListFragment_to_addNoteFragment)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    //||*******************||
    //||for use the adapter||
//****||*******************||*************************************************************************************************************************************
    inner class adapter_NotesList (var mynotelist:ArrayList<Note>):
        RecyclerView.Adapter<adapter_NotesList.viewHolder>(){

        // هذه الدالة تقوم بتجهيز جميع البيانات

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.noteslist_cardview,parent,false)
            return viewHolder(v)
        }

        // هذه الدالة تقوم بارجاع عدد ال items

        override fun getItemCount(): Int {
            return mynotelist.size
        }

        // هذه الدالة تقوم بجلب البيانات من  (model class (data class

        override fun onBindViewHolder(holder: viewHolder, position: Int) {
            var noteInfo = mynotelist[position]

            holder.noteTitle.text = noteInfo.title
            holder.noteDescription.text = noteInfo.note

            // for remove items from the recycler view
            holder.removeNote.setOnClickListener{

                removeItem(position)
            }

            // for edit items from the recycler view
            holder.editNote.setOnClickListener{

                editItem(noteInfo)
            }
        }

        // for remove items from the recycler view
        private fun removeItem(position: Int){

            try {
                // for use the data base
                // for remove items from the database
                var db = DatabaseAccess.getInstance(context)

                db.open()
                db.deleteNote(mynotelist[position])
                db.close()

                mynotelist.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(0,itemCount)

            }catch (e:Exception){

                Toast.makeText(context,"try again",Toast.LENGTH_SHORT).show()
                println(e)
            }
        }

        // for edit items from the recycler view
        private fun editItem(note:Note){

            var bundle = Bundle()
            bundle.putInt("id",note.id)
            bundle.putString("title",note.title)
            bundle.putString("note",note.note)

            requireView().findNavController().navigate(R.id.action_notesListFragment_to_addNoteFragment,bundle)
        }

        inner class viewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView)
        {
            var noteTitle= ItemView.findViewById(R.id.title1_tv) as TextView
            var noteDescription= ItemView.findViewById(R.id.note1_tv) as TextView

            var removeNote = ItemView.findViewById(R.id.delete1_iv) as ImageView
            var editNote = ItemView.findViewById(R.id.edit1_iv) as ImageView
        }

    }

//***************************************************************************************************************************************************************

}
