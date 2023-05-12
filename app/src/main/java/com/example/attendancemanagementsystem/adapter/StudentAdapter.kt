package com.example.employee.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.attendancemanagementsystem.R
import com.example.attendancemanagementsystem.model.Student

class StudentAdapter(private val activity: Activity,private val objects : Array<Student>):ArrayAdapter<Student>(activity,
    R.layout.takeattendancelayout,objects){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        var viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.takeattendancelayout, parent, false)
            viewHolder = ViewHolder()

            viewHolder.EnrollmentNo = view.findViewById(R.id.Enrollment)
            viewHolder.FirstName = view.findViewById(R.id.StudentName)
            viewHolder.AttendanceCheckBox=view.findViewById(R.id.Attendance)

            view.tag = viewHolder
        } else
            viewHolder = view.tag as ViewHolder

        viewHolder.FirstName.text = objects[position].FirstName

        return view!!
    }

    companion object {
        class ViewHolder{
            lateinit var EnrollmentNo : TextView
            lateinit var FirstName : TextView
            lateinit var AttendanceCheckBox : CheckBox
        }
    }
}