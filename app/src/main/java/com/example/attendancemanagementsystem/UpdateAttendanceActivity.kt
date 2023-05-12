package com.example.attendancemanagementsystem

import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.adapter.StudentAttendanceAdapter
import com.example.employee.api.Employees
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateAttendanceActivity : AppCompatActivity() {
    private lateinit var UpdateattendanceGrid: GridView
    private lateinit var UpdateAttendance: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_attendance)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Update Attendance"
        UpdateattendanceGrid=findViewById(R.id.UpdateStudent)
        UpdateAttendance=findViewById(R.id.updateAttendance)
        val sharePreference = getSharedPreferences("Credentials", MODE_PRIVATE)
        val id=sharePreference.getString("id","")

        val date=intent.getStringExtra("date")
        val subject=intent.getStringExtra("subject")


        CoroutineScope(Dispatchers.IO).launch {
                val attendance= Employees.getAttendanceByDate(id,date,subject)
                withContext(Dispatchers.Main) {
                    if(attendance.isEmpty()){
                    }else {
                    UpdateattendanceGrid.adapter = StudentAttendanceAdapter(
                        this@UpdateAttendanceActivity,
                        attendance,
                        UpdateAttendance,
                        date,
                        subject
                    )
                }
            }
        }
    }
}