package com.example.attendancemanagementsystem.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancemanagementsystem.HomeActivity
import com.example.attendancemanagementsystem.R
import com.example.attendancemanagementsystem.model.Student
import com.example.employee.api.Employees
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


class AttendanceAdapter(private val activity: Activity,
                        private val students: Array<Student>, private val onClick: (Student, Boolean) -> Unit
                        , private val checkAll: CheckBox,private val subject:String?,private val dates:String?, private val submit : Button
) : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {

    private val Attendance = mutableMapOf<String,Int>()
    var atndn=0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.takeattendancelayout, parent, false))
    }
    override fun getItemCount(): Int = students.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bind(position)
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val studentName = view.findViewById<TextView>(R.id.StudentName)
        private val studentEnrollment = view.findViewById<TextView>(R.id.Enrollment)
        private val attendace = view.findViewById<CheckBox>(R.id.Attendance)
        private val cardAtt = view.findViewById<CardView>(R.id.CardAttendance1)

        fun bind(position: Int) {
            var sharedPreference= activity.getSharedPreferences("Credentials", MODE_PRIVATE)
            var id=sharedPreference.getString("id","")!!
            val first = students[position].FirstName
            val middle = students[position].MiddleName
            val last = students[position].LastName
            studentName.text = first + " " + middle + " " + last
            studentEnrollment.text = students[position].EnrollmentNo
            attendace.isChecked = students[position].isChecked
            cardAtt.setBackgroundResource(R.color.red)

            if (checkAll.isChecked) {
                cardAtt.setBackgroundResource(R.color.green)
                atndn = 1
                Attendance.put(studentEnrollment.text.toString(), atndn)
            } else {
                cardAtt.setBackgroundResource(R.color.red)
                atndn = 0
                Attendance.put(studentEnrollment.text.toString(), atndn)
            }
            cardAtt.setOnClickListener {
                if (attendace.isChecked)
                {
                    attendace.isChecked=false
                    cardAtt.setBackgroundResource(R.color.red)
                }
                else
                {
                    attendace.isChecked=true
                    cardAtt.setBackgroundResource(R.color.green)
                }
            }
            attendace.setOnCheckedChangeListener { compoundButton, b ->
                onClick(students[position], attendace.isChecked)
                if (!students[position].isChecked) {
                    checkAll.isChecked = false
                }
                if (attendace.isChecked) {
                    cardAtt.setBackgroundResource(R.color.green)
                    attendace.setChecked(true)
                    atndn = 1
                    Attendance.put(studentEnrollment.text.toString(), atndn)
                }
                else {
                    cardAtt.setBackgroundResource(R.color.red)
                    attendace.setChecked(false)
                    atndn = 0
                    Attendance.put(studentEnrollment.text.toString(), atndn)
                }
            }

            submit.setOnClickListener {
                val builder = AlertDialog.Builder(activity)
                //set title for alert dialog
                builder.setTitle("Attention !")
                //set message for alert dialog
                builder.setMessage("Do You Want To Submit Attendance ..!")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing cancel action
                builder.setPositiveButton("Yes"){dialogInterface , which ->
                    if (!attendace.isChecked) {
                        atndn = 0
                        Attendance.put(studentEnrollment.text.toString(), atndn)
//                    cardAtt.setBackgroundResource(R.color.red)

                    } else {
                        atndn = 1
                        Attendance.put(studentEnrollment.text.toString(), atndn)
                    }

                    val jsonObject = JSONObject(Attendance.map { "${it.key}" to it.value }.toMap())
                    CoroutineScope(Dispatchers.IO).launch {
                        val message =
                            Employees.takeAttendance(subject, dates+"L1", id, jsonObject)
                        withContext(Dispatchers.Main) {
                            if (message.getBoolean("success")) {
                                val intent= Intent(activity, HomeActivity::class.java)
                                activity.startActivity(intent)
                                Toast.makeText(activity, "Attendance Submitted", Toast.LENGTH_SHORT).show()
                                activity.finish()
                            } else {

                            }
                        }
                    }
                }
                builder.setNegativeButton("No"){dilogInterface,which ->

                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }
}