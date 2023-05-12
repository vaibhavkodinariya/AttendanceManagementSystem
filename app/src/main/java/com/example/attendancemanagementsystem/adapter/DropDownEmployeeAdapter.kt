package com.example.attendancemanagementsystem.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.attendancemanagementsystem.R
import com.example.attendancemanagementsystem.model.Employee

class DropDownEmployeeAdapter(private val activity: Activity, private val objects: Array<Employee>) :
    ArrayAdapter<Employee>(activity, R.layout.dropdowngrid
        , objects) {
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.dropdowngrid, parent, false)
            viewHolder = ViewHolder()
            viewHolder.hiddenText = view.findViewById(R.id.HiddenTextSubjectId)
            viewHolder.employeeName = view.findViewById(R.id.dropDownItemText)

            view.tag = viewHolder
        } else
            viewHolder = view.tag as ViewHolder

        viewHolder.hiddenText.text = objects[position].employeeid.toString()
        viewHolder.employeeName.text = objects[position].firstname +" "+ objects[position].middlename+" " +objects[position].lastname

        return view!!
    }

    companion object {
        class ViewHolder {
            lateinit var hiddenText: TextView
            lateinit var employeeName: TextView
        }
    }
}