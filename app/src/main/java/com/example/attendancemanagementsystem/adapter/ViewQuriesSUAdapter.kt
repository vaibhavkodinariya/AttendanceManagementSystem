package com.example.attendancemanagementsystem.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.attendancemanagementsystem.R
import com.example.attendancemanagementsystem.model.Queries


class ViewQuriesSUAdapter(private val activity: Activity,private val Allqueries : Array<Queries>)
    : ArrayAdapter<Queries>(activity, R.layout.viewquerystudentcardgrid,Allqueries){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        var viewHolder: ViewQuriesSUAdapter.Companion.ViewHolder
        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.viewquerystudentcardgrid, parent, false)
            viewHolder = ViewQuriesSUAdapter.Companion.ViewHolder()

            viewHolder.semesterName=view.findViewById(R.id.QuerySemester)
            viewHolder.studentname=view.findViewById(R.id.QueryTeacherName)
            viewHolder.description=view.findViewById(R.id.QueryViewDiscription)
            viewHolder.subjectName=view.findViewById(R.id.QuerySubject)

            view.tag=viewHolder
        }else{
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.description.text=Allqueries[position].description
        viewHolder.studentname.text=Allqueries[position].firstname+" "+Allqueries[position].middlename
        viewHolder.subjectName.text=Allqueries[position].subjectname
        viewHolder.semesterName.visibility=View.GONE

        return view!!

    }
    companion object{
        class  ViewHolder{
            lateinit var description : TextView
            lateinit var subjectName : TextView
            lateinit var studentname : TextView
            lateinit var semesterName : TextView
        }
    }
    }