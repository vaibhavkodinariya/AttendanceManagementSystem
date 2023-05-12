package com.example.attendancemanagementsystem.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.attendancemanagementsystem.R
import com.example.attendancemanagementsystem.model.ListofSubjects

class DropDownSubjectAdapter(private val activity: Activity, private val objects: Array<ListofSubjects>) :
    ArrayAdapter<ListofSubjects>(activity, R.layout.dropdowngrid
        , objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.dropdowngrid, parent, false)
            viewHolder = ViewHolder()
            viewHolder.hiddenText = view.findViewById(R.id.HiddenTextSubjectId)
            viewHolder.sub = view.findViewById(R.id.dropDownItemText)
            viewHolder.semesterId=view.findViewById(R.id.HiddenTextSemesterId)

            view.tag = viewHolder
        } else
            viewHolder = view.tag as ViewHolder

        viewHolder.hiddenText.text = objects[position].subjectid.toString()
        viewHolder.sub.text = objects[position].subjects
        viewHolder.semesterId.text=objects[position].semesterid

        return view!!
    }

    companion object {
        class ViewHolder {
            lateinit var hiddenText: TextView
            lateinit var sub:TextView
            lateinit var semesterId:TextView
        }
    }
}