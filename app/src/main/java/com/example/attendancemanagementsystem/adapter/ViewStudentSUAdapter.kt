package com.example.attendancemanagementsystem.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.attendancemanagementsystem.R
import com.example.attendancemanagementsystem.ViewUserActivity
import com.example.attendancemanagementsystem.model.Student
import com.example.employee.api.SuperUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewStudentSUAdapter(private val activity : Activity, private val students : Array<Student>):
    ArrayAdapter<Student>(activity,R.layout.deleteusercardgrid,students) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.deleteusercardgrid, parent, false)

            viewHolder = ViewHolder()

            viewHolder.name = view.findViewById(R.id.DeleteUserStudentName)
            viewHolder.enrollmentno = view.findViewById(R.id.DeleteUserEnrollment)
            viewHolder.Delete   = view.findViewById(R.id.DeleteUserDeleteBtn)
            viewHolder.ViewDetails   = view.findViewById(R.id.DeleteUserViewBtn)

            view.tag = viewHolder
        }
        else
        {
                viewHolder = view.tag as ViewHolder
        }
        val Type="students"
        viewHolder.name.text=students[position].FirstName+" "+students[position].MiddleName+" "+students[position].LastName
        viewHolder.enrollmentno.text=students[position].EnrollmentNo
        viewHolder.Delete.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            //set title for alert dialog
            builder.setTitle("Warning !")
            //set message for alert dialog
            builder.setMessage("Delete this user will remove user ..!")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing cancel action
            builder.setPositiveButton("Ok"){dialogInterface , which ->
                CoroutineScope(Dispatchers.IO).launch{
                    SuperUser.DeleteData(viewHolder.enrollmentno.text.toString(),Type)
                }
                activity.recreate()
                Toast.makeText(activity, "User Deleted Successfully", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No"){dialogInterface,which ->

            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
        viewHolder.ViewDetails.setOnClickListener {
            val profile = Intent(activity, ViewUserActivity::class.java)
            profile.putExtra("type","student")
            profile.putExtra("id",viewHolder.enrollmentno.text.toString())
            activity.startActivity(profile)
        }

        return view!!
    }

    companion object{

        class ViewHolder{
            lateinit var enrollmentno: TextView
            lateinit var name: TextView
            lateinit var Delete : Button
            lateinit var ViewDetails : Button

        }
    }
    }