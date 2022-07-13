package com.example.notes_app

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserAdapter(val context: Context, val userList: ArrayList<UserData>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>(), Filterable {

    lateinit var backup: ArrayList<UserData>

    lateinit var sharedpreferences: SharedPreferences
    private var shared_Pref_Name: String = "myPref"
    private lateinit var mlistener: AdapterView.OnItemClickListener

    interface onItemClickListener
        : AdapterView.OnItemClickListener {
        fun onItemClick(position: Int)

    }

    fun setOnClickListener(listener: onItemClickListener) {
        mlistener = listener
    }

    inner class UserViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        var Title1: TextView
        var Descrip1: TextView
        var mmenus: ImageView

        init {
            Title1 = itemView.findViewById(R.id.Textview1)
            Descrip1 = itemView.findViewById(R.id.Textview2)
            mmenus = itemView.findViewById(R.id.more_menus)
            backup = ArrayList(userList)
            mmenus.setOnClickListener(this@UserViewHolder::menusPopUp)
        }

        private fun menusPopUp(v: View) {
            val position = userList[absoluteAdapterPosition]
            val pos = absoluteAdapterPosition


            val popupMenus = PopupMenu(context, v)
            popupMenus.inflate(R.menu.show_menu)
            popupMenus.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.edittext -> {
                        val v =
                            LayoutInflater.from(context).inflate(R.layout.fragment_add_notes, null)
                        v.findViewById<TextView>(R.id.TitleTextBox).text = position.Title
                        v.findViewById<TextView>(R.id.DescriptionTextBox).text = position.Descrip
                        val Title = v.findViewById<TextView>(R.id.TitleTextBox)
                        val Descrip1 = v.findViewById<TextView>(R.id.DescriptionTextBox)

                        AlertDialog.Builder(context)
                            .setView(v)
                            .setPositiveButton("ok") { dialog, _ ->
                                position.Title = Title.text.toString()
                                position.Descrip = Descrip1.text.toString()
                                notifyDataSetChanged()
                                Toast.makeText(context, "Edited", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("cancel")
                            { dialog, _ ->

                            }.create()
                            .show()
                        true
                    }
                    R.id.delete -> {
                        AlertDialog.Builder(context)
                            .setTitle("Delete")
                            .setIcon(R.drawable.ic_baseline_warning_24)
                            .setMessage("are you sure?")
                            .setPositiveButton("Yes") { dialog, _ ->
                                userList.removeAt(absoluteAdapterPosition)
                                backup.removeAt(absoluteAdapterPosition)
                                removeData(pos)
                                notifyDataSetChanged()
                                Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                        true
                    }


                    else -> true

                }

            }
            popupMenus.show()
        }

        init {
            itemView.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }
        }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view1 = inflater.inflate(R.layout.list_item, parent, false)
            return UserViewHolder(view1, mlistener as onItemClickListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val list = userList[position]
        holder.Title1.text = list.Title
        holder.Descrip1.text = list.Descrip
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun removeData(position: Int) {
        sharedpreferences = context.getSharedPreferences(
            shared_Pref_Name,
            AppCompatActivity.MODE_PRIVATE
        )!!
        var editor: SharedPreferences.Editor = sharedpreferences.edit()
        var gson = Gson()
        var json: String? = sharedpreferences.getString("taskList", null)
        var objnote: ArrayList<UserData> =
            gson.fromJson(json, object : TypeToken<ArrayList<UserData>>() {}.type)
        objnote.removeAt(position)
        val jsonString = gson.toJson(objnote)
        editor.putString("taskList", jsonString)
        editor.apply()
    }


    override fun getFilter(): Filter {

        return filter
    }

    private val filter = object : Filter() {
        override fun performFiltering(keyword: CharSequence?): FilterResults {
            val filteredData: ArrayList<UserData> = ArrayList()

            if (keyword.toString().isEmpty()) {
                filteredData.addAll(backup)
            } else {
                for (obj: UserData in backup) {
                    if (obj.Title.lowercase().contains(keyword.toString().lowercase())) {
                        filteredData.add(obj)
                    }
                }
            }
            val result = FilterResults()
            result.values = filteredData
            return result
        }

        override fun publishResults(keyword: CharSequence?, results: FilterResults?) {
            userList.clear()
            userList.addAll(results?.values as ArrayList<UserData>)
            userAdapter.notifyDataSetChanged()
        }
    }
    }

