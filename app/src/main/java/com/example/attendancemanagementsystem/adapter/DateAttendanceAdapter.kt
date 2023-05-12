package com.example.attendancemanagementsystem.adapter

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.attendancemanagementsystem.R
import com.example.attendancemanagementsystem.UpdateAttendanceActivity
import com.example.attendancemanagementsystem.model.DateAttendance


class DateAttendanceAdapter(private val activity: Activity,private val Dates : Array<DateAttendance>,private val subject:String):ArrayAdapter<DateAttendance>(activity,
    R.layout.updateattendanceemployeegrid,Dates) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        var viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.updateattendanceemployeegrid, parent, false)
            viewHolder = ViewHolder()
            viewHolder.dates=view.findViewById(R.id.DateOfLecturesEmployee)
            viewHolder.lecGrid=view.findViewById(R.id.LecturebuttonGridEmployee)
            view.tag = viewHolder
        } else
            viewHolder = view.tag as ViewHolder

        viewHolder.dates.text=Dates[position].Dates
        viewHolder.lecGrid.removeAllViews()

        for (i in 0 until Dates[position].Lectures.size){
            var lecturebtn= Button(activity)
            lecturebtn.text=Dates[position].Lectures[i]
            lecturebtn.setOnClickListener {
                val intent= Intent(activity, UpdateAttendanceActivity::class.java)
                intent.putExtra("date",Dates[position].Dates+lecturebtn.text)
                intent.putExtra("subject",subject)
                activity.startActivity(intent)
            }
            viewHolder.lecGrid.addView(lecturebtn)
        }
        return view!!
    }
    companion object{
        class ViewHolder{
            lateinit var dates : TextView
            lateinit var lecGrid:LinearLayout
        }
    }
}
