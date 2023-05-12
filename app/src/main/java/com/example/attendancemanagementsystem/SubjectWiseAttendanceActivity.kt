package com.example.attendancemanagementsystem

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.adapter.DropDownSubjectAdapter
import com.example.employee.api.Students
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubjectWiseAttendanceActivity : AppCompatActivity() {
    private lateinit var Send: Button
    private lateinit var subSpinner: AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_wise_attendance)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Subject Wise Attendance"
        Send = findViewById(R.id.ContinueSubjectwiseAttendance)
        subSpinner = findViewById(R.id.AutoCompleteSubjectwiseAttendance)
        val sharePreference = getSharedPreferences("Credentials", MODE_PRIVATE)
        val userId = sharePreference.getString("id", "")!!
        val types = sharePreference.getString("type", "")!!
        CoroutineScope(Dispatchers.IO).launch {
            val subjects = Students.getSubjectsByStudent(userId)
            subjects.also {
                    withContext(Dispatchers.Main) {
                        var subject = ""
                        var adapter =
                            DropDownSubjectAdapter(this@SubjectWiseAttendanceActivity, subjects)
                        subSpinner.setAdapter(adapter)
                        if (subSpinner.adapter == null || subSpinner.adapter.count == 0)
                        {
                            Toast.makeText(
                                this@SubjectWiseAttendanceActivity,
                                "Its Empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }else{
                        subSpinner.setOnItemClickListener { parent, view, position, id ->
                            subject = subjects[position].subjects
                        }
                        Send.setOnClickListener {
                           if (TextUtils.isEmpty(subSpinner.text))
                           {
                               val builder = AlertDialog.Builder(this@SubjectWiseAttendanceActivity)
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
                               val intent = Intent(
                                   this@SubjectWiseAttendanceActivity,
                                   BarGraphActivity::class.java
                               )
                               intent.putExtra("subject", subject)
                               startActivity(intent)
                           }
                        }

                    }
                }
            }
        }

    }
}