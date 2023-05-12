package com.example.attendancemanagementsystem

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.adapter.DropDownSubjectAdapter
import com.example.employee.api.Employees
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewAttendanceSuperuserActivity : AppCompatActivity() {
    private lateinit var Send: Button
    private lateinit var subSpinner: AutoCompleteTextView
    private lateinit var Enroll :EditText
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_view_attendance_superuser)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="View Attendance"

        Send = findViewById(R.id.submitViewAttendanceSuperUser)
        Enroll=findViewById(R.id.EnrollmentSU)
        subSpinner = findViewById(R.id.ViewAttendanceAutoCompleteSubjects)
        var subject = ""
        CoroutineScope(Dispatchers.IO).launch {
            val subjects = Employees.getAllSubjects()
            subjects.also {
                withContext(Dispatchers.Main) {
                    var adapter =
                        DropDownSubjectAdapter(this@ViewAttendanceSuperuserActivity, subjects)
                    subSpinner.setAdapter(adapter)
                    if (subSpinner.adapter == null || subSpinner.adapter.count == 0)
                    {

                    }else{
                        subSpinner.setOnItemClickListener { parent, view, position, id ->
                            subject = subjects[position].subjects
                        }
                        Send.setOnClickListener {
                            if(TextUtils.isEmpty(Enroll.text)&&TextUtils.isEmpty(subSpinner.text))
                            {
                                Toast.makeText(this@ViewAttendanceSuperuserActivity, "Please Fill All Details Properly", Toast.LENGTH_SHORT).show()
                            }
                            else if(TextUtils.isEmpty(Enroll.text)||TextUtils.isEmpty(subSpinner.text))
                            {
                                Toast.makeText(this@ViewAttendanceSuperuserActivity, "Please Fill All Details Properly", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                val intent = Intent(this@ViewAttendanceSuperuserActivity,BarGraphActivity::class.java)
                                intent.putExtra("subject", subject)
                                intent.putExtra("enrollmentno",Enroll.text.toString())
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        }

    }
}