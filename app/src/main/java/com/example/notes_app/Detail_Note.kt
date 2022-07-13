package com.example.notes_app

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class Detail_Note : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail__note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val Title = view.findViewById<TextView>(R.id.Textview_TitleDetail)
        val Descrip = view.findViewById<TextView>(R.id.Textbox_DescripDetail)
        val title_Note = requireArguments().getString("title1")
        val Des_note = requireArguments().getString("descrip1")
        Title.text = title_Note
        Descrip.text = Des_note
    }

    private fun editData() {
        val noteTitle= binding.TextviewTitleDetail.toString()
        val noteDescription = binding.TextboxDescripDetail.toString()
        if (inputCheck(noteTitle,noteDescription))
        {
            val notesUpdate= Notes(position,noteTitle,noteDescription)
            notesViewModel.updateNotes(notesUpdate)
            android.widget.Toast.makeText(requireContext(),"Updated", android.widget.Toast.LENGTH_SHORT).show()
        }
        else
        {
            android.widget.Toast.makeText(requireContext(),"Title is Empty", android.widget.Toast.LENGTH_SHORT).show()

        }

        notesAdapter.notifyDataSetChanged()
    }

    private fun inputCheck(noteTitle: String, noteDescription: String): Boolean{
        return !(android.text.TextUtils.isEmpty(noteTitle) && android.text.TextUtils.isEmpty(noteDescription))
    }

}