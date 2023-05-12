package com.example.attendancemanagementsystem.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.attendancemanagementsystem.R
import com.example.attendancemanagementsystem.model.Queries

    class StudentQueriesAdapter(private val activity: Activity,private val queries : Array<Queries>):ArrayAdapter<Queries>(activity,
        R.layout.viewquerystudentcardgrid,queries){
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        var viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.viewquerystudentcardgrid, parent, false)
            viewHolder = ViewHolder()
            viewHolder.semesterName=view.findViewById(R.id.QuerySemester)
            viewHolder.subjectName=view.findViewById(R.id.QuerySubject)
            viewHolder.employeeName=view.findViewById(R.id.QueryTeacherName)
            viewHolder.description=view.findViewById(R.id.QueryViewDiscription)

            view.tag = viewHolder
        } else
            viewHolder = view.tag as ViewHolder

        viewHolder.description.text="Description:- "+queries[position].description
        viewHolder.employeeName.text="Employee :- "+queries[position].firstname+" "+ queries[position].middlename
        viewHolder.subjectName.text="Subject :- "+queries[position].subjectname
        viewHolder.semesterName.text="Semester:- "+queries[position].semestername

        return view!!
    }

    companion object {
        class ViewHolder{
            lateinit var employeeName:TextView
            lateinit var semesterName:TextView
            lateinit var subjectName:TextView
            lateinit var description:TextView
        }
    }
}