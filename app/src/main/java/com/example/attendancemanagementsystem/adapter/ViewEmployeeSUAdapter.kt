package com.example.attendancemanagementsystem.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.attendancemanagementsystem.R
import com.example.attendancemanagementsystem.ViewUserActivity
import com.example.attendancemanagementsystem.model.Employee
import com.example.employee.api.SuperUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewEmployeeSUAdapter(private val activity : Activity, private val employees : Array<Employee>):
    ArrayAdapter<Employee>(activity, R.layout.deleteusercardgrid,employees) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val viewHolder: ViewHolder
        val Type="employees"

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.deleteusercardgrid, parent, false)

            viewHolder = ViewHolder()

            viewHolder.name = view.findViewById(R.id.DeleteUserStudentName)
            viewHolder.Email = view.findViewById(R.id.DeleteUserEnrollment)
            viewHolder.Delete   = view.findViewById(R.id.DeleteUserDeleteBtn)
            viewHolder.ViewDetails   = view.findViewById(R.id.DeleteUserViewBtn)

            view.tag = viewHolder
        }
        else
        {
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.name.text=employees[position].firstname+" "+employees[position].middlename+" "+employees[position].lastname
        viewHolder.Email.text=employees[position].email
        viewHolder.Delete.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            //set title for alert dialog
            builder.setTitle("Warning !")
            //set message for alert dialog
            builder.setMessage("User Data Will Be Wiped Off From The System Do You Still Want To Continue ..!")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing cancel action
            builder.setPositiveButton("Yes"){dialogInterface , which ->
                CoroutineScope(Dispatchers.IO).launch{
                    SuperUser.DeleteData(employees[position].employeeid.toString(),Type)
                }
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
            profile.putExtra("type","employees")
            profile.putExtra("id",employees[position].employeeid.toString())
            activity.startActivity(profile)
        }
        return view!!
    }

    companion object{

        class ViewHolder{
            lateinit var Email: TextView
            lateinit var name: TextView
            lateinit var Delete : Button
            lateinit var ViewDetails : Button

        }
    }
}