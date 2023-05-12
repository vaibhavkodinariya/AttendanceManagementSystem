package com.example.attendancemanagementsystem

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.adapter.DropDownSubjectAdapter
import com.example.employee.api.Students
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonthlyAttendanceActivity : AppCompatActivity() {
    private lateinit var Send: Button
    private lateinit var subSpinner: AutoCompleteTextView
    private lateinit var monthspinner: AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_attendance)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Monthly Attendance"

        Send = findViewById(R.id.ContinueMonthlyAttendance)
        subSpinner = findViewById(R.id.AutoCompleteSubjectMonthlyAttendance)
        monthspinner = findViewById(R.id.AutoCompleteMonthlyAttendance)

        val sharePreference = getSharedPreferences("Credentials", MODE_PRIVATE)
        val userId = sharePreference.getString("id", "")!!
        val types = sharePreference.getString("type", "")!!
        val arry = arrayListOf<String>("January","February","March","April","May","June","July","August","September","october","November","December")
        val monthadapter=ArrayAdapter(
            this@MonthlyAttendanceActivity ,android.R.layout.simple_list_item_1,arry
        )

        monthspinner.setAdapter(monthadapter)
        var month=""
        monthspinner.setOnItemClickListener { parent, view, position, id ->
            month=arry[position]
        }
        CoroutineScope(Dispatchers.IO).launch {
            val subjects = Students.getSubjectsByStudent(userId)
            subjects.also {
                    withContext(Dispatchers.Main) {
                        if(subjects.isEmpty()){

                        }else {
                            var subject = ""
                        var adapter =
                            DropDownSubjectAdapter(this@MonthlyAttendanceActivity, subjects)
                        subSpinner.setAdapter(adapter)
                        subSpinner.setOnItemClickListener { parent, view, position, id ->
                            subject = subjects[position].subjects

                        }
                        Send.setOnClickListener {
                            val builder = AlertDialog.Builder(this@MonthlyAttendanceActivity)
                            if(TextUtils.isEmpty(subSpinner.text)&&TextUtils.isEmpty(monthspinner.text))
                            {
                                //set title for alert dialog
                                builder.setTitle("Warning !")
                                //set message for alert dialog
                                builder.setMessage("Please Select Data properly ..!")
                                builder.setIcon(android.R.drawable.ic_dialog_alert)

                                //performing cancel action
                                builder.setNeutralButton("Ok"){dialogInterface , which ->
                                }
                                // Create the AlertDialog
                                val alertDialog: AlertDialog = builder.create()
                                // Set other dialog properties
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                            }
                            else if(TextUtils.isEmpty(subSpinner.text)||TextUtils.isEmpty(monthspinner.text))
                            {
                                //set title for alert dialog
                                builder.setTitle("Warning !")
                                //set message for alert dialog
                                builder.setMessage("Please Select Data properly ..!")
                                builder.setIcon(android.R.drawable.ic_dialog_alert)

                                //performing cancel action
                                builder.setNeutralButton("Ok"){dialogInterface , which ->
                                }
                                // Create the AlertDialog
                                val alertDialog: AlertDialog = builder.create()
                                // Set other dialog properties
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                            }
                            else{

                                val intent =
                                    Intent(this@MonthlyAttendanceActivity, BarGraphActivity::class.java)
                                intent.putExtra("subject", subject)
                                intent.putExtra("month", month)
                                startActivity(intent)
                            }
                        }

                    }
                }
            }
        }
    }
}