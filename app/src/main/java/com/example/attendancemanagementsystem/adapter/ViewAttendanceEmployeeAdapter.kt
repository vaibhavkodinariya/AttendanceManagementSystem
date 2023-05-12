package com.example.attendancemanagementsystem.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.attendancemanagementsystem.BarGraphActivity
import com.example.attendancemanagementsystem.R
import com.example.attendancemanagementsystem.model.Student

class ViewAttendanceEmployeeAdapter(private val activity: Activity, private val objects : Array<Student>,private val subject:String?):
    ArrayAdapter<Student>(activity,
    R.layout.viewattendanceemoloyeegrid,objects){
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        var viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.viewattendanceemoloyeegrid, parent, false)
            viewHolder = ViewHolder()
            viewHolder.ViewBtn=view.findViewById(R.id.ViewAttBtn)
            viewHolder.EnrollmentNo = view.findViewById(R.id.ViewAttendaceEnrollment)
            viewHolder.Name = view.findViewById(R.id.ViewAttendaceStudentName)

            view.tag = viewHolder
        } else
            viewHolder = view.tag as ViewHolder

        viewHolder.EnrollmentNo.text=objects[position].EnrollmentNo
        viewHolder.Name.text = objects[position].FirstName+" "+objects[position].MiddleName+" "+objects[position].LastName
        viewHolder.ViewBtn.setOnClickListener {
            val intent = Intent(activity,BarGraphActivity::class.java)
            intent.putExtra("subject",subject)
            intent.putExtra("enrollmentno",viewHolder.EnrollmentNo.text)
            activity.startActivity(intent)
        }
        return view!!
    }

    companion object {
        class ViewHolder{
            lateinit var EnrollmentNo : TextView
            lateinit var Name : TextView
            lateinit var ViewBtn:Button
        }
    }
}