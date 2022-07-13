package com.example.notes_app

import android.content.SharedPreferences
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type   

lateinit var userlist: ArrayList<UserData>
lateinit var userAdapter: UserAdapter
lateinit var recycl: RecyclerView
lateinit var toolbar: Toolbar


class Notesapp : Fragment(R.layout.fragment_notesapp)
{
    private lateinit var sharedpreferences: SharedPreferences
    private var shared_Pref_Name: String = "myPref"
    private lateinit var btn: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_notesapp, container, false)
        toolbar = view.findViewById(R.id.toolBar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.show()
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycl = view.findViewById(R.id.rcycle)
        btn = view.findViewById(R.id.floatingAddNoteButton)
            loadData()
            userAdapter = UserAdapter(requireContext(), userlist)
            recycl.layoutManager = LinearLayoutManager(context)
            recycl.adapter = userAdapter

        userAdapter.setOnClickListener(object : UserAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val title = userlist[position].Title
                val Descrip = userlist[position].Descrip
                val bundle = Bundle()
                bundle.putString("title1", title)
                bundle.putString("descrip1", Descrip)
                activity?.supportFragmentManager?.commit {
                    setReorderingAllowed(true)
                    hide(activity?.supportFragmentManager?.findFragmentByTag("main")!!)
                    add<Detail_Note>(R.id.fragmentContainer, args = bundle)
                    addToBackStack(null)
                }
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("Not yet implemented")
            }

        })
        btn.setOnClickListener()
        {
            activity?.supportFragmentManager?.commit {
                setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out
                )

                setReorderingAllowed(true)
                hide(activity?.supportFragmentManager?.findFragmentByTag("main")!!)
                add(R.id.fragmentContainer, AddNotes())
                addToBackStack(null)

            }
        }

    }

    private fun loadData() {
        sharedpreferences = activity?.getSharedPreferences(
            shared_Pref_Name,
            AppCompatActivity.MODE_PRIVATE
        )!!
        val gson=Gson()
        val json: String? = sharedpreferences.getString("taskList", null)
        val type: Type = object : TypeToken<ArrayList<UserData>>() {}.type
        if(json!=null) {
            userlist = gson.fromJson(json, type)
        }
        if(json==null)
        {
            userlist= ArrayList()
        }

    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu?.findItem(R.id.search)
        val searchView = menuItem.actionView as androidx.appcompat.widget.SearchView
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userAdapter.filter.filter(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu, inflater)


    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.finish()
    }
}






