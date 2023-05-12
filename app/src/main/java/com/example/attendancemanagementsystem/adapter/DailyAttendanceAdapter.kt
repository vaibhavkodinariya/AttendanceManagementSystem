package com.example.attendancemanagementsystem.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.attendancemanagementsystem.R
import com.example.attendancemanagementsystem.model.DailyAttendance

class DailyAttendanceAdapter(private val activity: Activity, private val Dates : Array<DailyAttendance>):
    ArrayAdapter<DailyAttendance>(activity,
    R.layout.dailyattendancegrid,Dates) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        var viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.dailyattendancegrid, parent, false)
            viewHolder = ViewHolder()
            viewHolder.dates=view.findViewById(R.id.DailyAttendanceDate)
            viewHolder.lecNo=view.findViewById(R.id.ViewAttendanceLecture)
            viewHolder.subject=view.findViewById(R.id.ViewAttendanceSubject)
            viewHolder.isPresent=view.findViewById(R.id.DailyAttendanceAP)

            view.tag = viewHolder
        } else
            viewHolder = view.tag as ViewHolder

        viewHolder.dates.text="Date:- "+Dates[position].date
        viewHolder.lecNo.text="Lecture:- "+Dates[position].lectures
        viewHolder.subject.text="Subject:- "+Dates[position].subject

        if(Dates[position].attendance.toString()=="0")
        {
            viewHolder.isPresent.text="Absent"
            viewHolder.isPresent.setBackgroundResource(R.color.red)
        }
        else{
            viewHolder.isPresent.text="Present"
            viewHolder.isPresent.setBackgroundResource(R.color.green)
        }
        return view!!
    }
    companion object{
        class ViewHolder{
            lateinit var dates : TextView
            lateinit var lecNo: TextView
            lateinit var subject: TextView
            lateinit var isPresent: TextView
        }
    }
}