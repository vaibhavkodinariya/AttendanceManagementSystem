package com.example.attendancemanagementsystem.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.attendancemanagementsystem.R
import com.example.attendancemanagementsystem.model.ListofSubjects

class SubjectDropDownAdapter(
    private val activity: Activity,
    objects1: Int,
    private val objects: Array<ListofSubjects>
) :
    ArrayAdapter<ListofSubjects>(activity, R.layout.dropdowngrid, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val viewHolder:ViewHolder
        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.dropdowngrid, parent, false)
            viewHolder = ViewHolder()
            viewHolder.SubjectsList=view.findViewById(R.id.dropDownItemText)

            view.tag = viewHolder
        } else
            viewHolder = view.tag as ViewHolder

        viewHolder.SubjectsList.text= objects[position]!!.subjects
        return view!!
    }

    companion object {
        class ViewHolder {
            lateinit var SubjectsList: TextView
        }
    }
}