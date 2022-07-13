package com.example.notes_app

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson

var title1 = ""
var json: String = ""


class AddNotes : Fragment() {
    private lateinit var sharedpreferences: SharedPreferences
    private var shared_Pref_Name: String = "myPref"
    lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_notes, container, false)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = view.findViewById<AutoCompleteTextView>(R.id.TitleTextBox)
        val AddNoteButton = view.findViewById<ImageView>(R.id.ok)



        AddNoteButton.setOnClickListener()
        {
            val descrip = view.findViewById<EditText>(R.id.DescriptionTextBox)
            title1 = title.text.toString()
            //Log.d("title1", "" + title1 + "")
            val des1 = descrip.text.toString()
            userlist.add(UserData("$title1", "$des1"))
            //stringAutoShow.add(title1)
            userAdapter.notifyDataSetChanged()
            Toast.makeText(context, "Note saved", Toast.LENGTH_SHORT).show()
            saveData()
        }
        var stringAutoShow: ArrayList<String> = ArrayList()

        for (e in userlist) {
            stringAutoShow.add("${e.Title}")
        }
        val stringArray: Array<String> =
            stringAutoShow.stream().toArray { arrayOfNulls<String>(it) }
        arrayAdapter =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                stringArray.takeLast(5)
            )
        title.setAdapter(arrayAdapter)
    }

    fun saveData() {
        sharedpreferences =
            activity?.getSharedPreferences(shared_Pref_Name, AppCompatActivity.MODE_PRIVATE)!!
        val editor: SharedPreferences.Editor = sharedpreferences.edit()
        val gson = Gson()
        json = gson.toJson(userlist)
        editor.putString("taskList", json)
        editor.apply()
        editor.commit()
    }
}
