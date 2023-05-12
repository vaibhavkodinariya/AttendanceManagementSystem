package com.example.attendancemanagementsystem.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.attendancemanagementsystem.R
import com.example.attendancemanagementsystem.model.ListofSubjects
import com.google.android.material.chip.Chip


class SubjectAdapter(private val activity: Activity, private val objects: Array<ListofSubjects>) :
    ArrayAdapter<ListofSubjects>(activity, R.layout.activity_profile
        , objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.subject_chip, parent, false)

            viewHolder = ViewHolder()
            viewHolder.subjectchip=view.findViewById(R.id.subjects)

            view.tag = viewHolder
        } else
            viewHolder = view.tag as ViewHolder

        viewHolder.subjectchip.text = objects[position].subjects
        return view!!
    }

    companion object {
        class ViewHolder {
            lateinit var subjectchip: Chip
        }
    }

}